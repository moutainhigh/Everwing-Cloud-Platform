
package com.everwing.coreservice.wy.core.service.impl.configuration.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.ThreadPool.ThreadPoolUtils;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.utils.BigDecimalUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.common.enums.StreamEnum;
import com.everwing.coreservice.common.wy.datasource.DataSourceUtil;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;
import com.everwing.coreservice.common.wy.entity.configuration.support.BillingSupEntity;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.property.stall.TcStall;
import com.everwing.coreservice.common.wy.fee.constant.AcChargeDetailBusinessTypeEnum;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.dto.AcChargeDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.BillDto;
import com.everwing.coreservice.common.wy.service.configuration.task.WyBillingTaskService;
import com.everwing.coreservice.wy.core.mq.MqSender;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import com.everwing.coreservice.wy.core.utils.BillingUtils;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

@Service("wyBillingTaskService")
public class WyBillingTaskServiceImpl extends Resources implements WyBillingTaskService{

	private static final Logger logger = LogManager.getLogger(WyBillingTaskServiceImpl.class);
	
	private static final String LOG_STR = "当前时间 : %s , 项目  -> %s : %s";
	
	private static final String MANAUL_STR = "手动";
	
	private static final String AUTO_STR = "自动";
	
	@Value("${batch.add.count}")
	private Integer BATCH_ADD_COUNT;
	
	@Value("${batch.update.count}")
	private Integer BATCH_UPDATE_COUNT;
	
	//消息队列 route_key 声明处
	@Value("${queue.wy2wyBilling.wy.manual.key}")
	private String wy_billing_key;					//物业管理费计费, 将数据丢消息队列里批量操作
	
	@Value("${queue.wy2wy.wy.billing.tBsProject.key}")
	private String wy_billing_auto_route_key;		//物业管理费自动计费, 按项目区分开各个项目的自动计费
	
	@Value("${queue.wy2wy.wy.billing.rebilling.key}")
	private String wy_billing_rebilling_key;		//物业管理费重新计费
	
	
	@Autowired
	private TcBuildingMapper tcBuildingMapper;
	
	@Autowired
	private MqSender mqSender;
	
	
	/**
	 * 
	 *   @TODO  计费:   计费只针对账户的费用计算, 不牵扯到通用账户扣减
	 *   
	 *   通用账户扣减,  需要提出来做一个单独的任务进行扫描 , 根据项目的计费优先级进行扣减 :  扣减任务应该提出来,放在审核之后扣减, 计费的时候不能保证其他账户已经计费完成, 无法通过优先级来扣减通用账户
	 *   
	 *   
	 * 
	 * 
	 */
	
