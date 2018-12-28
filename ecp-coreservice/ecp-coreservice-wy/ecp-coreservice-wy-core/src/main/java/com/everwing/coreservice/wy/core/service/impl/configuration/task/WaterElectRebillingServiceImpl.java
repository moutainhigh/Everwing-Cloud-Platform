package com.everwing.coreservice.wy.core.service.impl.configuration.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.StreamEnum;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bill.FeeItemDetail;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargeType;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingRules;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.service.configuration.task.WaterElectRebillingService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import com.everwing.coreservice.wy.core.utils.BillingUtils;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargeTypeMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargingRulesMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsRuleBuildingRelationMapper;
import com.everwing.utils.FormulaCalculationUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.util.*;


/**
 * @describe 水费和电费的重新计费操作实现  && 修正费用的实现
 * @author QHC
 * @data 2017-08-07 08:48:20
 */
@Service("waterElectRebillingServiceImpl")
public class WaterElectRebillingServiceImpl extends Resources implements WaterElectRebillingService {

	private static final Logger logger = LogManager.getLogger(WaterElectShareTaskServiceImpl.class);
	
	//用于对简单的加减乘除的计算，直接对string的字符串进行计算
	@SuppressWarnings("unused")
	private static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript"); 
	
	@SuppressWarnings("unused")
	private static final int DIGIT=2;  //财务要求的保留小数点位数
	
	@SuppressWarnings("unused")
	private static final String LOG_STR = "当前时间 : %s , 项目  -> %s : %s";
	
	@Autowired
	private TBsChargingRulesMapper tBsChargingRulesMapper;
	
	@Autowired
	private TBsRuleBuildingRelationMapper tBsRuleBuildingRelationMapper;
	
	@Autowired
	private TBsChargeTypeMapper tBsChargeTypeMapper;
	
	
	//消息队列 route_key 声明处
	@Value("${queue.waterElect.rebillingCorrect.key}")
	private String route_key_waterElect_rebilling;		//水电费修正费用路由键
	
	@Value("${queue.wy2wyBilling.wy.manual.key}")
	private String wy_billing_key;//重新计费实现路由键
	
