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
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareBasicsInfo;
import com.everwing.coreservice.common.wy.entity.configuration.support.BillingSupEntity;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.service.configuration.task.WyShareTaskService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @describe 物业管理费分摊计费
 * @author QHC
 *
 */
@Service("wyShareTaskServiceImpl")
public class WyShareTaskServiceImpl extends Resources implements WyShareTaskService{

	private static final Logger logger = LogManager.getLogger(WyShareTaskServiceImpl.class);
	
	private static final String LOG_STR = "当前时间 : %s , 项目  -> %s : %s";
	
	//消息队列 route_key 声明处
	@Value("${queue.waterElect.share.autoTask.key}")
	private String wy_share_auto_route_key;		//物业管理费自动计费用路由键
	
	/**
	 * 
	 *   @TODO  分摊：
  	 *				1.查询本次计费时间点符合要求的分摊信息（分摊是可以存在多个--特别是水电费）
  	 *				2.根据分摊类型进行单独分摊计费
     *   			物业分摊计费：分摊金额 * 用户房屋面积/分摊的用户的总面积  并且分摊针对真个项目中的用户
	 *	   			直接获得每户需要分摊的金额，在本月的账单信息中写入分摊部分  ，分摊结束
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto manualBilling(final String companyId , final TBsProject entity) {
		
		final MessageMap msgMap = new MessageMap(MessageMap.INFOR_WARNING,null);
		if(CommonUtils.isEmpty(entity.getId())){
			msgMap.setMessage("传入项目id为空,无法进行分摊计费.");
			logger.warn(msgMap.getMessage());
			return new BaseDto(msgMap);
		}
		logger.info("*********开始对项目：{} 进行物业管理费分摊计费*********",entity.getProjectName());
		
		
		RemoteModelResult<Company> rslt =  this.companyApi.queryCompany(companyId);
		if(!rslt.isSuccess() || rslt.getModel() == null){
			logger.warn("切换数据源失败。");
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"切换数据源失败。"));
		}
		final String companyStr = JSON.toJSONString(rslt.getModel());
		
		ThreadPoolUtils.getInstance().executeThread(new Runnable() {
			
			@Override
			public void run() {
				DataSourceUtil.changeDataSource(companyStr);
				
				//1.根据项目信息，查询当前项目是否存在需要进行计费的分摊。分摊都是自动进行的，没有手动
				/*--如何获取是否有符合本次计费条件的计费信息？
				 * 
				 *  分摊计费生效时间
				 *  计费频率：
				 * 
				 */
				
				//1. 根据projectCode判断当前项目是否开启物业计费.只有计费开启了才存在分摊
				TBsProject paramObj = new TBsProject();
				paramObj.setId(entity.getId());
//				paramObj.setWyStatus(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV());//物业管理费未计费状态
				paramObj.setStatus(BillingEnum.STATUS_START.getIntV());		//项目启用
				