	/**
	 * @TODO 手动计费:
	 */
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto manualBilling(final String companyId , final TBsProject entity, final Integer type) {
		
		final String feeStr = returnFeeStr(type);
		
		final MessageMap msgMap = new MessageMap(MessageMap.INFOR_WARNING,null);
		if(CommonUtils.isEmpty(entity.getId())){
			msgMap.setMessage(feeStr.concat("手动计费: 传入计费项目id为空."));
			logger.warn(msgMap.getMessage());
			return new BaseDto(msgMap);
		}
		
		//1. 根据projectCode判断当前项目是否开启物业计费.
		TBsProject paramObj = new TBsProject();
		paramObj.setId(entity.getId());
		if(type == 1){
			paramObj.setWyStatus(BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV());	//物业管理费未计费状态
		}else if(type == 2){
			paramObj.setBtStatus(BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV()); //本体基金未计费状态
		}
		paramObj.setStatus(BillingEnum.STATUS_START.getIntV());		//项目启用
		
		final TBsProject tBsProject = tBsProjectMapper.findByObj(paramObj);
		if(CommonUtils.isEmpty(tBsProject)){
			msgMap.setMessage("该项目未开启计费, 或者" + feeStr + "当前处于未启动/计费中/计费完成状态, 无法再次手动计费.");
			logger.warn("{}手动计费:  {}",feeStr,msgMap.getMessage());
			return new BaseDto(msgMap);
		}
		
		//2. 获取该项目下, 正在启用的scheme
		final TBsChargingScheme scheme = hasCanBillingScheme(tBsProject, false,type,feeStr);
		if(null == scheme){
			msgMap.setMessage("未找到可用的手动计费方案.");
			logger.warn("{}手动计费:  {}",feeStr,msgMap.getMessage());
			return new BaseDto(msgMap);
		}
		
		//3. 判断当前时间与上次计费时间是否相隔一个月以上
		TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
		paramTotal.setProjectId(entity.getProjectId());
		paramTotal.setType(scheme.getSchemeType());
//		paramTotal.setChargingType(BillingEnum.TYPE_MANUAL.getIntV());
		List<TBsChargeBillTotal> totals = tBsChargeBillTotalMapper.findCurrentBillTotal(paramTotal);
		if(!totals.isEmpty()){
			
			TBsChargeBillTotal currentTotal = totals.get(0);
			
			if(CommonUtils.isNotEmpty(currentTotal.getLastTotalId())){
				TBsChargeBillTotal lastTotal = tBsChargeBillTotalMapper.selectById(currentTotal.getLastTotalId());
				
				if(new Date().before(CommonUtils.addMonth(lastTotal.getBillingTime(), 1))){
					msgMap.setMessage("当前时间: " 
									   + CommonUtils.getDateStr() 
									   + ", 距离上月手动计费时间: "
									   + CommonUtils.getDateStr(lastTotal.getBillingTime())
									   + " 少于一个月,无法手动计费, 此次手动计费完成. ");
					logger.warn("当前时间: {}, 距离上月手动计费时间: {} 少于一个月,无法手动计费, 此次手动计费完成. ",CommonUtils.getDateStr(),CommonUtils.getDateStr(lastTotal.getBillingTime()));
					return new BaseDto(msgMap);
				}
			}
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
				DataSourceUtil.changeDataSource(companyStr); //切换数据源
				tBsProject.setModifyId(entity.getModifyId());
				
				String nextTotalId = CommonUtils.getUUID();
				Map<String,Object> returnMap = hasTotalBillInCurrentCycle(tBsProject, scheme);
				
				TBsChargeBillTotal currentTotalBill = (TBsChargeBillTotal) returnMap.get("entity");
				//根据本次的总账单，检查上一个总账单是否有审核，如果是未审核的状态则不能做本次的计费;加上这个判断限制主要是为了防止重复扣费
				String lastTotalId= currentTotalBill.getLastTotalId();
				if(StringUtils.isNotBlank(lastTotalId)){
					//如果上期总账单编号Id是空；则说明系统第一次计费，故没有上期总账单;
					TBsChargeBillTotal  lastBillTotal = tBsChargeBillTotalMapper.findTbsTotalbyId(lastTotalId);
					if(CommonUtils.isNotEmpty(lastBillTotal)){
						if(CommonUtils.isNotEmpty(lastBillTotal.getAuditStatus()) && lastBillTotal.getAuditStatus() !=1){
							logger.info(CommonUtils.log("总账单编号为["+lastTotalId+"]的总账单还未审核，不能做计费;手动计费停止...."));
							return;
						}
					}
				}
				
				List<TBsChargeBillHistory> insertList = new ArrayList<TBsChargeBillHistory>();
				List<TBsChargeBillHistory> updateList = new ArrayList<TBsChargeBillHistory>();
				List<AcChargeDetailDto> insertCurrentList =new ArrayList<AcChargeDetailDto>();
				Date lastBillDate = (Date) returnMap.get("lastBillDate");
				
				//4. 获取到scheme, 根据scheme来实现计费, tc_house与tc_store
				List<TcBuilding> buildingList = tcBuildingMapper.findChargeBuildingByProjectCode(tBsProject.getProjectId());
				if(CommonUtils.isEmpty(buildingList)){
					modifyProjectStatus(entity, type, BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV());  //本项目设置为计费完成状态
					logger.warn("物业管理费手动计费:  该项目未找到需要计费的建筑列表,计费动作完成.",msgMap.getMessage());
					return;
				}
					
				//循环对每个建筑进行物业管理费计费
				for(TcBuilding building : buildingList){
//					if( CommonUtils.isEmpty( buildingCodeModifyHoustCode(building.getBuildingCode()) ) ) {
//						continue;
//					}
					billingCommonFee(companyId,building, currentTotalBill, scheme, lastBillDate, nextTotalId,insertList,updateList,MANAUL_STR,type,feeStr,insertCurrentList,tBsProject.getProjectName());
				}
				
				//本期总账单: 本期总费用  + 上期欠费
				currentTotalBill.setTotalFee(currentTotalBill.getLastOwedFee() + currentTotalBill.getCurrentFee());
				
				sendMessage(companyId,new MqEntity(null, new BillingSupEntity(insertList, updateList)));
				
				if( insertCurrentList.size() >= 0 ){
					mqSender.sendAcChargeDetailList(insertCurrentList,companyId);
					insertCurrentList.clear();
					logger.info("***********调用批量插入AcCurrentChargeDetail 成功(最后一批次)***********");
				}
				
				//计算完毕之后,生成下个月的总账单 , 账单的上期数据由上方的总账单传入
				TBsChargeBillTotal nextBillTotal = new TBsChargeBillTotal();
				nextBillTotal.setId(nextTotalId);
				nextBillTotal.setAuditStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
				nextBillTotal.setChargingType(BillingEnum.TYPE_MANUAL.getIntV());
				nextBillTotal.setProjectId(tBsProject.getProjectId());
				nextBillTotal.setType(scheme.getSchemeType());
				nextBillTotal.setIsRebilling(BillingEnum.IS_REBILLING_NO.getIntV());
				nextBillTotal.setCreateId(entity.getModifyId());
				nextBillTotal.setCreateTime(new Date());
				nextBillTotal.setModifyTime(new Date());
				nextBillTotal.setCmacIsBilling(BillingEnum.common_account_is_not_bill.getIntV());
				nextBillTotal.setModifyId(entity.getModifyId());
				nextBillTotal.setSchemeId(scheme.getId());
				nextBillTotal.setLastTotalId(currentTotalBill.getId());
				nextBillTotal.setLastOwedFee(currentTotalBill.getTotalFee());

				//获取本期的总金额
				if((boolean) returnMap.get("isInsert")){
					tBsChargeBillTotalMapper.insertChargeBillTotal(currentTotalBill);
				}else{
					tBsChargeBillTotalMapper.update(currentTotalBill); 
				}
				tBsChargeBillTotalMapper.insertChargeBillTotal(nextBillTotal);
				
				assemTBsProject(tBsProject, currentTotalBill, type);
				
				modifyProjectStatus(entity, type, BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV());  //本项目设置为计费完成状态
				
				logger.info(getLogStr(tBsProject.getProjectName(), "手动计费完成"));
			}
		});
		
		modifyProjectStatus(entity, type, BillingEnum.PROJECT_BILLING_START.getIntV());  //该项目的物业管理费状态为进行中
		/*if(type == 1){
			entity.setWyStatus(BillingEnum.PROJECT_BILLING_START.getIntV());		
		}else if(type == 2){
			entity.setBtStatus(BillingEnum.PROJECT_BILLING_START.getIntV());
		}
		this.tBsProjectMapper.update(entity);*/
		
		msgMap.setFlag(MessageMap.INFOR_SUCCESS);
		msgMap.setMessage("异步手动计费开始,请稍后查看详情.");
		return new BaseDto(msgMap);
	}
	
	private void modifyProjectStatus(TBsProject entity, Integer type, Integer status){
		if(type == 1){
			entity.setWyStatus(status);		//该项目的物业管理费状态为进行中
		}else if(type == 2){
			entity.setBtStatus(status);
		}
		this.tBsProjectMapper.update(entity);
	}

	
	private void sendMessage(String companyId , MqEntity e){
		if(null != e){
			e.setCompanyId(companyId);
			this.amqpTemplate.convertAndSend(wy_billing_key, e);   //发送至wyBillingListener
		}
	}
	
	/**
	 * 描述：这里物业管理费进行了了逻辑调整，以前开发按照，本月收取上月物业管理费，现在改为预收，本月收取下月物业管理费
	 *     逻辑：
	 *     
	 *     根据建筑   查询history表数据，如果有 直接收取一整月物业管理费
	 *     						如果没有  查询此建筑的入伙时间   应该收取的物业管理费为：一个周期  +  入伙时间 ~ 入伙当月结算时间
	 *     物业管理费的收取和收费方案的创建时间没有关系，例如:十月1号收费    收取的的是十月份的物业管理       如果是10月八号收费  同样 收取的的是十月份的物业管理
	 *     
	 * @TODO 计算物业费种详细数据,并生成下个月的数据
	 * @return boolean
	 */
	private boolean billingCommonFee(
									 String companyId,
									 TcBuilding building,
									 TBsChargeBillTotal currentTotalBill,
									 TBsChargingScheme scheme,
									 Date lastBillDate,
									 String nextTotalId,
									 List<TBsChargeBillHistory> insertList,
									 List<TBsChargeBillHistory> updateList,
									 String oprStr,
									 Integer type,
									 String feeStr,
									 List<AcChargeDetailDto> insertCurrentList,
									 String projectName
									 )
		{
		
		//查找判断本月的该建筑的数据是否已经生成,若未生成,则生成本月的
		
		TBsChargeBillHistory paramBill = new TBsChargeBillHistory();
		paramBill.setProjectId(currentTotalBill.getProjectId());
		paramBill.setBuildingCode(building.getBuildingCode());
		paramBill.setChargeTotalId(currentTotalBill.getId());
		
		//查询是否有收费记录(两种收费方式)
		List<TBsChargeBillHistory> histories = this.tBsChargeBillHistoryMapper.findCurrentDetailBill(paramBill);
		
		TBsChargeBillHistory currentDetailBill;
		boolean isInsert = false;
		/**
		 * 三种情况  1. 有 history数据  直接计费一个月
		 * 		 2.没有history数据  	但是入伙时间为本次计费时间同月
		 * 		 3. 没有history数据      	但是如果时间非本月
		 */

		int isWhole=1;
		
		if(CommonUtils.isNotEmpty(histories)){
			currentDetailBill =  histories.get(0);
		}else{
			//type = 1 为物业, type = 2为本体
			TBsAssetAccount account = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(building.getBuildingCode(), type);
			double lastBillFee = 0.0;
			if(account != null && CommonUtils.null2Double(account.getAccountBalance()) < 0){
				lastBillFee = Math.abs(account.getAccountBalance());
			}
			
			currentDetailBill = new TBsChargeBillHistory();
			currentDetailBill.setId(CommonUtils.getUUID());
			currentDetailBill.setIsUsed(0);
			currentDetailBill.setChargeTotalId(currentTotalBill.getId());
			currentDetailBill.setBuildingCode(building.getBuildingCode());
			currentDetailBill.setFullName(building.getBuildingFullName());
			currentDetailBill.setLastBillFee(lastBillFee);
			currentDetailBill.setLastBillId(null);
			currentDetailBill.setLastPayed(0.0);
			currentDetailBill.setLateFee(0.0);
			currentDetailBill.setShareFee(0.0);
			currentDetailBill.setCommonDesummoney(0.0);
			currentDetailBill.setNoCommonDesummoney(0.0);
			currentDetailBill.setCreateId(currentDetailBill.getModifyId());
			currentDetailBill.setCreateTime(new Date());
			currentDetailBill.setProjectId(building.getProjectId());
			isInsert = true;
			isWhole=2;
		}
		currentDetailBill.setModifyId(currentDetailBill.getModifyId());
		currentDetailBill.setModifyTime(new Date());
		currentDetailBill.setBillingTime(new Date());
		
		//获取账户余额
		TBsAssetAccount account = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(building.getBuildingCode(), type);
		currentDetailBill.setAccountBalance((null == account)? 0.0 : CommonUtils.null2Double(account.getAccountBalance()));
		
		//计算本月账单
		BillDto bill = new BillDto(); //写入到分账单
		Double currentFee = bill(building, lastBillDate, currentDetailBill, scheme,type,isWhole, bill);
		
		currentDetailBill.setCurrentFee(currentFee); 	//本期产生金额
		//本期总账单 计算公式: 上期总账单   - 上期已付(上期已付最多把上期抹平,剩余全部充入该种账户余额) + 本期计费 + 本期分摊 + 本期违约金
		double currBillFee = CommonUtils.null2Double(currentDetailBill.getLastBillFee()) 			//上期应付
						   - CommonUtils.null2Double(currentDetailBill.getLastPayed()) 				//上期已付
						   + CommonUtils.null2Double(currentDetailBill.getShareFee()) 				//本期分摊 
						   + CommonUtils.null2Double(currentDetailBill.getLateFee())				//本期违约金
						   + currentFee;															//本期计费
		currentDetailBill.setCurrentBillFee(currBillFee);
		currentDetailBill.setTax( CommonUtils.getTax(currBillFee - CommonUtils.null2Double(currentDetailBill.getLateFee()) , scheme.getTaxRate()));//TODO 分摊是否计算? 当前分摊已经计算税金
		currentTotalBill.setCurrentFee(CommonUtils.null2Double(currentTotalBill.getCurrentFee()) + currentFee);	//总金额累计
		currentTotalBill.setLastOwedFee(CommonUtils.null2Double(currentTotalBill.getLastOwedFee()) + CommonUtils.getGap(currentDetailBill.getLastBillFee(), currentDetailBill.getLastPayed()));
		//计算该户以前欠费在本月需要计算的违约金    //每天定时任务刷违约金 此处不再计算
		
		
		//*************新增写入账单数据S*************
//		bill.setCurrBillFee(  );
		bill.setTotal( CommonUtils.null2String( currBillFee ) );
		bill.setCurrFee( CommonUtils.null2String( currentFee ) );
		bill.setLastUnPay(CommonUtils.null2String( currBillFee - currentFee ) );
//		bill.setAccountBalance( currentDetailBill.getAccountBalance() );
//		bill.setLastBillFee( CommonUtils.null2String(currentDetailBill.getLastBillFee()) );
//		bill.setLastPayed( CommonUtils.null2String(currentDetailBill.getLastPayed()) );
		bill.setLateFee( CommonUtils.null2Double( currentDetailBill.getLateFee() ) > 0 ? currentDetailBill.getLateFee().toString() : "0.0" );
		bill.setShareFee( CommonUtils.null2Double( currentDetailBill.getShareFee()) >0  ? currentDetailBill.getShareFee().toString() : "0.0");
		//*************新增写入账单数据E*************
		
		
		//生成下月的数据
		TBsChargeBillHistory nextBill = new TBsChargeBillHistory();
		nextBill.setBuildingCode(building.getBuildingCode());
		nextBill.setChargeTotalId(nextTotalId);
		nextBill.setCreateId(currentDetailBill.getCreateId());
		nextBill.setCreateTime(new Date());
		nextBill.setFullName(building.getBuildingFullName());
		nextBill.setId(CommonUtils.getUUID());
		nextBill.setLastBillId(currentDetailBill.getId());
		nextBill.setIsUsed(0);
		nextBill.setLastBillFee(currentDetailBill.getCurrentBillFee());
		nextBill.setProjectId(building.getProjectId());
		nextBill.setLastPayed(0.0);
		nextBill.setCurrentFee(0.0);
		nextBill.setLateFee(0.0);
		nextBill.setCurrentBillFee(0.0);
		nextBill.setAccountBalance(0.0);
		nextBill.setShareFee(0.0);
		nextBill.setCommonDesummoney(0.0);
		nextBill.setNoCommonDesummoney(0.0);
		//获取欠费数据
		if(null != account){
			List<Map<String,Object>> ohs = this.tBsOwedHistoryMapper.findSomeInfoByAccountId(account.getId());
			if(CommonUtils.isNotEmpty(ohs)){
				nextBill.setLastOwedInfo(JSON.toJSONString(ohs));
			}
		}
		
		if(isInsert){
			insertList.add(currentDetailBill);
		}else{
			updateList.add(currentDetailBill);
		}
		insertList.add(nextBill);
		
		//*****************************新账户数据获取处理**********************
		
		//分析：这里对于本期history来说有可能存在插入和更新，但是对于新增的  收费结果明细表  只会出现新增，因为这个只会出现在计费的时候（houseCodeNew先传老的building_code）
		AcChargeDetailDto acCurrentChargeD =  getAcCurrentChargeDetail(companyId,bill, type ,
			building.getProjectId(),projectName,building.getBuildingCode(),currentDetailBill.getId(),currentDetailBill.getCurrentBillFee());   
		insertCurrentList.add(acCurrentChargeD);
		//*****************************新账户数据获取处理**********************
		
		if(insertList.size() >= BATCH_ADD_COUNT){
			BillingSupEntity e = new BillingSupEntity(insertList, null);
			logger.info("{}{}计费: 批量插入元数据 , 数据: {}, 发送至消息队列开始. ", feeStr , oprStr, e.toString());
			
			sendMessage(companyId,new MqEntity(null,e));
			insertList.clear();
			
			logger.info("{}{}计费: 批量插入元数据 , 数据: {}, 发送至消息队列完成. ", feeStr , oprStr, e.toString());
		}
		if(updateList.size() >= BATCH_UPDATE_COUNT){
			BillingSupEntity e = new BillingSupEntity(null, updateList);
			logger.info("{}{}计费: 批量修改元数据 , 数据: {}, 发送至消息队列开始. ", feeStr , oprStr,e.toString());
			
			sendMessage(companyId,new MqEntity(null,e));
			updateList.clear();
			
			logger.info("{}{}计费: 批量修改元数据 , 数据: {}, 发送至消息队列完成. ", feeStr , oprStr,e.toString());
		}
		//增加一个对新账户进行批量处理的(走消息队列)
		if( insertCurrentList.size() >= BATCH_UPDATE_COUNT ){
			mqSender.sendAcChargeDetailList(insertCurrentList,companyId);
			insertCurrentList.clear();
			logger.info("***********调用批量插入AcCurrentChargeDetail 成功***********");
			
		}
		
		return true;
	}
	
	private Double bill(TcBuilding building,Date lastBillDate , TBsChargeBillHistory currentDetailBill,
			TBsChargingScheme scheme,Integer type,int isWhole,BillDto bill){
		
		double currentFee = 0.0;
		
		if(LookupItemEnum.buildingType_parkspace.getStringValue().equals(building.getBuildingType())){
			//车位
			TcStall stall=this.tcStallMapper.findByBuildingCode(building.getBuildingCode());
			//车位分为三种车位，只有私家车位收取物业管理费，并且没有本体基金
			if(type == 1 && "003".equals(stall.getStallType())) {
				currentFee=stall.getAdministrativeExpenese();//单位元/月
			}
		}else{
			//房屋 / 商铺 / 公建共用
			double price = (type == 1 ? CommonUtils.null2Double(building.getUnitWyPrice()) : CommonUtils.null2Double(building.getUnitBtPrice()));
			
			double area = CommonUtils.null2Double((0 ==  scheme.getChargingArea())? building.getBuildingArea():building.getUsableArea());
			bill.setPrice( new BigDecimal( price ).setScale(4,BigDecimal.ROUND_HALF_UP) );
			if(CommonUtils.isEmpty(price) || CommonUtils.isEmpty(area)){
				currentFee = 0.0;
			}else{
				//现在还没有 入伙时间先给定
				currentFee = calculationOfWYAmount(area,price,null,isWhole);
			}
		}
		return currentFee;
	}
	
	/**
	 * 根据入伙时间和是否有计费记录，计算本次的物业管理费金额

	 * @return
	 */
	@SuppressWarnings("deprecation")
	private double calculationOfWYAmount(double area,double price,Date occupationTime,int isWhole) {
		double currentFee=0.0;
		Calendar calendar=Calendar.getInstance();
		int currentMonth=calendar.get(GregorianCalendar.MONTH)+1;//当月
		int currentYear = calendar.get(GregorianCalendar.YEAR);
		
		if(1 == isWhole) {
			currentFee = BigDecimalUtils.mul(price , area );//直接算取一个周期
		}else if(2 == isWhole || 0 == isWhole) {
			//2：第一次计费   0:重新计费
			if(CommonUtils.isNotEmpty(occupationTime)) {
				//获取入伙时间的月份
				int year = occupationTime.getYear();
				int month = occupationTime.getMonth();
				if( currentMonth == month &&  currentYear == year) {
					//如果入伙时间在本月    直接算取一个周期
					currentFee = BigDecimalUtils.mul(price , area );
				}else if (currentMonth > currentYear) {
					//如果入伙时间不在本月
					int days = CommonUtils.getDiffDaysOfMonth(occupationTime);
					int day=occupationTime.getDate();
					currentFee = BigDecimalUtils.mul(price , area ) * (currentYear - year) * 12 
							+ BigDecimalUtils.mul(price , area ) *  
							BigDecimalUtils.mul((currentYear - year) , (currentMonth - month))
							+ ((days - day)/days * price * area);
				}
			}else {
				currentFee = BigDecimalUtils.mul(price , area );//直接算取一个周期
			}
		}
		currentFee = RoundingTotalAmount(currentFee);
		return currentFee;
		
	}
	
	public double RoundingTotalAmount(double currentFee) {
		if(currentFee <= 0) {
			return currentFee;
		}
		NumberFormat nf = NumberFormat.getNumberInstance();
//		nf.setMaximumFractionDigits(2);//两位小数 
		//nf.setRoundingMode(RoundingMode.UP);//四舍五入
		
		double result = new BigDecimal(String.valueOf(currentFee)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		
//		double result= Double.parseDouble(nf.format(currentFee));
		return result;
	}
	
	
	
	private String getLogStr(String projectName , String logStr){
		return String.format(LOG_STR ,CommonUtils.getDateStr(), projectName,logStr);
	}
	
	
	public static Double sumAllCurrentFee(Date startDate , Date endDate , Double unitPrice, Double area){
		
		Double currentFee = 0.0;
		if(endDate.compareTo(startDate) <= 0){
			return currentFee;
		}
		
		Calendar c = Calendar.getInstance();
		int maxDays,currDays,diffDays,i=1;
		String endMonthStr = CommonUtils.getDateStr(endDate,"yyyy-MM");
		if(CommonUtils.isEquals(endMonthStr, CommonUtils.getDateStr(startDate, "yyyy-MM"))){
			//同一个月
			c.setTime(startDate);
			maxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			int startDay = c.get(Calendar.DAY_OF_MONTH);
			c.setTime(endDate);
			int endDay = c.get(Calendar.DAY_OF_MONTH);
			
			currentFee += (Double)(((endDay - startDay)* unitPrice * area) / maxDays) ;
		}else{
			do{
				c.setTime(startDate);
				maxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
				if(i == 1){
					c.setTime(startDate);
					currDays = c.get(Calendar.DAY_OF_MONTH);
					diffDays = maxDays - currDays;
				}else{
					diffDays = maxDays;
				}
				currentFee += (Double)((diffDays* unitPrice * area) / maxDays);
				startDate = CommonUtils.changeMonths(startDate, 1);
				i++;
			}while(!CommonUtils.isEquals(endMonthStr, CommonUtils.getDateStr(startDate, "yyyy-MM")));
			
			c.setTime(endDate);
			maxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			currDays = c.get(Calendar.DAY_OF_MONTH);
			currentFee += (Double)((currDays* unitPrice * area) / maxDays) ;
		}
		return currentFee;
	}


	@Transactional(rollbackFor=Exception.class)
	@Override
	public void insert(String companyId, BillingSupEntity se) {

		if(CommonUtils.isNotEmpty(se.getInsertList())){
			this.tBsChargeBillHistoryMapper.batchInsert(se.getInsertList());
		}
		if(CommonUtils.isNotEmpty(se.getInsertAccountList())){
			this.tBsAssetAccountMapper.batchInsert(se.getInsertAccountList());
		}
		if(CommonUtils.isNotEmpty(se.getInsertStreamList())){
			this.tBsAssetAccountStreamMapper.batchInsert(se.getInsertStreamList());
		}
		if(CommonUtils.isNotEmpty(se.getInsertOhList())){
			this.tBsOwedHistoryMapper.batchInsert(se.getInsertOhList());
		}
		if(CommonUtils.isNotEmpty(se.getInsertInfoList())){
			this.tBsRebillingInfoMapper.batchInsert(se.getInsertInfoList());
		}
		if(CommonUtils.isNotEmpty(se.getUpdateList())){
			for(TBsChargeBillHistory h : se.getUpdateList()){
				this.tBsChargeBillHistoryMapper.updateBillHistory(h);
			}
		}
		if(CommonUtils.isNotEmpty(se.getUpdateAccountList())){
			for(TBsAssetAccount account : se.getUpdateAccountList()){
				this.tBsAssetAccountMapper.update(account);
			}
		}
		if(CommonUtils.isNotEmpty(se.getUpdateStreamList())){
			for(TBsAssetAccountStream stream : se.getUpdateStreamList()){
				this.tBsAssetAccountStreamMapper.update(stream);
			}
		}
		if(CommonUtils.isNotEmpty(se.getUpdateOhList())){
			for(TBsOwedHistory oh : se.getUpdateOhList()){
				this.tBsOwedHistoryMapper.update(oh);
			}
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto autoBilling(String companyId,Integer type) { 
		
		//寻找该公司下所有可计费的当月tBsProject billingTime is null
		//关联到t_bs_charge_scheme表, 
		//查询条件:
		//1. tBsProject 启用
		//2. tBsProject 该费种为待计费状态
		//3. tBsProject billingTime为null
		//4. scheme的project_id = tBsProject的project_id
		//5. scheme该费种存在启用,当天处于有效期内的方案
		//6. 当天为scheme的计费时间
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("status", BillingEnum.STATUS_START.getIntV());								//项目启用状态
		if(type == 1){
			paramMap.put("wyStatus", BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV());		//费种待计费状态
		}else if(type == 2){
			paramMap.put("btStatus", BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV());		//本体基金待计费状态
		}
		paramMap.put("chargeType", BillingEnum.TYPE_AUTO.getIntV());						//计费方案类型
		paramMap.put("isUsed", BillingEnum.IS_USED_USING.getIntV());							//计费方案可用状态
		paramMap.put("schemeType", type);
		List<TBsProject> projects = this.tBsProjectMapper.findCanBillingProjects(paramMap);		//获取可计费的项目
		
		if(projects.isEmpty()){
			logger.warn("当前物业公司下,未找到可计费的项目. 计费完成 . companyId : {}",companyId);
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前物业公司下未找到可计费的项目,计费完成. "));
		}
		
		
		//有数据的,再用消息队列去项目级的计费
		for(TBsProject p : projects){
			logger.info("{}自动计费: 数据: {}, 开始组装数据,准备发往mq, routeKey: {}",returnFeeStr(type),p.toString(),wy_billing_auto_route_key);
			
			MqEntity entity = new MqEntity(type,p);
			entity.setCompanyId(companyId);
			this.amqpTemplate.convertAndSend(wy_billing_auto_route_key, entity);
			
			logger.info("{}自动计费: mqEntity数据: {}, 发送到mq完成, routeKey: {}",returnFeeStr(type),entity.toString(),wy_billing_auto_route_key);
			
		}
		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"开始自动异步计费,请等待."));
	}


	/**
	 * @TODO 消息队列 消费者调用该方法实现 项目级的物业管理费计费
	 * type : 1 为物业管理费 , 2为本体基金
	 */
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings({ "unused", "rawtypes" })
	@Override
	public BaseDto autoBillingByProject(String companyId, TBsProject entity, Integer type) {
		
		String feeStr = returnFeeStr(type);
		logger.info("{}自动计费: 开始自动计费 .  项目: {}",feeStr,entity.toString());
		
		if(null == entity){
			logger.warn("{}自动计费: 项目为空, 自动计费完成. 自动计费流程停止. ",feeStr);
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"传入项目为空,无法自动计费. 自动计费流程停止. "));
		}
		
		//1. 获取该项目下可用的物业管理费计费方案
		TBsChargingScheme scheme = hasCanBillingScheme(entity, true,type,feeStr);
		if(null == scheme){
			logger.warn("{}自动计费: 当前项目下未找到可供计费的资产 . 自动计费流程停止. 项目: {} ", entity.toString(),feeStr);
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"未找到可用的自动计费方案. 自动计费流程停止. "));
		}
		
		
		//2. 获取本月的总账单,若未生成,则生成一张新的总账单
		String nextTotalId = CommonUtils.getUUID();
		Map<String,Object> returnMap = hasTotalBillInCurrentCycle(entity, scheme);
		 
		 TBsChargeBillTotal currentTotalBill = (TBsChargeBillTotal) returnMap.get("entity");
		//根据本次的总账单，检查上一个总账单是否有审核，如果是未审核的状态则不能做本次的计费;加上这个判断限制主要是为了防止重复扣费
			String lastTotalId= currentTotalBill.getLastTotalId();
			if(StringUtils.isNotBlank(lastTotalId)){
				//如果上期总账单编号Id是空；则说明系统第一次计费，故没有上期总账单;
				TBsChargeBillTotal  lastBillTotal = tBsChargeBillTotalMapper.findTbsTotalbyId(lastTotalId);
				if(CommonUtils.isNotEmpty(lastBillTotal)){
					if(CommonUtils.isNotEmpty(lastBillTotal.getAuditStatus()) && lastBillTotal.getAuditStatus() !=1){
						logger.info(CommonUtils.log("总账单编号为["+lastTotalId+"]的总账单还未审核，不能做计费!,自动计费停止......."));
						return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"总账单编号为["+lastTotalId+"]的总账单还未审核，不能做计费!"));
					}
				}
			}
		//3. 获取该项目下所有需要计费的建筑
		List<TcBuilding> buildingList = this.tcBuildingMapper.findChargeBuildingByProjectCode(entity.getProjectId());
		if(CommonUtils.isEmpty(buildingList)){
			logger.warn("{}自动计费: 当前项目下未找到可供计费的资产. 自动计费流程停止.  项目:{}",feeStr,entity.toString());
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"未找到可计费的资产."));
		}
		
		List<TBsChargeBillHistory> insertList = new ArrayList<TBsChargeBillHistory>();
		List<TBsChargeBillHistory> updateList = new ArrayList<TBsChargeBillHistory>();
		List<AcChargeDetailDto> insertCurrentList =new ArrayList<AcChargeDetailDto>();
		
		Date lastBillDate = (Date) returnMap.get("lastBillDate");
		for(TcBuilding building : buildingList){
			billingCommonFee(companyId, building, currentTotalBill, scheme, lastBillDate, nextTotalId, insertList, updateList,AUTO_STR,type,feeStr,insertCurrentList,entity.getProjectName());
		}
		
		//本期总账单: 本期总费用  + 上期欠费
		currentTotalBill.setTotalFee(currentTotalBill.getLastOwedFee() + currentTotalBill.getCurrentFee());
		
		MqEntity e = new MqEntity(null, new BillingSupEntity(insertList,updateList));
		sendMessage(companyId, e);

		//计算完毕之后,生成下个月的总账单 , 账单的上期数据由上方的总账单传入
		TBsChargeBillTotal nextBillTotal = new TBsChargeBillTotal();
		nextBillTotal.setId(nextTotalId);
		nextBillTotal.setAuditStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
		nextBillTotal.setChargingType(BillingEnum.TYPE_AUTO.getIntV());
		nextBillTotal.setProjectId(entity.getProjectId());
		nextBillTotal.setType(scheme.getSchemeType());
		nextBillTotal.setLastOwedFee(0.0);
		nextBillTotal.setIsRebilling(BillingEnum.IS_REBILLING_NO.getIntV());
		nextBillTotal.setCmacIsBilling(BillingEnum.common_account_is_not_bill.getIntV());
		nextBillTotal.setCreateId(entity.getModifyId());
		nextBillTotal.setCreateTime(new Date());
		nextBillTotal.setModifyTime(new Date());
		nextBillTotal.setModifyId(entity.getModifyId());
		nextBillTotal.setSchemeId(scheme.getId());
		nextBillTotal.setLastTotalId(currentTotalBill.getId());
		nextBillTotal.setLastOwedFee(currentTotalBill.getTotalFee());	//下月总账单才生成的时候, 默认上月欠费为上月的总账单
		
		//获取本期的总金额
		
		if((boolean) returnMap.get("isInsert")){
			tBsChargeBillTotalMapper.insertChargeBillTotal(currentTotalBill);
		}else{
			tBsChargeBillTotalMapper.update(currentTotalBill); 
		}
		tBsChargeBillTotalMapper.insertChargeBillTotal(nextBillTotal);
		
		
		tBsProjectMapper.update(assemTBsProject(entity, currentTotalBill, type));
		
		logger.info("{}自动计费: 自动计费完成. 项目: {}",feeStr,entity.toString());
		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"自动计费完成."));
	}
	
	/**
	 * @TODO 组装数据
	 * @param entity
	 * @param total
	 * @param type
	 * @return
	 */
	private TBsProject assemTBsProject(TBsProject entity , TBsChargeBillTotal total, Integer type){
		if(type == 1){
			entity.setWyStatus(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV());
		}else if(type == 2){
			entity.setBtStatus(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV());
		}
		entity.setCurrentFee(CommonUtils.null2Double(entity.getCurrentFee()) + total.getCurrentFee());
		entity.setLastOwedFee(CommonUtils.null2Double(entity.getLastOwedFee()) + total.getLastOwedFee());
		entity.setTotalFee(CommonUtils.null2Double(entity.getTotalFee()) + total.getTotalFee());
		return entity;
	}
	
	
	/**
	 * @TODO 判断在本周期内是否已经产生总账单 , 若未产生,则需要生成一张新的总账单
	 * @param project
	 * @param scheme

	 * @return
	 */
	private Map<String,Object> hasTotalBillInCurrentCycle(TBsProject project , TBsChargingScheme scheme){
		Map<String,Object> returnMap = new HashMap<String, Object>();
		boolean flag = false;
		TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
		paramTotal.setProjectId(project.getProjectId());
		paramTotal.setType(scheme.getSchemeType());
//		paramTotal.setChargingType(BillingEnum.TYPE_MANUAL.getIntV());
		List<TBsChargeBillTotal> totals = tBsChargeBillTotalMapper.findCurrentBillTotal(paramTotal);
		
		TBsChargeBillTotal currentTotalBill;
		Date lastBillDate = scheme.getStartUsingDate();
		if(CommonUtils.isNotEmpty(totals)){
			currentTotalBill = totals.get(0);
			currentTotalBill.setChargingType(scheme.getChargingType());
			TBsChargeBillTotal lastBill = tBsChargeBillTotalMapper.selectById(currentTotalBill.getLastTotalId());
			if(null != lastBill)  lastBillDate = lastBill.getBillingTime();
			
		}else{
			logger.info("物业管理费计费: 当前未找到本月的计费总账单,需要重新生成. 项目: {} ",project.toString());
			currentTotalBill = new TBsChargeBillTotal();
			
			currentTotalBill.setId(CommonUtils.getUUID());
			currentTotalBill.setProjectId(project.getProjectId());
			currentTotalBill.setType(scheme.getSchemeType());
			currentTotalBill.setLastOwedFee(0.0);
			currentTotalBill.setIsRebilling(BillingEnum.IS_REBILLING_NO.getIntV());
			currentTotalBill.setAuditStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
			currentTotalBill.setChargingType(scheme.getChargingType());
			currentTotalBill.setCreateId(project.getModifyId());
			currentTotalBill.setCreateTime(new Date());
			currentTotalBill.setCmacIsBilling(BillingEnum.common_account_is_not_bill.getIntV());
			flag = true;
			
		}
		currentTotalBill.setModifyTime(new Date());
		currentTotalBill.setModifyId(project.getModifyId());
		currentTotalBill.setBillingTime(new Date());
		currentTotalBill.setSchemeId(scheme.getId());
		
		returnMap.put("entity", currentTotalBill);
		returnMap.put("isInsert", flag);
		returnMap.put("lastBillDate", lastBillDate);
		return returnMap;
	}
	
	
	/**
	 * @TODO  判断当前项目是否含有可计费的计费方案
	 * @param entity
	 * @param isAuto
	 * @return
	 */
	private TBsChargingScheme hasCanBillingScheme(TBsProject entity , boolean isAuto, Integer type , String feeStr){
		TBsChargingScheme paramScheme = new TBsChargingScheme();
		if(type == 1){
			paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_WY.getIntV());	//物业管理费用scheme
		}else if(type == 2){
			paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_BT.getIntV());    //本体基金用scheme
		}
		paramScheme.setProjectId(entity.getProjectId());	//project code
		TBsChargingScheme scheme = tBsChargingSchemeMapper.findUsingScheme(paramScheme);
		
		String str = (isAuto) ? AUTO_STR : MANAUL_STR;
		if(CommonUtils.isEmpty(scheme)){
			logger.warn("{}{}计费:  该项目未找到可用的物业管理费计费方案,无法{}计费. 项目: {}",feeStr,str,str,entity.toString());
			return null;
			
		}else{
			if(isAuto){	//自动
				if(BillingEnum.TYPE_AUTO.getIntV() == scheme.getChargingType()){
					return scheme;
				}else{
					logger.warn("{}自动计费: 该项目可用的物业管理费计费方案为手动计费,不参与自动计费, 自动计费流程停止 . 项目: {}",feeStr,entity.toString());
					return null;
				}
			}else{//手动
				if(BillingEnum.TYPE_MANUAL.getIntV() == scheme.getChargingType()){
					return scheme;
				}else{
					logger.warn("{}手动计费: 该项目可用的物业管理费计费方案为自动计费,不参与手动计费, 手动计费流程停止. 项目: {}",feeStr,entity.toString());
					return null;
				}
			}
		}
	}


	/**
	 * @TODO 重新计费
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto reBillingOpr(String companyId, List<String> ids,String userId,Integer type) {
		if(CommonUtils.isEmpty(ids)){
			logger.warn("{}重新计费: 传入总账单为空 , 无法重新计费. ",returnFeeStr(type));
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "传入总账单为空,无法重新计费."));
		}
		
		//起消息队列进行异步重新计费, 同步返回数据
		MqEntity e = new MqEntity();
		e.setCompanyId(companyId);
		e.setUserId(userId);
		e.setData(ids);
		e.setOpr(type);
		logger.info("消息队列: [{}重新计费]  发送消息至队列  [开始]: routeKey:{} , 数据:{}", returnFeeStr(type),wy_billing_rebilling_key,e.toString());
		this.amqpTemplate.convertAndSend(wy_billing_rebilling_key, e);
		logger.info("消息队列: [{}重新计费]  发送消息至队列  [完成]: routeKey:{} , 数据:{}", returnFeeStr(type),wy_billing_rebilling_key,e.toString());
		
		
		return new BaseDto(new MessageMap(null, "开始异步计费,请稍后刷新页面查看."));
	}

	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void rebilling(String companyId, List<String> ids,String userId,Integer type) {
		String feeStr = returnFeeStr(type);
		//传入id, 将所有的总账单按时间升序排列
		List<TBsChargeBillTotal> totals = this.tBsChargeBillTotalMapper.selectRebillingTotalByIds(ids);
		if(CommonUtils.isEmpty(totals)){
			logger.info("监听队列: [{}重新计费] 计费  [完成] 未找到可重新计费的总账单. 传入数据: {} . ", feeStr,ids);
			return;
		}
		
		boolean isUsingCommonStatus = false;
		
		
		TBsProject paramProject = new TBsProject();
		TBsChargingScheme paramScheme = new TBsChargingScheme();
		TBsChargeBillHistory lastHistory = new TBsChargeBillHistory();
		List<TBsChargingScheme> schemes = null;
		TBsChargingScheme scheme = null;
		List<HashMap> lastOwedHistory = null;		//上期欠费数据
		
		int i = 0;
		//数据集合声明
		List<TBsAssetAccountStream> insertStream = new ArrayList<TBsAssetAccountStream>();
		List<TBsOwedHistory> insertOhList = new ArrayList<TBsOwedHistory>();
		List<TBsRebillingInfo> insertInfosList = new ArrayList<TBsRebillingInfo>();
		Map<String,TBsRebillingInfo> accountMap = new HashMap<String,TBsRebillingInfo>();
		List<TBsChargeBillHistory> insertHistories = new ArrayList<TBsChargeBillHistory>();
		List<TBsChargeBillHistory> updateHistories = new ArrayList<TBsChargeBillHistory>();
		
		for(TBsChargeBillTotal totalBill : totals){
			
			//判断本月的项目是否开启了通用账户计费
			paramProject.setBillingTime(totalBill.getBillingTime());
			paramProject.setStatus(BillingEnum.STATUS_START.getIntV());			//项目启用
			paramProject.setProjectId(totalBill.getProjectId());
			paramProject.setCommonStatus(BillingEnum.STATUS_START.getIntV());	//通用账户启用
			isUsingCommonStatus = (CommonUtils.isEmpty(this.tBsProjectMapper.findByObj(paramProject)) ? false : true);
			
			/**
			 * 重新计费 : 
			 * 		1. 找出本账单下 所有的分账单
			 * 		2. 原分账单设置为无效, 退费到账户与通用账户
			 * 		3. 建立新账单,设置为有效,进行重新计费 , 注意关联上上一期的分账单
			 * 		4. 计费中,同样进行违约金计算, 以及改成上期的分摊
			 * 		5. 计费后, 账户扣费 , 通用账户抵扣
			 * 		6. 总账单聚合
			 * 		7. 计费项目总数据聚合
			 * 		8. 重新计费日志记录
			 */
			
			paramScheme.setId(totalBill.getSchemeId());
			schemes = this.tBsChargingSchemeMapper.selectSchemeInfo(paramScheme);
			if(CommonUtils.isEmpty(schemes)){
				logger.info("监听队列: [{}重新计费] 计费时间: {}总账单计费  [完成] 未找到本账单使用的scheme. 传入数据: {} . ", feeStr, totalBill.getBillingTime() ,paramScheme.toString());
				continue;
			}
			scheme = schemes.get(0);
			
			TBsChargeBillTotal nextTotal = this.tBsChargeBillTotalMapper.findNextBillTotal(totalBill.getId());
			if(null == nextTotal){
				logger.info("监听队列: [{}重新计费] 计费时间: {}总账单计费 [完成] 未找到下期账单. 传入数据:{} .", feeStr , totalBill.getBillingTime(),totalBill.toString());
				continue;
			}
			if(i > 0){	//第一个周期的上期已付不做更改
				nextTotal.setLastOwedFee(0.0);
			}
			
			totalBill.setCurrentFee(0.0);
			totalBill.setLastOwedFee(0.0);
			totalBill.setTotalFee(0.0);
			
			//1. 找出本账单下 所有的分账单
			List<TBsChargeBillHistory> histories = this.tBsChargeBillHistoryMapper.findAllByTotalId(totalBill.getId());
		
			for(TBsChargeBillHistory history : histories){
				
				//2. 原分账单设置为无效, 退费到账户与通用账户
				history.setIsUsed(BillingEnum.IS_USED_STOP.getIntV());	//设置为无效
				

				//因为数据统一 , scheme , 总单以及账户的type都是一样的 0:通用账户 , 1: 物业 , 2:本体 , 3:水费, 4: 电费
				TBsAssetAccount account = assemAccount(history, accountMap, scheme.getSchemeType(),isUsingCommonStatus);

				//通用账户退费
				TBsAssetAccount commonAccount = assemAccount(history, accountMap, BillingEnum.ACCOUNT_TYPE_COMMON.getIntV(), isUsingCommonStatus);
				
				//3. 建立新账单,设置为有效,进行重新计费 , 注意关联上上一期的分账单 
				TBsChargeBillHistory newHistory = assemNewHistory(history,account,insertHistories,totalBill.getLastTotalId());
				//3.1 获取建筑数据
				TcBuilding building= findByBuildingCode(history.getBuildingCode());
				
				if(CommonUtils.isEmpty(building)){
					logger.info("监听队列: [{}重新计费] 计费时间: {}分账单计费  [完成] 未找到分账单对应的建筑. 传入数据: {} . ",feeStr, history.getBuildingCode());
					continue;
				}

				lastHistory = this.tBsChargeBillHistoryMapper.selectLastBillById(history.getLastBillId());
				Date lastBillDate = (null == lastHistory) ? scheme.getStartUsingDate() : lastHistory.getBillingTime();
				//上期已付
				newHistory.setLastPayed((null == lastHistory) ? 0 : CommonUtils.getSum(lastHistory.getCommonDesummoney() , lastHistory.getNoCommonDesummoney()));
				
				//重新计费 : 本期计费
				double currentFee = bill(building, lastBillDate, newHistory, scheme,type,0,null);//重新计费
				//4. 计费中,同样进行违约金计算, 以及改成上期的分摊
				/* 违约金计算逻辑:
						1. 之前在计费的时候,需要将截止到本月的欠费数据,写成json格式,存入下期分账单, 以便重新计费的时候使用
						2. 之前的欠费,违约金按正常流程计算
						3. 本月的账户欠费, 累计到当前重新计费的时间, 写入到一条欠费时间 */
				double currLateFee = 0.0;
				if(null != newHistory.getLastOwedInfo()){
					if(null == lastOwedHistory){
						lastOwedHistory = JSONObject.parseArray(newHistory.getLastOwedInfo(), HashMap.class);
					}
					for(Map m : lastOwedHistory){
						Date owedTime = new Date((Long)m.get("owedTime"));
						
						if(newHistory.getBillingTime().before(CommonUtils.changeDays(owedTime, scheme.getOverdueStartDates()))){
							//计费时间小于该笔违约金的开始计算时间
							continue;
						}
						//分别计算每一笔违约金
						currLateFee += billLateFee(m,lastBillDate,newHistory.getBillingTime(),scheme);
					}
				}
				
				newHistory.setLateFee(currLateFee);
				newHistory.setCurrentFee(currentFee); 	//本期产生金额
				//本期总账单 计算公式: 上期总账单   - 上期已付(上期已付最多把上期抹平,剩余全部充入该种账户余额) + 本期计费 + 本期分摊 + 本期违约金
				double currBillFee = CommonUtils.null2Double(newHistory.getLastBillFee()) 			//上期应付
								   - CommonUtils.null2Double(newHistory.getLastPayed()) 			//上期已付
								   + CommonUtils.null2Double(newHistory.getShareFee()) 				//本期分摊
								   + CommonUtils.null2Double(newHistory.getLateFee())				//本期违约金  后面会扣除违约金, 
								   + currentFee;													//本期计费
				newHistory.setCurrentBillFee(currBillFee);
				newHistory.setTax(CommonUtils.getTax(currBillFee - CommonUtils.null2Double(newHistory.getLateFee()), scheme.getTaxRate()));//TODO 税金计算
				
				//5. 计费后, 违约金 , 通用账户抵扣
				//5.1 先抵扣违约金,再抵扣本金
				double currKqQf = dkLateFee(lastOwedHistory,account,newHistory,scheme,insertStream,accountMap);		//抵扣本账户
				
				double ncd = CommonUtils.calKf(newHistory.getCurrentBillFee(), newHistory.getCommonDesummoney(), newHistory.getNoCommonDesummoney(), currKqQf);
				newHistory.setNoCommonDesummoney(ncd);
				
				if(isUsingCommonStatus){
					double commonKqQf = dkLateFee(lastOwedHistory,commonAccount,newHistory,scheme,insertStream,accountMap);	//抵扣通用账户
					currKqQf += commonKqQf;
					double cd = CommonUtils.calKf(newHistory.getCurrentBillFee(), newHistory.getNoCommonDesummoney(), newHistory.getCommonDesummoney(), commonKqQf);
					newHistory.setCommonDesummoney(cd);					
				}

				//5.2 扣除本月计费
				account.setAccountBalance(account.getAccountBalance() - (newHistory.getCurrentBillFee() - currKqQf));
				newHistory.setAccountBalance(account.getAccountBalance());
				if(account.getAccountBalance() > 0){
					double kq = 0.0;
					if(account.getAccountBalance() > (newHistory.getCurrentBillFee() - currKqQf)){
						kq = (account.getAccountBalance() > (newHistory.getCurrentBillFee() - currKqQf)) ? (newHistory.getCurrentBillFee() - currKqQf) : account.getAccountBalance();
					}
					
					ncd = CommonUtils.calKf(newHistory.getCurrentBillFee(), newHistory.getCommonDesummoney(), newHistory.getNoCommonDesummoney(), kq);
					newHistory.setNoCommonDesummoney(ncd);
				}
				assembleMap(account, accountMap, -(newHistory.getCurrentBillFee() - currKqQf));
				newHistory.setCurrentKqAmount(Math.abs(newHistory.getCurrentBillFee() - currKqQf));

				//5.3 插入本期金额流水
				insertStream.add(new TBsAssetAccountStream(account.getId(), -(newHistory.getCurrentBillFee() - currKqQf),StreamEnum.purpose_billing.getV()));
				
				//5.4 最后一个周期 , 若账户为负,则插入欠费信息
				if(account.getAccountBalance() < 0){
					nextTotal.setLastOwedFee(nextTotal.getLastOwedFee() + account.getAccountBalance());	//上期欠费
					if(i == totals.size() - 1){
						TBsOwedHistory oh = new TBsOwedHistory(account);
						oh.setOwedAmount(Math.abs(account.getAccountBalance()));
						insertOhList.add(oh);
						
						if(insertOhList.size() >= BATCH_ADD_COUNT){
							BillingUtils.sendInsertList(insertOhList, wy_billing_key, companyId, amqpTemplate);
						}
					}
				}
				insertHistories.add(newHistory);
				updateHistories.add(history);
				
				if(insertHistories.size() >= BATCH_ADD_COUNT){
					BillingUtils.sendInsertList(insertHistories, wy_billing_key, companyId, amqpTemplate);
				}
				if(updateHistories.size() >= BATCH_UPDATE_COUNT){
					BillingUtils.sendUpdateList(updateHistories, wy_billing_key, companyId, amqpTemplate);
				}
				
			}
			
			if(!insertHistories.isEmpty()){
				BillingUtils.sendInsertList(insertHistories, wy_billing_key, companyId, amqpTemplate);
			}
			if(!updateHistories.isEmpty()){
				BillingUtils.sendUpdateList(updateHistories, wy_billing_key, companyId, amqpTemplate);
			}
			
			i++;
		}
		
		
		//8. 根据各账户发生的数据流水, 插入重计费数据
		batchInsertRebillingInfo(accountMap, userId, totals.get(0).getBillingTime(),totals.get(0).getProjectId());
		
		if(!insertInfosList.isEmpty()){
			BillingUtils.sendInsertList(insertInfosList, wy_billing_key, companyId, amqpTemplate);
		}
		if(!insertOhList.isEmpty()){
			BillingUtils.sendInsertList(insertOhList, wy_billing_key, companyId, amqpTemplate);
		}
		
		//9 数据聚合
		TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
		for(TBsChargeBillTotal total : totals){
			//总单聚合,本期的总费用 总账单和下期的上期已付
			paramTotal.setProjectId(total.getProjectId());
			paramTotal.setBillingTime(total.getBillingTime());
			paramTotal.setType(type);
			Map<String,Object> rslt = this.tBsChargeBillTotalMapper.selectAllFee(paramTotal);
			if(null == rslt) continue;
			total.setCurrentFee(CommonUtils.null2Double(rslt.get("currentFee")));
			total.setTotalFee(CommonUtils.null2Double(rslt.get("currentBillFee")));
			total.setModifyTime(new Date());
			total.setIsRebilling(BillingEnum.IS_REBILLING_YES.getIntV());
			total.setBillStatus(BillingEnum.BILL_STATUS_WHOLE.getIntV());
			this.tBsChargeBillTotalMapper.update(total);
			
			//下期账单的上期欠费  上期总费用 -上期支付
			TBsChargeBillTotal nextTotal = this.tBsChargeBillTotalMapper.findNextBillTotal(total.getId());
			nextTotal.setLastOwedFee(Math.abs(total.getTotalFee() - CommonUtils.null2Double(rslt.get("currPayed"))));
			nextTotal.setModifyTime(new Date());
			this.tBsChargeBillTotalMapper.update(nextTotal);
			
			// 计费项目总数据聚合
			paramProject.setProjectId(total.getProjectId());
			paramProject.setBillingTime(total.getBillingTime());
			this.tBsProjectMapper.updateFee(paramProject);
		}
		
		/*totalBill.setCurrentFee(totalCurrentFee);	
		totalBill.setTotalFee(totalBill.getLastOwedFee() + totalCurrentFee);		//本期总账单 = 上期欠费   + 本期计费
		totalBill.setIsRebilling(BillingEnum.IS_REBILLING_YES.getIntV());
		double allPayed = this.tBsChargeBillHistoryMapper.findCurrPayedByTotalId(totalBill.getId());
		nextTotal.setLastOwedFee(totalBill.getTotalFee() - allPayed);
		this.tBsChargeBillTotalMapper.update(totalBill);
		this.tBsChargeBillTotalMapper.update(nextTotal);
		
		//7. 计费项目总数据聚合
		this.tBsProjectMapper.updateFee(paramProject);*/
		
	}
	
	private void assembleMap(TBsAssetAccount account , Map<String,TBsRebillingInfo> map, double changeAmount){
		if(!map.containsKey(account.getId())){
			TBsRebillingInfo info = new TBsRebillingInfo();
			info.setId(CommonUtils.getUUID());
			info.setAccountId(account.getId());
			info.setBuildingCode(account.getBuildingCode());
			info.setFullName(account.getFullName());
			info.setBillingTime(new Date());
			info.setType(account.getType());
			info.setChangeAmount(0.0);
			//info.setProjectId(account.getProjectId());
			map.put(account.getId(), info);
		}
		TBsRebillingInfo info = map.get(account.getId());
		info.setChangeAmount(info.getChangeAmount() + changeAmount);
	}
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	//抵扣违约金
	private double dkLateFee(List<HashMap> lastOwedHistory , 
						   TBsAssetAccount account , 
						   TBsChargeBillHistory newHistory,
						   TBsChargingScheme scheme,
						   List<TBsAssetAccountStream> insertList,
						   Map<String,TBsRebillingInfo> accountMap){
		
		double kqQf = 0.0;
		if(CommonUtils.isEmpty(lastOwedHistory)){
			return kqQf;		//传入欠费数据为空, 不做抵扣,直接返回
		}
		
		//判断账户余额
		if(account.getAccountBalance() > 0){
			//含有余额才能抵扣
			//先抵扣违约金
			for(Map m : lastOwedHistory){
				if(account.getAccountBalance() <= 0) break;
				if(BillingEnum.IS_USED_STOP.getIntV() == CommonUtils.null2Int(m.get("isUsed"))) continue;	//本条数据不再使用,跳过
				Date owedTime = new Date((Long)m.get("owedTime"));
				if(newHistory.getBillingTime().before(CommonUtils.changeDays(owedTime, scheme.getOverdueStartDates()))){
					//计费时间小于该笔违约金的开始计算时间
					continue;
				}
				double totalLateFee = CommonUtils.null2Double(m.get("totalLateFee"));
				//从账户进行抵扣 
				double balance = (account.getAccountBalance() > 0) ? account.getAccountBalance() : 0;
				double kfLateFee = (totalLateFee >= balance) ? balance : totalLateFee;
				kqQf += kfLateFee;
				account.setAccountBalance(account.getAccountBalance() - kfLateFee);	//账户直接扣除可扣除的
				
				insertList.add(new TBsAssetAccountStream(account.getId(), -kfLateFee,StreamEnum.purpose_billing_by_latefee.getV()));
				
			}
			
			//再抵扣本金
			for(Map m : lastOwedHistory){
				if(account.getAccountBalance() <= 0) break;
				
				if(BillingEnum.IS_USED_STOP.getIntV() == CommonUtils.null2Int(m.get("isUsed"))) continue;	//本条数据不再使用,跳过

				Date owedTime = new Date((Long)m.get("owedTime"));
				if(newHistory.getBillingTime().before(CommonUtils.changeDays(owedTime, scheme.getOverdueStartDates()))){
					//计费时间小于该笔违约金的开始计算时间
					continue;
				}
				double owedAmount = CommonUtils.null2Double(m.get("owedAmount"));
				//从账户进行抵扣 
				double balance = (account.getAccountBalance() > 0) ? account.getAccountBalance() : 0;
				double kfLateFee = (owedAmount >= balance) ? balance : owedAmount;
				kqQf += kfLateFee;
				if(balance >= kfLateFee){	//本金扣除之后
					m.put("isUsed", Constants.IS_USED_STOP);	//该条数据无效 //TODO 放入之后会 0会变成false, 1会变成true
					
					TBsOwedHistory oh = new TBsOwedHistory();
					oh.setIsUsed(BillingEnum.IS_USED_STOP.getIntV());
					oh.setId((String)m.get("id"));
					this.tBsOwedHistoryMapper.update(oh);
				}
				account.setAccountBalance(account.getAccountBalance() - kfLateFee);	//账户直接扣除可扣除的
				insertList.add(new TBsAssetAccountStream(account.getId(), -kfLateFee,StreamEnum.purpose_billing.getV()));
			}
		}
		assembleMap(account, accountMap, -kqQf);
		return kqQf;
	}
	
	//计算本期违约金
	private double billLateFee(Map<String,Object> map , Date startDate , Date endDate , TBsChargingScheme scheme){
		double currLateFee = CommonUtils.null2Double(map.get("totalLateFee"));
		double owedAmount = CommonUtils.null2Double(map.get("owedAmount"));
		Date owedEndTime = (CommonUtils.isEmpty(map.get("owedEndTime"))) ? null : new Date((Long)map.get("owedEndTime"));
		boolean isNotUsed = (boolean)map.get("isUsed");
		
		if(isNotUsed){		//本条数据不再使用,违约金为0
			return 0.0;
		}
		
		//费用结清时间少于开始时间: 属于上个周期已经结清的费用,已经被过滤
		//费用结清时间大于开始时间 , 小于计费结束时间 , 只计算到结束时间
		if(owedEndTime != null && owedEndTime.before(endDate) && owedEndTime.after(startDate)){
			endDate = owedEndTime;
		}
		
		double currMonthLateFee = 0.0;		//本周期的违约金
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		int startDayNum = c.get(Calendar.DAY_OF_YEAR);
		
		c.setTime(endDate);
		int endDayNum = c.get(Calendar.DAY_OF_YEAR);
		
		int days = endDayNum - startDayNum;
		
		
		if(BillingEnum.SCHEME_CLAC_TYPE_SIMPLE.getIntV() == scheme.getCalculationType()){
			//计算单利的违约金
			currLateFee += owedAmount * scheme.getProportion() * days / 1000;				//有基数
			currMonthLateFee += owedAmount * scheme.getProportion() * days / 1000;			//无基数
		}else if(BillingEnum.SCHEME_CLAC_TYPE_CPX.getIntV() == scheme.getCalculationType()){
			//计算复利的违约金
			 for(int i = 0 ; i < days ; i++){
				 double lateFee = (owedAmount + currLateFee) * scheme.getProportion()  / 1000 ; 
				 currLateFee += lateFee; 
				 currMonthLateFee += lateFee;
			 }
		}
		
		//将本期金额写入
		map.put("totalLateFee", currLateFee);
		return currMonthLateFee;
	}

	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void singleRebilling(String companyId, TBsRebillingInfo entity,Integer type) {

		List<String> buildingCodes = CommonUtils.str2List(entity.getBuildingCode(), Constants.STR_COMMA);
	    
		//参数声明
		Map<String,Object> paramMap = new HashMap<String,Object>();
		TBsProject paramProject = new TBsProject();
		TBsChargingScheme scheme = null;

		//数据存储
		Map<String,Boolean> totalBillMap = new HashMap<String, Boolean>();
		Map<String,String> billIdMap = new HashMap<String,String>();
		Map<String,TBsRebillingInfo> accountMap = new HashMap<String,TBsRebillingInfo>();
		List<HashMap> lastOwedHistory = null;	//欠费信息解析用存储集合
		
		//批量处理数据集合
		List<TBsChargeBillHistory> insertHistories = new ArrayList<TBsChargeBillHistory>();
		List<TBsChargeBillHistory> updateHistories = new ArrayList<TBsChargeBillHistory>();
		List<TBsAssetAccountStream> insertStream = new ArrayList<TBsAssetAccountStream>();
		List<TBsOwedHistory> insertOhList = new ArrayList<TBsOwedHistory>();
		int count = 0;
		for(String buildingCode : buildingCodes){
			paramMap.put("buildingCode", buildingCode);
			paramMap.put("startTime", entity.getRebillingStartTime());
			paramMap.put("type",type);
			List<TBsChargeBillHistory> histories = this.tBsChargeBillHistoryMapper.findAllByObjAfterTime(paramMap);
			
			if(CommonUtils.isNotEmpty(histories)){
				for(TBsChargeBillHistory history : histories){
					//从每一个房屋的历史账单的维度出发
					scheme = this.tBsChargingSchemeMapper.selectByTotalId(history.getChargeTotalId());
					
					if(!totalBillMap.containsKey(history.getChargeTotalId())){
						paramProject.setBillingTime(history.getBillingTime());
						paramProject.setStatus(BillingEnum.STATUS_START.getIntV());			//项目启用
						paramProject.setProjectId(history.getProjectId());
						paramProject.setCommonStatus(BillingEnum.STATUS_START.getIntV());	//通用账户启用
						boolean isUsingCommonStatus = (CommonUtils.isEmpty(this.tBsProjectMapper.findByObj(paramProject)) ? false : true);
						totalBillMap.put(history.getChargeTotalId(), isUsingCommonStatus);
						
						TBsChargeBillTotal total = this.tBsChargeBillTotalMapper.selectById(history.getChargeTotalId());
						billIdMap.put(total.getId(), total.getLastTotalId());
					}
					
					//本期账单置为无效
					history.setIsUsed(BillingEnum.IS_USED_STOP.getIntV());
					//获取本账户并退费
					TBsAssetAccount account = assemAccount(history, accountMap, entity.getType(), totalBillMap.get(history.getChargeTotalId()));
					//获取通用账户并退费
					TBsAssetAccount commonAccount = assemAccount(history, accountMap, BillingEnum.ACCOUNT_TYPE_COMMON.getIntV(), totalBillMap.get(history.getChargeTotalId()));
					//根据历史数据,生成新分单数据
					TBsChargeBillHistory newHistory = assemNewHistory(history,account,insertHistories,billIdMap.get(history.getChargeTotalId()));
					
					
					//重新计费 : 本期计费
					TcBuilding building = findByBuildingCode(buildingCode);
					
					if(CommonUtils.isEmpty(building)){
						logger.info("监听队列: [{]重新计费] 计费时间: {}分账单计费  [完成] 未找到分账单对应的建筑. 传入数据: {} . ",returnFeeStr(type),history.getBuildingCode());
						continue;
					}
					
					TBsChargeBillHistory lastHistory = this.tBsChargeBillHistoryMapper.selectLastBillById(newHistory.getLastBillId());
					Date lastBillDate = (null == lastHistory) ? scheme.getStartUsingDate() : lastHistory.getBillingTime();
					//上期已付
					newHistory.setLastPayed((null == lastHistory) ? 0 : CommonUtils.getSum(lastHistory.getCommonDesummoney() , lastHistory.getNoCommonDesummoney()));
					
					double currentFee = bill(building, lastBillDate, newHistory, scheme,type,0,null);
					
					/* 违约金计算逻辑:
							1. 之前在计费的时候,需要将截止到本月的欠费数据,写成json格式,存入下期分账单, 以便重新计费的时候使用
							2. 之前的欠费,违约金按正常流程计算
							3. 本月的账户欠费, 累计到当前重新计费的时间, 写入到一条欠费时间 */
					double currLateFee = 0.0;
					
					if(null != newHistory.getLastOwedInfo()){
						if(null == lastOwedHistory){
							lastOwedHistory = JSONObject.parseArray(newHistory.getLastOwedInfo(), HashMap.class);
						}
						for(Map m : lastOwedHistory){
							Date owedTime = new Date((Long)m.get("owedTime"));
							if(newHistory.getBillingTime().before(CommonUtils.changeDays(owedTime, scheme.getOverdueStartDates()))){
								//计费时间小于该笔违约金的开始计算时间
								continue;
							}
							//分别计算每一笔违约金
							currLateFee += billLateFee(m,lastBillDate,newHistory.getBillingTime(),scheme);
						}
					}
					
					newHistory.setLateFee(currLateFee);
					newHistory.setCurrentFee(currentFee); 	//本期产生金额
					//本期总账单 计算公式: 上期总账单   - 上期已付(上期已付最多把上期抹平,剩余全部充入该种账户余额) + 本期计费 + 本期分摊 + 本期违约金
					double currBillFee = CommonUtils.null2Double(newHistory.getLastBillFee()) 			//上期应付
									   - CommonUtils.null2Double(newHistory.getLastPayed()) 			//上期已付
									   + CommonUtils.null2Double(newHistory.getShareFee()) 				//本期分摊
									   + CommonUtils.null2Double(newHistory.getLateFee())				//本期违约金  后面会扣除违约金, 
									   + currentFee;													//本期计费
					
					newHistory.setTax( CommonUtils.getTax(currBillFee - CommonUtils.null2Double(newHistory.getLateFee()), scheme.getTaxRate()));//TODO 税金计算, 当前把分摊也计算进去
					newHistory.setCurrentBillFee(currBillFee);
					
					
					//计费后, 违约金 , 通用账户抵扣
					//先抵扣违约金,再抵扣本金
					double currKqQf = dkLateFee(lastOwedHistory,account,newHistory,scheme,insertStream,accountMap);		//抵扣本账户
					double ncd = CommonUtils.calKf(newHistory.getCurrentBillFee(), newHistory.getCommonDesummoney(), newHistory.getNoCommonDesummoney(), currKqQf);
					newHistory.setNoCommonDesummoney(ncd);
					if(totalBillMap.get(history.getChargeTotalId())){
						double commonKqQf = dkLateFee(lastOwedHistory,commonAccount,newHistory,scheme,insertStream,accountMap);	//抵扣通用账户
						currKqQf += commonKqQf;
						double cd = CommonUtils.calKf(newHistory.getCurrentBillFee(), newHistory.getNoCommonDesummoney(), newHistory.getCommonDesummoney(), commonKqQf);
						newHistory.setCommonDesummoney(cd);					
					}
					
					//扣除本月计费
					account.setAccountBalance(account.getAccountBalance() - (newHistory.getCurrentBillFee() - currKqQf));
					newHistory.setAccountBalance(account.getAccountBalance());
					if(account.getAccountBalance() > 0){
						double kq = 0.0;
						if(account.getAccountBalance() > (newHistory.getCurrentBillFee() - currKqQf)){
							kq = (account.getAccountBalance() > (newHistory.getCurrentBillFee() - currKqQf)) ? 
												(newHistory.getCurrentBillFee() - currKqQf) : account.getAccountBalance();
						}
						ncd = CommonUtils.calKf(newHistory.getCurrentBillFee(), newHistory.getCommonDesummoney(), newHistory.getNoCommonDesummoney(), kq);
						newHistory.setNoCommonDesummoney(ncd);
					}
					assembleMap(account, accountMap, -(newHistory.getCurrentBillFee() - currKqQf));
					
					// 插入本期金额流水
					insertStream.add(new TBsAssetAccountStream(account.getId(), -(newHistory.getCurrentBillFee() - currKqQf),StreamEnum.purpose_billing.getV()));
					
					//最后一个周期 , 若账户为负,则插入欠费信息
					if(account.getAccountBalance() < 0 && count == histories.size() - 1){
							TBsOwedHistory oh = new TBsOwedHistory(account);
							oh.setOwedAmount(Math.abs(account.getAccountBalance()));
							insertOhList.add(oh);
							
							if(insertOhList.size() >= BATCH_ADD_COUNT){
								BillingUtils.sendInsertList(insertOhList, wy_billing_key, companyId, amqpTemplate);
							}
					}
					
					updateHistories.add(history);
					insertHistories.add(newHistory);
					
					if(insertHistories.size() >= BATCH_ADD_COUNT){
						BillingUtils.sendInsertList(insertHistories, wy_billing_key, companyId, amqpTemplate);
					}
					if(updateHistories.size() >= BATCH_UPDATE_COUNT){
						BillingUtils.sendUpdateList(updateHistories, wy_billing_key, companyId, amqpTemplate);
					}
				}
			}
			
			if(!insertHistories.isEmpty()){
				BillingUtils.sendInsertList(insertHistories, wy_billing_key, companyId, amqpTemplate);
			}
			if(!updateHistories.isEmpty()){
				BillingUtils.sendUpdateList(updateHistories, wy_billing_key, companyId, amqpTemplate);
			}
			if(!insertOhList.isEmpty()){
				BillingUtils.sendInsertList(insertOhList, wy_billing_key, companyId, amqpTemplate);
			}
			
			count ++;
		}
		
		//插入重计费信息
		batchInsertRebillingInfo(accountMap, entity.getCreateId(),entity.getRebillingStartTime(),entity.getProjectId());
		
		Date date = entity.getRebillingStartTime();
		TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
		while(date.before(new Date())){
			
			//总单聚合,本期的总费用 总账单和下期的上期已付
			paramTotal.setProjectId(entity.getProjectId());
			paramTotal.setBillingTime(date);
			paramTotal.setType(type);
			Map<String,Object> rslt = this.tBsChargeBillTotalMapper.selectAllFee(paramTotal);
			if(rslt == null || CommonUtils.isEmpty(rslt.get("id"))){
				break;
			}
			TBsChargeBillTotal currTotal = this.tBsChargeBillTotalMapper.selectById(CommonUtils.null2String(rslt.get("id")));
			currTotal.setCurrentFee(CommonUtils.null2Double(rslt.get("currentFee")));
			currTotal.setTotalFee(CommonUtils.null2Double(rslt.get("currentBillFee")));
			currTotal.setModifyTime(new Date());
			this.tBsChargeBillTotalMapper.update(currTotal);
			
			//下期账单的上期欠费  上期总费用 -上期支付
			TBsChargeBillTotal nextTotal = this.tBsChargeBillTotalMapper.findNextBillTotal(currTotal.getId());
			nextTotal.setLastOwedFee(Math.abs(currTotal.getTotalFee() - CommonUtils.null2Double(rslt.get("currPayed"))));
			nextTotal.setModifyTime(new Date());
			this.tBsChargeBillTotalMapper.update(nextTotal);
			
			// 计费项目总数据聚合
			paramProject.setProjectId(entity.getProjectId());
			paramProject.setBillingTime(date);
			this.tBsProjectMapper.updateFee(paramProject);
			date = CommonUtils.changeMonths(date, 1);
		}
	}
	
	/**
	 * @TODO 批量处理重计费数据
	 * @param accountMap

	 */
	private void batchInsertRebillingInfo(Map<String,TBsRebillingInfo> accountMap,
										  String userId,
										  Date startTime,
										  String projectId){
		if(CommonUtils.isNotEmpty(accountMap)){
			List<TBsRebillingInfo> insertInfos = new ArrayList<TBsRebillingInfo>();
			for(String accountId : accountMap.keySet()){
				TBsRebillingInfo info = accountMap.get(accountId);
				info.setStatus((info.getChangeAmount() < 0) ? BillingEnum.rebilling_deduct.getIntV() : 
															  (info.getChangeAmount() > 0) ? BillingEnum.rebilling_back.getIntV() : BillingEnum.rebilling_common.getIntV());	
				info.setCreateId(userId); 
				info.setModifyId(userId);
				info.setRebillingStartTime(startTime);
				info.setProjectId(projectId);
				insertInfos.add(info);
			}
			if(CommonUtils.isNotEmpty(insertInfos)){
				this.tBsRebillingInfoMapper.batchInsert(insertInfos);
			}
		}
	}
	
	
	/**
	 * @TODO 组装成账户
	 * @param history
	 * @param accountMap
	 * @param type
	 * @param isCommonStatus
	 * @return
	 */
	private TBsAssetAccount assemAccount(TBsChargeBillHistory history, Map<String,TBsRebillingInfo> accountMap, int type , boolean isCommonStatus){
		TBsAssetAccount account = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(history.getBuildingCode(), type);
		//账户退费
		if(type != BillingEnum.ACCOUNT_TYPE_COMMON.getIntV()){
			
			//将本月从账户扣除的,都加入账户
			account.setAccountBalance(account.getAccountBalance() + history.getCurrentKqAmount());
			if(history.getNoCommonDesummoney() != 0){
				assembleMap(account, accountMap, history.getNoCommonDesummoney());
			}
		}else if(type == BillingEnum.ACCOUNT_TYPE_COMMON.getIntV() && isCommonStatus && history.getCommonDesummoney() != 0){
			account.setAccountBalance(account.getAccountBalance() + history.getCommonDesummoney());
			assembleMap(account, accountMap, history.getCommonDesummoney());
		}
		return account;
	}
	
	/**
	 * @TODO 组装成history分单
	 * @param old
	 * @param account
	 * @return
	 */
	private TBsChargeBillHistory assemNewHistory(TBsChargeBillHistory old,
												 TBsAssetAccount account,
												 List<TBsChargeBillHistory> insertList,
												 String lastTotalId){
		TBsChargeBillHistory newHistory = new TBsChargeBillHistory();
		BeanUtils.copyProperties(old, newHistory);	//这里已经对last_bill_id进行关联,以及原来的分摊

		String lastId = null;
		if(CommonUtils.isNotEmpty(insertList)){
			for(TBsChargeBillHistory h : insertList){
				if(CommonUtils.isEquals(h.getChargeTotalId(), lastTotalId)
						&& CommonUtils.isEquals(h.getBuildingCode(), old.getBuildingCode())){
					lastId = h.getId();
				}
			}
		}
		
		if(null != lastTotalId && null == lastId){
			lastId = this.tBsChargeBillHistoryMapper.findNewLastId(old.getChargeTotalId(),old.getBuildingCode());
			newHistory.setLastBillId(lastId);
		}
		newHistory.setId(CommonUtils.getUUID());
		newHistory.setAccountBalance(account.getAccountBalance());
		newHistory.setCreateTime(new Date());
//		newHistory.setCurrentBillFee(currentBillFee);
//		newHistory.setCurrentFee(currentFee);
//		newHistory.setFeeItemDetail(feeItemDetail);
		newHistory.setIsUsed(BillingEnum.IS_USED_USING.getIntV());
		newHistory.setLateFee(0.0);
		newHistory.setModifyTime(new Date());
		newHistory.setCommonDesummoney(0.0);
		newHistory.setNoCommonDesummoney(0.0);
		newHistory.setCurrentKqAmount(0.0);
		return newHistory;
	}
	
	private TcBuilding findByBuildingCode(String code){
		TcBuildingSearch condition = new TcBuildingSearch();
		condition.setBuildingCode(code);
		List<TcBuildingList> buildings = this.tcBuildingMapper.findByCondition(condition);
		return (CommonUtils.isEmpty(buildings) ? null : buildings.get(0));
	}
	
	private String returnFeeStr(Integer type){
		return (type == 1) ? Constants.BILLING_WY : Constants.BILLING_BT;
	}
	
	

	/**
	 * 获取一个本月收费详情的实体 
	 * @author qhc
	 * @date 2018-05-30
	 * @param bill 计费详情
	 * @param type 账户类型
	 * @param projectId  项目code
	 * @param projectName
	 * @param houseCodeNew  新房屋编码（但是前期要先存放旧的building_code）

	 * @param lastBillId 上期计费id
	 * @param currentBill 本期应交
	 * @return 一个本月收费详情的实体用于插入
	 */
	public AcChargeDetailDto getAcCurrentChargeDetail ( String companyId,
															BillDto bill,
															int type,
															String projectId,
															String projectName,
															String houseCodeNew,
															String lastBillId,
															double currentBill ) {
//		AcCurrentChargeDetail acCurrentDetail = new AcCurrentChargeDetail();//用来做插入
		AcChargeDetailDto acCurrentDetail  = new AcChargeDetailDto();
		//新的收费结果明细表
		acCurrentDetail.setProjectId( projectId );
		acCurrentDetail.setProjectName(projectName);
		acCurrentDetail.setCompanyId(companyId);
		acCurrentDetail.setOperator( "system" );
		acCurrentDetail.setAuditTime(null);
		acCurrentDetail.setChargeTime( new Date() );
		acCurrentDetail.setChargingType(ChargingType.getChargingTypeByCode(type));
		acCurrentDetail.setAssignAmount( new BigDecimal( bill.getShareFee() ) ); 
		acCurrentDetail.setPayedAmount(BigDecimal.valueOf(0));
		acCurrentDetail.setSpecialDiKou(BigDecimal.valueOf(0));
		acCurrentDetail.setCommonDiKou(BigDecimal.valueOf(0));
		//这里因为会初始化数据所以只去本月的
		acCurrentDetail.setChargeAmount( new BigDecimal( bill.getCurrFee() ) );
		acCurrentDetail.setChargeDetail( JSON.toJSONString( bill ) ); 
		acCurrentDetail.setCurrentArreas( new BigDecimal( bill.getCurrFee() ) );
		acCurrentDetail.setPayableAmount( new BigDecimal( bill.getCurrFee() ) ); 
		//acCurrentDetail.setHouseCodeNew(buildingCodeModifyHoustCode(houseCodeNew)); //新的房屋编号
		acCurrentDetail.setBusinessTypeEnum( AcChargeDetailBusinessTypeEnum.CHARGE );  //计费
		return acCurrentDetail;
	}

}
