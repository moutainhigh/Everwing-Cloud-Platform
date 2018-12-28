package com.everwing.coreservice.wy.core.service.impl.configuration.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.ThreadPool.ThreadPoolUtils;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.utils.BigDecimalUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.TcOrderComplaintAndCompleteEnum;
import com.everwing.coreservice.common.wy.datasource.DataSourceUtil;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.bill.FeeItemDetail;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TempCurrentFee;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargeType;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingRules;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.configuration.support.BillingSupEntity;
import com.everwing.coreservice.common.wy.entity.order.TcOrderComplete;
import com.everwing.coreservice.common.wy.fee.constant.AcChargeDetailBusinessTypeEnum;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.dto.AcChargeDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.BillDto;
import com.everwing.coreservice.common.wy.service.configuration.task.WaterAndElectBillingTaskService;
import com.everwing.coreservice.wy.core.mq.MqSender;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.TBsProjectMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillHistoryMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillTotalMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargeTypeMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargingRulesMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargingSchemeMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsRuleBuildingRelationMapper;
import com.everwing.coreservice.wy.dao.mapper.order.complete.TcOrderCompleteMapper;
import com.everwing.utils.FormulaCalculationUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service("electFeeBillingTaskServiceImpl")
public class WaterAndElectBillingTaskServiceImpl extends Resources implements WaterAndElectBillingTaskService{

	private static final Logger log = LogManager.getLogger(WaterAndElectBillingTaskServiceImpl.class);
	
	private static final String LOG_STR = "当前时间 : %s , 项目  -> %s : %s";
	//消息队列 route_key 声明处
	@Value("${queue.wy2wyBilling.waterelectFee.manual.key}")
	private String waterelect_billing_manaul_route_key;		//水电费手动计费用路由键
	
	@Value("${queue.wy2wyBilling.electFee.auto.key}")
	private String elect_billing_auto_route_key; //电费自动计费
	
	@Value("${queue.wy2wyBilling.waterFee.auto.key}")
	private String water_billing_auto_route_key;
	
	@Value("${batch.add.count}")
	private Integer BATCH_ADD_COUNT;
	
	@Value("${batch.update.count}")
	private Integer BATCH_UPDATE_COUNT;
	
	private static final String MANAUL_STR = "手动";
	
	private static final String AUTO_STR = "自动";
	
	@Autowired
	private TBsProjectMapper tBsProjectMapper;
	
	@Autowired
	private TBsChargingSchemeMapper tBsChargingSchemeMapper;
	
	@Autowired
	private TBsChargingRulesMapper tBsChargingRulesMapper;
	
	@Autowired
	private TBsRuleBuildingRelationMapper tBsRuleBuildingRelationMapper;
	
	@Autowired
	private TBsChargeTypeMapper tBsChargeTypeMapper;
	
	@Autowired
	private  TBsChargeBillTotalMapper tBsChargeBillTotalMapper;
	
	@Autowired
	private TcMeterDataMapper tcMeterDataMapper;
	
	@Autowired
	private TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;
	
	@Autowired
	private TcOrderCompleteMapper tcOrderCompleteMapper;
	
	@Autowired
	private MqSender mqSender;
	
	private static Map<String,String> projectIdAndCompanyIdmap = new HashMap<String,String>(); //用来存放项目编号和公式编号的组合，防止重复操作
	