				TBsProject tBsProject = tBsProjectMapper.findByObj(paramObj);
				if(CommonUtils.isEmpty(tBsProject)){
					logger.warn(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getId() , "该项目未开启计费,无法进行分摊"));
					return;
				}
				
				//2. 获取该项目下, 正在启用的scheme
				TBsChargingScheme paramScheme = new TBsChargingScheme();
				paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_WY.getIntV());	//物业管理费用scheme
				paramScheme.setProjectId(tBsProject.getProjectId());	//project code
				TBsChargingScheme scheme = tBsChargingSchemeMapper.findUsingScheme(paramScheme);
				if(CommonUtils.isEmpty(scheme)){
					logger.warn(getLogStr(tBsProject.getProjectName(), "该项目未找到可用的物业管理费计费方案,无法进行分摊"));
					return;
				}
				
				//3. 获取该项目下, 正在启用且本月需要分摊的分摊信息（有可能会启用但本月不需要进行分摊）
				List<String> shareinfosF=new ArrayList<>();
				List<TBsShareBasicsInfo> shareInfos=tBsShareBasicsInfoMapper.getUsedShareInfo(entity.getProjectId(),String.valueOf(BillingEnum.SHARE_TYPE_WY.getIntV()));
				if(CommonUtils.isEmpty(shareInfos)){
					logger.warn(getLogStr(tBsProject.getProjectName(), "该项目目前没有可用的物业分摊,不用进行分摊"));
					return;
				}
				
				//查询本期总账单信息(分摊费用是要写入到账单元数据信息中，所以一定要计费了才又分摊)
				TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
				paramTotal.setProjectId(tBsProject.getProjectId());
				paramTotal.setType(scheme.getSchemeType());
				List<TBsChargeBillTotal> totals = tBsChargeBillTotalMapper.findCurrentBillTotalForShare(paramTotal);
				if ( CommonUtils.isEmpty(totals) ){
					logger.warn(getLogStr(tBsProject.getProjectName(), "本项目本周期上位计费，还无法分摊"));
					return;
				}else{
					paramTotal=totals.get(0);//只会有一条数据
				}
				
				double totalShareAmount=0;
				for (TBsShareBasicsInfo tBsShareBasicsInfo : shareInfos) {
					if(CommonUtils.isEmpty(tBsShareBasicsInfo.getShareAmount())){
						logger.warn(getLogStr(tBsShareBasicsInfo.getShareName(), "该物业分摊分摊金额为空,无法进行分摊"));
						continue;
					}
					totalShareAmount+=tBsShareBasicsInfo.getShareAmount();
					shareinfosF.add(tBsShareBasicsInfo.getId());//用作后面的修改
				}
				
				//4.进行分摊 : 物业分摊是针对整个小区的，和水电不同，所以直接计算分摊的总金额，然后得到项目中的用户信息
				List<TcBuilding> buildingList = tcBuildingMapper.findChargeBuildingByProjectCode(tBsProject.getProjectId());
				if(CommonUtils.isEmpty(buildingList)){
					logger.warn(getLogStr(tBsProject.getProjectName(), "该项目未找到需要计费的建筑列表,分摊动作完成."));
					return;
				}
				
				//根据项目查询项目的总物业面积(区分建筑面积和套内面积)
				TcBuilding tc=new TcBuilding();
				tc.setProjectId(tBsProject.getProjectId());
				if(BillingEnum.SCHEME_BILLING_AREA_JZ.getIntV() == scheme.getChargingArea()) {
					tc.setBuildingArea(100.0);//这里这样来区分建筑面积和套内面积
				}
				Double totalArea=tcBuildingMapper.getSumHouseAreaByProject(tc);
				if(CommonUtils.isEmpty(totalArea)) {
					logger.warn(getLogStr(tBsProject.getProjectName(), "该项目未找到可用物业分摊面积,无法分摊."));
					return;
				}
				
				//这里注意：分摊方式是   分摊总金额  * 资产面积/项目总物业面积
				List<TBsChargeBillHistory> updateList = new ArrayList<TBsChargeBillHistory>();
				
				for (TcBuilding tcBuilding : buildingList) {
					//单个建筑进行计算和修改   总分摊金额  ，分摊面积，总物业面积
					propertySharingBilling(paramTotal.getId(),companyId,scheme.getChargingArea(),tcBuilding,updateList,totalShareAmount,totalArea);
				}
				
				
				BillingSupEntity e = new BillingSupEntity(null,updateList);
				sendMessage(companyId,e);
				
				//2，修改分摊基础信息的上次分摊时间（第一步做完成功执行）
				tBsShareBasicsInfoMapper.updateShareBasicInfo(shareinfosF);
				
				
			}
		});
		
		logger.info("*********项目：{} 进行物业管理费分摊计费结束*********",entity.getProjectName());
		msgMap.setFlag(null);
		msgMap.setMessage("异步手动计费开始,请稍后查看详情.");
		return new BaseDto(msgMap);
	}

	
	/**
	 * @describe 进行单户的物业分摊计算
	 * @param chargeTotalId 总账单id
	 * @param companyId 公司id
	 * @param building 建筑信息
	 * @param updateList 需要进行更新的集合
	 * @param totalShareMoney 分摊总金额
	 * @param totalArea 项目的总物业面积
	 * @return 
	 */
	private boolean propertySharingBilling(String chargeTotalId,String companyId,int chargingArea,
			 TcBuilding building, List<TBsChargeBillHistory> updateList,Double totalShareMoney,Double totalArea) {
		//计算每户的分摊金额
		//查询单个建筑的面积(区分套内面积和建筑面积,在scheme中或有体现    0：建筑面积 1：套内面积)
		
		if(chargingArea == BillingEnum.SCHEME_BILLING_AREA_JZ.getIntV()) {
			building.setBuildingArea(100.0);//区分套内和建筑面积
		}
		Double houseArea = tcBuildingMapper.getSumHouseAreaByProject(building);
		if(CommonUtils.isEmpty(houseArea)) {
			return false;
		}
		
		Double shareMoney= BigDecimalUtils.div(BigDecimalUtils.mul(totalShareMoney, houseArea),totalArea,2);
		TBsChargeBillHistory cbh=new TBsChargeBillHistory();
		cbh.setChargeTotalId(chargeTotalId);
		cbh.setShareFee(shareMoney);
		cbh.setBuildingCode(building.getBuildingCode());
		updateList.add(cbh);
		//500条数据丢一次消息队列
		if(updateList.size()>500){
			BillingSupEntity e = new BillingSupEntity(null, updateList);
			
			logger.info("总账单："+chargeTotalId+"的物业分摊计费进行批量修改，放入消息队列",null,e.toString());
			sendMessage(companyId,e);
			updateList.clear();
		}
		return true;
	}
	
	
	private void sendMessage(String companyId , BillingSupEntity se){
		if(null != se){
			MqEntity e = new MqEntity(BillingEnum.manaul_billing.getIntV(), se);
			e.setCompanyId(companyId);
			this.amqpTemplate.convertAndSend(wy_share_auto_route_key, e);
		}
	}
	
	private String getLogStr(String projectName , String logStr){
		return String.format(LOG_STR ,CommonUtils.getDateStr(), projectName,logStr);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void update(String companyId, BillingSupEntity se) {
		logger.info("======物业费分摊后修改账单元数据信息======="+se.getUpdateList().toString());
		int num=0;
		if(CommonUtils.isNotEmpty(se.getUpdateList())){
			for(TBsChargeBillHistory h : se.getUpdateList()){
				num += this.tBsChargeBillHistoryMapper.updateChargeHistoryForShare(h);
			}
		}
		logger.info("======物业费分摊后修改账单元数据信息成功"+num+"条数据=======");
		
		
	}

	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto doWyShareByCompnay(String companyId) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("wyStatus", BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV()); //电费种待计费状态
		paramMap.put("schemeType", BillingEnum.SCHEME_TYPE_WY.getIntV());						//方案类型
		List<TBsProject> projects = this.tBsProjectMapper.findShareBillingProjects(paramMap);	//获取可计费的项目
		if(projects.isEmpty()){
			logger.warn("当前物业公司下,未找到可计费的项目. 计费完成 . companyId : {}",companyId);
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前物业公司下未找到可计费的项目,计费完成. "));
		}
		
		for (TBsProject tBsProject : projects) {
			logger.info("循环对各个项目进行独立线程的计费操作");
			manualBilling(companyId,tBsProject);
		}
		
		logger.info("各个项目正在单独线程中执行自由的分摊任务，请稍后查看详情");
		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"各个项目正在单独线程中执行自由的分摊任务，请稍后查看详情"));
	}
	
	
	
}
