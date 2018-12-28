package com.everwing.coreservice.wy.core.service.impl.configuration.tbcassetacount;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.StreamEnum;
import com.everwing.coreservice.common.wy.common.enums.TcOrderComplaintAndCompleteEnum;
import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfoDto;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.*;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.importEntity.TBsAssetAccountImportBean;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.AccountBillDto;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bill.*;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistoryDto;
import com.everwing.coreservice.common.wy.entity.configuration.project.*;
import com.everwing.coreservice.common.wy.entity.cust.person.TBcCillectionName;
import com.everwing.coreservice.common.wy.entity.order.TcOrderComplete;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingDto;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingAllParent;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.property.stall.TcStall;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import com.everwing.coreservice.common.wy.service.configuration.tbcassetacount.TBsAssetAccountServie;
import com.everwing.coreservice.common.wy.service.sys.TSysLookupService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.service.impl.configuration.cmac.single.SingleCmacBillingServiceImpl;
import com.everwing.coreservice.wy.core.task.TBsAssetAccountImportTask;
import com.everwing.coreservice.wy.core.utils.BillingUtils;
import com.everwing.coreservice.wy.dao.mapper.account.pay.TBsPayInfoMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.TBsProjectMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.TBcCollectionMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillHistoryMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillTotalMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.owed.TBsOwedHistoryMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargeTypeMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargingRulesMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargingSchemeMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsRuleBuildingRelationMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.tbs.assetsaccount.TBsAssetAccountMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.tbs.assetsaccount.stream.TBsAssetAccountStreamMapper;
import com.everwing.coreservice.wy.dao.mapper.order.complete.TcOrderCompleteMapper;
import com.everwing.coreservice.wy.dao.mapper.personbuilding.PersonBuildingNewMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcStallMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysUserMapper;
import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.factory.DefinitionFactory;
import com.everwing.myexcel.factory.xml.XMLDefinitionFactory4commonImport;
import com.everwing.myexcel.resolver.poi.POIExcelResolver4commonImport;
import com.everwing.utils.FormulaCalculationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service("tBsAssetAccountServieImpl")
public class TBsAssetAccountServieImpl extends POIExcelResolver4commonImport implements TBsAssetAccountServie {

	private static final Logger log = Logger.getLogger(TBsAssetAccountServieImpl.class);
	
	@Autowired
	private TcBuildingMapper tcBuildingMapper;
	
	@Autowired
	private TSysUserMapper tSysUserMapper;
	
	@Autowired
	private TBsAssetAccountMapper tBsAssetAccountMapper;
	
	@Autowired
	private TBsChargeBillTotalMapper tBsChargeBillTotalMapper;
	
	@Autowired
	private TBsChargingSchemeMapper tBsChargingSchemeMapper;
	
	@Autowired
	private TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;
	
	@Autowired
	private TBsOwedHistoryMapper tBsOwedHistoryMapper;
	
	@Autowired
	private TBsProjectMapper tBsProjectMapper;
	
	@Autowired
	private TBsAssetAccountStreamMapper tBsAssetAccountStreamMapper;
	
	@Autowired
	private TBsChargingRulesMapper tBsChargingRulesMapper;
	
	@Autowired
	private TBsRuleBuildingRelationMapper tBsRuleBuildingRelationMapper;
	
	@Autowired
	private TBsChargeTypeMapper tBsChargeTypeMapper;
	
	@Autowired
	private TcMeterDataMapper tcMeterDataMapper;
	
	@Autowired
	private TcOrderCompleteMapper tcOrderCompleteMapper;
	
	@Autowired
	private TSysLookupService tSysLookupService;

	@Autowired
	private ImportExportMapper importExportMapper;

	@Autowired
	private TBsPayInfoMapper tBsPayInfoMapper;
	
	@Autowired
	private FastDFSApi fastDFSApi;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private  TBcCollectionMapper tBcCollectionMapper;
	@Autowired
	protected TcStallMapper tcStallMapper;
	@Autowired
	private PersonBuildingNewMapper personBuildingNewMapper;
	
	@Value("${queue.wy2wyBilling.wy.manual.key}")
	private String wy_billing_key;					//物业管理费计费, 将数据丢消息队列里批量操作
	
	@Value("${batch.add.count}")
	private Double BATCH_ADD_COUNT;
	
	@Value("${batch.update.count}")
	private Double BATCH_UPDATE_COUNT;
	
	private WyBusinessContext ctx;

	private static final String AUTO_GENER = "system";
	
	/**
	 * 根据建筑编号查询物业管理费、水费、电费、本体基金账户情况
	 * 1、查询前，检查该收费对象是否存在已计费未审核的费用(若存在，则先要单独审核该收费对象的费用，扣费；注意该收费对象单独审核之后，总单部分审核，再对该总单审核时，应该剔除单独审核的这单)
	 * 2、查询前，检查该收费对象是否存在已经抄表未计费的情况(若存在，先计费(忽略手动自定),在审核费用  酷飞；注意总单为部分计费，再针对项目计费时对于单独计费了的这单不能再做计费，审核之后，总单为部分审核，再对总单进行审核时，应该剔除这单)
	 */
	@Override
	public BaseDto queryAccountSituationByBuildCode(WyBusinessContext ctx,
			String buildCode,String buildName) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msg= new MessageMap();
		try {
			if(StringUtils.isNotBlank(buildCode)){
//			  String resultError =checkNoBillOrNoAudit(ctx,buildCode,buildName);
				String resultError="";
			  if(StringUtils.isBlank(resultError)){
				  List<TBsAssetAccount> result=	this.tBsAssetAccountMapper.queryAccountSituationByBuildCode(buildCode);
				  baseDto.setLstDto(result);
				  msg.setFlag(MessageMap.INFOR_SUCCESS);
			  }else{
				  msg.setFlag(MessageMap.INFOR_ERROR);
				  msg.setMessage(resultError);
			  }
			}else{
				msg.setFlag(MessageMap.INFOR_ERROR);
				msg.setMessage("传入参数:建筑编号不能为空!");
			}
			baseDto.setMessageMap(msg);
			return baseDto;
		} catch (Exception e) {
			log.info(CommonUtils.log(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
		
	}
	
	/**
	 * 检查是否存在已抄表，未计费，或者已抄表，费用未审核的情况
	 */
	@Transactional(rollbackFor=Exception.class)
	public String checkNoBillOrNoAudit(WyBusinessContext ctx,String buildCode,String buildName){
	    String error ="";
		//1、已计费未审核扣费的
		//1.1、物业管理费已计费 未审核做 审核扣费处理
		String wyError = checkBillAndNoAudit(ctx,buildCode,"1");
		//1.2、本体基金已计费 未审核做 审核扣费处理
		String btError = checkBillAndNoAudit(ctx,buildCode,"2");
		//1.3、水费已计费未审核 审核做扣费处理
		String waterError =checkBillAndNoAudit(ctx,buildCode,"3");
		//1.4、电费已计费未审核  审核做扣费处理
		String electError = checkBillAndNoAudit(ctx,buildCode,"4");
		
		//2、已抄表,未计费,未审核 做计费审核处理
		String waterReadError= checkReadMeterNoBilling(ctx,buildCode,buildName,"water");
		String waterNobillError="";
		if(StringUtils.isBlank(waterReadError)){
			waterNobillError= checkBillAndNoAudit(ctx,buildCode,"3");
		}
		String electReadError = checkReadMeterNoBilling(ctx,buildCode,buildName,"elect");
		String electNobillError="";
		if(StringUtils.isBlank(electReadError)){
			electNobillError=checkBillAndNoAudit(ctx,buildCode,"4");
		}
//		Map<String,Object> paramMap = new HashMap<String,Object>();
//		paramMap.put("billingTime", new DateTime().toString("yyyy-MM-dd"));
//		paramMap.put("meterFlag", 3); //这里只需要查到计费项目给个水费查到的是同一条数据
//		paramMap.put("projectId", ctx.getProjectId());
	    TBsProject tBsProject = tBsProjectMapper.findCurrentMonthAndBilltime(new DateTime().toString("yyyy-MM-dd"),ctx.getProjectId());
		//3、通用账户扣费
		SingleCmacBillingServiceImpl singleCmacBillingServiceImpl = (SingleCmacBillingServiceImpl)SpringContextHolder.getBean("singleCmacBillingService");
		singleCmacBillingServiceImpl.billing(ctx.getCompanyId(), tBsProject, buildCode);
		
		error=wyError+btError+waterError+electError+waterNobillError+electNobillError+electReadError+waterReadError;
		return error;
	}
	
	//水费电费  已经抄表，未计费情况
	private String checkReadMeterNoBilling(WyBusinessContext ctx,String buildCode,String buildName,String meterFlag){
		//查询计费项目
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("billingTime", new DateTime().toString("yyyy-MM-dd"));
		if(meterFlag.equals("water")){
			paramMap.put("meterFlag", 3);
		}else if(meterFlag.equals("elect")){
			paramMap.put("meterFlag", 4);
		}
		paramMap.put("projectId", ctx.getProjectId());
	    TBsProject tBsProject = tBsProjectMapper.findByItemStatueAndBilltime(paramMap);
	    if(CommonUtils.isEmpty(tBsProject)){
	    	//表示已经计费直接返回
	    	return "";
	    }
		
		//检查是否存在可以计费的计费方案
		 String error="";
		 Map<String,Object> resultMap = hasCanBillingScheme(ctx,meterFlag);
		 Object objError= resultMap.get("error");
		 if(CommonUtils.isNotEmpty(objError)){
			 error=String.valueOf(objError);
			 return error;
		 }
		 Object obj = resultMap.get("scheme");
		 TBsChargingScheme scheme=null;
		 if(CommonUtils.isNotEmpty(obj)){ //方案存在
			 	scheme = (TBsChargingScheme)obj;
			 //找规则
				List<TBsChargingRules> ruleList = tBsChargingRulesMapper.getTBsChargingRulesBySchemeId(scheme.getId());
				if(CollectionUtils.isEmpty(ruleList)){
					error="方案["+scheme.getSchemeName()+"]不存在规则!";
					log.info(CommonUtils.log(error));
					return "";
//					return error;
				}else{
					TBsChargingRules buildRule =null; //收费对象绑定的规则
					for(TBsChargingRules tbsRule:ruleList){
						TBsRuleBuildingRelation  tbsRelation =this.tBsRuleBuildingRelationMapper.getRelationByBuilCodeAndRuleId(buildCode, tbsRule.getId());
						if(CommonUtils.isNotEmpty(tbsRelation)){
							buildRule=tbsRule;
							break;
						}
					}
				  if(CommonUtils.isEmpty(buildRule)){
					  error="收费对象["+buildName+"]不存在收费规则,不能计费!";
					  log.info(CommonUtils.log(error));
					  return "";
//					  return error;
				  }
				  String nextTotalId = CommonUtils.getUUID();//下一个总单的ID
				  Map<String,Object> totalMap =	hasTotalBillInCurrentCycle(ctx,meterFlag,scheme);
				  TBsChargeBillTotal tbsTotal = (TBsChargeBillTotal)totalMap.get("entity");
				  boolean totalInsertFlag = (boolean)totalMap.get("isInsert");
				  String totalId = tbsTotal.getId();
				  Date lastBillDate = (Date)totalMap.get("lastBillDate");
				  //得到该建筑的明细计费
				 Map<String,Object> historyMap = getTBsChargeBillHistory(ctx,totalId,buildCode,buildName);
				 TBsChargeBillHistory chargeHistory= (TBsChargeBillHistory)historyMap.get("tbsHistory");
				 boolean historyFlag= (boolean)historyMap.get("insertFlag");
				 //检查计费项和公式等情况
				 Map<String,Object> chargeTypeMap = checkChargTypeAndFumal(buildRule.getId(),buildRule.getRuleName());
				 List<TBsChargeType> listCharge =null;
				 Object objChargeError =chargeTypeMap.get("error");
				 if(CommonUtils.isNotEmpty(objChargeError)){
					 error = String.valueOf(objChargeError);
					 return error;
				 }
				 if(CommonUtils.isNotEmpty(chargeTypeMap.get("listChargType"))){
					 listCharge = (List<TBsChargeType>)chargeTypeMap.get("listChargType");
				 }
			 //得到未计费的表读数
			 	List<Map<String,Object>> listData= getMeterDate(ctx,meterFlag,lastBillDate,buildCode);
			 	//得到产权变更未计费的表读数
			 	List<Map<String, Object>> changeAssetData =this.tcOrderCompleteMapper.getNoBill(buildCode, ctx.getProjectId());
			 	//假如listData未空，这找产权变更的抄表记录
				   if(CommonUtils.isNotEmpty(listData)){
					 //计费
						waterAndElectBill(listData,listCharge,chargeHistory,tbsTotal,scheme,nextTotalId);
				   }
				  if(CommonUtils.isNotEmpty(changeAssetData)){
						//做产权变更的计费
						waterAndElectBill(changeAssetData,listCharge,chargeHistory,tbsTotal,scheme,nextTotalId);
				  }
				
					//计费玩之后，更改那些产权变更工单为已经计费状态
					for(Map<String, Object> mapComplete:changeAssetData){
						String orderCompleteId = String.valueOf(mapComplete.get("id"));
						TcOrderComplete  tcOrderComplete = this.tcOrderCompleteMapper.findById(orderCompleteId);
						tcOrderComplete.setIsAlreadyBilling(TcOrderComplaintAndCompleteEnum.IS_BILLING_YES.getIntV());
						this.tcOrderCompleteMapper.singleUpdate(tcOrderComplete);
					}
				
				//保存history分单
				if(historyFlag==false){
					this.tBsChargeBillHistoryMapper.updateBillHistory(chargeHistory);
				}else{
					this.tBsChargeBillHistoryMapper.insertBillHistory(chargeHistory);
				}
				//保存total总单
				if(totalInsertFlag==false){
					this.tBsChargeBillTotalMapper.update(tbsTotal);
				}else{
					this.tBsChargeBillTotalMapper.insertChargeBillTotal(tbsTotal);
				}
				
				
			
				//聚合项目
				if(meterFlag.equals("elect")){
					tBsProject.setElectStatus(BillingEnum.PROJECT_BILLING_START_APARTCOMPLETE.getIntV());
				}
				if(meterFlag.equals("water")){
					tBsProject.setWaterStatus(BillingEnum.PROJECT_BILLING_START_APARTCOMPLETE.getIntV());
				}
				tBsProject.setCurrentFee(CommonUtils.null2Double(tBsProject.getCurrentFee()) + CommonUtils.null2Double(tbsTotal.getCurrentFee()));
				tBsProject.setLastOwedFee(CommonUtils.null2Double(tBsProject.getLastOwedFee()) + CommonUtils.null2Double(tbsTotal.getLastOwedFee()));
				tBsProject.setTotalFee(CommonUtils.null2Double(tBsProject.getTotalFee()) + CommonUtils.null2Double(tbsTotal.getTotalFee()));
				tBsProjectMapper.update(tBsProject);
		  }
		 }else{//方案不存在
			 error=String.valueOf(resultMap.get("error"));
			 return error;
		 }
		 return error;
	}
	
	/**
	 * 水电费计费
	 */
	private String waterAndElectBill(List<Map<String,Object>> listData,List<TBsChargeType> listCharge,
			TBsChargeBillHistory currentHistory,TBsChargeBillTotal tbstotal,TBsChargingScheme scheme, 
			String nextTotalId)throws ECPBusinessException{
		String error ="";
		List<FeeItemDetail> list = new ArrayList<FeeItemDetail>();
		Double currentFee = 0.0; //计算本月账单
		if(!CollectionUtils.isEmpty(listData) && !CollectionUtils.isEmpty(listCharge)){
			for(TBsChargeType tbsChargeType:listCharge){
				String feeItem="";
				Double CountValue =0.0;
				Double minCriticalpoint = tbsChargeType.getMinCriticalpoint();
				Double maxCriticalpoint = tbsChargeType.getMaxCriticalpoint();
				String formulaInfoValue = tbsChargeType.getFormulaInfoValue();
				for(int i=0;i<listData.size();i++){
					String[] formulaArray = formulaInfoValue.split(" ");
					Double userCount =Double.parseDouble(String.valueOf(CommonUtils.isEmpty(listData.get(i).get("useCount"))?"0":listData.get(i).get("useCount")));
					Double peakCount = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(listData.get(i).get("peakCount"))?"0":listData.get(i).get("peakCount")));
					Double vallyCount = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(listData.get(i).get("vallyCount"))?"0":listData.get(i).get("vallyCount")));
					Double commonCount = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(listData.get(i).get("commonCount"))?"0":listData.get(i).get("commonCount")));
					