	/**
	 * 手动进行水费和电费的计费操作
	 */
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto manualElectBilling(final String companyId,final String flag,final TBsProject entity) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		String companyIdAndProjectId=companyId+"_"+entity.getProjectId();
		try {
			if(StringUtils.isBlank(entity.getId())){
				msgMap.setMessage("传入计费项目id为空.");
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				log.warn(String.format("当前时间 : %s , "+(flag.equals("elect")?"电费计费":"水费计费")+"异常  -> %s" ,CommonUtils.getDateStr(),msgMap.getMessage()));
				baseDto.setMessageMap(msgMap);
				return baseDto;
			}
			if(projectIdAndCompanyIdmap.containsKey(companyIdAndProjectId)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("项目["+entity.getProjectName()+"]正在进行手动计费;请稍后进行操作");
				log.warn("项目["+entity.getProjectName()+"]正在进行手动计费;请稍后进行操作");
				baseDto.setMessageMap(msgMap);
				return baseDto;
			}
			
			//将项目边和和公司编号存放到projectIdAndCompanyIdmap
			projectIdAndCompanyIdmap.put(companyIdAndProjectId, companyIdAndProjectId);
			
			//根据项目ID查询，该项目的水电费是否有计费完成，如果完成了则不能继续手动计费，只能重新计费
			TBsProject tbsp= this.tBsProjectMapper.findById(entity.getId());
			if(CommonUtils.isEmpty(tbsp)){
				msgMap.setMessage("计费项目["+entity.getProjectName()+"]不存在，可能已经被删除!");
				log.warn(String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(),msgMap.getMessage()));
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				baseDto.setMessageMap(msgMap);
				projectIdAndCompanyIdmap.remove(companyIdAndProjectId);
				return baseDto;
			}else{
				if(flag.equals("elect")){
					if(tbsp.getElectStatus().equals(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV())){ //电费计费完成 不能手动计费
						msgMap.setMessage("计费项目["+entity.getProjectName()+"]电费计费完成,不能再次手动计费!");
						log.warn(String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(),msgMap.getMessage()));
						msgMap.setFlag(MessageMap.INFOR_ERROR);
						baseDto.setMessageMap(msgMap);
						projectIdAndCompanyIdmap.remove(companyIdAndProjectId);
						return baseDto;
					}
				}
				if(flag.equals("water")){
					if(tbsp.getWaterStatus().equals(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV())){ //水费计费完成不能手动计费
						msgMap.setMessage("计费项目["+entity.getProjectName()+"]水费计费完成,不能再次手动计费!");
						log.warn(String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(),msgMap.getMessage()));
						msgMap.setFlag(MessageMap.INFOR_ERROR);
						baseDto.setMessageMap(msgMap);
						projectIdAndCompanyIdmap.remove(companyIdAndProjectId);
						return baseDto;
					}
				}
			}
			//1. 根据projectCode判断当前项目是否开启
			TBsProject paramObj = new TBsProject();
			paramObj.setId(entity.getId());
			if(flag.equals("elect")){
				paramObj.setElectStatus(BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV());	//电费未计费状态
			}
			if(flag.equals("water")){
				paramObj.setWaterStatus(BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV()); //水费计费状态
			}
			
			paramObj.setStatus(BillingEnum.STATUS_START.getIntV());	
			
			final TBsProject tBsProject = tBsProjectMapper.findByObj(paramObj);
			if(CommonUtils.isEmpty(tBsProject)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				if(flag.equals("water")){
					log.warn(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getId() , "该项目未开启计费,水费计费未启动或已计费,手动计费动作完成."));
					msgMap.setMessage("该项目未开启计费,水费计费未启动或已计费,手动计费动作完成.");
				}
				if(flag.equals("elect")){
					log.warn(String.format(LOG_STR ,CommonUtils.getDateStr(), entity.getId() , "该项目未开启计费,电费计费未启动或已计费,手动计费动作完成."));
					msgMap.setMessage("该项目未开启计费,电费计费未启动或已计费,手动计费动作完成.");
				}
				baseDto.setMessageMap(msgMap);
				projectIdAndCompanyIdmap.remove(companyIdAndProjectId);
				return baseDto;
			}
			final TBsChargingScheme scheme = hasCanBillingScheme(entity,flag,false);
			if(CommonUtils.isEmpty(scheme)){
				outPutLog(tBsProject.getProjectName(),flag.equals("elect")?"该项目未找到可用的电费计费方案,手动计费动作完成.":"该项目未找到可用的水费计费方案,手动计费动作完成.");
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(flag.equals("elect")?"该项目未找到可用的电费计费方案,手动计费动作完成.":"该项目未找到可用的水费计费方案,手动计费动作完成.");
				baseDto.setMessageMap(msgMap);
				projectIdAndCompanyIdmap.remove(companyIdAndProjectId);
				return baseDto;
			}else if(BillingEnum.TYPE_AUTO.getIntV() == scheme.getChargingType()){
				//自动计费,无法手动计费
				outPutLog(tBsProject.getProjectName(),flag.equals("elect")?"该项目的电费为自动计费状态,手动计费动作完成.":"该项目的水费为自动计费状态,手动计费动作完成.");
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(flag.equals("elect")?"该项目的电费为自动计费状态,手动计费动作完成.":"该项目的水费为自动计费状态,手动计费动作完成.");
				baseDto.setMessageMap(msgMap);
				projectIdAndCompanyIdmap.remove(companyIdAndProjectId);
				return baseDto;
			}
			
			//根据计费方案查规则（这个规则是存在一个计费方案多条规则的）-- 理论一个资产只有一种计费规则
			final List<TBsChargingRules> rulesList = tBsChargingRulesMapper.getTBsChargingRulesBySchemeId(scheme.getId());
			if(CollectionUtils.isEmpty(rulesList)){
				outPutLog(scheme.getSchemeName(),flag.equals("elect")?"该电费计费方案下没有计费规则,手动计费动作完成.":"该水费计费方案下没有计费规则,手动计费动作完成");
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(flag.equals("elect")?"该电费计费方案下没有计费规则,手动计费动作完成.":"该水费计费方案下没有计费规则,手动计费动作完成");
				baseDto.setMessageMap(msgMap);
				projectIdAndCompanyIdmap.remove(companyIdAndProjectId);
				return baseDto;
			}
			
			//根据关联建筑&&费用项公式,计算费用
			//先查找判断本期的中账单是否已经生成 , 若已经生成, 则直接修改部分参数 , 若未生成 , 则创建一个TBsChargeBillTotal对象 
			TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
			paramTotal.setProjectId(tBsProject.getProjectId());
			paramTotal.setType(scheme.getSchemeType());
			paramTotal.setChargingType(BillingEnum.TYPE_MANUAL.getIntV());
			List<TBsChargeBillTotal> totals = tBsChargeBillTotalMapper.findCurrentBillTotal(paramTotal);
			if(!totals.isEmpty()){
				
				TBsChargeBillTotal currentTotal = totals.get(0);
				
				if(CommonUtils.isNotEmpty(currentTotal.getLastTotalId())){
					TBsChargeBillTotal lastTotal = tBsChargeBillTotalMapper.selectById(currentTotal.getLastTotalId());
					
					if(new Date().compareTo(CommonUtils.addMonth(lastTotal.getBillingTime(), 1)) < 0){
						msgMap.setFlag(MessageMap.INFOR_ERROR);
						msgMap.setMessage("当前时间: " 
										   + CommonUtils.getDateStr() 
										   + ", 距离上月手动计费时间: "
										   + CommonUtils.getDateStr(lastTotal.getBillingTime())
										   + " 少于一个月,无法手动计费, 此次手动计费完成. ");
						
						log.warn("当前时间: {}, 距离上月手动计费时间: {} 少于一个月,无法手动计费, 此次手动计费完成. ",CommonUtils.getDateStr(),CommonUtils.getDateStr(lastTotal.getBillingTime()));
						baseDto.setMessageMap(msgMap);
						projectIdAndCompanyIdmap.remove(companyIdAndProjectId);
						return baseDto;
					}
				}
			}
			
			RemoteModelResult<Company> rslt =  this.companyApi.queryCompany(companyId);
			if(!rslt.isSuccess() || rslt.getModel() == null){
				log.warn("切换数据源失败。");
				return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"切换数据源失败。"));
			}
			final String companyStr = JSON.toJSONString(rslt.getModel());
			
			//这里启动一个线程处理计费方案,为了防止dubbo超时，将需要操作费用表的数据发送到MQ异步执行。
			ThreadPoolUtils.getInstance().executeThread(new Runnable() {
				@Override
				public void run() {
					//切换数据源
					DataSourceUtil.changeDataSource(companyStr);
					tBsProject.setModifyId(entity.getModifyId());
					tBsProject.setModifyTime(new Date());
					
					String nextTotalId = CommonUtils.getUUID();
					Map<String,Object> returnMap = hasTotalBillInCurrentCycle(tBsProject,flag,scheme);
					TBsChargeBillTotal currentTotalBill = (TBsChargeBillTotal) returnMap.get("entity");
					//根据本次的总账单，检查上一个总账单是否有审核，如果是未审核的状态则不能做本次的计费;加上这个判断限制主要是为了防止重复扣费
					String lastTotalId= currentTotalBill.getLastTotalId();
					if(StringUtils.isNotBlank(lastTotalId)){
						//如果上期总账单编号Id是空；则说明系统第一次计费，故没有上期总账单;
						TBsChargeBillTotal  lastBillTotal = tBsChargeBillTotalMapper.findTbsTotalbyId(lastTotalId);
						if(CommonUtils.isNotEmpty(lastBillTotal)){
							if(CommonUtils.isNotEmpty(lastBillTotal.getAuditStatus()) && lastBillTotal.getAuditStatus() !=1){
								log.info(CommonUtils.log("总账单编号为["+lastTotalId+"]的总账单还未审核，不能做计费;手动计费停止...."));
								return;
							}
						}
					}
					Date lastBillDate = (Date) returnMap.get("lastBillDate");  //方案启用时间
					
					
					List<TBsChargeBillHistory> insertList = new ArrayList<TBsChargeBillHistory>();
					List<TBsChargeBillHistory> updateList = new ArrayList<TBsChargeBillHistory>();
					
					List<AcChargeDetailDto> insertCurrentList =new ArrayList<AcChargeDetailDto>();
					
					//检查关联建筑是否有产生最新的抄表数据
					Map<String,Object> paramMap = new HashMap<String,Object>();
					if(flag.equals("elect")){
						paramMap.put("meterType", 1); //电表
					}
					if(flag.equals("water")){
						paramMap.put("meterType", 0); //水表
					}
					paramMap.put("projectId", tBsProject.getProjectId());
					paramMap.put("lastBillTime", lastBillDate);
					
					//这里得到抄表数据信息，房号，用量，本期读数，上期读数
					List<Map<String,Object>> resultMap = tcMeterDataMapper.getCountAndFeeObjByProjct(paramMap);
					if(CollectionUtils.isEmpty(resultMap)){
						outPutLog(tBsProject.getProjectName(),flag.equals("elect")?"电表抄表数据未产生或者审核未通过,手动计费动作完成":"水表抄表数据未产生或者审核未通过,手动计费动作完成");
						return;
					}
					//根据计费规则查找关联的建筑和费用项(一个资产有一个计费规则但是一个计费规则会有多个计费项)
					for(TBsChargingRules tBsChargingRules:rulesList){
						//计费的全部核心
						Map<String,Object> paramRuleMap = new HashMap<String,Object>();
						if(flag.equals("elect")){
							paramRuleMap.put("meterType", 1);
						}
						if(flag.equals("water")){
							paramRuleMap.put("meterType", 0);
						}
						paramRuleMap.put("ruleId", tBsChargingRules.getId());
						paramRuleMap.put("projectId", tBsProject.getProjectId());
						List<String> relationIdsList = tBsRuleBuildingRelationMapper.getBuildingCodeByRuleId(paramRuleMap); //水电表查找有收费对象的关联建筑
						if(CollectionUtils.isEmpty(relationIdsList)){
							outPutLog(tBsChargingRules.getRuleName(),flag.equals("elect")?"该电费计费规则没有关联建筑,手动计费动作完成.":"该水费计费规则没有关联建筑,手动计费动作完成.");
							continue;
						}
						//查找费用项公式（计费公式）-- 计费项（如：标准水费，污水处理费 == ）
						List<TBsChargeType> tBsChargingTypeList = tBsChargeTypeMapper.selectChargeType(tBsChargingRules.getId());
						if(CollectionUtils.isEmpty(tBsChargingTypeList)){
							outPutLog(tBsChargingRules.getRuleName(),flag.equals("elect")?"该电费计费规则没有收费项,手动计费动作完成.":"该水费计费规则没有收费项,手动计费动作完成.");
							continue;
						}
						
						/**
						 * 每个规则都要对房屋集合进行循环，不然会漏掉，有可能一个资产需要多个计费项进行计费
						 */
						for(String ruleationId:relationIdsList){
//								if(!ruleationId.equals("1019_1000_10000_10009_10000_10000_10000_10000")) continue;
							boolean bool = billingWaterElectFee(companyId,scheme,ruleationId,flag,tBsChargingRules.getRuleName(),currentTotalBill,tBsChargingTypeList,lastBillDate,nextTotalId,insertList,updateList,MANAUL_STR,insertCurrentList);
							if(bool==false) continue;
						}
					}
					//超过定义的批量数量后剩下的。
					BillingSupEntity se = new BillingSupEntity();
					if(insertList.size()>0){
						se.setInsertList(insertList);
					}
					if(updateList.size()>0){
						se.setUpdateList(updateList);
					}
					sendMessage(companyId,se);
					
					if( insertCurrentList.size() > 0 ){
						mqSender.sendAcChargeDetailList(insertCurrentList,companyId);
						insertCurrentList.clear();
						log.info("***********调用批量插入AcCurrentChargeDetail 成功***********");
					}
					
					//计算完毕之后,生成下个月的总账单 , 账单的上期数据由上方的总账单传入
					TBsChargeBillTotal nextBillTotal = new TBsChargeBillTotal();
					nextBillTotal.setId(nextTotalId);
					nextBillTotal.setAuditStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
					nextBillTotal.setChargingType(BillingEnum.TYPE_MANUAL.getIntV());
					nextBillTotal.setProjectId(tBsProject.getProjectId());
					nextBillTotal.setType(scheme.getSchemeType());
					nextBillTotal.setLastOwedFee(0.0);
					nextBillTotal.setSunStatus(0); //未汇总
					nextBillTotal.setIsRebilling(BillingEnum.IS_REBILLING_NO.getIntV());
					nextBillTotal.setCmacIsBilling(BillingEnum.common_account_is_not_bill.getIntV());
					nextBillTotal.setCreateId(entity.getModifyId());
					nextBillTotal.setCreateTime(new Date());
					nextBillTotal.setModifyTime(new Date());
					nextBillTotal.setModifyId(entity.getModifyId());
					nextBillTotal.setSchemeId(scheme.getId());
					nextBillTotal.setLastTotalId(currentTotalBill.getId());
					
					//获取本期的总金额
					
					if((boolean)returnMap.get("isInsert")){
						tBsChargeBillTotalMapper.insertChargeBillTotal(currentTotalBill);
					}else{
						tBsChargeBillTotalMapper.update(currentTotalBill); 
					}
					tBsChargeBillTotalMapper.insertChargeBillTotal(nextBillTotal);
					if(flag.equals("elect")){
						tBsProject.setElectStatus(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV());
					}
					if(flag.equals("water")){
						tBsProject.setWaterStatus(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV());
					}
					
					tBsProject.setCurrentFee(CommonUtils.null2Double(tBsProject.getCurrentFee()) + CommonUtils.null2Double(currentTotalBill.getCurrentFee()));
					tBsProject.setLastOwedFee(CommonUtils.null2Double(tBsProject.getLastOwedFee()) + CommonUtils.null2Double(currentTotalBill.getLastOwedFee()));
					tBsProject.setTotalFee(CommonUtils.null2Double(tBsProject.getTotalFee()) + CommonUtils.null2Double(currentTotalBill.getTotalFee()));
					tBsProjectMapper.update(tBsProject);
					log.info(getLogStr(tBsProject.getProjectName(), flag.equals("elect")?"电费":"水费"+"手动计费完成"));
				}});
			 
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			msgMap.setMessage(getFeeType(flag)+"异步手动计费开始,请稍后查看详情.");
			baseDto.setMessageMap(msgMap);
			projectIdAndCompanyIdmap.remove(companyIdAndProjectId);
			return baseDto;
		} catch (Exception e) {
			projectIdAndCompanyIdmap.remove(companyIdAndProjectId);
			log.info(String.format("当前时间 : %s , "+(flag.equals("elect")?"电费计费":"水费计费")+"异常  -> %s" ,CommonUtils.getDateStr(),e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}
	
	/**
	 * @TODO 判断在本周期内是否已经产生总账单 , 若未产生,则需要生成一张新的总账单
	 * @param project
	 * @param scheme

	 * @return
	 */
	private Map<String,Object> hasTotalBillInCurrentCycle(TBsProject project ,String meterFlag,TBsChargingScheme scheme){
		Map<String,Object> returnMap = new HashMap<String, Object>();
		boolean flag = false;
		TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
		paramTotal.setProjectId(project.getProjectId());
		paramTotal.setType(scheme.getSchemeType());
//		paramTotal.setChargingType(BillingEnum.TYPE_MANUAL.getIntV());
		List<TBsChargeBillTotal> totals = tBsChargeBillTotalMapper.findCurrentBillTotal(paramTotal);
		
		TBsChargeBillTotal currentTotalBill =null;
		Date lastBillDate = scheme.getStartUsingDate();
		if(CommonUtils.isNotEmpty(totals)){
			if(totals.size()==1){
				currentTotalBill = totals.get(0);
			}else if(totals.size()>1){
				//这里两个总单 只有两种 一个总单，计费时间不为空，且切分砖头未2    另外一个总单 计费时间为空，且然后计费状态为0  是未计费状态
				for(TBsChargeBillTotal totalBill:totals){
					Date billtime = totalBill.getBillingTime();
					Integer billStatus = totalBill.getBillStatus();
					if(CommonUtils.isNotEmpty(billtime) && billStatus.equals(BillingEnum.BILL_STATUS_APART.getIntV())){
						currentTotalBill = totalBill; //这个表示部分计费的总单
						break;
					}else{
						currentTotalBill = totalBill;
					}
				}
			}
			TBsChargeBillTotal lastBill = tBsChargeBillTotalMapper.selectById(currentTotalBill.getLastTotalId());
			if(null != lastBill)  lastBillDate = lastBill.getBillingTime();
			
		}else{
			
			log.info(meterFlag.equals("elect")?"电费计费: 当前未找到本月的计费总账单,需要重新生成. 项目: {} ":"水费计费: 当前未找到本月的计费总账单,需要重新生成. 项目: {} ",project.toString());
			currentTotalBill = new TBsChargeBillTotal();
			
			currentTotalBill.setId(CommonUtils.getUUID());
			currentTotalBill.setProjectId(project.getProjectId());
			currentTotalBill.setType(scheme.getSchemeType());
			currentTotalBill.setLastOwedFee(0.0);
			currentTotalBill.setIsRebilling(BillingEnum.IS_REBILLING_NO.getIntV());
			currentTotalBill.setAuditStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
			currentTotalBill.setChargingType(BillingEnum.TYPE_MANUAL.getIntV());
			currentTotalBill.setCreateId(project.getModifyId());
			currentTotalBill.setCreateTime(new Date());
			currentTotalBill.setCmacIsBilling(BillingEnum.common_account_is_not_bill.getIntV());
			flag = true;
			
		}
		currentTotalBill.setModifyTime(new Date());
		currentTotalBill.setModifyId(project.getModifyId());
		currentTotalBill.setBillingTime(new Date());
		currentTotalBill.setSchemeId(scheme.getId());
		currentTotalBill.setSunStatus(0); //未汇总
		
		returnMap.put("entity", currentTotalBill);
		returnMap.put("isInsert", flag);
		returnMap.put("lastBillDate", lastBillDate);
		return returnMap;
	}
	
	//判断后日志输出
	private void outPutLog(String moduleName,String loging){
			log.warn(getLogStr(moduleName, loging));
	}
	
	private String getLogStr(String projectName , String logStr){
		return String.format(LOG_STR ,CommonUtils.getDateStr(), projectName,logStr);
	}
	
	/**
	 * @TODO 计算水电费种详细数据,并生成下个月的数据
	 * @return
	 */
	private boolean billingWaterElectFee (String companyId,
			                         TBsChargingScheme scheme,
									  String ruleationId,
									  String meterFlag,
									  String ruleName,
			                         TBsChargeBillTotal currentTotalBill,
			                         List<TBsChargeType> tBsChargingTypeList,
									 Date lastBillDate,
									 String nextTotalId,
									 List<TBsChargeBillHistory> insertList,
									 List<TBsChargeBillHistory> updateList,
									 String oprStr,
									 List<AcChargeDetailDto> insertCurrentList
									 )
		{
			//根据收费对象ID查找表，如果表不存在或者表不为收费对象，则跳过
			if(meterFlag.equals("elect")){
				ElectMeter electmeter = tcElectMeterMapper.getElectMeterByReationId(currentTotalBill.getProjectId(), ruleationId);
				if(CommonUtils.isEmpty(electmeter)){
					log.info(CommonUtils.log("关联建筑编码["+ruleationId+"]没有可用的电表!"));
					return false;
				}else{
					if(electmeter.getIsbilling() !=null && electmeter.getIsbilling().equals(1)){
						log.info(CommonUtils.log("关联建筑编码["+ruleationId+"]对应的电表不为收费表，不用计费"));
						return false;
					}
				}
			}
			if(meterFlag.equals("water")){
				List<TcWaterMeter> tcWater = tcWaterMeterMapper.findByRelarionId(currentTotalBill.getProjectId(), ruleationId);
				if(CommonUtils.isEmpty(tcWater)){
					log.info(CommonUtils.log("关联建筑编码["+ruleationId+"]没有可用的水表!"));
					return false;
				}else{
					if(tcWater.get(0).getIsBilling() !=null && tcWater.get(0).getIsBilling().equals(1)){
						log.info(CommonUtils.log("关联建筑编码["+ruleationId+"]对应的水表不为收费表，不用计费"));
						return false;
					}
				}
			}
			Map<String,Object> paramMap = new HashMap<String,Object>();
			if(meterFlag.equals("elect")){
				paramMap.put("meterType", 1); //电表
			}
			if(meterFlag.equals("water")){
				paramMap.put("meterType", 0); //水表
			}
			paramMap.put("projectId", currentTotalBill.getProjectId());
			paramMap.put("lastBillTime", lastBillDate);
			paramMap.put("relationBuilding", ruleationId);
			
			//这里重复查询了本期读数，上期读数，用量这些数据   -- 2018-06-08  qhc
			List<Map<String,Object>> resultDetailMap = tcMeterDataMapper.getCountAndFeeObjByProjct(paramMap);
			//得到产权变更未计费的表读数
		 	List<Map<String, Object>> changeAssetData = this.tcOrderCompleteMapper.getNoBill(ruleationId,currentTotalBill.getProjectId());
			//这里还需要找产权变更的抄表
		 	if(CommonUtils.isEmpty(resultDetailMap) && CommonUtils.isEmpty(changeAssetData)){
				log.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"规则名:"+ruleName+"对用的建筑编号:"+ruleationId+"没有找到抄表数据,请检查!"));
				return false;
			}
		 	boolean returnMessage1=true;
		 	boolean returnMessage2=true;
		 	//正常计费的情况
			if(CommonUtils.isNotEmpty(resultDetailMap)){
				returnMessage1= calculationByUseCount(currentTotalBill,ruleationId,resultDetailMap,tBsChargingTypeList,oprStr,nextTotalId,insertList,updateList,meterFlag,companyId,scheme,insertCurrentList);
			}
			
			//这里有一个特殊情况是进行了产权变更的情况
			if(CommonUtils.isNotEmpty(changeAssetData)){
				returnMessage2=calculationByUseCount(currentTotalBill,ruleationId,changeAssetData,tBsChargingTypeList,oprStr,nextTotalId,insertList,updateList,meterFlag,companyId,scheme,insertCurrentList);
			}
			
			//计费完之后，更改那些产权变更工单为已经计费状态
			for(Map<String, Object> mapComplete:changeAssetData){
				String orderCompleteId = String.valueOf(mapComplete.get("id"));
				TcOrderComplete  tcOrderComplete = this.tcOrderCompleteMapper.findById(orderCompleteId);
				tcOrderComplete.setIsAlreadyBilling(TcOrderComplaintAndCompleteEnum.IS_BILLING_YES.getIntV());
				this.tcOrderCompleteMapper.singleUpdate(tcOrderComplete);
			}
			if(returnMessage1==true && returnMessage2 ==true){
				return true;
			}else{
				return false;
			}	
		}
	
	
	/**
	 * 进行核心的计出金额的水电费计费操作
	 * @param currentTotalBill  本期总账单
	 * @param ruleationId	计费规则id
	 * @param resultDetailMap   计费详情(用量，上次读数，这次读数)
	 * @param tBsChargingTypeList  计费项集合
	 * @param oprStr
	 * @param nextTotalId   下期总账单
	 * @param insertList    需要做插入的集合
	 * @param updateList	需要做修改的集合	
	 * @param meterFlag     表类型(elect 电表   water 水表)
	 * @param companyId   公司id
	 * @param scheme     计费方案
	 * @return
	 */
	private Boolean calculationByUseCount( TBsChargeBillTotal currentTotalBill, 
										   String ruleationId,
										   List<Map<String,Object>> resultDetailMap,
										   List<TBsChargeType> tBsChargingTypeList,
										   String oprStr, 
										   String nextTotalId, 
										   List<TBsChargeBillHistory> insertList,
										   List<TBsChargeBillHistory> updateList,
										   String meterFlag,
										   String companyId,
										   TBsChargingScheme scheme,
										   List<AcChargeDetailDto> insertCurrentList ){
		boolean returnMessage = true;
		//查找判断本月的该建筑的数据是否已经生成,若未生成,则生成本月的
		TBsChargeBillHistory paramBill = new TBsChargeBillHistory();
		paramBill.setProjectId(currentTotalBill.getProjectId());
		paramBill.setBuildingCode(ruleationId);
		paramBill.setChargeTotalId(currentTotalBill.getId());
		List<TBsChargeBillHistory> histories = this.tBsChargeBillHistoryMapper.findCurrentDetailBill(paramBill);
		
		TBsChargeBillHistory currentDetailBill =null;
		int type = 0;
		boolean isInsert = false;
		if(CommonUtils.isNotEmpty(histories)){
			currentDetailBill =  histories.get(0);
			currentDetailBill.setModifyId(currentDetailBill.getModifyId());
			currentDetailBill.setModifyTime(new Date());
		}else{
			//这里新增一个第一次计费，如果之前用户就有欠费的情况
			
			if(meterFlag.equals("elect")){
				type = 4; //电表
			}else if(meterFlag.equals("water")) {
				type = 3;
			}
			
			TBsAssetAccount account = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(ruleationId, type);
			double lastBillFee = 0.0;
			if(account != null && CommonUtils.null2Double(account.getAccountBalance()) < 0){
				lastBillFee = Math.abs(account.getAccountBalance());
			}
			
			currentDetailBill = new TBsChargeBillHistory();
			
			currentDetailBill.setId(CommonUtils.getUUID());
			currentDetailBill.setIsUsed(0);
			currentDetailBill.setChargeTotalId(currentTotalBill.getId());
			currentDetailBill.setBuildingCode(ruleationId);
			currentDetailBill.setLateFee(0.0);
			currentDetailBill.setFullName((String)resultDetailMap.get(0).get("buildingFullName"));
			currentDetailBill.setLastBillFee(lastBillFee);
			currentDetailBill.setLastBillId(null);
			currentDetailBill.setLastPayed(0.0);
			currentDetailBill.setCreateId(currentDetailBill.getModifyId());
			currentDetailBill.setCreateTime(new Date());
			currentDetailBill.setProjectId(currentTotalBill.getProjectId());
			isInsert = true;
		}
		
		BillDto bill = new BillDto();
		//*************新增写入账单数据S*************
		bill.setTotal( "0.0" );
		bill.setCurrFee( "0.0" );
		bill.setLateFee( CommonUtils.null2Double( currentDetailBill.getLateFee() ) > 0 ? currentDetailBill.getLateFee().toString() : "0.0" );
		bill.setShareFee( CommonUtils.null2Double( currentDetailBill.getShareFee()) >0  ? currentDetailBill.getShareFee().toString() : "0.0");
		//*************新增写入账单数据E*************
		
		//如果这里是虚拟表，这里费用不需要累加，（项目里只计子表，不计总表虚拟表，因为虚拟总表的用量是其下各个子表的用量之和）
		if(currentTotalBill.getType().equals(4)){//电表
			//查找是否是虚拟电表总表（虚拟总表是不进行计费的，只是会在所有子表计费后进行费用的汇总）
			ElectMeter electMeter = this.tcElectMeterMapper.findMByBuildCodeAndProjectId(currentDetailBill.getProjectId(),currentDetailBill.getBuildingCode());
			if(!CommonUtils.isEmpty(electMeter)){
//				currentTotalBill.setCurrentFee(CommonUtils.null2Double(currentTotalBill.getCurrentFee()) + currentDetailBill.getCurrentFee());	//总金额累计
//				currentTotalBill.setTotalFee(CommonUtils.null2Double(currentTotalBill.getTotalFee()) + currBillFee);
				TBsChargeBillHistory nextBill = createNextBill(ruleationId,nextTotalId,(String)resultDetailMap.get(0).get("buildingFullName"),currentTotalBill.getProjectId(),currentDetailBill);
				currentDetailBill.setBillingTime(new Date());
				//TODO 这里其实都还没有计出费用就行了对本期账单的更新操作，查看后面是否又进行了一次更新，如果计费结束后依然更新，这里的去掉
				/*if(isInsert){
					insertList.add(currentDetailBill);
				}else{
					updateList.add(currentDetailBill);
				}*/
				insertList.add(nextBill);
				sendMessageQueue(insertList,updateList,companyId,meterFlag,oprStr);
				//同样这里需要同步到新账户数据
				AcChargeDetailDto acCurrentChargeD =  getAcCurrentChargeDetail( companyId,bill,currentTotalBill.getType(),
						scheme.getProjectId(),scheme.getProjectName(),ruleationId,
						CommonUtils.null2String(currentDetailBill.getLastBillId()) ,
						CommonUtils.isEmpty(currentDetailBill.getCurrentBillFee()) ? 0.0 : currentDetailBill.getCurrentBillFee() );
				insertCurrentList.add(acCurrentChargeD);
				return returnMessage;
			}
		}else if(currentTotalBill.getType().equals(3)){//水表
			//查找是否是虚拟水表总表 ..如果是项目则不参与累加
			TcWaterMeter tcWaterMeter = this.tcWaterMeterMapper.findMByBuildCodeAndProjectId(currentDetailBill.getProjectId(),currentDetailBill.getBuildingCode());
			if(!CommonUtils.isEmpty(tcWaterMeter)){
//				currentTotalBill.setCurrentFee(CommonUtils.null2Double(currentTotalBill.getCurrentFee()) + currentDetailBill.getCurrentFee());	//总金额累计
//				currentTotalBill.setTotalFee(CommonUtils.null2Double(currentTotalBill.getTotalFee()) + currBillFee);
				TBsChargeBillHistory nextBill = createNextBill(ruleationId,nextTotalId,(String)resultDetailMap.get(0).get("buildingFullName"),currentTotalBill.getProjectId(),currentDetailBill);
				currentDetailBill.setBillingTime(new Date());
				//TODO 这里其实都还没有计出费用就行了对本期账单的更新操作，查看后面是否又进行了一次更新，如果计费结束后依然更新，这里的去掉
				/*if(isInsert){
					insertList.add(currentDetailBill);
				}else{
					updateList.add(currentDetailBill);
				}*/
				insertList.add(nextBill);
				sendMessageQueue(insertList,updateList,companyId,meterFlag,oprStr);
				
				//同样这里需要同步到新账户数据
				AcChargeDetailDto acCurrentChargeD =  getAcCurrentChargeDetail( companyId,bill,currentTotalBill.getType(),
						scheme.getProjectId(),scheme.getProjectName(),ruleationId,
						CommonUtils.null2String(currentDetailBill.getLastBillId()) ,
						CommonUtils.isEmpty(currentDetailBill.getCurrentBillFee()) ? 0.0 : currentDetailBill.getCurrentBillFee() );
				insertCurrentList.add(acCurrentChargeD);
				return returnMessage;
			}
		}
		
		//计算本月账单
		Double currentFee = 0.0;
		
		
		//费用项
		try {
//			Calculator calculator = new Calculator(); //由于不用计算需要安装MATLAB的运行环境；且运行环境较大；本业务不需要做较复杂的数值计算;故弃用MATLAB来计算；改用解析js脚本
			List<FeeItemDetail> list = new ArrayList<FeeItemDetail>();
			for(TBsChargeType tbChargType : tBsChargingTypeList){
				//得到公式
				Double CountValue =0.0;
				String feeItem="";  //TODO 从代码看没看到具体作用在哪（只是存放了一个标识）
				Double minCriticalpoint = tbChargType.getMinCriticalpoint();
				Double maxCriticalpoint = tbChargType.getMaxCriticalpoint();
				String formulaInfoValue= tbChargType.getFormulaInfoValue(); //计费公式（一个计费项目里面只能有一个公式）
				if(StringUtils.isBlank(formulaInfoValue)){
					log.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"公式为空,不能计算，请先维护公式!"));
					continue;
				}
				for(int i=0;i<resultDetailMap.size();i++){
					
					//1、临界点一和临界点二都为0表示，没有临界点；该公式适用所有计算项目的值,所以不用做判断
					//2、临界点一为0；临界点二不为0；表示计费项目在小于临界点二的范围内
					//3、临界点一不为0.临界点二为0；表示计费项目在大于临界点一的范围内
					//4、临界点一和临界点二都不为0；表示计费项目在临界点一到临界点二的范围内
					String[] formulaArray = formulaInfoValue.split(" ");  // 2.67 * 22 + 4.01 * ( $Count - 22 )  运算符左右是空格
					
					//获取表的用量（如果是电表分时表获取峰谷平值的用量）
					Double userCount =Double.parseDouble(String.valueOf(CommonUtils.isEmpty(resultDetailMap.get(i).get("useCount"))?"0":resultDetailMap.get(i).get("useCount")));
					Double peakCount = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(resultDetailMap.get(i).get("peakCount"))?"0":resultDetailMap.get(i).get("peakCount")));
					Double vallyCount = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(resultDetailMap.get(i).get("vallyCount"))?"0":resultDetailMap.get(i).get("vallyCount")));
					Double commonCount = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(resultDetailMap.get(i).get("commonCount"))?"0":resultDetailMap.get(i).get("commonCount")));
					Double totalReading = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(resultDetailMap.get(i).get("totalReading"))?"0":resultDetailMap.get(i).get("totalReading")));
					Double lastTotalReading = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(resultDetailMap.get(i).get("lastTotalReading"))?"0":resultDetailMap.get(i).get("lastTotalReading")));
					String rate = String.valueOf( resultDetailMap.get(i).get("rate") );
					bill.setUseCount( CommonUtils.null2String( userCount ) );
					bill.setLastReading( CommonUtils.null2String( lastTotalReading ) );
					bill.setTotalReading( CommonUtils.null2String( totalReading ) );
					bill.setRate( CommonUtils.null2String( rate ) );
					
					//   确定计费项目
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
					
					//  **************** 校验计费数据是否都符合公式的范围 S  只有在公司范围内的才进行计算，不然跳过
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
					//  **************** 校验计费数据是否都符合公式的范围 E
					
					
