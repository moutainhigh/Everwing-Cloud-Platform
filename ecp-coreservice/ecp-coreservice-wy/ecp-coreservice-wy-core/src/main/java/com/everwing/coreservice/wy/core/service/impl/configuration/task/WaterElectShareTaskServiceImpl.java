package com.everwing.coreservice.wy.core.service.impl.configuration.task;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.ThreadPool.ThreadPoolUtils;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.utils.BigDecimalUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.datasource.DataSourceUtil;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.project.*;
import com.everwing.coreservice.common.wy.entity.configuration.support.BillingSupEntity;
import com.everwing.coreservice.common.wy.service.configuration.task.WaterElectShareTaskService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.Map.Entry;

@Service("waterElectShareTaskServiceImpl")
public class WaterElectShareTaskServiceImpl extends Resources implements WaterElectShareTaskService{

	private static final Logger logger = LogManager.getLogger(WaterElectShareTaskServiceImpl.class);
	
	//用于对简单的加减乘除的计算，直接对string的字符串进行计算
	private static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript"); 
	
	private static final int DIGIT=2;  //财务要求的保留小数点位数
	
	private static final String LOG_STR = "当前时间 : %s , 项目  -> %s : %s";
	
	
	//消息队列 route_key 声明处
	@Value("${queue.waterElect.share.autoTask.key}")
	private String water_elect_share_auto_route_key;		//物业管理费自动计费用路由键
	
	@Value("${queue.waterElect.share.autoTask.key}")
	private String wy_billing_key;//重新计费路由键
	
	
	/**
	 * 水电费的分摊计费：
	 * 		区分手动和自动，按户和按用量：
	 * 			1.手动  &&　按户分摊　：直接获取金额，获取需要进行分摊的用户集合　　和物业费分摊类似
	 * 			2.手动 && 按用量 :获取金额，获取分摊任务中此分摊关联的房屋信息，根据房屋信息得到各房屋的用水/电量，进行按用量分摊
	 * 			3.自动 && 按户：根据分摊信息中的分摊公式   得到分摊的 水/电 的用量差，根据单价得到分摊总金额。
	 * 				获取需要进行分摊的用户集合，进行平均分摊
	 * 			4.自动 && 按用量 根据分摊信息中的分摊公式   得到分摊的 水/电 的用量差，根据单价得到分摊总金额。
	 * 					获取分摊任务中此分摊关联的房屋信息，根据房屋信息得到各房屋的用水/电量，进行按用量分摊
	 * 注：分摊和计费有区别。分摊下面的分摊项中是可以重复包含同一个关联资产。
	 *    这里不能像物业费直接覆盖元数据中分摊费用的值。    share_fee=share_fee +amount;
	 * 分摊计费结束
	 */
	