	@Value("${queue.wy2WaterElect.reBilling.manual.key}")
	private String waterElect_billing_rebilling_key;//重新计费路由键
	
	
	
	
	
	
	/**
	 * @describe 水电费重新计费（根据项目）
	 * @param companyId 公司信息
	 * @param ids 需要进行重新计费的总账单ids
	 * @param userId 执行重新计费操作用户信息
	 * @param meterType 0：水表    1：电表
	 */
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public MessageMap waterElectRebilling(String companyId, List<String> ids, String userId,int meterType) {

		//传入id, 将所有的总账单按时间升序排列,按计费月份顺序排列
		List<TBsChargeBillTotal> totals = this.tBsChargeBillTotalMapper.selectRebillingTotalByIds(ids);
		if(CommonUtils.isEmpty(totals)){
			logger.info("监听队列: [物业管理费重新计费] 计费  [完成] 未找到可重新计费的总账单. 传入数据: {} . ", ids);
			return new MessageMap(MessageMap.INFOR_ERROR,"未找到可重新计费的总账单，无法重新计费");
		}
		
		String meterTypeName= (meterType == 0 ? "水费":"电费");
		
		boolean isUsingCommonStatus = false;
		
		
		TBsProject paramProject = new TBsProject();
		TBsChargingScheme paramScheme = new TBsChargingScheme();//计费方案信息
		TBsChargeBillHistory lastHistory = new TBsChargeBillHistory();//历史账单元数据信息
		TcBuildingSearch condition = new TcBuildingSearch();
		List<TBsChargingScheme> schemes = null;  	//计费方案集合
		TBsChargingScheme scheme = null;			//计费方案信息
		List<HashMap> lastOwedHistory = null;		//上期欠费数据
		
		int i = 0;
		//数据集合声明
		List<TBsAssetAccountStream> insertStream = new ArrayList<TBsAssetAccountStream>();  //要进行插入的账户信息集合
		List<TBsOwedHistory> insertOhList = new ArrayList<TBsOwedHistory>();				//要进行插入的历史欠费集合
		List<TBsRebillingInfo> insertInfosList = new ArrayList<TBsRebillingInfo>();         //重新计费操作日志集合
		Map<String,TBsRebillingInfo> accountMap = new HashMap<String,TBsRebillingInfo>();
		List<TBsChargeBillHistory> insertHistories = new ArrayList<TBsChargeBillHistory>(); //需要进行插入操作的历史账单元数据集合
		List<TBsChargeBillHistory> updateHistories = new ArrayList<TBsChargeBillHistory>(); //需要进行更新操作的历史账单元数据集合
		
		//循环对每个月份（周期）的总账单进行重新计费
		for(TBsChargeBillTotal totalBill : totals){
			logger.info("开始对总账单id为: {} 的"+meterTypeName+"重新计费操作",totalBill.getId());
			double totalCurrentFee = 0.0;		//本期计费
			//判断本月的项目是否开启了通用账户计费(计费月份)
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
			
			//查询计费方案信息(这里根据id查询)
			paramScheme.setId(totalBill.getSchemeId());
			schemes = this.tBsChargingSchemeMapper.selectSchemeInfo(paramScheme);
			if(CommonUtils.isEmpty(schemes)){
				logger.info("监听队列: ["+meterTypeName+"] 计费时间: {}总账单计费  [完成] 未找到本账单使用的scheme. 传入数据: {} . ", totalBill.getBillingTime() ,paramScheme.toString());
				continue;
			}
			scheme = schemes.get(0);
			
			TBsChargeBillTotal nextTotal = this.tBsChargeBillTotalMapper.findNextBillTotal(totalBill.getId());
			if(null == nextTotal){
				logger.info("监听队列: ["+meterTypeName+"] 计费时间: {}总账单计费 [完成] 未找到下期账单. 传入数据:{} .",totalBill.getBillingTime(),totalBill.toString());
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
		
			//对分账单进行循环操作
			for(TBsChargeBillHistory history : histories){
				
				//2. 原分账单设置为无效, 退费到账户与通用账户
				history.setIsUsed(BillingEnum.IS_USED_STOP.getIntV());	//设置为无效
				//水费账户--
				//TODO  区分
				TBsAssetAccount account;
				if(0 == meterType) {
					account = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(history.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_WATER.getIntV());
				}else {
					account = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(history.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_ELECT.getIntV());
				}
				//通用账户
				TBsAssetAccount commonAccount = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(history.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_COMMON.getIntV());
				
				//账户退费
				if(history.getNoCommonDesummoney() != 0){
					//新的账户余额=当前账户余额+退费
					account.setAccountBalance(account.getAccountBalance() + history.getCommonDesummoney());
					assembleMap(account, accountMap, history.getNoCommonDesummoney());
				}
				
				//通用账户退费
				if(isUsingCommonStatus && history.getCommonDesummoney() != 0){
					commonAccount.setAccountBalance(commonAccount.getAccountBalance() + history.getNoCommonDesummoney());
					assembleMap(commonAccount, accountMap, history.getCommonDesummoney());
				}
				
				//3. 建立新账单,设置为有效,进行重新计费 , 注意关联上上一期的分账单 
				TBsChargeBillHistory newHistory = new TBsChargeBillHistory();
				BeanUtils.copyProperties(history, newHistory);	//这里已经对last_bill_id进行关联,以及原来的分摊

				//记完一次i++
				if(i > 0){
					String lastId = this.tBsChargeBillHistoryMapper.findNewLastId(totalBill.getId(),history.getBuildingCode());
					newHistory.setLastBillId(lastId);
				} 
				newHistory.setId(CommonUtils.getUUID());
				newHistory.setAccountBalance(account.getAccountBalance());
				newHistory.setCreateTime(new Date());
//				newHistory.setCurrentBillFee(currentBillFee);
//				newHistory.setCurrentFee(currentFee);
//				newHistory.setFeeItemDetail(feeItemDetail);
				newHistory.setIsUsed(BillingEnum.IS_USED_USING.getIntV());
				newHistory.setLateFee(0.0);
				newHistory.setShareFee(0.0);
				newHistory.setModifyTime(new Date());
				newHistory.setCommonDesummoney(0.0);
				newHistory.setNoCommonDesummoney(0.0);
				newHistory.setAduitStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
				condition.setBuildingCode(history.getBuildingCode());
				List<TcBuildingList> buildings= this.tcBuildingMapper.findByCondition(condition);
				//TODO
				if(CommonUtils.isEmpty(buildings)){
					totalCurrentFee += history.getCurrentFee();
					logger.info("监听队列: ["+meterTypeName+"] 计费时间: {}分账单计费  [完成] 未找到分账单对应的建筑. 传入数据: {} . ",history.getBuildingCode());
					continue;
				}

				//上一期账单元数据
				lastHistory = this.tBsChargeBillHistoryMapper.selectLastBillById(newHistory.getLastBillId());
				//上次计费时间
				Date lastBillDate = (null == lastHistory) ? scheme.getStartUsingDate() : lastHistory.getBillingTime();
				//上期已付
				newHistory.setLastPayed((null == lastHistory) ? 0 : CommonUtils.getSum(lastHistory.getCommonDesummoney(),lastHistory.getNoCommonDesummoney()));
				
				//重新计费 : 本期计费
				double currentFee=0.0;
				currentFee = waterBilling( buildings.get(0),lastBillDate,newHistory,scheme,totalBill,meterType);

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
							//计费时间小于该笔违约金的开始计算时间,还不能开始收取违约金
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
				newHistory.setTax( (currBillFee - CommonUtils.null2Double(newHistory.getLateFee()) ) * scheme.getTaxRate() / 100);	//TODO 分摊是否计算? 当前分摊已经计算税金
				totalCurrentFee += currentFee;					//总金额累计
				//5. 计费后, 违约金 , 通用账户抵扣
				
				//存在欠费信息才进行违约金和欠费本金的抵扣
				double currKqQf=0.0;//违约金和欠费本金的抵扣金额
				if( CommonUtils.isNotEmpty(lastOwedHistory) ) {
					//5.1 先抵扣违约金,再抵扣本金(之前的欠费本金)
					currKqQf = dkLateFee(lastOwedHistory,account,newHistory,scheme,insertStream,accountMap);		//抵扣本账户
					
					double ncd = CommonUtils.calKf(newHistory.getCurrentBillFee(), newHistory.getCommonDesummoney(), newHistory.getNoCommonDesummoney(), currKqQf);
					newHistory.setNoCommonDesummoney(ncd);  //设置非通用账户抵扣金额
					
					//开通了通用账户
					if(isUsingCommonStatus){
						double commonCurrKqQf=dkLateFee(lastOwedHistory,commonAccount,newHistory,scheme,insertStream,accountMap);
						currKqQf += commonCurrKqQf;	//抵扣通用账户
						
						double cd = CommonUtils.calKf(newHistory.getCurrentBillFee(), newHistory.getNoCommonDesummoney(), newHistory.getCommonDesummoney(), commonCurrKqQf);
						newHistory.setCommonDesummoney(cd);			 //设置通用账户抵扣金额		
					}
				}
				/**
				 * 此时只是对违约金及其本金进行了扣减，是否扣减完不一定
				 * 	1.账户余额扣减足够了
				 * 	2.账户余额和通用账户余额扣减足够
				 *  3.账户余额扣减
				 */
				
				//5.2 扣除本月计费   newHistory.getCurrentBillFee() 总的账单金额
				//非通用账户的余额可以是负数（本月账单已经包含了currKqQf部分，所以要减掉）
				account.setAccountBalance(account.getAccountBalance() - (newHistory.getCurrentBillFee() - currKqQf));   
				newHistory.setAccountBalance(account.getAccountBalance());
				if(account.getAccountBalance() > 0){ //余额还>0
					double kq = 0.0;
					if(account.getAccountBalance() > (newHistory.getCurrentBillFee() - currKqQf)){
						kq = (account.getAccountBalance() > (newHistory.getCurrentBillFee() - currKqQf)) ? (newHistory.getCurrentBillFee() - currKqQf) : account.getAccountBalance();
					}
					
					double ncd = CommonUtils.calKf(newHistory.getCurrentBillFee(), newHistory.getCommonDesummoney(), newHistory.getNoCommonDesummoney(), kq);
					newHistory.setNoCommonDesummoney(ncd);//非通用账户抵扣
				}
				assembleMap(account, accountMap, -(newHistory.getCurrentBillFee() - currKqQf));

				//5.3 插入本期金额流水
				insertStream.add(new TBsAssetAccountStream(account.getId(), -(newHistory.getCurrentBillFee() - currKqQf), StreamEnum.purpose_billing.getV())); //计费扣费
				
				//5.4 最后一个周期 , 若账户为负,则插入欠费信息
				if(account.getAccountBalance() < 0){
					nextTotal.setLastOwedFee(nextTotal.getLastOwedFee() + account.getAccountBalance());	//上期欠费
					if(i == totals.size()){
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
			
			//6. 总账单聚合
			totalBill.setModifyTime(new Date());
			totalBill.setCurrentFee(totalCurrentFee);	
			totalBill.setTotalFee(totalBill.getLastOwedFee() + totalCurrentFee);		//本期总账单 = 上期欠费   + 本期计费
			totalBill.setIsRebilling(BillingEnum.IS_REBILLING_YES.getIntV());
			totalBill.setBillStatus(BillingEnum.BILL_STATUS_WHOLE.getIntV());
			double allPayed = this.tBsChargeBillHistoryMapper.findCurrPayedByTotalId(totalBill.getId());
			nextTotal.setLastOwedFee(totalBill.getTotalFee() - allPayed);
			this.tBsChargeBillTotalMapper.update(totalBill);
			this.tBsChargeBillTotalMapper.update(nextTotal);
			
			//7. 计费项目总数据聚合
			this.tBsProjectMapper.updateFee(paramProject);
		}
		
		
		//8. 根据各账户发生的数据流水, 插入重计费数据
		if(CommonUtils.isNotEmpty(accountMap)){
			for(String accountId : accountMap.keySet()){
				TBsRebillingInfo info = accountMap.get(accountId);
				info.setStatus((info.getChangeAmount() < 0) ? BillingEnum.rebilling_deduct.getIntV() : 
															  (info.getChangeAmount() > 0) ? BillingEnum.rebilling_back.getIntV() : BillingEnum.rebilling_common.getIntV());	
				info.setCreateId(userId); 
				info.setModifyId(userId);
				info.setRebillingStartTime(totals.get(0).getBillingTime());
				info.setProjectId(totals.get(0).getProjectId());
				insertInfosList.add(info);
				
				if (insertInfosList.size() >= BATCH_ADD_COUNT) {
					BillingUtils.sendInsertList(insertInfosList, wy_billing_key, companyId, amqpTemplate);
				}
			}
		}
		
		if(!insertInfosList.isEmpty()){
			BillingUtils.sendInsertList(insertInfosList, wy_billing_key, companyId, amqpTemplate);
		}
		if(!insertOhList.isEmpty()){
			BillingUtils.sendInsertList(insertOhList, wy_billing_key, companyId, amqpTemplate);
		}
		return new MessageMap(MessageMap.INFOR_SUCCESS,"重新计费正在异步进行中,请稍后查看详情");
		
	}
	
	
	private void assembleMap(TBsAssetAccount account , Map<String,TBsRebillingInfo> map, double changeAmount){
		if(!map.containsKey(account.getId())){
			//新建一个
			TBsRebillingInfo info = new TBsRebillingInfo();
			info.setId(CommonUtils.getUUID());
			info.setAccountId(account.getId());
			info.setBuildingCode(account.getBuildingCode());
			info.setFullName(account.getFullName());
			info.setBillingTime(new Date());
			info.setType(account.getType());
			info.setChangeAmount(0.0);
			info.setProjectId(account.getProjectId());
			map.put(account.getId(), info);
		}
		TBsRebillingInfo info = map.get(account.getId());
		//到账结果
		info.setChangeAmount(info.getChangeAmount() + changeAmount);
	}
	
	
	/*private Double bill(TcBuilding building,Date lastBillDate , TBsChargeBillHistory currentDetailBill,TBsChargingScheme scheme){
		double currentFee = 0.0;
		if(LookupItemEnum.buildingType_house.getStringValue().equals(building.getBuildingType())){
			//房屋
			TcHouse house = this.tcHouseMapper.findByBuildingCode(building.getBuildingCode());
			if(CommonUtils.isEmpty(house) || CommonUtils.isEmpty(house.getUnitWyPrice())){
				currentFee = 0.0;
			}else{
				//0 为建筑面积 , 1为套内面积
				currentFee = sumAllCurrentFee(lastBillDate, 						//上次计费时间
											  currentDetailBill.getBillingTime(), 	//本次计费时间
											  house.getUnitWyPrice(), 				//计费物业单价    元/(月*平方米)
											  (0 == scheme.getChargingArea()) ? house.getHouseArea():house.getUsableArea()
											 );
			}
			
			
			
		}else if(LookupItemEnum.buildingType_store.getStringValue().equals(building.getBuildingType())){
			//商铺
			TcStore store = this.tcStoreMapper.findByBuildingCode(building.getBuildingCode()); 
			if(CommonUtils.isEmpty(store) || CommonUtils.isEmpty(store.getUnitWyPrice())){
				currentFee = 0.0;
			}else{
				//0 为建筑面积 , 1为套内面积
				currentFee = sumAllCurrentFee(lastBillDate, 						//上次计费时间
											  currentDetailBill.getBillingTime(), 	//本次计费时间
											  store.getUnitWyPrice(), 				//计费物业单价    元/(月*平方米)
											  (0 == scheme.getChargingArea()) ? store.getBuildingArea():store.getUsableArea()
											 );
			}
		}
		
		return currentFee;
	}*/
	
	
	//计算本期违约金
	private double billLateFee(Map<String,Object> map , Date startDate , Date endDate , TBsChargingScheme scheme){
		double currLateFee = CommonUtils.null2Double(map.get("totalLateFee"));  //总欠费
		double owedAmount = CommonUtils.null2Double(map.get("owedAmount"));		//违约金
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
			currLateFee += owedAmount * scheme.getProportion() * days / 1000;     //有基数
			currMonthLateFee += owedAmount * scheme.getProportion() * days / 1000;  //无基数
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
	
	
	
	//抵扣违约金
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private double dkLateFee(List<HashMap> lastOwedHistory , 
						   TBsAssetAccount account , 
						   TBsChargeBillHistory newHistory,
						   TBsChargingScheme scheme,
						   List<TBsAssetAccountStream> insertList,
						   Map<String,TBsRebillingInfo> accountMap){
		
		double kqQf = 0.0;
		if(CommonUtils.isEmpty(lastOwedHistory)){
			return kqQf;		//传入,欠费数据为空 不做抵扣,直接返回
		}
		
		//判断账户余额
		if(account.getAccountBalance() > 0){
			//含有余额才能抵扣
			//先抵扣违约金,应该不会存在循环多次的情况。如果账户有钱会在上一次进行扣减完成，制作本次
			for(Map m : lastOwedHistory){
				if(account.getAccountBalance() <= 0) break;
				if(BillingEnum.IS_USED_STOP.getIntV() == CommonUtils.null2Int(m.get("isUsed"))) continue;	//本条数据不再使用,跳过
				Date owedTime = new Date((Long)m.get("owedTime"));//欠费时间
				if(newHistory.getBillingTime().before(CommonUtils.changeDays(owedTime, scheme.getOverdueStartDates()))){
					//计费时间小于该笔违约金的开始计算时间
					continue;
				}
				double totalLateFee = CommonUtils.null2Double(m.get("totalLateFee"));//已产生的违约金
				//从账户进行抵扣 
				double balance = (account.getAccountBalance() > 0) ? account.getAccountBalance() : 0;//余额
				double kfLateFee = (totalLateFee >= balance) ? balance : totalLateFee;//扣减金额 
				kqQf += kfLateFee;
				account.setAccountBalance(account.getAccountBalance() - kfLateFee);	//账户直接扣除可扣除的
				
				insertList.add(new TBsAssetAccountStream(account.getId(), -kfLateFee, StreamEnum.purpose_billing_by_latefee.getV()));
				
			}
			
			//再抵扣本金
			for(Map m : lastOwedHistory){
				if(account.getAccountBalance() <= 0) break;
				
				if(BillingEnum.IS_USED_STOP.getIntV() == CommonUtils.null2Int(m.get("isUsed"))) continue;	//本条数据不再使用,跳过

				Date owedTime = new Date((Long)m.get("owedTime"));
				//TODO这里进行本金扣减不应该判断违约金吧
				/*if(newHistory.getBillingTime().before(CommonUtils.changeDays(owedTime, scheme.getOverdueStartDates()))){
					//计费时间小于该笔违约金的开始计算时间
					continue;
				}*/
				double owedAmount = CommonUtils.null2Double(m.get("owedAmount"));		//欠费金额
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
				insertList.add(new TBsAssetAccountStream(account.getId(), -kfLateFee,StreamEnum.purpose_billing.getV()));  //扣费
			}
		}
		assembleMap(account, accountMap, -kqQf);
		return kqQf;
	}
	
	/**
	 * 水费或电费计费(按照具体的builingcode进行单个计费)
	 * @param building  建筑信息
	 * @param lastBillDate 上次计费时间
	 * @param currentDetailBill 本期账单元数据
	 * @param totalBill 总计费账单
	 * @param meterType 表类型  0：水表   1：电表
	 */
	@SuppressWarnings("unused")
	private double waterBilling(TcBuilding building,
							  Date lastBillDate , 
							  TBsChargeBillHistory currentDetailBill,
							  TBsChargingScheme scheme,
							  TBsChargeBillTotal totalBill,
							  int meterType) {
		
		//根据计费方案查规则
		final List<TBsChargingRules> rulesList = tBsChargingRulesMapper.getTBsChargingRulesBySchemeId(scheme.getId());
		if(CollectionUtils.isEmpty(rulesList)){
			logger.info("计费方案 {} 下没有可用的计费规则，无法计费",scheme.getId());
			return 0.0;
		}
	
		//检查关联建筑是否有产生最新的抄表数据
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if( 1 == meterType ){
			paramMap.put("meterType", 1); //电表
		}
		if( 0 == meterType ){
		paramMap.put("meterType", 0); //水表
		}
		paramMap.put("projectId", totalBill.getProjectId());
		paramMap.put("lastBillTime", lastBillDate);
		paramMap.put("relationBuilding", building.getBuildingCode());//这里单一查询单个建筑的信息
		List<Map<String,Object>> resultMap = tcMeterDataMapper.getCountAndFeeObjByProjct(paramMap);
		if(CollectionUtils.isEmpty(resultMap)){
			logger.info("code为 {} 的建筑信息未找到可用的抄表数据,无法进行重新计费操作",building.getBuildingCode());
			return 0.0;
		}
		//根据计费规则查找关联的建筑和费用项
		double currentFee=0.0;//本期水/电费的总计费
		for(TBsChargingRules tBsChargingRules:rulesList){
			Map<String,Object> paramRuleMap = new HashMap<String,Object>();
			if( 1 == meterType ){
				paramRuleMap.put("meterType", 1);
			}
			if( 0 == meterType ){
				paramRuleMap.put("meterType", 0);
			}
			paramRuleMap.put("ruleId", tBsChargingRules.getId());
			paramRuleMap.put("projectId", totalBill.getProjectId());
			paramRuleMap.put("relationBuilding", building.getBuildingCode());//这里单一查询单个建筑的信息
			 //水电表查找有收费对象的关联建筑集合（只查询了builing_code）
			List<String> relationIdsList = tBsRuleBuildingRelationMapper.getBuildingCodeByRuleId(paramRuleMap);
			if(CollectionUtils.isEmpty(relationIdsList)){
				logger.info("规则id {} && 建筑id {} 下未找到符合要求的建筑信息，无法进行计费操作",tBsChargingRules.getId(),building.getBuildingCode());
				continue;
			}
			//查找费用项公式
			List<TBsChargeType> tBsChargingTypeList = tBsChargeTypeMapper.selectChargeType(tBsChargingRules.getId());
			if(CollectionUtils.isEmpty(tBsChargingTypeList)){
				logger.info("规则id {} 下未找到符合要求的收费项信息，无法进行计费操作",tBsChargingRules.getId());
				continue;
			}
			
			//每一个规则下的每一个建筑都需要通过这个规则下所有的费用项进行计算费用
			for(String ruleationId:relationIdsList){
				currentFee  +=  billingWaterElectFee(meterType,ruleationId,tBsChargingRules.getRuleName(),totalBill,tBsChargingTypeList,lastBillDate);
			}
		}
		return currentFee;
		
	}
	
	
	
	/**
	 * @TODO 对单个建筑计算水电费种详细数据
	 * @param meterType 表类型  0：水表  1：电表
	 * @return double 计费金额
	 */
	@SuppressWarnings({"unused" })
	private double billingWaterElectFee (	int meterType,
			  								String ruleationId,
			  								String ruleName,
			  								TBsChargeBillTotal currentTotalBill,
			  								List<TBsChargeType> tBsChargingTypeList,
			  								Date lastBillDate){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if( 1 == meterType ){
			paramMap.put("meterType", 1); //电表
		}
		if( 0 == meterType ){
			paramMap.put("meterType", 0); //水表
		}
		paramMap.put("projectId", currentTotalBill.getProjectId());
		paramMap.put("lastBillTime", lastBillDate);
		paramMap.put("relationBuilding", ruleationId);
		List<Map<String,Object>> resultDetailMap = tcMeterDataMapper.getCountAndFeeObjByProjct(paramMap);
		
		if(CommonUtils.isEmpty(resultDetailMap)){
			logger.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"规则名:"+ruleName+"对用的建筑编号:"+ruleationId+"没有找到抄表数据,请检查!"));
			return 0.0;
		}
		
		//计算本月账单
		Double currentFee = 0.0;
//		Calculator calculator = new Calculator();
		List<FeeItemDetail> list = new ArrayList<FeeItemDetail>();
		for(TBsChargeType tbChargType:tBsChargingTypeList){
			//得到公式
			Double CountValue =0.0;
			String feeItem="";
			Double minCriticalpoint = tbChargType.getMinCriticalpoint();
			Double maxCriticalpoint = tbChargType.getMaxCriticalpoint();
			for(int i=0;i<resultDetailMap.size();i++){
				String formulaInfoValue= tbChargType.getFormulaInfoValue(); //一个公式里面只能有一个计费项目
				//1、临界点一和临界点二都为0表示，没有临界点；该公式适用所有计算项目的值,所以不用做判断
				//2、临界点一为0；临界点二不为0；表示计费项目在小于临界点二的范围内
				//3、临界点一不为0.临界点二为0；表示计费项目在大于临界点一的范围内
				//4、临界点一和临界点二都不为0；表示计费项目在临界点一到临界点二的范围内
				String[] formulaArray = formulaInfoValue.split(" ");
				Double userCount =Double.parseDouble(String.valueOf(CommonUtils.isEmpty(resultDetailMap.get(i).get("useCount"))?"0":resultDetailMap.get(i).get("useCount")));
				Double peakCount = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(resultDetailMap.get(i).get("peakCount"))?"0":resultDetailMap.get(i).get("peakCount")));
				Double vallyCount = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(resultDetailMap.get(i).get("vallyCount"))?"0":resultDetailMap.get(i).get("vallyCount")));
				Double commonCount = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(resultDetailMap.get(i).get("commonCount"))?"0":resultDetailMap.get(i).get("commonCount")));
				//确定计费项目
				