//					//置空公式然后得到新的运算式子
					
					//根据用量和公式计算出对应的费用
					Object obj = FormulaCalculationUtil.waterElectCalculation(formulaInfoValue,userCount, peakCount, vallyCount, commonCount);
					if(CommonUtils.isEmpty(obj)){
						log.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"公式有误,不能做计算!"));
						continue;
					}else{
						if(obj.toString().equals("Infinity")){
							log.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"公式计算有误,除数不能为0!"));
							continue;
						}else{
							//本计费想计出的费用
							CountValue = CountValue+Double.parseDouble(String.valueOf(obj));
						}
					}
					
				}
				//这里因为，double计算可能会丢失精度，导致后续四舍五入不准确，一般正常的水电费单价在四位小数左右，对于丢失精度的四舍五入保留前十位即可
				currentFee = BigDecimalUtils.add(currentFee, CountValue);
				currentFee = new BigDecimal(currentFee).setScale(10,BigDecimal.ROUND_HALF_UP).doubleValue();
				//得到了费用，这里获取账单需要显示的详细的数据信息
				bill = getBillDetailInfo( bill, tbChargType.getFormulaInfo() );
				
				FeeItemDetail feeItemDetail = new FeeItemDetail();
				feeItemDetail.setFeeName(tbChargType.getChargingName());
				feeItemDetail.setCountValue(String.valueOf(CountValue));
				list.add(feeItemDetail);
			}