	/**
	 * @TODO 水费/电费分摊计费
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto shareBilling(final String companyId , final TBsProject entity,final int meterType) {
		String meterName=BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType ? "电费" : "水费";
		logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getId() , "开始进行"+meterName+"的分摊计费"));
		//项目信息
		final MessageMap msgMap = new MessageMap(MessageMap.INFOR_WARNING,null);
		if(CommonUtils.isEmpty(entity.getId())){
			msgMap.setMessage("传入计费项目id为空.");
			logger.warn(msgMap.getMessage());
			return new BaseDto(msgMap);
		}
		
		//水电费的分摊在一起，所以类型一定要有
		if(!( 0== meterType || 1 == meterType)){
			msgMap.setMessage("传入分摊类型为空");
			logger.warn(msgMap.getMessage());
			return new BaseDto(msgMap);
		}
		
		RemoteModelResult<Company> rslt =  this.companyApi.queryCompany(companyId);
		if(!rslt.isSuccess() || rslt.getModel() == null){
			logger.warn("切换数据源失败。");
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"切换数据源失败。"));
		}
		final String companyStr = JSON.toJSONString(rslt.getModel());
		
		ThreadPoolUtils.getInstance().executeThread(new Runnable() {
			@Override
			public void run() {
				DataSourceUtil.changeDataSource(companyStr);//切换数据源
				
				String meterName=BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType ? "电费" : " 水费";
				logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getProjectName() , "开始进行本次"+meterName+"分摊费用"));
				//1. 根据projectCode判断当前项目是否开启物业计费.只有计费开启了才存在分摊
				TBsProject paramObj = new TBsProject();
				paramObj.setId(entity.getId());
//				paramObj.setWaterStatus(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV());//水费计费后才可进行水费分摊
				paramObj.setStatus(BillingEnum.STATUS_START.getIntV());		//项目启用
				
				TBsProject tBsProject = tBsProjectMapper.findByObj(paramObj);
				if(CommonUtils.isEmpty(tBsProject)){
					logger.warn(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getId() , "该项目未开启计费,"+meterName+"计费未启动或尚未计费,无法进行"+meterName+"分摊."));
					msgMap.setMessage("该项目尚未开启计费，或"+meterName+"计费尚未完成，无法开始进行分摊操作");
					return;
				}
				
				//2. 获取该项目下, 正在启用的scheme
				TBsChargingScheme paramScheme = new TBsChargingScheme();
				if(BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType){
					//电费
					paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_ELECT.getIntV());	//电费管理scheme
				}else{
					//水费
					paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_WATER.getIntV());	//水费管理scheme
				}
					
				paramScheme.setProjectId(tBsProject.getProjectId());	//project code
				TBsChargingScheme scheme = tBsChargingSchemeMapper.findUsingScheme(paramScheme);
				if(CommonUtils.isEmpty(scheme)){
					logger.warn(getLogStr(tBsProject.getProjectName(), "该项目未找到可用的"+meterName+"计费方案,无法进行分摊"));
					msgMap.setMessage("该项目未找到可用的"+meterName+"计费方案,无法进行分摊");
					return;
				}
				
				//3. 获取该项目下, 正在启用且本月需要分摊的分摊信息（有可能会启用但本月不需要进行分摊）
				List<String> shareinfosF=new ArrayList<>();
				List<TBsShareBasicsInfo> shareInfos;
				if(BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType ){
					shareInfos=tBsShareBasicsInfoMapper.getUsedShareInfo(entity.getProjectId(),String.valueOf(BillingEnum.SHARE_TYPE_ELECT.getIntV()));
				}else{
					shareInfos=tBsShareBasicsInfoMapper.getUsedShareInfo(entity.getProjectId(),String.valueOf(BillingEnum.SHARE_TYPE_WATER.getIntV()));
				}
				if(CommonUtils.isEmpty(shareInfos)){
					logger.warn(getLogStr(tBsProject.getProjectName(), "该项目目前没有可用的"+meterName+"分摊,不用进行分摊"));
					msgMap.setMessage("该项目目前没有可用的"+meterName+"分摊,不用进行分摊");
					return;
				}
				
				//查询本期总账单信息(分摊费用是要写入到账单元数据信息中，所以一定要计费了才又分摊)
				TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
				paramTotal.setProjectId(tBsProject.getProjectId());
				paramTotal.setType(scheme.getSchemeType());
				List<TBsChargeBillTotal> totals = tBsChargeBillTotalMapper.findCurrentBillTotalForShare(paramTotal);
				if ( CommonUtils.isEmpty(totals) ){
					logger.warn(getLogStr(tBsProject.getProjectName(), "本项目本周期尚未进行"+meterName+"计费，还无法分摊"));
					msgMap.setMessage("本项目本周期尚未进行"+meterName+"计费，还无法分摊");
					return;
				}else{
					paramTotal=totals.get(0);//汇总数据只会有一条数据
				}
				
				logger.info("项目开启了计费 && 有可用的计费方案 && 本周期已计费     可以进行本次的"+meterName+"分摊计费 ");
				
				//开始分开计费。区分水电费
				if(BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType){
					electShareBilling(entity,shareInfos,scheme,paramTotal,companyId);
				}else{
					waterShareBilling(entity,shareInfos,scheme,paramTotal,companyId);
				}
				
				
			}
		});
		
		msgMap.setFlag(MessageMap.INFOR_SUCCESS);
		msgMap.setMessage("异步手动计费开始,请稍后查看详情.");
		return new BaseDto(msgMap);
	}

	/**
	 * @describe 水费分摊
	 * @param entity
	 * @param meterType
	 * @return
	 */
	private MessageMap waterShareBilling(
										TBsProject entity,List<TBsShareBasicsInfo> shareInfos,
										TBsChargingScheme scheme,
										TBsChargeBillTotal total,
										String companyId){
		logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getProjectName() , "开始本周期水费分摊的计算"));
		//存在分摊方案信息  shareInfos  校验分摊任务，关联建筑信息（）
		/**
		 *  分摊方案要么自动要么手动，存在多条
		 */
		List<TBsShareRelatedTask> tasks=new ArrayList<>();
		//账单元数据的集合信息，用于计算出水分分摊详情后的修改
		List<TBsShareTaskBuilding> shareTaskBuilding=new ArrayList<>();
		tasks= tBsShareRelatedTaskMapper.getShareTaskByIds(shareInfos);
		if(CommonUtils.isEmpty(tasks)){
			logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getProjectName() , "没有查询到可用的分摊任务信息"));
			return new MessageMap(MessageMap.INFOR_WARNING,"未找到可使用的分摊任务");
		}
		
		for (TBsShareBasicsInfo tBsShareBasicsInfo : shareInfos) {
			//分摊方案下关联有分摊任务,查询分摊方案下面的分摊任务
			for (TBsShareRelatedTask tBsShareRelatedTask : tasks) {
				if(tBsShareRelatedTask.getShareBasicsId().equals(tBsShareBasicsInfo.getId())){
					shareTaskBuilding.add(getTbsShareTaskInfo(tBsShareBasicsInfo.getStartMode(),tBsShareRelatedTask,tBsShareBasicsInfo.getShareFrequency()));
				}
			}
		}
		
		
		if(shareTaskBuilding.size()==0){
			logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getProjectName() , "没有查询到可用的分摊任务信息"));
			return new MessageMap(MessageMap.INFOR_WARNING,"未找到可使用的分摊任务");
		}
		
		//根据任务集合得到每个分摊任务的分摊金额，以及分摊的关联建筑信息
		for (TBsShareTaskBuilding tBsShareTaskBuilding : shareTaskBuilding) {
			if(BillingEnum.manaul_billing.getIntV() == tBsShareTaskBuilding.getStartMode()){
				//自动  需要通过公式获取
				if(CommonUtils.isEmpty(tBsShareTaskBuilding.getShareAmountFormula())){
					logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), tBsShareTaskBuilding.getTaskId(), "没有查询到可用的分摊公式信息"));
					continue;
				}
				String shareForFormula=tBsShareTaskBuilding.getShareAmountFormula();
				//根据公式计算分摊的量
				Double usedAmount= getTotalUsedAmountByCode(shareForFormula,String.valueOf(BillingEnum.SHARE_WATER_TYPE.getIntV()),tBsShareTaskBuilding.getShareFrequency());//
				if(null == usedAmount ||  usedAmount <= 0){
					logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), tBsShareTaskBuilding.getTaskId(), "没有查询到可用的分摊公式信息"));
					continue;
				}
				//得到每个分摊任务的分摊量
				tBsShareTaskBuilding.setShareAmount(usedAmount);
			}else{
				//手动   直接获取金额，获取任务的关联收费对象
				if(CommonUtils.isEmpty(tBsShareTaskBuilding.getShareMoney())){
					logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), tBsShareTaskBuilding.getTaskId(), "没有查询到可用的分摊金额信息"));
					continue;
				}
				
			}
			//获取分摊的关联建筑信息--区分按户和按用量
			//分按户和按用量两种
			if(BillingEnum.SHARE_BY_HOUSEHOLD.getIntV() == tBsShareTaskBuilding.getShareType()){
				//按户--只用获取需要进行分摊的用户数量和关联建筑信息  
				List<TBsShareBuildingRelation> resultList= tBsShareRelatedTaskMapper.getRightBuilingInfos(tBsShareTaskBuilding.getTaskId());
				if(CommonUtils.isEmpty(resultList)) continue;
				tBsShareTaskBuilding.settBsRuleBuildingRelation(resultList);
			}else{
				
				//按用量--需要先进行用量的计算再进行分摊计算。同时获取关联建筑信息
				//如果是第一次   需要拿一个分摊周期的数据，如果还没有这么多用初始化数据
				Double totalUseAmount=tBsShareRelatedTaskMapper.getTotalUseAmountByTaskId(tBsShareTaskBuilding.getTaskId(),String.valueOf(BillingEnum.SHARE_WATER_TYPE.getIntV()),String.valueOf(tBsShareTaskBuilding.getShareFrequency()));
				if(CommonUtils.isEmpty(totalUseAmount)) continue;
				tBsShareTaskBuilding.setTotalUseAmount(totalUseAmount);
				List<TBsShareBuildingRelation> resultListAuto= tBsShareRelatedTaskMapper.getUseAmountByBuilding(tBsShareTaskBuilding.getTaskId(),String.valueOf(tBsShareTaskBuilding.getShareFrequency()));
				tBsShareTaskBuilding.settBsRuleBuildingRelation(resultListAuto);
			}
		}
		
		//所有任务需要的数据已经拿到，剩下计算出来，然后得出需要修改的集合，丢入消息队列
		logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getProjectName() , "本期水费分摊计费结束"));
		if(doCalculationShare(shareTaskBuilding,total.getId(),companyId)) {
			return new MessageMap(MessageMap.INFOR_SUCCESS,entity.getProjectName()+"本次水费分摊计费成功");
		}else {
			return new MessageMap(MessageMap.INFOR_ERROR,entity.getProjectName()+"本次水费分摊计费失败");
		}
	}
	
	/**
	 *  得到了计算分摊的所有数据后进行分摊的计算，以及丢入消息队列
	 * @return 执行结果
	 */
	private boolean doCalculationShare(List<TBsShareTaskBuilding> shareTaskBuilding,String chargeTotalId,String companyId){
		logger.info("======开始进行分摊计费====");
		//一个资产可以关联进多个
		//最终会使用的字段是房屋code，分摊金额，总账单id
		Map<String, Double> resultMap=new HashMap<>();//存放所有的用户和各自分摊金额信息
		for (TBsShareTaskBuilding tBsShareTaskBuilding : shareTaskBuilding) {
			if(CommonUtils.isEmpty(tBsShareTaskBuilding.gettBsRuleBuildingRelation())){
				logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), tBsShareTaskBuilding.getTaskId() , "下未发现可用建筑信息，无法分摊"));
				continue;
			}
			if(BillingEnum.SHARE_BY_HOUSEHOLD.getIntV() == tBsShareTaskBuilding.getShareType()){
				//按户--只用获取需要进行分摊的用户数量和关联建筑信息  
				int shareNumber=tBsShareTaskBuilding.gettBsRuleBuildingRelation().size();//需要进行分摊的用户数
				Double shareMoney=tBsShareTaskBuilding.getShareMoney();//分摊总金额
				Double shareMoneyEveryone=BigDecimalUtils.div(shareMoney, shareNumber, DIGIT);//保留两位小数
				for (TBsShareBuildingRelation relation : tBsShareTaskBuilding.gettBsRuleBuildingRelation()) {
					//每家分摊shareMoneyEveryone
					putShareMoneyToHouse(resultMap,relation.getRelationBuildingCode(),shareMoneyEveryone);
				}
			}else{
				Double totalUseAmount=tBsShareTaskBuilding.getTotalUseAmount();//总用量
				Double shareAmount=tBsShareTaskBuilding.getShareAmount();//需要分摊的总用量
				Double price=tBsShareTaskBuilding.getSharePrice();//分摊的单价
				if(CommonUtils.isEmpty(price)) {
					continue;//一定要有单价
				}
				Double shareMoney=BigDecimalUtils.mul(shareAmount, price);//分摊总金额
				for (TBsShareBuildingRelation relation : tBsShareTaskBuilding.gettBsRuleBuildingRelation()) {
					//每家分摊shareMoneyEveryone
					Double useAmont=relation.getUseAmount();//需要分摊的每户的使用量
					Double shareMoneyEveryone=BigDecimalUtils.div(BigDecimalUtils.mul(shareMoney, useAmont),totalUseAmount, DIGIT);
					putShareMoneyToHouse(resultMap,relation.getRelationBuildingCode(),shareMoneyEveryone);
				}
			}
		}
		
		logger.info("========计算出每户需要分摊的金额=========");
		List<TBsChargeBillHistory> updateList=new ArrayList<>();
		getUpdateListFormMap(resultMap,updateList,chargeTotalId,companyId);
		
		//将要处理的信息放入消息队列之后，需要修改分摊基础信息的上次分摊时间
		List<String> ids=new ArrayList<>();
		for (TBsShareTaskBuilding shareB : shareTaskBuilding) {
			if(CommonUtils.isNotEmpty(shareB.getShareBasicsId())) {
				ids.add(shareB.getShareBasicsId());
			}
		}
		if(ids.size()>0) {
			tBsShareBasicsInfoMapper.updateShareBasicInfo(ids);
		}

		
		logger.info("======分摊计费结束，计费成功====");
		return true;
	}
	
	/**
	 * @describe 将
	 * @param resultMap 存放有关联建筑需要分摊的金额和建筑信息
	 * @param updateList  将用于修改的集合信息
	 */
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	private void getUpdateListFormMap(
										Map<String, Double> resultMap,
										List<TBsChargeBillHistory> updateList,
										String chargeTotalId,
										String companyId){
		
		Iterator it=resultMap.entrySet().iterator();
		while(it.hasNext()){
			TBsChargeBillHistory cb=new TBsChargeBillHistory();
			Entry<String, Double> entry=(Entry<String, Double>) it.next();
			cb.setShareFee(entry.getValue());
			cb.setBuildingCode(entry.getKey());
			cb.setChargeTotalId(chargeTotalId);
			updateList.add(cb);
			
			
			//500条数据丢一次消息队列
			if(updateList.size()>500){
				BillingSupEntity e = new BillingSupEntity(null, updateList);
				
				logger.info("总账单："+chargeTotalId+"的分摊计费进行批量修改，放入消息队列",null,e.toString());
				sendMessage(companyId,e);
				updateList.clear();
				
			}
		}
		
		if(updateList.size()>0) {
			BillingSupEntity e = new BillingSupEntity(null, updateList);
			
			logger.info("总账单："+chargeTotalId+"的分摊计费进行批量修改，放入消息队列",null,e.toString());
			sendMessage(companyId,e);
		}
		
		
	}
	
	
	/**
	 * 得到每户需要分摊的金额后，将金额对号入座放入对应的关联资产
	 * @param resultMap  已关联建筑的code做key   分摊金额做value（重复就递加）
	 */
	private void putShareMoneyToHouse(Map<String, Double> resultMap,String relatinoBuilding,Double shareMoney){
		// 
		if(resultMap.containsKey(relatinoBuilding)){
			//此建筑存在于多个分摊
			Double oldMoney= resultMap.get(relatinoBuilding);
			Double newMoney=BigDecimalUtils.add(oldMoney,shareMoney);
			resultMap.put(relatinoBuilding, newMoney);
		}else{
			resultMap.put(relatinoBuilding, shareMoney);
		}
	}
	
	
	/**
	 * 根据用户之前定义分摊规则的公式算出此次需要分摊的总用量
	 * @param shareForFormula 分摊公式
	 * @return
	 */
	private Double getTotalUsedAmountByCode(String shareForFormula,String meterType,int shareFrequency){
		//TODO
		logger.info("=====通过公式得出需要分摊的用量=====");
		//取出表编号，查出每个表的量，替换掉公式中的表编号进行计算(已#分割)
		//example   #code1#+/-*/#code2#*#code3#....
		String [] codes=shareForFormula.split("#");//去单数就可以得到水表号
		for(int i=0;i<=codes.length / 2-1;i++){
			String meterCode=codes[i*2+1];
			//查询出此水表的读数
			Double reading= tcMeterDataMapper.getLastMeterReadingByCode(meterCode,shareFrequency+"",meterType);
			if(CommonUtils.isEmpty(reading)){
				reading=0.0;
			}
			shareForFormula=shareForFormula.replace("#"+meterCode+"#",""+reading);
		}
		//循环替换后进行计算
		try {
			Double shareAmount= (Double) jse.eval(shareForFormula);
			return shareAmount;
		} catch (ScriptException e) {
			e.printStackTrace();
			logger.info("计算分摊量信息失败");
		}
		return null;
	}
	
	/**
	 * 将分摊任务转换为综合实体信息
	 * @param startMode
	 * @param task
	 * @return
	 */
	private TBsShareTaskBuilding getTbsShareTaskInfo(int startMode,TBsShareRelatedTask task,int shareFrequency){
		TBsShareTaskBuilding tst=new TBsShareTaskBuilding();
		tst.setStartMode(startMode);
		tst.setShareAmount(task.getShareAmount());
		tst.setShareAmountFormula(task.getShareAmountFormula());
		tst.setShareBasicsId(task.getShareBasicsId());
		tst.setShareType(task.getShareType());
		tst.setTaskId(task.getId());
		tst.setShareFrequency(shareFrequency);
		tst.setShareMoney(task.getShareMoney());
		tst.setSharePrice(task.getSharePrice());
		return tst;
	}
	
	/**
	 * @describe 电费分摊
	 * @param entity
	 * @param meterType
	 * @return
	 */
	private MessageMap electShareBilling(
					TBsProject entity,List<TBsShareBasicsInfo> shareInfos,
					TBsChargingScheme scheme,
					TBsChargeBillTotal total,
					String companyId){
		
		logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getProjectName() , "开始此项目本周期电费分摊的计算"));
		//存在分摊方案信息  shareInfos  校验分摊任务，关联建筑信息（）
		/**
		*  分摊方案要么自动要么手动，存在多条
		*/
		List<TBsShareRelatedTask> tasks=new ArrayList<>();
		//账单元数据的集合信息，用于计算出水分分摊详情后的修改
		List<TBsShareTaskBuilding> shareTaskBuilding=new ArrayList<>();
		tasks= tBsShareRelatedTaskMapper.getShareTaskByIds(shareInfos);
		
		if(CommonUtils.isEmpty(tasks)){
			logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getProjectName() , "没有查询到可用的分摊任务信息"));
			return new MessageMap(MessageMap.INFOR_WARNING,"未找到可使用的分摊任务");
		}
		
		for (TBsShareBasicsInfo tBsShareBasicsInfo : shareInfos) {
			//分摊方案下关联有分摊任务,查询分摊方案下面的分摊任务
			for (TBsShareRelatedTask tBsShareRelatedTask : tasks) {
				if(tBsShareRelatedTask.getShareBasicsId().equals(tBsShareBasicsInfo.getId())){
					shareTaskBuilding.add(getTbsShareTaskInfo(tBsShareBasicsInfo.getStartMode(),tBsShareRelatedTask,tBsShareBasicsInfo.getShareFrequency()));
				}
			}
		}
		
		
		if(shareTaskBuilding.size()==0){
			logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getProjectName() , "没有查询到可用的分摊任务信息"));
			return new MessageMap(MessageMap.INFOR_WARNING,"未找到可使用的分摊任务");
		}
		
		//根据任务集合得到每个分摊任务的分摊金额，以及分摊的关联建筑信息
		for (TBsShareTaskBuilding tBsShareTaskBuilding : shareTaskBuilding) {
			if(BillingEnum.manaul_billing.getIntV() == tBsShareTaskBuilding.getStartMode()){
				//自动  需要通过公式获取
				if(CommonUtils.isEmpty(tBsShareTaskBuilding.getShareAmountFormula())){
					logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), tBsShareTaskBuilding.getTaskId(), "没有查询到可用的分摊公式信息"));
					continue;
				}
				String shareForFormula=tBsShareTaskBuilding.getShareAmountFormula();
				//根据公式计算分摊的量
				Double usedAmount= getTotalUsedAmountByCode(shareForFormula,String.valueOf(BillingEnum.SHARE_ELECT_TYPE.getIntV()),tBsShareTaskBuilding.getShareFrequency());
				
				if(null == usedAmount ||  usedAmount <= 0){
					logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), tBsShareTaskBuilding.getTaskId(), "分摊金额小于0，请检查公式是否准确."));
					continue;
				}
				//得到每个分摊任务的分摊量
				tBsShareTaskBuilding.setShareAmount(usedAmount);
			}else{
				//手动   直接获取金额，获取任务的关联收费对象
				if(CommonUtils.isEmpty(tBsShareTaskBuilding.getShareMoney())){
					logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), tBsShareTaskBuilding.getTaskId(), "没有查询到可用的分摊金额信息"));
					continue;
				}
			
			}
			
			//获取分摊的关联建筑信息--区分按户和按用量
			//分按户和按用量两种
			if(BillingEnum.SHARE_BY_HOUSEHOLD.getIntV() == tBsShareTaskBuilding.getShareType()){
				//按户--只用获取需要进行分摊的用户数量和关联建筑信息  
				List<TBsShareBuildingRelation> resultList= tBsShareRelatedTaskMapper.getRightBuilingInfos(tBsShareTaskBuilding.getTaskId());
				if(CommonUtils.isEmpty(resultList)) continue;
				tBsShareTaskBuilding.settBsRuleBuildingRelation(resultList);
			}else{
				//按用量--需要先进行用量的计算再进行分摊计算。同时获取关联建筑信息
				//如果是第一次   需要拿一个分摊周期的数据，如果还没有这么多用初始化数据
				Double totalUseAmount=tBsShareRelatedTaskMapper.getElectTotalUseAmountByTaskId(tBsShareTaskBuilding.getTaskId(),String.valueOf(BillingEnum.SHARE_ELECT_TYPE.getIntV()),String.valueOf(tBsShareTaskBuilding.getShareFrequency()));
				if(CommonUtils.isEmpty(totalUseAmount)) continue;
				tBsShareTaskBuilding.setTotalUseAmount(totalUseAmount);
				List<TBsShareBuildingRelation> resultListAuto= tBsShareRelatedTaskMapper.getUseAmountByBuildingForElect(tBsShareTaskBuilding.getTaskId(),String.valueOf(tBsShareTaskBuilding.getShareFrequency()));
				tBsShareTaskBuilding.settBsRuleBuildingRelation(resultListAuto);
			}
		}
		
		//所有任务需要的数据已经拿到，剩下计算出来，然后得出需要修改的集合，丢入消息队列
		logger.info(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getProjectName() , "本期水费分摊计费结束"));
		if(doCalculationShare(shareTaskBuilding,total.getId(),companyId)) {
			return new MessageMap(MessageMap.INFOR_SUCCESS,entity.getProjectName()+"本次水费分摊计费成功");
		}else {
			return new MessageMap(MessageMap.INFOR_ERROR,entity.getProjectName()+"本次水费分摊计费失败");
		}
	}
	
	
	private void sendMessage(String companyId , BillingSupEntity se){
		if(null != se){
			MqEntity e = new MqEntity(BillingEnum.manaul_billing.getIntV(), se);
			e.setCompanyId(companyId);
			this.amqpTemplate.convertAndSend(water_elect_share_auto_route_key, e);
		}
	}
	
	
	private String getLogStr(String projectName , String logStr){
		return String.format(LOG_STR ,CommonUtils.getDateStr(), projectName,logStr);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void update(String companyId, BillingSupEntity se) {
		logger.info("======开始进行分摊后修改账单元数据信息======="+se.getUpdateList().toString());
		int num=0;
		if(CommonUtils.isNotEmpty(se.getUpdateList())){
			for(TBsChargeBillHistory h : se.getUpdateList()){
				num += this.tBsChargeBillHistoryMapper.updateChargeHistoryForShare(h);
			}
		}
		logger.info("======进行分摊后修改账单元数据信息成功"+num+"条数据=======");
	}

	
	/**
	 * @describe 定时任务平台按照公司进行水电费的分摊计费，此方法为定时任务平台调用方法，通过companyId再继续进行分发
	 * @author QHC
	 */
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto doWaterElectShareBillingByCompnay(String companyId,int meterType) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
//		paramMap.put("status", BillingEnum.STATUS_START.getIntV());						//项目启用状态
		if(BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType){
			paramMap.put("electStatus", BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV());		//电费种待计费状态
			paramMap.put("schemeType", BillingEnum.SCHEME_TYPE_ELECT.getIntV());//计费方案可用状态
		}
		if(BillingEnum.SHARE_WATER_TYPE.getIntV() == meterType){
			paramMap.put("waterStatus", BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV()); //电费种待计费状态
			paramMap.put("schemeType", BillingEnum.SCHEME_TYPE_WATER.getIntV());//计费方案可用状态
		}
//		paramMap.put("chargeType", BillingEnum.TYPE_AUTO.getIntV());						//计费方案类型
		paramMap.put("isUsed", BillingEnum.IS_USED_USING.getIntV());	
		List<TBsProject> projects = this.tBsProjectMapper.findShareBillingProjects(paramMap);		//获取可计费的项目
		if(projects.isEmpty()){
			logger.warn("当前物业公司下,未找到可计费的项目. 计费完成 . companyId : {}",companyId);
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前物业公司下未找到可计费的项目,计费完成. "));
		}
		
		for (TBsProject tBsProject : projects) {
			if(BillingEnum.SHARE_WATER_TYPE.getIntV() == meterType) {
				logger.info("*************开始对项目："+tBsProject.getProjectName()+"进行水费的分摊计费");
				shareBilling(companyId,tBsProject,BillingEnum.SHARE_WATER_TYPE.getIntV());
			}else if(BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType) {
				logger.info("*************开始对项目："+tBsProject.getProjectName()+"进行电费的分摊计费");
				shareBilling(companyId,tBsProject,BillingEnum.SHARE_ELECT_TYPE.getIntV());
			}
		}
		
		logger.info("各个项目正在单独线程中执行自由的分摊任务，请稍后查看详情");
		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"各个项目正在单独线程中执行自由的分摊任务，请稍后查看详情"));
		
	}

	
}