				for(int j=0;j<formulaArray.length;j++){
					if(formulaArray[j].equals("$Count")){
//							formulaArray[j]=String.valueOf(userCount);
						feeItem="$Count";
					}
					if(formulaArray[j].equals("$PeakCount")){
//							formulaArray[j] = String.valueOf(peakCount);
						feeItem="$PeakCount";
					}
					if(formulaArray[j].equals("$VallCount")){
//							formulaArray[j] = String.valueOf(vallyCount);
						feeItem="$VallCount";
					}
					if(formulaArray[j].equals("$CommCount")){
//							formulaArray[j] = String.valueOf(commonCount);
						feeItem="$CommCount";
					}
				}
				if(feeItem.equals("$Count")){
					boolean flag = judgeCriticalpoint(minCriticalpoint,maxCriticalpoint,userCount);
					if(flag ==false) continue;
				}
				
				if(feeItem.equals("$PeakCount")){
					boolean flag = judgeCriticalpoint(minCriticalpoint,maxCriticalpoint,peakCount);
					if(flag ==false) continue;
				}
				
				if(feeItem.equals("$VallCount")){
					boolean flag = judgeCriticalpoint(minCriticalpoint,maxCriticalpoint,vallyCount);
					if(flag ==false) continue;
				}
				
				if(feeItem.equals("$CommCount")){
					boolean flag = judgeCriticalpoint(minCriticalpoint,maxCriticalpoint,commonCount);
					if(flag ==false) continue;
				}
				//通过公式计算结果
				Object obj = FormulaCalculationUtil.waterElectCalculation(formulaInfoValue,userCount, peakCount, vallyCount, commonCount);
				if(CommonUtils.isEmpty(obj)){
					logger.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"公式有误,不能做计算!"));
					continue;
				}else{
					if(obj.toString().equals("Infinity")){
						logger.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"公式计算有误,除数不能为0!"));
						continue;
					}else{
						currentFee += Double.parseDouble(String.valueOf(obj));
					}
				}
				
			}
		}
		if(currentFee>=0) {
			//进行四舍五入处理
			currentFee = new BigDecimal(String.valueOf(currentFee)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return currentFee;
	}
	
	
	
	/**
	 * 判断临界点
	 * @param minCriticalpoint
	 * @param maxCriticalpoint
	 * @param dosage
	 * @return
	 */
	private Boolean judgeCriticalpoint(Double minCriticalpoint,Double maxCriticalpoint,Double dosage){boolean flag=true;
		if(null == minCriticalpoint || null == maxCriticalpoint){
			flag=false;
			logger.info("临界点为空;不能计费!");
			return flag;
		}
		if(minCriticalpoint==0 && maxCriticalpoint==0){
			flag=true;
			return flag;
		}
		if(minCriticalpoint !=0 && maxCriticalpoint==0){
			if(dosage<minCriticalpoint){
				flag = false;
				logger.info(CommonUtils.log("计费项目["+dosage+"]不在["+minCriticalpoint+","+"+∞]范围内!"));
				return flag;
			}
		}
		if(minCriticalpoint ==0 && maxCriticalpoint !=0){
			if(dosage>maxCriticalpoint){
				flag = false;
				logger.info(CommonUtils.log("计费项目["+dosage+"]不在["+0+","+maxCriticalpoint+"]范围内!"));
				return flag;
			}
		}
		if(minCriticalpoint !=0 && maxCriticalpoint !=0){
			if(dosage <=minCriticalpoint || dosage>maxCriticalpoint){
				flag = false;
				logger.info(CommonUtils.log("计费项目["+dosage+"]不在["+minCriticalpoint+","+maxCriticalpoint+"]范围内!"));
				return flag;
			}
		}
		return flag;
	}


	/**
	 * @describe 费用修正
	 * @param companyId
	 * @param entity
	 * @param type
	 */
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto rebillingCorrect(String companyId, TBsRebillingInfo entity, Integer type) {
		MessageMap msgMap = null;
		
		if(CommonUtils.paramsHasNull(entity,entity.getBuildingCode(),entity.getFullName(),entity.getRebillingStartTime()) 
				|| entity.getType() == null || entity.getType() < 0 || entity.getType() > 5){
			msgMap = new MessageMap(MessageMap.INFOR_WARNING,"传入参数为空,无法重新计费.");
			logger.warn("手动重新计费: 传入参数为空,无法重新计费. 传入参数: {}",(entity == null) ? "" : entity.toString());
		}else{
			
			//检验传入的buildingCode是否含有非计费对象
			List<String> buildingCodes = CommonUtils.str2List(entity.getBuildingCode(), Constants.STR_COMMA);
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("buildingCodes", buildingCodes);
			paramMap.put("projectId", entity.getProjectId());
//			paramMap.put("isChargeObj", LookupItemEnum.yesNo_yes.getStringValue());
			List<TcBuilding> buildings = this.tcBuildingMapper.findByParams(paramMap);
			
			if(buildings.isEmpty()){
				msgMap = new MessageMap(MessageMap.INFOR_WARNING,"所有建筑都为非计费节点,无法重新计费.");
				logger.warn("手动重新计费: 所有建筑都为非计费节点,无法重新计费. 数据:{}.",entity.getBuildingCode());
				return new BaseDto(msgMap);
			}else if(buildings.size() < buildingCodes.size()){
				msgMap = new MessageMap(MessageMap.INFOR_SUCCESS,"有部分建筑为非计费节点,无法重新计费. ");
				logger.warn("手动重新计费: 有部分建筑为非计费节点. 传入数据: {}, 可计费数据: {}.",entity.getBuildingCode(), JSON.toJSONString(buildings));
			}else {
				msgMap = new MessageMap(null,"手动重新计费: 所有建筑节点都符合重新计费条件. ");
			}
			logger.info("手动重新计费: 将可计费数据投递至消息队列. 开始组装数据. ");
			MqEntity me = new MqEntity();
			me.setCompanyId(companyId);
			me.setOpr(type);
			if(entity.getType() == 5){
				//全部进行重新计费,丢四次消息队列
				for(int i = 1 ; i < 5 ; i++){
					entity.setType(i);
					me.setData(entity);
					this.amqpTemplate.convertAndSend(route_key_waterElect_rebilling, me);
					logger.info("手动重新计费: 将消息投递到消息队列完成. routeKey:{},数据:{}",route_key_waterElect_rebilling,me.toString());
				}
			}else{
				me.setData(entity);
				this.amqpTemplate.convertAndSend(route_key_waterElect_rebilling, me);
				logger.info("手动重新计费: 将消息投递到消息队列完成. routeKey:{},数据:{}",route_key_waterElect_rebilling,me.toString());
			}
			msgMap.setMessage(msgMap.getMessage().concat(" 开始异步计费 , 详细结果请稍后查看. "));
		}
		return new BaseDto(msgMap);
	}


	/**
	 * @describe 针对单个房屋的重新计费
	 * @param type 1-5  3.水，4.电
	 * 
	 */
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void singleRebilling(String companyId, TBsRebillingInfo entity, Integer type) {

		//这里builing_code是组装的
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
		List<TBsChargeBillHistory> insertHistories = new ArrayList<TBsChargeBillHistory>(); //需要重新插入有效的账单元数据
		List<TBsChargeBillHistory> updateHistories = new ArrayList<TBsChargeBillHistory>(); //失效之前的账单元数据
		List<TBsAssetAccountStream> insertStream = new ArrayList<TBsAssetAccountStream>();  //账户交易记录信息
		List<TBsOwedHistory> insertOhList = new ArrayList<TBsOwedHistory>();				//欠费信息
		int count = 0;
		
		//单个建筑进行循环操作
		for(String buildingCode : buildingCodes){
			paramMap.put("buildingCode", buildingCode);
			paramMap.put("startTime", entity.getRebillingStartTime());
			paramMap.put("type",type);
			//查询符合条件的历史账单信息
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
					newHistory.setLastPayed((null == lastHistory) ? 0 : CommonUtils.getSum(lastHistory.getCommonDesummoney(),lastHistory.getNoCommonDesummoney()));
					
					/*********水电费的修正费用稍微有点改动S*********/
					//  ( buildings.get(0),lastBillDate,newHistory,scheme,totalBill,meterType);
					TBsChargeBillTotal totalBilling=new TBsChargeBillTotal();
					totalBilling.setProjectId(newHistory.getProjectId());
					int meterType=6;//这里设定一个不正常的值即可
					if(3 == type) {
						meterType=0;
					}else if (4 == type) {
						meterType=1;
					}
					double currentFee=0.0;
					currentFee = waterBilling(building, lastBillDate, newHistory,scheme,totalBilling,meterType);
					/*********水电费的修正费用稍微有点改动E*********/
					
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
					newHistory.setAduitStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
					
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
					insertStream.add(new TBsAssetAccountStream(account.getId(), -(newHistory.getCurrentBillFee() - currKqQf), StreamEnum.purpose_billing.getV()));
					
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
			if( CommonUtils.isEmpty(rslt) || CommonUtils.isEmpty(rslt.get("id"))){
				break;
			}
			TBsChargeBillTotal currTotal = this.tBsChargeBillTotalMapper.selectById(CommonUtils.null2String(rslt.get("id")));
			currTotal.setCurrentFee(CommonUtils.null2Double(rslt.get("currentFee")));
			currTotal.setTotalFee(CommonUtils.null2Double(rslt.get("currentBillFee")));
			currTotal.setModifyTime(new Date());
			currTotal.setBillStatus(BillingEnum.BILL_STATUS_WHOLE.getIntV());
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
		return (type == 0) ? Constants.BILLING_WATER : Constants.BILLING_ELECT;
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
	 * @TODO 重新计费
	 */
	@Override
	public MessageMap waterElectReBillingOpr(String companyId, List<String> ids,String userId,Integer type) {
		if(CommonUtils.isEmpty(ids)){
			logger.warn("{}重新计费: 传入总账单为空 , 无法重新计费. ",returnFeeStr(type));
			return new MessageMap(MessageMap.INFOR_WARNING, "传入总账单为空,无法重新计费.");
		}
		
		//起消息队列进行异步重新计费, 同步返回数据
		MqEntity e = new MqEntity();
		e.setCompanyId(companyId);
		e.setUserId(userId);
		e.setData(ids);
		e.setOpr(type);
		logger.info("消息队列: [{}重新计费]  发送消息至队列  [开始]: routeKey:{} , 数据:{}", returnFeeStr(type),waterElect_billing_rebilling_key,e.toString());
		this.amqpTemplate.convertAndSend(waterElect_billing_rebilling_key, e);
		logger.info("消息队列: [{}重新计费]  发送消息至队列  [完成]: routeKey:{} , 数据:{}", returnFeeStr(type),waterElect_billing_rebilling_key,e.toString());
		
		
		return new MessageMap(null, "开始异步计费,请稍后刷新页面查看.");
	}
	
	

}