//		 if(currentFee>60.15 && currentFee<60.17){
//			 currentDetailBill.setCurrentFee(new BigDecimal(String.valueOf(currentFee)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
//		 }
			//经典四舍五入取两位小数
			currentDetailBill.setCurrentFee(new BigDecimal(String.valueOf(currentFee)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()); 	//本期产生金额
			JSONObject json = new JSONObject();
			currentDetailBill.setFeeItemDetail(json.toJSONString(list)); //设置各个费用项组成的json字符串
			//本期总账单 计算公式: 上期总账单   - 上期已付(上期已付最多把上期抹平,剩余全部充入该种账户余额) + 本期计费 + 本期分摊 + 本期违约金
//			double currBillFee = CommonUtils.null2Double(currentDetailBill.getLastBillFee()) 
//							   - CommonUtils.null2Double(currentDetailBill.getLastPayed()) 
//							   + CommonUtils.null2Double(currentDetailBill.getShareFee()) 
//							   + CommonUtils.null2Double(currentDetailBill.getLateFee())
//							   + currentDetailBill.getCurrentFee();
			double currBillFee =BigDecimalUtils.add(BigDecimalUtils.add(
					BigDecimalUtils.add(
					BigDecimalUtils.sub(
					CommonUtils.null2Double(currentDetailBill.getLastBillFee())
					, CommonUtils.null2Double(currentDetailBill.getLastPayed()))
					, CommonUtils.null2Double(currentDetailBill.getShareFee()))
					,CommonUtils.null2Double(currentDetailBill.getLateFee()))
					, currentDetailBill.getCurrentFee()); 
					
					
			currentDetailBill.setCurrentBillFee(currBillFee);
			String temporaryBill = currentDetailBill.getTemporaryBill();
			if(StringUtils.isNotBlank(temporaryBill)){//表示，临时计费过，这个分单还需要计费计费和扣费
				//如果是先临时计费过，则也需要将本次的正常计费写入临时字段中
				currentTotalBill.setAuditStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
				List<TempCurrentFee> listTempCurrentFee =new ArrayList<TempCurrentFee>();
				 listTempCurrentFee= json.parseArray(temporaryBill, TempCurrentFee.class);
				 TempCurrentFee tempCurrentFee = new TempCurrentFee();
				 tempCurrentFee.setId(CommonUtils.getUUID());
				 tempCurrentFee.setCurrentFee(currentDetailBill.getCurrentFee());
				 listTempCurrentFee.add(tempCurrentFee);
				 currentDetailBill.setTemporaryBill(json.toJSONString(listTempCurrentFee)); //记录这次临时计费
			};
			

			//*************新增写入账单数据S*************
			bill.setTotal( CommonUtils.null2String( currBillFee ) );
			bill.setCurrFee( CommonUtils.null2String( currentFee ) );
			bill.setLastUnPay(CommonUtils.null2String( currBillFee - currentFee ));
			bill.setLateFee( CommonUtils.null2Double( currentDetailBill.getLateFee() ) > 0 ? currentDetailBill.getLateFee().toString() : "0.0" );
			bill.setShareFee( CommonUtils.null2Double( currentDetailBill.getShareFee()) >0  ? currentDetailBill.getShareFee().toString() : "0.0");
			//*************新增写入账单数据E*************
			
			currentDetailBill.setBillingTime(new Date());
			currentTotalBill.setCurrentFee(BigDecimalUtils.add(CommonUtils.null2Double(currentTotalBill.getCurrentFee()), currentDetailBill.getCurrentFee()));	//总金额累计
			currentTotalBill.setTotalFee(BigDecimalUtils.add(CommonUtils.null2Double(currentTotalBill.getTotalFee()), currBillFee));
			currentDetailBill.setTax(CommonUtils.getTax(BigDecimalUtils.sub(currBillFee,CommonUtils.null2Double(currentDetailBill.getLateFee())), scheme.getTaxRate())); //税金计算
			int chargingType = oprStr.equals(MANAUL_STR)?BillingEnum.TYPE_MANUAL.getIntV():BillingEnum.TYPE_AUTO.getIntV(); //设置计费类型
			currentTotalBill.setChargingType(chargingType);
			
			//生成下月的数据
			TBsChargeBillHistory nextBill = createNextBill(ruleationId,nextTotalId,(String)resultDetailMap.get(0).get("buildingFullName"),currentTotalBill.getProjectId(),currentDetailBill);
			
			if(isInsert){
				insertList.add(currentDetailBill);
			}else{
				updateList.add(currentDetailBill);
			}
			insertList.add(nextBill);
			
			//*****************************新账户数据获取处理**********************
			
			//分析：这里对于本期history来说有可能存在插入和更新，但是对于新增的  收费结果明细表  只会出现新增，因为这个只会出现在计费的时候（houseCodeNew先传老的building_code）
			AcChargeDetailDto acCurrentChargeD =  getAcCurrentChargeDetail( companyId,bill,currentTotalBill.getType(),
					scheme.getProjectId(),scheme.getProjectName(),ruleationId,currentDetailBill.getLastBillId(),currentDetailBill.getCurrentBillFee());
					
			insertCurrentList.add(acCurrentChargeD);
			//*****************************新账户数据获取处理**********************
			
			if( insertCurrentList.size() >= BATCH_UPDATE_COUNT ){
				mqSender.sendAcChargeDetailList(insertCurrentList,companyId);
				insertCurrentList.clear();
				log.info("***********调用批量插入AcCurrentChargeDetail 成功***********");
			}
			
			sendMessageQueue(insertList,updateList,companyId,meterFlag,oprStr);
			
		} catch (ECPBusinessException e) {
			log.info(String.format("当前时间 : %s , 计算异常 -> %s" ,CommonUtils.getDateStr(),e.getMessage()));
			return false;
		}
		return returnMessage;
	}
	
	 //发送至消息队列
	private void sendMessageQueue(List<TBsChargeBillHistory> insertList, List<TBsChargeBillHistory> updateList,String companyId,String meterFlag,String oprStr){
		if(insertList.size() >= BATCH_ADD_COUNT){
			BillingSupEntity e = new BillingSupEntity(insertList, null);
			log.info(getFeeType(meterFlag)+"{}计费: 批量插入元数据 , 数据: {}, 发送至消息队列开始. ",oprStr, e.toString());
			sendMessage(companyId,e);
			insertList.clear();
			log.info(getFeeType(meterFlag)+"{}计费: 批量插入元数据 , 数据: {}, 发送至消息队列完成. ", oprStr, e.toString());
		}
		if(updateList.size() >= BATCH_UPDATE_COUNT){
			BillingSupEntity e = new BillingSupEntity(null, updateList);
			log.info(getFeeType(meterFlag)+"{}计费: 批量修改元数据 , 数据: {}, 发送至消息队列开始. ",oprStr,e.toString());
			sendMessage(companyId,e);
			updateList.clear();
			log.info(getFeeType(meterFlag)+"计费: 批量修改元数据 , 数据: {}, 发送至消息队列完成. ", oprStr,e.toString());
		}
	}
	

	
	//生成下月数据
	private TBsChargeBillHistory createNextBill(String ruleationId,String nextTotalId,String buildFullName,String projectId,TBsChargeBillHistory currentDetailBill){
		TBsChargeBillHistory nextBill = new TBsChargeBillHistory();
		nextBill.setBuildingCode(ruleationId);
		nextBill.setChargeTotalId(nextTotalId);
		nextBill.setCreateId(currentDetailBill.getCreateId());
		nextBill.setCreateTime(new Date());
		nextBill.setFullName(buildFullName);
		nextBill.setId(CommonUtils.getUUID());
		nextBill.setLastBillId(currentDetailBill.getId());
		nextBill.setIsUsed(0);
		nextBill.setLateFee(0.0);
		nextBill.setLastBillFee(currentDetailBill.getCurrentBillFee());
		nextBill.setProjectId(projectId);
		return nextBill;
	}
	
	/**
	 * 判断临界点

	 * @param minCriticalpoint  最小临界值
	 * @param maxCriticalpoint  最大临界值
	 * @param dosage  用量
	 * @return
	 */
	private Boolean judgeCriticalpoint(Double minCriticalpoint,Double maxCriticalpoint,Double dosage){
		boolean flag=true;
		if(null == minCriticalpoint || null == maxCriticalpoint){
			flag=false;
			log.info("临界点为空;不能计费!");
			return flag;
		}
		if(minCriticalpoint==0 && maxCriticalpoint==0){
			flag=true;
			return flag;
		}
		if(minCriticalpoint !=0 && maxCriticalpoint==0){
			if(dosage<=minCriticalpoint){
				flag = false;
				log.info(CommonUtils.log("计费项目["+dosage+"]不在["+minCriticalpoint+","+"+∞]范围内!"));
				return flag;
			}
		}
		if(minCriticalpoint ==0 && maxCriticalpoint !=0){
			if(dosage>maxCriticalpoint){
				flag = false;
				log.info(CommonUtils.log("计费项目["+dosage+"]不在["+0+","+maxCriticalpoint+"]范围内!"));
				return flag;
			}
		}
		if(minCriticalpoint !=0 && maxCriticalpoint !=0){
			if(dosage <=minCriticalpoint || dosage>maxCriticalpoint){
				flag = false;
				log.info(CommonUtils.log("计费项目["+dosage+"]不在["+minCriticalpoint+","+maxCriticalpoint+"]范围内!"));
				return flag;
			}
		}
		return flag;
	}
	
	private void sendMessage(String companyId , BillingSupEntity se){
		if(null != se){
			MqEntity e = new MqEntity(BillingEnum.manaul_billing.getIntV(), se);
			e.setCompanyId(companyId);
			this.amqpTemplate.convertAndSend(waterelect_billing_manaul_route_key, e);
		}
	}
	
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void insert(String companyId, BillingSupEntity se) {

		if(CommonUtils.isNotEmpty(se.getInsertList())){
			this.tBsChargeBillHistoryMapper.batchInsert(se.getInsertList());
		}
		if(CommonUtils.isNotEmpty(se.getUpdateList())){
			for(TBsChargeBillHistory h : se.getUpdateList()){
				this.tBsChargeBillHistoryMapper.updateBillHistory(h);
			}
		}
		
	}
	
	
	
	/**
	 * 水电费自动计费
	 */
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	public BaseDto autoBilling(String companyId,String meterFlag) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("status", BillingEnum.STATUS_START.getIntV());								//项目启用状态
		if(meterFlag.equals("elect")){
			paramMap.put("electStatus", BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV());		//电费种待计费状态
			paramMap.put("schemeType", 4);
		}
		if(meterFlag.equals("water")){
			paramMap.put("waterStatus", BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV()); //电费种待计费状态
			paramMap.put("schemeType", 3);
		}
		paramMap.put("chargeType", BillingEnum.TYPE_AUTO.getIntV());						//计费方案类型
		paramMap.put("isUsed", BillingEnum.IS_USED_USING.getIntV());							//计费方案可用状态
		List<TBsProject> projects = this.tBsProjectMapper.findCanNewBillingProjects(paramMap);		//获取可计费的项目
		if(projects.isEmpty()){
			log.warn("当前物业公司下,未找到可计费的项目. 计费完成 . companyId : {}",companyId);
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,getFeeType(meterFlag)+"当前物业公司下未找到可计费的项目,计费完成. "));
		}
		
		//电费有数据的,再用消息队列去项目级的计费
		if(meterFlag.equals("elect")){
			for(TBsProject p : projects){
				log.info(getFeeType(meterFlag)+"自动计费: 数据: {}, 开始组装数据,准备发往mq, routeKey: {}",p.toString(),elect_billing_auto_route_key);
				MqEntity entity = new MqEntity(BillingEnum.auto_billing.getIntV(),p);
				entity.setCompanyId(companyId);
				this.amqpTemplate.convertAndSend(elect_billing_auto_route_key, entity);
				log.info(getFeeType(meterFlag)+"自动计费: mqEntity数据: {}, 发送到mq完成, routeKey: {}",entity.toString(),elect_billing_auto_route_key);
			}
		}else{
			//水费有数据的,再用消息队列去项目级的计费
			for(TBsProject p : projects){
				log.info(getFeeType(meterFlag)+"自动计费: 数据: {}, 开始组装数据,准备发往mq, routeKey: {}",p.toString(),elect_billing_auto_route_key);
				MqEntity entity = new MqEntity(BillingEnum.auto_billing.getIntV(),p);
				entity.setCompanyId(companyId);
				this.amqpTemplate.convertAndSend(water_billing_auto_route_key, entity);
				log.info(getFeeType(meterFlag)+"自动计费: mqEntity数据: {}, 发送到mq完成, routeKey: {}",entity.toString(),elect_billing_auto_route_key);
			}
		}
		
		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,getFeeType(meterFlag)+"开始自动异步计费,请等待."));
				
	}
	
	
	/**
	 * 水电费自动计费
	 */
	@Transactional(rollbackFor=Exception.class)
	public void autoBillingByProject(String companyId, TBsProject entity,String meterTypeFlag){
		if(null == entity){
			log.warn(getFeeType(meterTypeFlag)+"自动计费: 项目为空, 自动计费完成. 自动计费流程停止. ");
			return;
//			throw new ECPBusinessException("传入项目为空,"+(getFeeType(meterTypeFlag))+"无法自动计费. 自动计费流程停止.");
		}
		log.info(getFeeType(meterTypeFlag)+"自动计费: 开始自动计费 .  项目: {}",entity.toString());
		//1. 获取该项目下可用的物业管理费计费方案
		TBsChargingScheme scheme = hasCanBillingScheme(entity,meterTypeFlag,true);
		if(null == scheme){
			log.warn(getFeeType(meterTypeFlag)+"自动计费: 当前项目下未找到可供计费的方案 . 自动计费流程停止. 项目: {} ", entity.toString());
			return;
//			throw new ECPBusinessException(getFeeType(meterTypeFlag)+"自动计费未找到可用的自动计费方案. 自动计费流程停止.");
		}
		List<TBsChargingRules> rulesList = tBsChargingRulesMapper.getTBsChargingRulesBySchemeId(scheme.getId());
		if(CollectionUtils.isEmpty(rulesList)){
			log.warn(getFeeType(meterTypeFlag)+"自动计费: 当前项目下的 方案:"+scheme.getSchemeName()+"中未找到计费规则.自动计费流程停止. 项目: {} ", entity.toString());
			return;
			//			throw new ECPBusinessException(getFeeType(meterTypeFlag)+"自动计费: 当前项目下的 方案:"+scheme.getSchemeName()+"中未找到计费规则.自动计费流程停止");
		}
		
		//2. 获取本月的总账单,若未生成,则生成一张新的总账单
		String nextTotalId = CommonUtils.getUUID();
		Map<String,Object> returnMap = hasTotalBillInCurrentCycle(entity,meterTypeFlag,scheme);
		TBsChargeBillTotal currentTotalBill = (TBsChargeBillTotal) returnMap.get("entity");
		Date lastBillDate = (Date) returnMap.get("lastBillDate");  //方案启用时间
		
		//根据本次的总账单，检查上一个总账单是否有审核，如果是未审核的状态则不能做本次的计费;加上这个判断限制主要是为了防止重复扣费
		String lastTotalId= currentTotalBill.getLastTotalId();
		if(StringUtils.isNotBlank(lastTotalId)){
			//如果上期总账单编号Id是空；则说明系统第一次计费，故没有上期总账单;
			TBsChargeBillTotal  lastBillTotal = tBsChargeBillTotalMapper.findTbsTotalbyId(lastTotalId);
			if(CommonUtils.isNotEmpty(lastBillTotal)){
				if(CommonUtils.isNotEmpty(lastBillTotal.getAuditStatus()) && lastBillTotal.getAuditStatus() !=1){
					log.info(CommonUtils.log("总账单编号为["+lastTotalId+"]的总账单还未审核，不能做计费;自动计费停止..."));
					return;
				}
			}
		}
		
		
		List<TBsChargeBillHistory> insertList = new ArrayList<TBsChargeBillHistory>();
		List<TBsChargeBillHistory> updateList = new ArrayList<TBsChargeBillHistory>();
		
		List<AcChargeDetailDto> insertCurrentList =new ArrayList<AcChargeDetailDto>();
		
		//检查关联建筑是否有产生最新的抄表数据     (这里不需要检查是否已经产生最新的抄表数据，因为计算哪里已经检查)
//		Map<String,Object> paramMap = new HashMap<String,Object>();
//		if(meterTypeFlag.equals("elect")){
//			paramMap.put("meterType", 1); //电表
//		}
//		if(meterTypeFlag.equals("water")){
//			paramMap.put("meterType", 0); //水表
//		}
//		paramMap.put("projectId", entity.getProjectId());
//		paramMap.put("lastBillTime", lastBillDate);
//		List<Map<String,Object>> resultMap = tcMeterDataMapper.getCountAndFeeObjByProjct(paramMap);
//		if(CollectionUtils.isEmpty(resultMap)){
//			outPutLog(entity.getProjectName(),meterTypeFlag.equals("elect")?"电表":"水表"+"抄表数据未产生或者审核未通过,自动计费动作完成");
//			throw new ECPBusinessException(getFeeType(meterTypeFlag)+"表抄表数据未产生或者审核未通过,自动计费动作完成. 自动计费流程停止.");
//		}
		//根据计费规则查找关联的建筑和费用项
		for(TBsChargingRules tBsChargingRules:rulesList){
			Map<String,Object> paramRuleMap = new HashMap<String,Object>();
			if(meterTypeFlag.equals("elect")){
				paramRuleMap.put("meterType", 1);
			}
			if(meterTypeFlag.equals("water")){
				paramRuleMap.put("meterType", 0);
			}
			paramRuleMap.put("ruleId", tBsChargingRules.getId());
			paramRuleMap.put("projectId", entity.getProjectId());
			List<String> relationIdsList = tBsRuleBuildingRelationMapper.getBuildingCodeByRuleId(paramRuleMap); //水电表查找有收费对象的关联建筑
			if(CollectionUtils.isEmpty(relationIdsList)){
				outPutLog(tBsChargingRules.getRuleName(),getFeeType(meterTypeFlag)+"计费规则没有关联建筑,自动计费动作完成");
				continue;
			}
			//查找费用项公式
			List<TBsChargeType> tBsChargingTypeList = tBsChargeTypeMapper.selectChargeType(tBsChargingRules.getId());
			if(CollectionUtils.isEmpty(tBsChargingTypeList)){
				outPutLog(tBsChargingRules.getRuleName(),getFeeType(meterTypeFlag)+"计费规则没有收费项,自动计费动作完成");
				continue;
			}
			//每一个规则下的每一个建筑都需要通过这个规则下所有的费用项进行计算费用
				for(String ruleationId:relationIdsList){
					boolean bool = billingWaterElectFee(companyId,scheme,ruleationId,meterTypeFlag,tBsChargingRules.getRuleName(),currentTotalBill,tBsChargingTypeList,lastBillDate,nextTotalId,insertList,updateList,AUTO_STR,insertCurrentList);
					if(bool==false) continue;
				}
		}
		
		//超过定义超过批量的数量后还剩下的
		BillingSupEntity se = new BillingSupEntity();
		if(insertList.size()>0){
			se.setInsertList(insertList);
		}
		if(updateList.size()>0){
			se.setUpdateList(updateList);
		}
		if( insertCurrentList.size() >= 0 ){
			mqSender.sendAcChargeDetailList(insertCurrentList,companyId);
			insertCurrentList.clear();
		}
		sendMessage(companyId,se);
		
		//计算完毕之后,生成下个月的总账单 , 账单的上期数据由上方的总账单传入
		TBsChargeBillTotal nextBillTotal = new TBsChargeBillTotal();
		nextBillTotal.setId(nextTotalId);
		nextBillTotal.setAuditStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
		nextBillTotal.setChargingType(BillingEnum.TYPE_MANUAL.getIntV());
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
		//获取本期的总金额
		if((boolean)returnMap.get("isInsert")){
			tBsChargeBillTotalMapper.insertChargeBillTotal(currentTotalBill);
		}else{
			tBsChargeBillTotalMapper.update(currentTotalBill); 
		}
		tBsChargeBillTotalMapper.insertChargeBillTotal(nextBillTotal);
		if(meterTypeFlag.equals("elect")){
			entity.setElectStatus(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV());
		}
		if(meterTypeFlag.equals("water")){
			entity.setWaterStatus(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV());
		}

		entity.setCurrentFee(CommonUtils.null2Double(entity.getCurrentFee()) + CommonUtils.null2Double(currentTotalBill.getCurrentFee()));
		entity.setLastOwedFee(CommonUtils.null2Double(entity.getLastOwedFee()) +CommonUtils.null2Double(currentTotalBill.getLastOwedFee()));
		entity.setTotalFee(CommonUtils.null2Double(entity.getTotalFee()) + CommonUtils.null2Double(currentTotalBill.getTotalFee()));

		tBsProjectMapper.update(entity);
		log.info(getFeeType(meterTypeFlag)+"自动计费: 自动计费完成. 项目: {}",entity.toString());
		
		

		
	}
	
	
	/**
	 * @TODO  判断当前项目是否含有可计费的计费方案
	 * @param entity
	 * @param isAuto
	 * @return
	 */
	private TBsChargingScheme hasCanBillingScheme(TBsProject entity ,String meterFlag, boolean isAuto){
		//2. 获取该项目下, 正在启用的scheme
		TBsChargingScheme paramScheme = new TBsChargingScheme();
		if(meterFlag.equals("elect")){
			paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_ELECT.getIntV());	//电费scheme
		}
		if(meterFlag.equals("water")){
			paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_WATER.getIntV());	//水费scheme
		}
		paramScheme.setProjectId(entity.getProjectId());	//project code
		if(isAuto) {
			paramScheme.setChargingType(0);
		}else{
			paramScheme.setChargingType(1);
		}
		TBsChargingScheme scheme = tBsChargingSchemeMapper.findUsingScheme(paramScheme);
		
		String str = (isAuto) ? AUTO_STR : MANAUL_STR;
		if(CommonUtils.isEmpty(scheme)){
			log.warn(getFeeType(meterFlag)+"{}计费:  该项目未找到可用的"+(getFeeType(meterFlag))+"计费方案,无法{}计费. 项目: {}",str,str,entity.toString());
			return null;
			
		}else{
			if(isAuto){	//自动
				if(BillingEnum.TYPE_AUTO.getIntV() == scheme.getChargingType()){
					return scheme;
				}else{
					log.warn(getFeeType(meterFlag)+"自动计费: 该项目可用的"+(getFeeType(meterFlag))+"计费方案为手动计费,不参与自动计费, 自动计费流程停止 . 项目: {}",entity.toString());
					return null;
				}
			}else{//手动
				if(BillingEnum.TYPE_MANUAL.getIntV() == scheme.getChargingType()){
					return scheme;
				}else{
					log.warn(getFeeType(meterFlag)+"手动计费: 该项目可用的物业管理费计费方案为自动计费,不参与手动计费, 手动计费流程停止. 项目: {}",entity.toString());
					return null;
				}
			}
		}
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
		acCurrentDetail.setChargeTime( new Date() );
		acCurrentDetail.setAuditTime(null);
		acCurrentDetail.setPayedAmount(BigDecimal.valueOf(0));
		acCurrentDetail.setSpecialDiKou(BigDecimal.valueOf(0));
		acCurrentDetail.setCommonDiKou(BigDecimal.valueOf(0));
		acCurrentDetail.setChargingType(ChargingType.getChargingTypeByCode(type));
		acCurrentDetail.setAssignAmount( new BigDecimal( bill.getShareFee() ) ); 
		acCurrentDetail.setChargeAmount( new BigDecimal( bill.getCurrFee() ) );
		acCurrentDetail.setChargeDetail( JSON.toJSONString( bill ) ); 
		acCurrentDetail.setCurrentArreas( new BigDecimal( bill.getCurrFee() ) );
		acCurrentDetail.setPayableAmount( new BigDecimal( bill.getCurrFee() ) ); 
		//acCurrentDetail.setHouseCodeNew(buildingCodeModifyHoustCode(houseCodeNew)); //新的房屋编号 合版删除
		acCurrentDetail.setBusinessTypeEnum( AcChargeDetailBusinessTypeEnum.CHARGE );  //计费
		return acCurrentDetail;
	}

	/**
	 * 
	 * @param bill  账单明细
	 * @param formulaInfo 计费公式
	 * @return bill  账单明细
	 */
	public BillDto getBillDetailInfo(BillDto bill,String formulaInfo) {

        if (formulaInfo != null) {
            String[] constantArray = formulaInfo.split(" ");
            for (int i = 0; i < constantArray.length; i++) {
                String item = constantArray[i];
                if (item.indexOf("标准水费") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setWaterPrice( CommonUtils.null2String( keyValue[1] ) );
                    }
                }
                if (item.indexOf("超额水费一") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setWaterPrice1(CommonUtils.null2String( keyValue[1]));
                    }
                }
                if (item.indexOf("超额水费二") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setWaterPrice2(CommonUtils.null2String( keyValue[1]));
                    }
                }
                if (item.indexOf("污水处理费") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setPollutedPrice(CommonUtils.null2String( keyValue[1]));
                    }
                }
                if (item.indexOf("污水处理费一") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setPollutedPrice1(CommonUtils.null2String( keyValue[1]));
                    }
                }
                if (item.indexOf("污水处理费二") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setPollutedPrice2(CommonUtils.null2String( keyValue[1]));
                    }
                }
                if (item.indexOf("垃圾处理费") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setRubbishPrice(CommonUtils.null2String( keyValue[1]));
                    }
                }
                if (item.indexOf("垃圾处理费一") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setRubbishPrice1(CommonUtils.null2String( keyValue[1]));
                    }
                }
                if (item.indexOf("垃圾处理费二") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setRubbishPrice2(CommonUtils.null2String( keyValue[1]));
                    }
                }
                if (item.indexOf("电费计费单价") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setPrice( new BigDecimal(keyValue[1]) );
                    }
                }
            }
        }
        
        return bill;
	}
	
	
	private String getFeeType(String meterTypeFlag){
		
		return meterTypeFlag.equals("elect")?"电费":"水费";
	}
	
	
	
////	
//	@Test
//	public void testNumber(){
////		double d = 60.165;
////		BigDecimal bd=new BigDecimal(String.valueOf(0.0+40.05+12.15));
////		double   f1   =   bd.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
////		System.out.println(f1);
//		
//		Double d2=40.05;
//		Double d3=12.15;
//		Double d4=0.000;
//		Double d5=0.000;
//		Double d6=d4+d2+d5+d3;
//		System.out.println(d6);
//		System.out.println(new BigDecimal(d6).setScale(4,BigDecimal.ROUND_HALF_UP));
//		
//		System.out.println(new BigDecimal(52.199999999999996).setScale(10,BigDecimal.ROUND_HALF_UP));
//		
////		double d1 = 20.055;
////		BigDecimal bd1=new BigDecimal(String.valueOf(d1));
////		double   f2   =   bd1.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
////		System.out.println(f2);
////		System.out.println(Math.round(d));
//	}
}