					//确定计费项目
					for(int j=0;j<formulaArray.length;j++){
						if(formulaArray[j].equals("$Count")){
							feeItem="$Count";
						}
						if(formulaArray[j].equals("$PeakCount")){
							feeItem="$PeakCount";
						}
						if(formulaArray[j].equals("$VallCount")){
							feeItem="$VallCount";
						}
						if(formulaArray[j].equals("$CommCount")){
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
					Object obj = FormulaCalculationUtil.waterElectCalculation(formulaInfoValue,userCount, peakCount, vallyCount, commonCount);
					if(CommonUtils.isEmpty(obj)){
						log.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"公式有误,不能做计算!"));
						continue;
					}else{
						if(obj.toString().equals("Infinity")){
							log.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"公式计算有误,除数不能为0!"));
							continue;
						}else{
							CountValue = CountValue+Double.parseDouble(String.valueOf(obj));
						}
					}
				}
				currentFee = new BigDecimal(currentFee).setScale(10,BigDecimal.ROUND_HALF_UP).doubleValue() + CountValue;
				FeeItemDetail feeItemDetail = new FeeItemDetail();
				feeItemDetail.setFeeName(tbsChargeType.getChargingName());
				feeItemDetail.setCountValue(String.valueOf(CountValue));
				list.add(feeItemDetail);
			}
			if(currentFee==0.00){
				log.info(CommonUtils.log("未计算出费用,可能用量为0或者用量不在公式的临界点内!"));
			}
			currentHistory.setCurrentFee(new BigDecimal(String.valueOf(currentFee)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()); 	//本期产生金额//本期产生金额
			JSONObject json = new JSONObject();
			currentHistory.setFeeItemDetail(json.toJSONString(list)); //设置各个费用项组成的json字符串
			//本期总账单 计算公式: 上期总账单   - 上期已付(上期已付最多把上期抹平,剩余全部充入该种账户余额) + 本期计费 + 本期分摊 + 本期违约金
			double currBillFee = CommonUtils.null2Double(currentHistory.getLastBillFee()) 
							   - CommonUtils.null2Double(currentHistory.getLastPayed()) 
							   + CommonUtils.null2Double(currentHistory.getShareFee()) 
							   + CommonUtils.null2Double(currentHistory.getLateFee())
							   + currentHistory.getCurrentFee();
			currentHistory.setCurrentBillFee(currBillFee);
			tbstotal.setCurrentFee(CommonUtils.null2Double(tbstotal.getCurrentFee()) + currentHistory.getCurrentFee());	//总金额累计
			tbstotal.setTotalFee(CommonUtils.null2Double(tbstotal.getTotalFee()) + currBillFee);
			currentHistory.setTax(new BigDecimal(String.valueOf((currBillFee - CommonUtils.null2Double(currentHistory.getLateFee()) ) * scheme.getTaxRate() / 100)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()); //税金计算
			tbstotal.setChargingType(BillingEnum.TYPE_MANUAL.getIntV());
			currentHistory.setAduitStatus(BillingEnum.AUDIT_STATUS_COMPELTE.getIntV());
			currentHistory.setBillingTime(new Date());
			
			//临时计费，写入数据到临时计费字段
			String temporaryBill = currentHistory.getTemporaryBill();
			List<TempCurrentFee> listTempCurrentFee =new ArrayList<TempCurrentFee>();
			if(StringUtils.isNotBlank(temporaryBill)){
			 listTempCurrentFee= json.parseArray(temporaryBill, TempCurrentFee.class);
			};
			 TempCurrentFee tempCurrentFee = new TempCurrentFee();
			 tempCurrentFee.setId(CommonUtils.getUUID());
			 tempCurrentFee.setCurrentFee(currentHistory.getCurrentFee());
			 listTempCurrentFee.add(tempCurrentFee);
			 currentHistory.setTemporaryBill(json.toJSONString(listTempCurrentFee)); //记录这次临时计费
		}
		
		 
		//生成下一个月的总单 生成下一个月总账单需要先查询，存在则更新，不存在则生成
			TBsChargeBillTotal nextBillTotal=	this.tBsChargeBillTotalMapper.findTbsTotalBylastTotalId(tbstotal.getId());
			
			//计算完毕之后,生成下个月的总账单 , 账单的上期数据由上方的总账单传入
			if(CommonUtils.isEmpty(nextBillTotal)){
				nextBillTotal = new TBsChargeBillTotal();
				nextBillTotal.setId(nextTotalId);
				nextBillTotal.setAuditStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
				nextBillTotal.setChargingType(BillingEnum.TYPE_MANUAL.getIntV());
				nextBillTotal.setProjectId(tbstotal.getProjectId());
				nextBillTotal.setType(scheme.getSchemeType());
				nextBillTotal.setLastOwedFee(0.0);
				nextBillTotal.setIsRebilling(BillingEnum.IS_REBILLING_NO.getIntV());
				nextBillTotal.setCreateId(currentHistory.getModifyId());
				nextBillTotal.setCreateTime(new Date());
				nextBillTotal.setSchemeId(scheme.getId());
				nextBillTotal.setLastTotalId(tbstotal.getId());
				nextBillTotal.setBillStatus(BillingEnum.BILL_STATUS_NO.getIntV());
				this.tBsChargeBillTotalMapper.insertChargeBillTotal(nextBillTotal);
			}else{
				nextBillTotal.setModifyTime(new Date());
				nextBillTotal.setModifyId(currentHistory.getModifyId());
				this.tBsChargeBillTotalMapper.update(nextBillTotal);
			}
		//生成下月的数据
		//生成下月数据之前先查询下月数据是否已经生成
		TBsChargeBillHistory nextBill =  this.tBsChargeBillHistoryMapper.getBylastBilllIdAndBuildCode(currentHistory.getId(), currentHistory.getBuildingCode());
		if(CommonUtils.isEmpty(nextBill)){
			nextBill = new TBsChargeBillHistory();
			nextBill.setBuildingCode(currentHistory.getBuildingCode());
			nextBill.setChargeTotalId(nextTotalId);
			nextBill.setCreateId(currentHistory.getCreateId());
			nextBill.setCreateTime(new Date());
			nextBill.setFullName(currentHistory.getFullName());
			nextBill.setId(CommonUtils.getUUID());
			nextBill.setLastBillId(currentHistory.getId());
			nextBill.setIsUsed(0);
			nextBill.setLastBillFee(currentHistory.getCurrentBillFee());
			nextBill.setProjectId(tbstotal.getProjectId());
			nextBill.setAduitStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV()); //未审核
			this.tBsChargeBillHistoryMapper.insertBillHistory(nextBill);
		}else{
			nextBill.setModifyId(currentHistory.getModifyId());
			nextBill.setModifyTime(new Date());
			this.tBsChargeBillHistoryMapper.updateBillHistory(nextBill);
		}
		return error;
	}
	
	/**
	 * 判断临界点

	 * @param minCriticalpoint
	 * @param maxCriticalpoint
	 * @param dosage
	 * @return
	 */
	private Boolean judgeCriticalpoint(Double minCriticalpoint,Double maxCriticalpoint,Double dosage){
		boolean flag=true;
		if(minCriticalpoint ==null || minCriticalpoint==null){
			flag=false;
			log.info("临界点为空;不能计费!");
			return flag;
		}
		if(minCriticalpoint==0 && maxCriticalpoint==0){
			flag=true;
			return flag;
		}
		if(minCriticalpoint !=0 && maxCriticalpoint==0){
			if(dosage<minCriticalpoint){
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
	
	//针对已经计费但是未审核   物业管理费Or本体基金Or水费Or电费
	private String checkBillAndNoAudit(WyBusinessContext ctx,String buildCode,String meterFalg){
		String error="";
		//物业管理费只存在费用未审核的情况 
		//查询物业管理费未审核的总单
		List<TBsChargeBillTotal> totalList =this.tBsChargeBillTotalMapper.getNoAuditByProjectIdAndType(ctx.getProjectId(), meterFalg);
		if(CollectionUtils.isEmpty(totalList)){
			return error;
		}else{
			//对某个建筑的费用进行部分审核，然后扣费
			for(TBsChargeBillTotal tbsTotal:totalList){
				String totalId = tbsTotal.getId();
				//根据总单id、buildCode和未审核条件查询分单
				TBsChargeBillHistory tbsChargeHistory= this.tBsChargeBillHistoryMapper.getBytotalIdAndBuildCode(totalId, buildCode);
				if(CommonUtils.isEmpty(tbsChargeHistory)){//表示该收费对象已经审核扣费了
					continue;
				}else{
					String errorKouFei =singleKoufei(ctx.getCompanyId(),tbsTotal,tbsChargeHistory);
					if(StringUtils.isBlank(errorKouFei)){ //扣费成功
						tbsTotal.setAuditStatus(BillingEnum.AUDIT_STATUS_APART_COMPLETE.getIntV());
						this.tBsChargeBillTotalMapper.update(tbsTotal);
					}else{//扣费失败，累计错误信息
						error =error+";"+errorKouFei;
					}
				}
			}
		}
		return error;
	}
	
	/**
	 * 针对单个扣费
	 */
	private String singleKoufei(String companyId, TBsChargeBillTotal total,TBsChargeBillHistory tbsCharge){
		String error="";
		double lastOwedFee=0.00;
		//找到总账单对应的项目信息
		TBsProject paramProject = new TBsProject();
		paramProject.setBillingTime(total.getBillingTime());
		paramProject.setProjectId(total.getProjectId());
		paramProject.setCommonStatus(BillingEnum.STATUS_START.getIntV());
		TBsProject currProject = this.tBsProjectMapper.findByObj(paramProject);
		
		//找到下期的总账单
		TBsChargeBillTotal nextTotal = this.tBsChargeBillTotalMapper.findNextBillTotal(total.getId()); 
		if(CommonUtils.isEmpty(nextTotal)){
			 error="账户扣费:未找到下期总账单,扣费失败!本期账单数据:{}"+total.toString();
			log.info(CommonUtils.log("账户扣费: 未找到下期总账单, 扣费失败. 本期账单数据: {}"+total.toString()));
			return error;
		}
		TBsChargeBillHistory nextHistory = this.tBsChargeBillHistoryMapper.findNextHistory(tbsCharge);
		//找到当前账单对应的账户,然后从账户内扣减
		TBsAssetAccount account = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(tbsCharge.getBuildingCode(), total.getType());//已经统一 , 总单的type与账户的type类型一致
		boolean accountIsInsert = false;
		if(account == null){
			//该户不存在账户,直接跳过? 还是创建一个账户?
			account = new TBsAssetAccount();
			account.setId(CommonUtils.getUUID());
			account.setAccountBalance(0.0);
			account.setBuildingCode(tbsCharge.getBuildingCode());
			account.setCreateId(AUTO_GENER);
			account.setCreateTime(new Date());
			account.setFullName(tbsCharge.getFullName());
			account.setModifyId(AUTO_GENER);
			account.setModifyTime(new Date());
			account.setProjectId(tbsCharge.getProjectId());
			account.setType(total.getType());		//已经统一 , 总单的type与账户的type类型一致
			account.setUseStatus(0);
			
			accountIsInsert = true;
		}
		double balance = (account.getAccountBalance() >= 0) ? account.getAccountBalance() : 0; 
		
		double sumAmount = CommonUtils.null2Double(tbsCharge.getCurrentFee()) + CommonUtils.null2Double(tbsCharge.getShareFee());	//本期费用与本期分摊是必定要扣的
		double lastOwed = CommonUtils.null2Double(tbsCharge.getLastBillFee()) - CommonUtils.null2Double(tbsCharge.getLastPayed());
		
		//获取本账户的所有欠费数据
		List<TBsAssetAccount> updateAccountList = new ArrayList<TBsAssetAccount>();					//待更新的账户表
		List<TBsAssetAccount> insertAccountList = new ArrayList<TBsAssetAccount>();					//带插入的账户表
		List<TBsAssetAccountStream> insertStreamList = new ArrayList<TBsAssetAccountStream>();		//带插入的账户流水集合
		List<TBsChargeBillHistory> updateHistories = new ArrayList<TBsChargeBillHistory>();			//待更新的历史数据集合
		List<TBsOwedHistory> updateOwdedHistories = new ArrayList<TBsOwedHistory>();				//待更新的欠费数据集合
		List<TBsOwedHistory> insertOwedHistories = new ArrayList<TBsOwedHistory>();
		
		//获取本账户的所有欠费数据
		List<TBsOwedHistory> ohs = this.tBsOwedHistoryMapper.findAllByAccountId(account.getId());
		double kqAmount = 0.0;	//扣取的违约金 + 扣取的账户欠费
		double kqLateFee = 0.0;
		double kqLateAmount = 0.0;
		if(balance > 0 && !ohs.isEmpty()){
			//首选抵扣违约金
			for(TBsOwedHistory oh : ohs){
				if(account.getAccountBalance() <= 0){
					break;
				}
				double bnc = CommonUtils.null2Double(account.getAccountBalance());
				double lateFee = CommonUtils.null2Double(oh.getTotalLateFee());
				//账户余额小于本条数据的违约金, 只扣除余额部分的违约金
				oh.setTotalLateFee((bnc >= lateFee) ? 0.0 : lateFee - bnc);
				account.setAccountBalance( (bnc >= lateFee) ? bnc - lateFee : 0.0 );
				kqLateFee += (bnc >= lateFee) ? lateFee : bnc;
				//插入违约金扣取流水
				insertStreamList.add(new TBsAssetAccountStream(account.getId(), (bnc >= lateFee) ? -lateFee : -bnc, StreamEnum.purpose_billing_by_latefee.getV()));
			}
			//再扣取欠费
			if(account.getAccountBalance() > 0){
				for(TBsOwedHistory oh : ohs){
					if(CommonUtils.null2Double(account.getAccountBalance()) <= 0) break;
					double bnc = account.getAccountBalance();
					double owedAmount = oh.getOwedAmount();
					oh.setOwedAmount( (bnc >= owedAmount) ? 0.0 : owedAmount - bnc );
					oh.setIsUsed( (bnc >= owedAmount) ? BillingEnum.IS_USED_STOP.getIntV() : BillingEnum.IS_USED_USING.getIntV());
					if(balance >= owedAmount){
						oh.setOwedEndTime(new Date());
					}
					account.setAccountBalance( (bnc >= owedAmount) ? bnc - owedAmount : 0.0 );
					kqLateAmount += (bnc >= owedAmount) ? owedAmount : bnc;
					
					//插入本金扣取流水
					insertStreamList.add(new TBsAssetAccountStream(account.getId(), (bnc >= owedAmount) ? -owedAmount : -bnc, StreamEnum.purpose_billing.getV()));
				}
			}
			updateOwdedHistories.addAll(ohs);
		}
		lastOwed -= kqLateAmount;	//上期欠费总共抵扣了多少
		sumAmount += lastOwed + (CommonUtils.null2Double(tbsCharge.getLateFee()) - kqLateFee);	//现在 : 本期费用 + 本期分摊 + 上期欠费去掉了扣除的 + 违约金扣除剩下的
		kqAmount = kqLateAmount + kqLateFee;
		kqAmount += (account.getAccountBalance() >= sumAmount) ? sumAmount : (account.getAccountBalance() >= 0) ? account.getAccountBalance() : 0;
		account.setAccountBalance(account.getAccountBalance() - sumAmount);		//违约金已经在上方扣取部分 , 此处只需要扣取本期账单部分(已经计算了违约金)即可
		tbsCharge.setCurrentKqAmount(sumAmount);
		//sumAmount必定大于等于0
		//若本项目未开启通用账户扣费, 则在此处就直接生成本期欠费信息
		if(currProject == null && account.getAccountBalance() < 0){
			//若扣除之后少于0 , 则需要记录本期欠费信息 ,
			List<TBsOwedHistory> bohs = this.tBsOwedHistoryMapper.findAllByAccountId(account.getId());
			TBsOwedHistory oh = new TBsOwedHistory(account);
			if(CommonUtils.isEmpty(bohs)){
				oh.setOwedAmount(Math.abs(account.getAccountBalance()));
			}else{
				double currOwed = Math.abs(account.getAccountBalance());
				for(TBsOwedHistory o : bohs){
					currOwed = currOwed - o.getOwedAmount() - o.getTotalLateFee();
				}
				oh.setOwedAmount(currOwed);
			}
			insertOwedHistories.add(oh);
		}
		tbsCharge.setAccountBalance(CommonUtils.null2Double(account.getAccountBalance()));
		tbsCharge.setAduitStatus(BillingEnum.AUDIT_STATUS_COMPELTE.getIntV());
		
		//记录本期的账户扣取  只计算可扣取至0的那一部分
		double ncd = CommonUtils.calKf(tbsCharge.getCurrentBillFee(), tbsCharge.getCommonDesummoney(), tbsCharge.getNoCommonDesummoney(), kqAmount);
		tbsCharge.setNoCommonDesummoney(ncd);
		//下期账单的上期已付
		double lp = CommonUtils.calKf(nextHistory.getLastBillFee(), 0.0, nextHistory.getLastPayed(), kqAmount);
		nextHistory.setLastPayed(lp);
		account.setModifyId(AUTO_GENER);
		account.setModifyTime(new Date());
		account.setModifyName(AUTO_GENER);
		if(accountIsInsert){
			insertAccountList.add(account);
		}else{
			updateAccountList.add(account);
		}
		tbsCharge.setAduitStatus(BillingEnum.AUDIT_STATUS_COMPELTE.getIntV()); //该分单设置为审核状态
		updateHistories.add(tbsCharge);
		updateHistories.add(nextHistory);
		//账户扣减玩之后，计算总账单还欠费多少记录到下期总账单的上期欠费;只记录账户是负数的情况
		double accountBanlance = account.getAccountBalance();
		if(accountBanlance<0){
			lastOwedFee = lastOwedFee+accountBanlance;
		}
		nextTotal.setLastOwedFee(Double.parseDouble(String.valueOf(lastOwedFee).replace("-", "")));
		this.tBsChargeBillTotalMapper.update(nextTotal);
		
		//更新分单
		if(updateHistories.size()>0){
			this.tBsChargeBillHistoryMapper.batchUpdate(updateHistories);
		}
		//账号插入
		if(insertAccountList.size()>0){
			this.tBsAssetAccountMapper.batchInsert(insertAccountList);
		}
		//账号更新
		if(updateAccountList.size()>0){
			this.tBsAssetAccountMapper.batchUpdate(updateAccountList);
		}
		//流水插入
		if(insertStreamList.size()>0){
			this.tBsAssetAccountStreamMapper.batchInsert(insertStreamList);
		}
		//插入欠费表
		if(insertOwedHistories.size()>0){
			this.tBsOwedHistoryMapper.batchInsert(insertOwedHistories);
		}
		//更新欠费表
		if(updateOwdedHistories.size()>0){
			this.tBsOwedHistoryMapper.batchUpdate(updateOwdedHistories);
		}
		return error;
	}
	
	
	
	/**
	 * 得到未计费的读数
	 */
	private List<Map<String,Object>> getMeterDate(WyBusinessContext ctx,String meterFlag,Date lastBillDate,String buildCode){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(meterFlag.equals("elect")){
			paramMap.put("meterType", 1); //电表
		}
		if(meterFlag.equals("water")){
			paramMap.put("meterType", 0); //水表
		}
		paramMap.put("projectId", ctx.getProjectId());
		paramMap.put("lastBillTime", lastBillDate);
		paramMap.put("relationBuilding", buildCode);
		List<Map<String,Object>> resultDetailMap = tcMeterDataMapper.getCountAndFeeObjByProjct(paramMap);
		return resultDetailMap;
	}
	
	/**
	 * 获取计费明细对象TBsChargeBillHistory,，这里分单的费用状态不一定是审核状态的，因为在一个正常计费周期里可能有多次变更
	 */
	private Map<String,Object> getTBsChargeBillHistory(WyBusinessContext ctx,String totalId,String buildCode,String buildFullName){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean isInsert = false;
		TBsChargeBillHistory  tbsHistory= this.tBsChargeBillHistoryMapper.findByTotalIdAndBuildCode(totalId,buildCode);
		if(CommonUtils.isEmpty(tbsHistory)){//新建
			tbsHistory = new TBsChargeBillHistory();
			
			tbsHistory.setId(CommonUtils.getUUID());
			tbsHistory.setIsUsed(0);
			tbsHistory.setChargeTotalId(totalId);
			tbsHistory.setBuildingCode(buildCode);
			
			tbsHistory.setFullName(buildFullName);
			tbsHistory.setLastBillFee(0.0);
			tbsHistory.setLastBillId(null);
			tbsHistory.setLastPayed(0.0);
			tbsHistory.setCreateId(ctx.getUserId());
			tbsHistory.setCreateTime(new Date());
			tbsHistory.setProjectId(ctx.getProjectId());
			isInsert = true;
		}else{
			tbsHistory.setModifyId(ctx.getUserId());
			tbsHistory.setModifyTime(new Date());
		}
		map.put("insertFlag", isInsert);
		map.put("tbsHistory", tbsHistory);
		return map;
	}
	
	/**
	 * 检验费用项和公式是否存在
	 */
	private Map<String,Object> checkChargTypeAndFumal(String ruleId,String ruleName){
		Map<String,Object> map = new HashMap<String,Object>();
		String error="";
		List<TBsChargeType> listChargType= this.tBsChargeTypeMapper.selectChargeType(ruleId);
		List<TBsChargeType> newListChargeType = new ArrayList<TBsChargeType>();
		if(CollectionUtils.isEmpty(listChargType)){
			error="收费规则["+ruleName+"]没有收费项,不能进行计费!";
			map.put("error", error);
			map.put("listChargType", null);
		}else{
			for(TBsChargeType tbsChargeType:listChargType){
				//检查是否有公式存在
				String fumal = tbsChargeType.getFormulaInfoValue();
				if(StringUtils.isBlank(fumal)){
					continue;
				}else{
					newListChargeType.add(tbsChargeType);
				}
			}
			if(newListChargeType.size()==0){
				error="收费规则["+ruleName+"]的所有收费项下没有计费公式,不能进行计费!";
				map.put("error", error);
				map.put("listChargType", null);
			}else{
				map.put("error", error);
				map.put("listChargType", newListChargeType);
			}
		}
		return map;
		
	}
	
	/**
	 * @TODO  判断当前项目是否含有可计费的计费方案

	 * @return
	 */
	private Map<String,Object> hasCanBillingScheme(WyBusinessContext ctx ,String meterFlag){
		Map<String,Object> map = new HashMap<String,Object>();
		//2. 获取该项目下, 正在启用的scheme
		TBsChargingScheme paramScheme = new TBsChargingScheme();
		if(meterFlag.equals("elect")){
			paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_ELECT.getIntV());	//电费scheme
		}else if(meterFlag.equals("water")){
			paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_WATER.getIntV());	//水费scheme
		}else if(meterFlag.equals("wy")){ //物业管理费
			paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_WY.getIntV());
		}else if(meterFlag.equals("bt")){//本体基金
			paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_BT.getIntV());
		}
		paramScheme.setProjectId(ctx.getProjectId());	//project code
		TBsChargingScheme scheme = this.tBsChargingSchemeMapper.findUsingScheme(paramScheme);
	
		String error="";
		if(CommonUtils.isEmpty(scheme)){
			error = convertError(meterFlag);
			log.info(CommonUtils.log(error));
			map.put("error", error);
			map.put("scheme", null);
		}else{
			map.put("error", error);
			map.put("scheme", scheme);
		}
		return map;
	}
	
	private String convertError(String meterFlag){
		String error ="";
		if(meterFlag.equals("elect")){
			error="电费计费:该项目未找到可用的计费方案,无法计费!";
			return error;
		}else if(meterFlag.equals("water")){
			error="水费计费:该项目未找到可用的计费方案,无法计费!";
			return error;
		}else if(meterFlag.equals("wy")){
			error="物业管理费计费:该项目未找到可用的计费方案,无法计费!";
			return error;
		}else if(meterFlag.equals("bt")){
			error="本体基金计费:该项目未找到可用的计费方案,无法计费!";
		}
		return error;
	}
	
	
	/**
	 * @TODO 判断在本周期内是否已经产生总账单 , 若未产生,则需要生成一张新的总账单  不需要根据手动自动来区分。

	 * @return
	 */
	private Map<String,Object> hasTotalBillInCurrentCycle(WyBusinessContext ctx ,String meterFlag,TBsChargingScheme scheme){
		Map<String,Object> returnMap = new HashMap<String, Object>();
		boolean flag = false;
		TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
		paramTotal.setProjectId(ctx.getProjectId());
		paramTotal.setType(scheme.getSchemeType());
		List<TBsChargeBillTotal> totals = tBsChargeBillTotalMapper.findCurrentBillTotal(paramTotal);
		
		TBsChargeBillTotal currentTotalBill =null;
		Date lastBillDate = scheme.getStartUsingDate();
		if(CommonUtils.isNotEmpty(totals)){ //这里最多只能查两条数据来，如果是能查出两条数据，就是当前总单可能做了产权变更
			if(totals.size()==1){
				currentTotalBill = totals.get(0); 
			}else if(totals.size()>1){
				//这里两个总单 只有两种 一个总单，计费时间不为空，且计费状态2    另外一个总单 计费时间为空，且然后计费状态为0  是未计费状态
				for(TBsChargeBillTotal totalBill:totals){
					Date billtime = totalBill.getBillingTime();
					Integer billStatus = totalBill.getBillStatus();
					if(CommonUtils.isNotEmpty(billtime) && billStatus.equals(BillingEnum.BILL_STATUS_APART.getIntV())){
						currentTotalBill = totalBill; //这个表示部分计费的总单
						break;
					}else{
						currentTotalBill = totalBill; //正常总单
					}
				}
			}
			
			TBsChargeBillTotal lastBill = tBsChargeBillTotalMapper.selectById(currentTotalBill.getLastTotalId());
			if(null != lastBill)  lastBillDate = lastBill.getBillingTime();
			
			
		}else{
			String error ="";
			if(meterFlag.equals("elect")){
				error="电费计费: 当前未找到本月的计费总账单,需要重新生成!";
			}else if(meterFlag.equals("water")){
				error= "水费计费:当前未找到本月的计费总账单,需要重新生成!";
			}else if(meterFlag.equals("wy")){//1、物业管理费  2、本体基金
				error="物业管理费:当前未找到本月的计费总账单,需要重新生成!";
			}else if(meterFlag.equals("bt")){
				error="本体基金:当前未找到本月的计费总账单,需要重新生成!";
			}
			log.info(CommonUtils.log(error));
			currentTotalBill = new TBsChargeBillTotal();
			
			currentTotalBill.setId(CommonUtils.getUUID());
			currentTotalBill.setProjectId(ctx.getProjectId());
			currentTotalBill.setType(scheme.getSchemeType());
			currentTotalBill.setLastOwedFee(0.0);
			currentTotalBill.setIsRebilling(BillingEnum.IS_REBILLING_NO.getIntV());
			currentTotalBill.setAuditStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
			currentTotalBill.setChargingType(BillingEnum.TYPE_MANUAL.getIntV());
			currentTotalBill.setCreateId(ctx.getUserId());
			currentTotalBill.setCreateTime(new Date());
			flag = true;
			
		}
		currentTotalBill.setModifyTime(new Date());
		currentTotalBill.setModifyId(ctx.getUserId());
		currentTotalBill.setBillingTime(new Date());
		currentTotalBill.setSchemeId(scheme.getId());
		currentTotalBill.setBillStatus(BillingEnum.BILL_STATUS_APART.getIntV());
		returnMap.put("entity", currentTotalBill);
		returnMap.put("isInsert", flag);
		returnMap.put("lastBillDate", lastBillDate);
		return returnMap;
	}



	@Override
	public BaseDto queryTotalArrears(String companyId,List<TcBuildingList> tcBuildingList) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		tcBuildingList.removeAll(Collections.singleton(null));
		try {
			for(TcBuildingList tList:tcBuildingList){
				String buildCode = tList.getBuildingCode();
				String projectId=tList.getProjectId();
				Double totalArrears = this.tBsAssetAccountMapper.queryTotalArrears(projectId, buildCode);
				if(CommonUtils.isEmpty(totalArrears)){
					tList.setTotalArrears(0.00);
				}else{
					tList.setTotalArrears(totalArrears);
				}
			}
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			baseDto.setLstDto(tcBuildingList);
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			log.info(CommonUtils.log(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}


	@Override
	public MessageMap importBeforeDatas(WyBusinessContext ctx, String batchNo,String excelPath, String oprUserId) {
        MessageMap mm = new MessageMap();

        this.ctx = ctx;
        //采用分布式文件服务器方式来做
        //通过batchNo查询uploadFileId信息作为参数传递给文件服务器
        TSysImportExportList tSysImportExportListExist = null;
        TSysImportExportSearch condition = new TSysImportExportSearch();
        condition.setBatchNo(batchNo);
        List<TSysImportExportList> tSysImportExportListList = importExportMapper.findByCondtion(condition);
        if(CommonUtils.isNotEmpty(tSysImportExportListList)){
            tSysImportExportListExist = tSysImportExportListList.get(0);
        }else{
            throw new ECPBusinessException("没有文件上传记录，请先上传文件");
        }
        
        try {
            RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.loadFilePathById(tSysImportExportListExist.getUploadFileId());
            if(remoteModelResult.isSuccess()){
                UploadFile uploadFile = remoteModelResult.getModel();
                URL url = new URL(uploadFile.getPath());
                HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
                uc.connect();
                super.excelInputStream = uc.getInputStream();
            }
        } catch (Exception e) {
            throw new ECPBusinessException("导入失败，读取文件失败："+e.getMessage());
        }

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        try {
            Callable task = new TBsAssetAccountImportTask(this,ctx,batchNo,excelPath, oprUserId);
            Future<MessageMap> messageMapFuture = executorService.submit(task);
            mm = messageMapFuture.get();
        }  catch (Exception e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(e.getMessage());
        }
        executorService.shutdown();

        while (true) {
            if (executorService.isTerminated()) {
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {

            }
        }
 
        executorService.shutdown();
        return mm;
	}

	@Override
	protected ExcelDefinitionReader getExcelDefinition() {
		 DefinitionFactory definitionReaderFactory = new XMLDefinitionFactory4commonImport("importExport/import/xml/before_asset_account.xml");
	     return definitionReaderFactory.createExcelDefinitionReader();
	}

	@Override
	protected String getLookupItemCodeByName(String lookupCode, String parentCode, String name) {
		return null;
	}

	protected String getLookupCodeByName(String parentCode, String name) {
		 return tSysLookupService.getLookupCodeByName(ctx,parentCode,name);
	}

	protected String getLookupItemCodeByName(String parentCode, String name) {
		return tSysLookupService.getLookupItemCodeByName(ctx,parentCode,name);
	}

	@Transactional(rollbackFor=Exception.class)
	public MessageMap importDatas(WyBusinessContext ctx, String batchNo,List<TBsAssetAccountImportBean> accountImportBeans, String oprUserId) {
		MessageMap msgMap = new MessageMap();
		//开始导入
		if(CommonUtils.isEmpty(accountImportBeans)){
			msgMap.setFlag(MessageMap.INFOR_WARNING);
			msgMap.setMessage("表格内未找到可供导入的数据. ");
			return msgMap;
		}
		
		//数据处理,组装成数据,进行插入
		List<TBsAssetAccount> insertAccounts = new ArrayList<TBsAssetAccount>();
		List<TBsOwedHistory> insertHistories = new ArrayList<TBsOwedHistory>();
		List<TBsAssetAccount> updateAccounts = new ArrayList<TBsAssetAccount>();
		
		//参数声明
		int rowNo = 1;
		int completeCount = 0 ;
		int failedCount = 0;
		StringBuffer sb = new StringBuffer();
		List<TcBuildingList> buildings = null;
		TcBuildingList building = null;
		TcBuildingSearch condition = new TcBuildingSearch();
		
		TSysUserSearch searchCondition = new TSysUserSearch();
		searchCondition.setUserId(oprUserId);
		List<TSysUserList> users = this.tSysUserMapper.findByCondition(searchCondition);
		TSysUserList user = users.get(0);
		
		for(TBsAssetAccountImportBean importBean : accountImportBeans){
			rowNo ++ ;
			if(isEmptyDataRow(importBean,rowNo, sb)){
				failedCount ++ ;
				continue;
			}
			
			//根据房号找房子
			condition.setHouseCode(CommonUtils.null2String(importBean.getId()));
			buildings = this.tcBuildingMapper.findByCondition(condition);
			
			if(CommonUtils.isEmpty(buildings)){
				sb.append("第").append(rowNo).append("行[房屋未找到], 房号:").append(importBean.getId()).append(",导入失败. \n");
				failedCount ++ ;
				continue;
			}
			
			//根据房屋找到账户
			building = buildings.get(0);

			//根据账户余额, 来找相应的账户
			oprAccounts(insertAccounts, updateAccounts, insertHistories, importBean, building, user);
			
			checkAndSendMessage(ctx.getCompanyId(), insertAccounts, updateAccounts, insertHistories);
			
			completeCount ++ ;
		}
		
		if(!insertAccounts.isEmpty()){
			BillingUtils.sendInsertList(insertAccounts, wy_billing_key, ctx.getCompanyId(), amqpTemplate);
		}

		if(!updateAccounts.isEmpty()){
			BillingUtils.sendUpdateList(updateAccounts, wy_billing_key, ctx.getCompanyId(), amqpTemplate);
		}
		
		if(!insertHistories.isEmpty()){
			BillingUtils.sendInsertList(insertHistories, wy_billing_key, ctx.getCompanyId(), amqpTemplate);
		}
		
		//日志上传
		try {
			ByteArrayInputStream ais = new ByteArrayInputStream(sb.toString().getBytes());
			RemoteModelResult<UploadFile> rslt = this.fastDFSApi.uploadFile(ais, batchNo + ".txt");
			if(rslt.isSuccess() && rslt.getModel() != null){
				TSysImportExportSearch tSysImportExportSearch = new TSysImportExportSearch();
                tSysImportExportSearch.setBatchNo(batchNo);
                List<TSysImportExportList> tSysImportExportLists =  importExportMapper.findByCondtion(tSysImportExportSearch);
                if(CommonUtils.isNotEmpty(tSysImportExportLists)) {
                    TSysImportExportList tSysImportExportList = tSysImportExportLists.get(0);
                    tSysImportExportList.setUploadMessageId(rslt.getModel().getUploadFileId());
                    tSysImportExportList.setEndTime(new Date());
                    importExportMapper.modify(tSysImportExportList);
                }
			}
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("日志文件上传时失败. ");
			e.printStackTrace();
			return msgMap;
		}
		
		if(sb.length() == 0){
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			msgMap.setMessage("导入成功.");
		}else{
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			msgMap.setMessage("导入成功: " + completeCount + " 条, 导入失败: " + failedCount + " 条, 具体情况请下载日志查看. ");
		}
		return msgMap;
	}




	/**
	 * 检查并发送到消息队列
	 * @param insertAccounts
	 * @param updateAccounts
	 * @param insertHistories
	 */
	private void checkAndSendMessage(String companyId,
									 List<TBsAssetAccount> insertAccounts,
									 List<TBsAssetAccount> updateAccounts,
									 List<TBsOwedHistory> insertHistories) {
		if(insertAccounts.size() >= BATCH_ADD_COUNT){
			BillingUtils.sendInsertList(insertAccounts, wy_billing_key, companyId, amqpTemplate);
		}

		if(updateAccounts.size() >= BATCH_UPDATE_COUNT){
			BillingUtils.sendUpdateList(updateAccounts, wy_billing_key, companyId, amqpTemplate);
		}
		
		if(insertHistories.size() >= BATCH_ADD_COUNT){
			BillingUtils.sendInsertList(insertHistories, wy_billing_key, companyId, amqpTemplate);
		}
	}

	private void oprAccounts(List<TBsAssetAccount> insertAccounts,
							 List<TBsAssetAccount> updateAccounts,
							 List<TBsOwedHistory> insertHistories,
							 TBsAssetAccountImportBean importBean, 
							 TcBuildingList building,
							 TSysUserList user) {
		
		if(CommonUtils.isNotEmpty(importBean.getWyAmount()) || CommonUtils.isNotEmpty(importBean.getWyLateFee())){
			//查找物业管理费账户
			TBsAssetAccount wyAccount = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(building.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_WY.getIntV());
			if(wyAccount == null){
				insertAccounts.add(getAccount(insertHistories, 
											  null,
											  importBean.getWyAmount(), 
											  importBean.getWyLateFee(), 
											  building, 
											  BillingEnum.ACCOUNT_TYPE_WY.getIntV(),
											  user));
			}else{
				updateAccounts.add(getAccount(insertHistories,
											  wyAccount, 
											  importBean.getWyAmount(), 
											  importBean.getWyLateFee(), 
											  building,BillingEnum.ACCOUNT_TYPE_WY.getIntV(),
											  user));
			}
		}
		if(CommonUtils.isNotEmpty(importBean.getBtAmount())){
			//查找本体基金账户
			TBsAssetAccount btAccount = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(building.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_BT.getIntV());
			if(btAccount == null){
				insertAccounts.add(getAccount(insertHistories, 
											  null,
											  importBean.getBtAmount(), 
											  0.0, 
											  building, 
											  BillingEnum.ACCOUNT_TYPE_BT.getIntV(),
											  user));
			}else{
				updateAccounts.add(getAccount(insertHistories,
											  btAccount, 
											  importBean.getBtAmount(), 
											  0.0, 
											  building,BillingEnum.ACCOUNT_TYPE_BT.getIntV(),
											  user));
			}
		}
		if(CommonUtils.isNotEmpty(importBean.getWaterAmount())){
			//查找水费账户
			TBsAssetAccount waterAccount = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(building.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_WATER.getIntV());
			if(waterAccount == null){
				insertAccounts.add(getAccount(insertHistories, 
											  null,
											  importBean.getWaterAmount(), 
											  0.0, 
											  building, 
											  BillingEnum.ACCOUNT_TYPE_WATER.getIntV(),
											  user));
			}else{
				updateAccounts.add(getAccount(insertHistories,
											  waterAccount, 
											  importBean.getWaterAmount(), 
											  0.0, 
											  building,BillingEnum.ACCOUNT_TYPE_WATER.getIntV(),
											  user));
			}
		} 
		if(CommonUtils.isNotEmpty(importBean.getElectAmount())){
			//查找水费账户
			TBsAssetAccount electAccount = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(building.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_ELECT.getIntV());
			if(electAccount == null){
				insertAccounts.add(getAccount(insertHistories, 
											  null,
											  importBean.getElectAmount(), 
											  0.0, 
											  building, 
											  BillingEnum.ACCOUNT_TYPE_WATER.getIntV(),
											  user));
			}else{
				updateAccounts.add(getAccount(insertHistories,
											  electAccount, 
											  importBean.getElectAmount(), 
											  0.0, 
											  building,BillingEnum.ACCOUNT_TYPE_WATER.getIntV(),
											  user));
			}
		}
	}

	//获取账户
	private TBsAssetAccount getAccount(List<TBsOwedHistory> insertHistories, 
								       TBsAssetAccount account,
								       Double amount, 
								       Double lateFee, 
								       TcBuildingList building, 
								       int accountType,
								       TSysUserList user) {
		amount = CommonUtils.null2Double(amount);
		lateFee = CommonUtils.null2Double(lateFee);
		if(account == null){
			account = new TBsAssetAccount();
			account.setId(CommonUtils.getUUID());
			account.setBuildingCode(building.getBuildingCode());
			account.setCreateId(user.getUserId());
			account.setCreateName(user.getStaffName());
			account.setCreateTime(new Date());
			account.setAccountBalance(- (amount + lateFee));
			account.setFullName(building.getBuildingFullName());
			account.setProjectId(building.getProjectId());
			account.setProjectName(building.getProjectName());
			account.setType(accountType);
			account.setUseStatus(BillingEnum.IS_USED_USING.getIntV());
		}else{
			account.setAccountBalance(CommonUtils.null2Double(account.getAccountBalance()) - (amount + lateFee));
		}
		
		if(account.getAccountBalance() < 0){
			insertHistories.add(new TBsOwedHistory(CommonUtils.getUUID(),
													account.getProjectId(),
													Math.abs(amount),
													account.getId(),
													new Date(),
													BillingEnum.IS_USED_USING.getIntV(),
													Math.abs(lateFee),
													new Date(),
													user.getUserId(),null,null));
		}
		
		return account;
	}

	//判断每行的数据是否含有空值
	private boolean isEmptyDataRow(TBsAssetAccountImportBean importBean,int rowNo, StringBuffer sb) {
		if(CommonUtils.isEmpty(importBean.getId())){
			sb.append("第").append(rowNo).append("行[房号]为空. 导入失败 . \n");
			return true;
		}else if(CommonUtils.isEmpty(importBean.getFullName())){
			sb.append("第").append(rowNo).append("行[房屋全名]为空. 导入失败.  \n ");
			return true;
		}
		return false;
	}

	/**
	 * 第三方催缴报表
	 * @param companyId
	 * @param projectCode
	 * @return
	 */
	@Override
	public List<AccountPaymentEntiy> getListThirdTcBuildingAllParent(String companyId, String projectCode) {
		List<TcBuildingAllParent>tcBuildingAllParentList=tcBuildingMapper.fingListThirdBuildingCodeAndArrears(projectCode);
		List<TBsOwedHistoryDto>tBsOwedHistoryDtoList=tBsOwedHistoryMapper.fingListThirdTBsOwedHistory(projectCode);
		List<PersonBuildingDto>personBuildingDtoList=personBuildingNewMapper.fingListPersonBuildingDto(projectCode);
		List<TBcCillectionName>tBcCillectionNameList=tBcCollectionMapper.findByTBcClillectionNameByProjectId(projectCode);
		List<TBsAssetAccountInitialization> tBsAssetAccountIniwtializationList
				=tBsAssetAccountMapper.findByThirdInitializationAndProjectId(projectCode);
		List<TBsAssetAccountDto> tBsAssetAccountDtoList=tBsAssetAccountMapper.findListThirdAccountAndArrears((projectCode));
		List<TBsPayInfoDto>tBsPayInfoDtoList= tBsPayInfoMapper.finfListTBsPayInfo(projectCode);
		List<TBsChargeBillHistoryDto>tBsChargeBillHistoryDtoList= tBsChargeBillHistoryMapper.findListTBsChargeBillHistory(projectCode);
		List<AccountPaymentEntiy> accountPaymentEntiyList =new ArrayList<>();

		for (TcBuildingAllParent tcBuilding:tcBuildingAllParentList) {


			AccountPaymentEntiy accountPaymentEntiy = new AccountPaymentEntiy();
			accountPaymentEntiy = createAccountPaytPaymentEntiy(accountPaymentEntiy, tcBuilding);
			for (TBsAssetAccountDto tBsAssetAccountDto:tBsAssetAccountDtoList){
				if(tcBuilding.getBuildingCode().equals(tBsAssetAccountDto.getBuildingCode())){
					accountPaymentEntiy=createTBsAssetAccountDto(accountPaymentEntiy,tBsAssetAccountDto);
				}
			}
			for (TBsPayInfoDto tBsPayInfoDto:tBsPayInfoDtoList){
				if (tcBuilding.getBuildingCode().equals(tBsPayInfoDto.getBuildingCode())){
					accountPaymentEntiy=createTBsPayInfoDto(accountPaymentEntiy,tBsPayInfoDto);
				}
			}
			for (TBsChargeBillHistoryDto tBsChargeBillHistoryDto:tBsChargeBillHistoryDtoList){
				if (tcBuilding.getBuildingCode().equals(tBsChargeBillHistoryDto.getBuildingCode())){
					accountPaymentEntiy=createTBsChargeBillHistoryDto(accountPaymentEntiy,tBsChargeBillHistoryDto);
				}
			}
			for (TBsOwedHistoryDto tBsOwedHistoryDto:tBsOwedHistoryDtoList){
				if (tcBuilding.getBuildingCode().equals(tBsOwedHistoryDto.getBuildingCode())){
					accountPaymentEntiy=createTBsOwedHistoryDto(accountPaymentEntiy,tBsOwedHistoryDto);
				}
			}
			for (PersonBuildingDto personBuildingDto:personBuildingDtoList){
				if (tcBuilding.getBuildingCode().equals(personBuildingDto.getBuildingCode())){
					accountPaymentEntiy=createpersonBuildingDto(accountPaymentEntiy,personBuildingDto);
				}
			}
			for (TBcCillectionName tBcCillectionName:tBcCillectionNameList){
				if (tcBuilding.getBuildingCode().equals(tBcCillectionName.getBuildingCode())){
					accountPaymentEntiy=createTBcCillectionName(accountPaymentEntiy,tBcCillectionName);
				}
			}
			for (TBsAssetAccountInitialization initialization : tBsAssetAccountIniwtializationList) {
				if (tcBuilding.getBuildingCode().equals(initialization.getBuildingCode())) {
					initialization.setLateFeeInit(0.0);
					accountPaymentEntiy = createInitialization(accountPaymentEntiy, initialization);
				}
			}
			accountPaymentEntiy = createNullAndAccountPaymentEntiy(accountPaymentEntiy);

			accountPaymentEntiy.setWyBenJin(CommonUtils.getScaleNumber(accountPaymentEntiy.getWy() + accountPaymentEntiy.getWylate(), 2));
			accountPaymentEntiy.setBtBenJin(CommonUtils.getScaleNumber(accountPaymentEntiy.getBt() + accountPaymentEntiy.getBtlate(), 2));
			accountPaymentEntiy.setWaterBenJin(CommonUtils.getScaleNumber(accountPaymentEntiy.getWater() + accountPaymentEntiy.getWaterlate(), 2));

			accountPaymentEntiy.setEleBenJin(CommonUtils.getScaleNumber(accountPaymentEntiy.getEle() + accountPaymentEntiy.getElelate(), 2));
			accountPaymentEntiy.setTotalAmount(CommonUtils.getScaleNumber(accountPaymentEntiy.getWy() + accountPaymentEntiy.getBt() + accountPaymentEntiy.getWater() + accountPaymentEntiy.getEle(), 2));
			accountPaymentEntiy=createPayOwn(accountPaymentEntiy);

			accountPaymentEntiyList.add(accountPaymentEntiy);

		}
		return accountPaymentEntiyList;


	}


	/**
	 * 催缴报表数据先全部拿到数据在根据buildingCode去查找想对应的数据
	 * @param companyId
	 * @param projectCode
	 * @return
	 */
	@Override
	public List<AccountPaymentEntiy> getListTcBuildingAllParent(String companyId, String projectCode) {
       List<TcBuildingAllParent>tcBuildingAllParentList=tcBuildingMapper.fingListBuildingCodeAndArrears(projectCode);
       List<TBsAssetAccountDto> tBsAssetAccountDtoList=tBsAssetAccountMapper.findListAccountAndArrears((projectCode));
      List<TBsPayInfoDto>tBsPayInfoDtoList= tBsPayInfoMapper.finfListTBsPayInfo(projectCode);
     List<TBsChargeBillHistoryDto>tBsChargeBillHistoryDtoList= tBsChargeBillHistoryMapper.findListTBsChargeBillHistory(projectCode);
     List<TBsOwedHistoryDto>tBsOwedHistoryDtoList=tBsOwedHistoryMapper.fingListTBsOwedHistory(projectCode);
		List<PersonBuildingDto>personBuildingDtoList=personBuildingNewMapper.fingListPersonBuildingDto(projectCode);
		List<TBcCillectionName>tBcCillectionNameList=tBcCollectionMapper.findByTBcClillectionNameByProjectId(projectCode);
		List<TBsAssetAccountInitialization> tBsAssetAccountIniwtializationList
				=tBsAssetAccountMapper.findByInitializationAndProjectId(projectCode);
		List<TcStall>tcStallList=tcStallMapper.findByList();
		List<AccountPaymentEntiy> accountPaymentEntiyList =new ArrayList<>();
		//以下根据buildingCode作为唯一标识,把信息添加AccountPaymentEntiy
		for (TcBuildingAllParent tcBuilding:tcBuildingAllParentList) {


				AccountPaymentEntiy accountPaymentEntiy = new AccountPaymentEntiy();
				accountPaymentEntiy = createAccountPaytPaymentEntiy(accountPaymentEntiy, tcBuilding);
			for (TBsAssetAccountDto tBsAssetAccountDto:tBsAssetAccountDtoList){
				if(tcBuilding.getBuildingCode().equals(tBsAssetAccountDto.getBuildingCode())){
					accountPaymentEntiy=createTBsAssetAccountDto(accountPaymentEntiy,tBsAssetAccountDto);
				}
			}
			for (TBsPayInfoDto tBsPayInfoDto:tBsPayInfoDtoList){
				if (tcBuilding.getBuildingCode().equals(tBsPayInfoDto.getBuildingCode())){
					accountPaymentEntiy=createTBsPayInfoDto(accountPaymentEntiy,tBsPayInfoDto);
				}
			}
			for (TBsChargeBillHistoryDto tBsChargeBillHistoryDto:tBsChargeBillHistoryDtoList){
				if (tcBuilding.getBuildingCode().equals(tBsChargeBillHistoryDto.getBuildingCode())){
					accountPaymentEntiy=createTBsChargeBillHistoryDto(accountPaymentEntiy,tBsChargeBillHistoryDto);
				}
			}
			for (TBsOwedHistoryDto tBsOwedHistoryDto:tBsOwedHistoryDtoList){
				if (tcBuilding.getBuildingCode().equals(tBsOwedHistoryDto.getBuildingCode())){
					accountPaymentEntiy=createTBsOwedHistoryDto(accountPaymentEntiy,tBsOwedHistoryDto);
				}
			}
			for (PersonBuildingDto personBuildingDto:personBuildingDtoList){
				if (tcBuilding.getBuildingCode().equals(personBuildingDto.getBuildingCode())){
					accountPaymentEntiy=createpersonBuildingDto(accountPaymentEntiy,personBuildingDto);
				}
			}
			for (TBcCillectionName tBcCillectionName:tBcCillectionNameList){
				if (tcBuilding.getBuildingCode().equals(tBcCillectionName.getBuildingCode())){
					accountPaymentEntiy=createTBcCillectionName(accountPaymentEntiy,tBcCillectionName);
				}
			}
				for (TBsAssetAccountInitialization initialization : tBsAssetAccountIniwtializationList) {
					if (tcBuilding.getBuildingCode().equals(initialization.getBuildingCode())) {
						accountPaymentEntiy = createInitialization(accountPaymentEntiy, initialization);
					}
				}
				accountPaymentEntiy = createNullAndAccountPaymentEntiy(accountPaymentEntiy);
			accountPaymentEntiy=createCatAndPaymentEntiy(accountPaymentEntiy,tcStallList,tcBuilding);
				accountPaymentEntiy.setWyBenJin(CommonUtils.getScaleNumber(accountPaymentEntiy.getWy() + accountPaymentEntiy.getWylate(), 2));
				accountPaymentEntiy.setBtBenJin(CommonUtils.getScaleNumber(accountPaymentEntiy.getBt() + accountPaymentEntiy.getBtlate(), 2));
				accountPaymentEntiy.setWaterBenJin(CommonUtils.getScaleNumber(accountPaymentEntiy.getWater() + accountPaymentEntiy.getWaterlate(), 2));

				accountPaymentEntiy.setEleBenJin(CommonUtils.getScaleNumber(accountPaymentEntiy.getEle() + accountPaymentEntiy.getElelate(), 2));
				accountPaymentEntiy.setTotalAmount(CommonUtils.getScaleNumber(accountPaymentEntiy.getWy() + accountPaymentEntiy.getBt() + accountPaymentEntiy.getWater() + accountPaymentEntiy.getEle(), 2));
				accountPaymentEntiy=createPayOwn(accountPaymentEntiy);

				accountPaymentEntiyList.add(accountPaymentEntiy);

		}
		return accountPaymentEntiyList;


	  }



	private AccountPaymentEntiy createCatAndPaymentEntiy(AccountPaymentEntiy accountPaymentEntiy, List<TcStall> tcStallList, TcBuildingAllParent tcBuilding) {
		for (TcStall tcStall:tcStallList){
			if (tcStall.getBuildingCode().equals(tcBuilding.getBuildingCode())){
				accountPaymentEntiy.setWyMonth(tcStall.getAdministrativeExpenese());
			}
		}
		return accountPaymentEntiy;
	}

	/**
	 *获取已支付的违约金金额
	 * @param accountPaymentEntiy
	 * @return
	 */
	private AccountPaymentEntiy createPayOwn(AccountPaymentEntiy accountPaymentEntiy) {
		double wyOwn=accountPaymentEntiy.getWyBillingFee()-accountPaymentEntiy.getWyChuHuaMoney()
				-accountPaymentEntiy.getWylate()-accountPaymentEntiy.getWyJMFee()-accountPaymentEntiy.getWyPayFee()+accountPaymentEntiy.getWy()+accountPaymentEntiy.getWyRefundFee();
    if (wyOwn<0){
    	accountPaymentEntiy.setWyPayOwnMoney(CommonUtils.getScaleNumber(Math.abs(wyOwn),2));
	}else {
    	accountPaymentEntiy.setWyPayOwnMoney(0.0);
	}

		double btOwn=accountPaymentEntiy.getBtBillingFee()-accountPaymentEntiy.getBtChuHuaMoney()
				-accountPaymentEntiy.getBtJMFee()-accountPaymentEntiy.getBtPayFee()+accountPaymentEntiy.getBt()+accountPaymentEntiy.getBtRefundFee();
		if (btOwn<0){
			accountPaymentEntiy.setBtPayOwnMoney(CommonUtils.getScaleNumber(Math.abs(btOwn),2));
		}else {
			accountPaymentEntiy.setBtPayOwnMoney(0.0);
		}
		double waterOwn=accountPaymentEntiy.getWaterBillingFee()-accountPaymentEntiy.getWaterChuHuaMoney()
				-accountPaymentEntiy.getWaterJMfFee()-accountPaymentEntiy.getWaterPayFee()+accountPaymentEntiy.getWater()+accountPaymentEntiy.getWaterRefundFee();
		if (waterOwn<0){
			accountPaymentEntiy.setWaterPayOwnMoney(CommonUtils.getScaleNumber(Math.abs(waterOwn),2));
		}else {
			accountPaymentEntiy.setWaterPayOwnMoney(0.0);
		}
		double eleOwn=accountPaymentEntiy.getEleBillingFee()-accountPaymentEntiy.getEleChuHuaMoney()
				-accountPaymentEntiy.getEleJMFee()-accountPaymentEntiy.getElePayFee()+accountPaymentEntiy.getEle()+accountPaymentEntiy.getEleRefundFee();
		if (eleOwn<0){
			accountPaymentEntiy.setElePayOwnMoney(CommonUtils.getScaleNumber(Math.abs(eleOwn),2));
		}else {
			accountPaymentEntiy.setElePayOwnMoney(0.0);
		}

		return accountPaymentEntiy;
	}

	/**
	 *
	 * 账户初始化金额
	 * @param accountPaymentEntiy
	 * @param initialization
	 * @return
	 */
	private AccountPaymentEntiy createInitialization(AccountPaymentEntiy accountPaymentEntiy, TBsAssetAccountInitialization initialization) {


		accountPaymentEntiy.setWyChuHuaMoney(initialization.getWyInit()+initialization.getWyArreasInit());
		if (initialization.getWyArreasInit()==initialization.getLateFeeInit()){
		accountPaymentEntiy.setWyLateMoney(0.0);
		}else {
			accountPaymentEntiy.setWyLateMoney(initialization.getLateFeeInit());
		}
		accountPaymentEntiy.setBtChuHuaMoney(initialization.getBtInit()+initialization.getBtArreasInit());
		accountPaymentEntiy.setWaterChuHuaMoney(initialization.getWaterInit()+initialization.getWaterArreasInit());
		accountPaymentEntiy.setEleChuHuaMoney(initialization.getElectInit()+initialization.getElectArreasInit());
		accountPaymentEntiy.setCommChuHuaMoney(initialization.getCommonInit());
		return accountPaymentEntiy;
	}

	/**
	 * 处理数据中的空值
	 * @param accountPaymentEntiy
	 * @return
	 */
	private AccountPaymentEntiy createNullAndAccountPaymentEntiy(AccountPaymentEntiy accountPaymentEntiy) {

		if (CommonUtils.isEmpty(accountPaymentEntiy.getWyChuHuaMoney())){
			accountPaymentEntiy.setWyChuHuaMoney(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWyLateMoney())){
			accountPaymentEntiy.setWyLateMoney(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getBtChuHuaMoney())){
			accountPaymentEntiy.setBtChuHuaMoney(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWaterChuHuaMoney())){
			accountPaymentEntiy.setWaterChuHuaMoney(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getEleChuHuaMoney())){
			accountPaymentEntiy.setEleChuHuaMoney(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getCommChuHuaMoney())){
			accountPaymentEntiy.setCommChuHuaMoney(0.0);
		}

		if (CommonUtils.isEmpty(accountPaymentEntiy.getQu())){
			accountPaymentEntiy.setQu("");
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getDong())){
			accountPaymentEntiy.setDong("");
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getDangYuan())){
			accountPaymentEntiy.setDangYuan("");
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getCeng())){
			accountPaymentEntiy.setCeng("");
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getHouseNumber())){
			accountPaymentEntiy.setHouseNumber("");
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getHouseNumber())){
			accountPaymentEntiy.setHouseNumber("");
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWy())){
			accountPaymentEntiy.setWy(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getBt())){
			accountPaymentEntiy.setBt(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWater())){
			accountPaymentEntiy.setWater(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getEle())){
			accountPaymentEntiy.setEle(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWyPayFee())){
			accountPaymentEntiy.setWyPayFee(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWyRefundFee())){
			accountPaymentEntiy.setWyRefundFee(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWyJMFee())){
			accountPaymentEntiy.setWyJMFee(0.0);
		}

		if (CommonUtils.isEmpty(accountPaymentEntiy.getBtPayFee())){
			accountPaymentEntiy.setBtPayFee(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getBtRefundFee())){
			accountPaymentEntiy.setBtRefundFee(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getBtJMFee())){
			accountPaymentEntiy.setBtJMFee(0.0);
		}

		if (CommonUtils.isEmpty(accountPaymentEntiy.getWater())){
			accountPaymentEntiy.setWater(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWaterPayFee())){
			accountPaymentEntiy.setWaterPayFee(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWaterRefundFee())){
			accountPaymentEntiy.setWaterRefundFee(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWaterJMfFee())){
			accountPaymentEntiy.setWaterJMfFee(0.0);
		}

		if (CommonUtils.isEmpty(accountPaymentEntiy.getElePayFee())){
			accountPaymentEntiy.setElePayFee(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getEleRefundFee())){
			accountPaymentEntiy.setEleRefundFee(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getEleJMFee())){
			accountPaymentEntiy.setEleJMFee(0.0);
		}

		if (CommonUtils.isEmpty(accountPaymentEntiy.getCommon())){
			accountPaymentEntiy.setCommon(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getCommonRefundFee())){
			accountPaymentEntiy.setCommonRefundFee(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWyBillingFee())){
		accountPaymentEntiy.setWyBillingFee(0.0);}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getBtBillingFee())){
		accountPaymentEntiy.setBtBillingFee(0.0);}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWaterBillingFee())){
		accountPaymentEntiy.setWaterBillingFee(0.0);}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getEleBillingFee())){
		accountPaymentEntiy.setEleBillingFee(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWylate())){
		accountPaymentEntiy.setWylate(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getBtlate())){
		accountPaymentEntiy.setBtlate(0.0);}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getWaterlate())){
		accountPaymentEntiy.setWaterlate(0.0);
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getElelate())){
		accountPaymentEntiy.setElelate(0.0);
		}

		if (CommonUtils.isEmpty(accountPaymentEntiy.getName())){
		accountPaymentEntiy.setName("");}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getRephone())){
			accountPaymentEntiy.setRephone("");
			accountPaymentEntiy.setPhone("");
		}
		if (CommonUtils.isEmpty(accountPaymentEntiy.getBlanceAccount())){
			accountPaymentEntiy.setBlanceAccount("");
			accountPaymentEntiy.setBlance("");
		}
		return accountPaymentEntiy;
	}


	/**
	 *添加TcbuildingCode 数据
	 * @param accountPaymentEntiy
	 * @param tcBuildingAllParent
	 * @return
	 */
	private AccountPaymentEntiy createAccountPaytPaymentEntiy(AccountPaymentEntiy accountPaymentEntiy, TcBuildingAllParent tcBuildingAllParent) {
		String str=tcBuildingAllParent.getHouseCode();
       str= str.substring(5,6);
       accountPaymentEntiy.setType(str);
		accountPaymentEntiy.setQu(tcBuildingAllParent.getQu());

		if (CommonUtils.isEmpty(tcBuildingAllParent.getQu())){
			accountPaymentEntiy.setQu("");
		}
		accountPaymentEntiy.setDong(tcBuildingAllParent.getDong());
		if (CommonUtils.isEmpty(tcBuildingAllParent.getDong())){
			accountPaymentEntiy.setDong("");
		}

		accountPaymentEntiy.setCeng(tcBuildingAllParent.getCeng());
		if (CommonUtils.isEmpty(tcBuildingAllParent.getCeng())){
			accountPaymentEntiy.setCeng("");
		}
        accountPaymentEntiy.setHouseNumber(tcBuildingAllParent.getHouseNumber());
		if (CommonUtils.isEmpty(tcBuildingAllParent.getHouseNumber())){
			accountPaymentEntiy.setHouseNumber("");
		}
		accountPaymentEntiy.setDangYuan(tcBuildingAllParent.getDangYuan());
		if (CommonUtils.isEmpty(tcBuildingAllParent.getDangYuan())){
			accountPaymentEntiy.setDangYuan("");
		}
		accountPaymentEntiy.setHouseCode(tcBuildingAllParent.getHouseCode());
		accountPaymentEntiy.setAddress(tcBuildingAllParent.getAddress());
		if (CommonUtils.isEmpty(tcBuildingAllParent.getWyPrice())||CommonUtils.isEmpty(tcBuildingAllParent.getBuildingArea())){
			accountPaymentEntiy.setWyMonth(0.0);
		}else {
		accountPaymentEntiy.setWyMonth(CommonUtils.getScaleNumber(tcBuildingAllParent.getWyPrice()*tcBuildingAllParent.getBuildingArea(),2));}
		return accountPaymentEntiy;
	}

	/**
	 * 添加账户余额信息
	 * @param accountPaymentEntiy
	 * @param tBsAssetAccountDto
	 * @return
	 */
	private AccountPaymentEntiy createTBsAssetAccountDto(AccountPaymentEntiy accountPaymentEntiy, TBsAssetAccountDto tBsAssetAccountDto) {
		accountPaymentEntiy.setWy(tBsAssetAccountDto.getWyAmount());
		if (CommonUtils.isEmpty(tBsAssetAccountDto.getWyAmount())){
		accountPaymentEntiy.setWy(0.0);
		}else if (tBsAssetAccountDto.getWyAmount()>0){
			accountPaymentEntiy.setWy(0.0);
		}
		accountPaymentEntiy.setBt(tBsAssetAccountDto.getBtamount());
		if (CommonUtils.isEmpty(tBsAssetAccountDto.getBtamount())){
		accountPaymentEntiy.setBt(0.0);
		}else  if (tBsAssetAccountDto.getBtamount()>0){
			accountPaymentEntiy.setBt(0.0);
		}
		accountPaymentEntiy.setWater(tBsAssetAccountDto.getWateramount());
		if (CommonUtils.isEmpty(tBsAssetAccountDto.getWateramount())){
		accountPaymentEntiy.setWater(0.0);
		}else  if (tBsAssetAccountDto.getWateramount()>0){
			accountPaymentEntiy.setWater(0.0);
		}
		accountPaymentEntiy.setEle(tBsAssetAccountDto.getEleamount());
		if (CommonUtils.isEmpty(tBsAssetAccountDto.getEleamount())){
		accountPaymentEntiy.setEle(0.0);
		}else if (tBsAssetAccountDto.getEleamount()>0){
			accountPaymentEntiy.setEle(0.0);
		}
		return accountPaymentEntiy;
	}

	/**
	 * 添加payinfo 信息
	 * @param accountPaymentEntiy
	 * @param tBsPayInfoDto
	 * @return
	 */
	private AccountPaymentEntiy createTBsPayInfoDto(AccountPaymentEntiy accountPaymentEntiy, TBsPayInfoDto tBsPayInfoDto) {

	accountPaymentEntiy.setWyPayFee(tBsPayInfoDto.getWypayN());
	if (CommonUtils.isEmpty(tBsPayInfoDto.getWypayN())){
		accountPaymentEntiy.setWyPayFee(0.0);
		}
	accountPaymentEntiy.setWyRefundFee(tBsPayInfoDto.getWypayBack());
		if (CommonUtils.isEmpty(tBsPayInfoDto.getWypayBack())){
			accountPaymentEntiy.setWyRefundFee(0.0);
		}
	accountPaymentEntiy.setWyJMFee(tBsPayInfoDto.getWypayJM());
		if (CommonUtils.isEmpty(tBsPayInfoDto.getWypayJM())){
			accountPaymentEntiy.setWyJMFee(0.0);
		}
	accountPaymentEntiy.setBtPayFee(tBsPayInfoDto.getBtpayN());
		if (CommonUtils.isEmpty(tBsPayInfoDto.getBtpayN())){
			accountPaymentEntiy.setBtPayFee(0.0);
		}
	accountPaymentEntiy.setBtRefundFee(tBsPayInfoDto.getBtpayBack());
		if (CommonUtils.isEmpty(tBsPayInfoDto.getBtpayBack())){
			accountPaymentEntiy.setBtRefundFee(0.0);
		}
	accountPaymentEntiy.setBtJMFee(tBsPayInfoDto.getBtpayJM());
		if (CommonUtils.isEmpty(tBsPayInfoDto.getBtpayJM())){
			accountPaymentEntiy.setBtJMFee(0.0);
		}
	accountPaymentEntiy.setWaterPayFee(tBsPayInfoDto.getWaterpayN());
		if (CommonUtils.isEmpty(tBsPayInfoDto.getWaterpayN())){
			accountPaymentEntiy.setWaterPayFee(0.0);
		}
	accountPaymentEntiy.setWaterRefundFee(tBsPayInfoDto.getWaterpayBack());
		if (CommonUtils.isEmpty(tBsPayInfoDto.getWaterpayBack())){
			accountPaymentEntiy.setWaterRefundFee(0.0);
		}
	accountPaymentEntiy.setWaterJMfFee(tBsPayInfoDto.getWaterpayJM());
		if (CommonUtils.isEmpty(tBsPayInfoDto.getWaterpayJM())){
			accountPaymentEntiy.setWaterJMfFee(0.0);
		}
	accountPaymentEntiy.setElePayFee(tBsPayInfoDto.getElectpayN());
		if (CommonUtils.isEmpty(tBsPayInfoDto.getElectpayN())){
			accountPaymentEntiy.setElePayFee(0.0);
		}
	accountPaymentEntiy.setEleRefundFee(tBsPayInfoDto.getElectpayBack());
		if (CommonUtils.isEmpty(tBsPayInfoDto.getElectpayBack())){
			accountPaymentEntiy.setEleRefundFee(0.0);
		}
	accountPaymentEntiy.setEleJMFee(tBsPayInfoDto.getElectpayJM());
		if (CommonUtils.isEmpty(tBsPayInfoDto.getElectpayJM())){
			accountPaymentEntiy.setEleJMFee(0.0);
		}
	accountPaymentEntiy.setCommon(tBsPayInfoDto.getCommonpayN());
		if (CommonUtils.isEmpty(tBsPayInfoDto.getCommonpayN())){
			accountPaymentEntiy.setCommon(0.0);
		}
	accountPaymentEntiy.setCommonRefundFee(tBsPayInfoDto.getCommonpayBack());
		if (CommonUtils.isEmpty(tBsPayInfoDto.getCommonpayBack())){
			accountPaymentEntiy.setCommonRefundFee(0.0);
		}
	return accountPaymentEntiy;
	}

	/**
	 * 导入计费数据
	 * @param accountPaymentEntiy
	 * @param tBsChargeBillHistoryDto
	 * @return
	 */
	private AccountPaymentEntiy createTBsChargeBillHistoryDto(AccountPaymentEntiy accountPaymentEntiy, TBsChargeBillHistoryDto tBsChargeBillHistoryDto) {
		accountPaymentEntiy.setWyBillingFee(tBsChargeBillHistoryDto.getWyamount());
		if (CommonUtils.isEmpty(tBsChargeBillHistoryDto.getWyamount())){
			accountPaymentEntiy.setWyBillingFee(0.0);
		}
		accountPaymentEntiy.setBtBillingFee(tBsChargeBillHistoryDto.getBtamount());
		if (CommonUtils.isEmpty(tBsChargeBillHistoryDto.getBtamount())){
			accountPaymentEntiy.setBtBillingFee(0.0);
		}
		accountPaymentEntiy.setWaterBillingFee(tBsChargeBillHistoryDto.getWateramount());
		if (CommonUtils.isEmpty(tBsChargeBillHistoryDto.getWyamount())){
			accountPaymentEntiy.setWaterBillingFee(0.0);
		}
		accountPaymentEntiy.setEleBillingFee(tBsChargeBillHistoryDto.getElectamount());
		if (CommonUtils.isEmpty(tBsChargeBillHistoryDto.getElectamount())){
			accountPaymentEntiy.setEleBillingFee(0.0);
		}
		return  accountPaymentEntiy;
	}

	/**
	 * 添加违约金数据
	 * @param accountPaymentEntiy
	 * @param tBsOwedHistoryDto
	 * @return
	 */
	private AccountPaymentEntiy createTBsOwedHistoryDto(AccountPaymentEntiy accountPaymentEntiy, TBsOwedHistoryDto tBsOwedHistoryDto) {
		accountPaymentEntiy.setWylate(tBsOwedHistoryDto.getWyoweamount());
		if (CommonUtils.isEmpty(tBsOwedHistoryDto.getWyoweamount())){
			accountPaymentEntiy.setWylate(0.0);
		}
		accountPaymentEntiy.setBtlate(tBsOwedHistoryDto.getBtoweamount());
		if (CommonUtils.isEmpty(tBsOwedHistoryDto.getBtoweamount())){
			accountPaymentEntiy.setBtlate(0.0);
		}
		accountPaymentEntiy.setWaterlate(tBsOwedHistoryDto.getWateroweamount());
		if (CommonUtils.isEmpty(tBsOwedHistoryDto.getWateroweamount())){
			accountPaymentEntiy.setWaterlate(0.0);
		}
		accountPaymentEntiy.setElelate(tBsOwedHistoryDto.getEleoweamount());
		if (CommonUtils.isEmpty(tBsOwedHistoryDto.getEleoweamount())){
			accountPaymentEntiy.setElelate(0.0);
		}
		return accountPaymentEntiy;
	}

	/**
	 * 添加电话号码
	 * @param accountPaymentEntiy
	 * @param personBuildingDto
	 * @return
	 */
	private AccountPaymentEntiy createpersonBuildingDto(AccountPaymentEntiy accountPaymentEntiy, PersonBuildingDto personBuildingDto) {

		accountPaymentEntiy.setName(personBuildingDto.getName());
		if (CommonUtils.isNotEmpty(personBuildingDto.getPhone())) {
			accountPaymentEntiy.setRephone(personBuildingDto.getPhone());
			accountPaymentEntiy.setPhone(personBuildingDto.getPhone());
		}else {
			accountPaymentEntiy.setRephone("");
			accountPaymentEntiy.setPhone("");
		}
		return accountPaymentEntiy;
	}

	/**
	 * 天骄银行卡号信息
	 * @param accountPaymentEntiy
	 * @param tBcCillectionName
	 * @return
	 */
	private AccountPaymentEntiy createTBcCillectionName(AccountPaymentEntiy accountPaymentEntiy, TBcCillectionName tBcCillectionName) {
		if (CommonUtils.isNotEmpty(tBcCillectionName.getCardNo())){
			accountPaymentEntiy.setBlanceAccount(tBcCillectionName.getCardNo());
			accountPaymentEntiy.setBlance(tBcCillectionName.getBackName());
		}else {
			accountPaymentEntiy.setBlanceAccount("");
			accountPaymentEntiy.setBlance("");
		}
		return accountPaymentEntiy;
	}

	public List<AccountBillDto> getAccountBillDto(String companyId, String buildingCode) {
		List<AccountBillDto> accountBillDtoList=new ArrayList<>();
		List<TBsPayInfoDto> payInfoDtoList=tBsPayInfoMapper.findDatePayInfo(buildingCode);
		List<TBsChargeBillHistoryDto>tBsChargeBillHistoryDtoList=tBsChargeBillHistoryMapper.findDateCharge(buildingCode);
		TBsOwedHistoryDto tBsOwedHistoryDto=tBsOwedHistoryMapper.findOweAndBuildingCodeAndTime(buildingCode);
		List<MeterDateAndUseCount>waterUseCountList=tcMeterDataMapper.findAllWaterDateAndUseCount(buildingCode);
		List<MeterDateAndUseCount> eleUseCountList=tcMeterDataMapper.findAllEleDateAndUseCount(buildingCode);
		waterUseCountList=dealWithDateMonthAddOne(waterUseCountList);
		eleUseCountList=dealWithDateMonthAddOne(eleUseCountList);
		accountBillDtoList=createAccountAndBuildingCodeBill(accountBillDtoList,tBsOwedHistoryDto,payInfoDtoList,tBsChargeBillHistoryDtoList,waterUseCountList,eleUseCountList);

		return accountBillDtoList;
	}




	private List<AccountBillDto> createAccountAndBuildingCodeBill
			(List<AccountBillDto> accountBillDtoList, TBsOwedHistoryDto tBsOwedHistoryDto, List<TBsPayInfoDto> payInfoDtoList, List<TBsChargeBillHistoryDto> tBsChargeBillHistoryDtoList, List<MeterDateAndUseCount> waterUseCountList, List<MeterDateAndUseCount> eleUseCountList) {
        if (CommonUtils.isNotEmpty(tBsChargeBillHistoryDtoList)){
				for (TBsChargeBillHistoryDto tBsChargeBillHistoryDto:tBsChargeBillHistoryDtoList){
					AccountBillDto accountBillDto= new AccountBillDto();
					accountBillDto.setWy(tBsChargeBillHistoryDto.getWyamount());
					accountBillDto.setBt(tBsChargeBillHistoryDto.getBtamount());
					accountBillDto.setWater(tBsChargeBillHistoryDto.getWateramount());
					accountBillDto.setEle(tBsChargeBillHistoryDto.getElectamount());
					accountBillDto.setTime(tBsChargeBillHistoryDto.getReadTime().toString());
				if (CommonUtils.isNotEmpty(payInfoDtoList)){
					for (TBsPayInfoDto tBsPayInfoDto:payInfoDtoList) {
						if (tBsChargeBillHistoryDto.getReadTime().equals(tBsPayInfoDto.getDate())){
							accountBillDto.setWyPay(tBsPayInfoDto.getWypayN());
							accountBillDto.setWyBack(tBsPayInfoDto.getWypayBack());
							accountBillDto.setWaterPay(tBsPayInfoDto.getWaterpayN());
							accountBillDto.setWaterBack(tBsPayInfoDto.getBtpayBack());
							accountBillDto.setBtPay(tBsPayInfoDto.getBtpayN());
							accountBillDto.setBtBack(tBsPayInfoDto.getBtpayBack());
							accountBillDto.setElePay(tBsPayInfoDto.getElectpayN());
							accountBillDto.setEleBack(tBsPayInfoDto.getElectpayBack());
							accountBillDto.setComm(tBsPayInfoDto.getCommonpayN());
							accountBillDto.setCommBack(tBsPayInfoDto.getCommonpayBack());
						}
					}
				}
				if (CommonUtils.isNotEmpty( waterUseCountList)){
					for (MeterDateAndUseCount waterUseCount:waterUseCountList){
					if (tBsChargeBillHistoryDto.getReadTime().toString().equals(waterUseCount.getTime())){
						accountBillDto.setWaterUserCount(waterUseCount.getUseCount());
					}

				}
				}
				if (CommonUtils.isNotEmpty(eleUseCountList)){
					for (MeterDateAndUseCount eleUseCount:eleUseCountList){
						if (tBsChargeBillHistoryDto.getReadTime().toString().equals(eleUseCount.getTime())){
							accountBillDto.setWaterUserCount(eleUseCount.getUseCount());
						}
				}
					}
				if (CommonUtils.isNotEmpty(tBsOwedHistoryDto)){
					if (tBsChargeBillHistoryDto.getReadTime().toString().equals("2018-05")){
						accountBillDto.setWyOwn(tBsOwedHistoryDto.getWyoweamount());
						accountBillDto.setBtOwn(tBsOwedHistoryDto.getBtoweamount());
						accountBillDto.setWaterOwn(tBsOwedHistoryDto.getWateroweamount());
						accountBillDto.setEleOwn(tBsOwedHistoryDto.getEleoweamount());
					}
					}
					accountBillDto=createAccount(accountBillDto);
					accountBillDtoList.add(accountBillDto);
				}

        }
		return  accountBillDtoList;
	}

	/**
	 * 判断accountBillDto里的为空
	 * @param accountBillDto
	 * @return
	 */
	private AccountBillDto createAccount(AccountBillDto accountBillDto) {
		if (CommonUtils.isEmpty(accountBillDto.getWyPay())){
			accountBillDto.setWyPay(0.0);
		}
		if (CommonUtils.isEmpty(accountBillDto.getWyBack())){
			accountBillDto.setWyBack(0.0);
		}
		if (CommonUtils.isEmpty(accountBillDto.getWyOwn())){
			accountBillDto.setWyOwn(0.0);
		}

		if (CommonUtils.isEmpty(accountBillDto.getBtPay())){
			accountBillDto.setBtPay(0.0);
		}
		if (CommonUtils.isEmpty(accountBillDto.getBtBack())){
			accountBillDto.setBtBack(0.0);
		}
		if (CommonUtils.isEmpty(accountBillDto.getBtOwn())){
			accountBillDto.setBtOwn(0.0);
		}

		if (CommonUtils.isEmpty(accountBillDto.getWaterPay())){
			accountBillDto.setWaterPay(0.0);
		}
		if (CommonUtils.isEmpty(accountBillDto.getWaterBack())){
			accountBillDto.setWaterBack(0.0);
		}
		if (CommonUtils.isEmpty(accountBillDto.getWaterOwn())){
			accountBillDto.setWaterOwn(0.0);
		}

		if (CommonUtils.isEmpty(accountBillDto.getElePay())){
			accountBillDto.setElePay(0.0);
		}
		if (CommonUtils.isEmpty(accountBillDto.getEleBack())){
			accountBillDto.setEleBack(0.0);
		}
		if (CommonUtils.isEmpty(accountBillDto.getEleOwn())){
			accountBillDto.setEleOwn(0.0);
		}
		if (CommonUtils.isEmpty(accountBillDto.getComm())){
			accountBillDto.setComm(0.0);
		}
		if (CommonUtils.isEmpty(accountBillDto.getCommBack())){
			accountBillDto.setCommBack(0.0);
		}
		if (CommonUtils.isEmpty(accountBillDto.getEleUserCount())){
			accountBillDto.setEleUserCount(0.0);
		}
		if (CommonUtils.isEmpty(accountBillDto.getWaterUserCount())){
			accountBillDto.setWaterUserCount(0.0);
		}

		return  accountBillDto;
	}

	private List<MeterDateAndUseCount> dealWithDateMonthAddOne(List<MeterDateAndUseCount> useCountList) {
		if (CommonUtils.isNotEmpty(useCountList)){
			List<MeterDateAndUseCount> useCountList1=new ArrayList<>();
			for (MeterDateAndUseCount useCount:useCountList){

				Date useTime=CommonUtils.addMonth(useCount.getTimeDate(),1);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");

				useCount.setTime(simpleDateFormat.format(useTime));
				useCountList1.add(useCount);
			}
			return  useCountList1;
		}
		return useCountList;
	}

}


