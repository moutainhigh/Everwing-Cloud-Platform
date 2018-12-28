package com.everwing.autotask.core.service.impl.bill;

import com.alibaba.fastjson.JSON;
import com.everwing.autotask.core.dao.*;
import com.everwing.autotask.core.service.bill.WaterElectAutoBillingService;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.BigDecimalUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.TcOrderComplaintAndCompleteEnum;
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
import com.everwing.utils.FormulaCalculationUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 水电自动计费
 *
 * @author DELL shiny
 * @create 2018/6/5
 */
@Log4j2
@Service
public class WaterElectAutoBillingServiceImpl implements WaterElectAutoBillingService {

    @Value("${batch_add_max_count}")
    private int BATCH_ADD_COUNT;

    @Value("${batch_update_max_count}")
    private int BATCH_UPDATE_COUNT;

    @Autowired
    private TBsProjectMapper tBsProjectMapper;

    @Autowired
    private TBsChargingSchemeMapper tBsChargingSchemeMapper;

    @Autowired
    private TBsChargingRulesMapper tBsChargingRulesMapper;

    @Autowired
    private TBsChargeBillTotalMapper tBsChargeBillTotalMapper;

    @Autowired
    private TBsRuleBuildingRelationMapper tBsRuleBuildingRelationMapper;

    @Autowired
    private TBsChargeTypeMapper tBsChargeTypeMapper;

    @Autowired
    private TcElectMeterMapper tcElectMeterMapper;

    @Autowired
    private TcWaterMeterMapper tcWaterMeterMapper;

    @Autowired
    private TcMeterDataMapper tcMeterDataMapper;

    @Autowired
    private TcOrderCompleteMapper tcOrderCompleteMapper;

    @Autowired
    private TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;

    @Autowired
    private TBsAssetAccountMapper tBsAssetAccountMapper;

    @Override
    public void autoBilling(String companyId, Integer type) {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("status", BillingEnum.STATUS_START.getIntV());								//项目启用状态
        if(type.equals(BillingEnum.ACCOUNT_TYPE_ELECT.getIntV())){
            paramMap.put("electStatus", BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV());		//电费种待计费状态
            paramMap.put("schemeType", 4);
        }
        if(type.equals(BillingEnum.ACCOUNT_TYPE_WATER.getIntV())){
            paramMap.put("waterStatus", BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV()); //电费种待计费状态
            paramMap.put("schemeType", 3);
        }
        paramMap.put("chargeType", BillingEnum.TYPE_AUTO.getIntV());						//计费方案类型
        paramMap.put("isUsed", BillingEnum.IS_USED_USING.getIntV());							//计费方案可用状态
        List<TBsProject> projects = tBsProjectMapper.findCanNewBillingProjects(paramMap);		//获取可计费的项目
        if(projects.isEmpty()){
            log.warn("当前物业公司下,未找到可计费的项目. 计费完成 . companyId : {}",companyId);
            return ;
        }

        //电费有数据的,再用消息队列去项目级的计费
        if(type.equals(BillingEnum.ACCOUNT_TYPE_ELECT.getIntV())){
            for(TBsProject p : projects){
                log.info("{}自动计费: 项目: {}","电费",p.toString());
                billing(p,BillingEnum.ACCOUNT_TYPE_ELECT.getIntV(),companyId);
            }
        }else{
            //水费有数据的,再用消息队列去项目级的计费
            for(TBsProject p : projects){
                log.info("{}自动计费: 项目: {}","水费",p.toString());
                billing(p,BillingEnum.ACCOUNT_TYPE_WATER.getIntV(),companyId);
            }
        }
    }

    private void billing(TBsProject project,Integer type,String companyId){
        TBsChargingScheme scheme = hasCanBillingScheme(project,type);
        if(null == scheme){
            log.warn("{}自动计费: 当前项目下未找到可供计费的方案 . 自动计费流程停止. 项目: {} ",type, project.toString());
            return;
//			throw new ECPBusinessException(getFeeType(meterTypeFlag)+"自动计费未找到可用的自动计费方案. 自动计费流程停止.");
        }
        List<TBsChargingRules> rulesList = tBsChargingRulesMapper.getTBsChargingRulesBySchemeId(scheme.getId());
        if(CollectionUtils.isEmpty(rulesList)){
            log.warn("{}自动计费: 当前项目下的 方案:{}中未找到计费规则.自动计费流程停止. 项目: {} ",type,scheme.getSchemeName(), project.toString());
            return;
            //			throw new ECPBusinessException(getFeeType(meterTypeFlag)+"自动计费: 当前项目下的 方案:"+scheme.getSchemeName()+"中未找到计费规则.自动计费流程停止");
        }

        //2. 获取本月的总账单,若未生成,则生成一张新的总账单
        String nextTotalId = CommonUtils.getUUID();
        Map<String,Object> returnMap = hasTotalBillInCurrentCycle(project,type,scheme);
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
            if(type.equals(BillingEnum.ACCOUNT_TYPE_ELECT.getIntV())){
                paramRuleMap.put("meterType", 1);
            }
            if(type.equals(BillingEnum.ACCOUNT_TYPE_WATER.getIntV())){
                paramRuleMap.put("meterType", 0);
            }
            paramRuleMap.put("ruleId", tBsChargingRules.getId());
            paramRuleMap.put("projectId", project.getProjectId());
            List<String> relationIdsList = tBsRuleBuildingRelationMapper.getBuildingCodeByRuleId(paramRuleMap); //水电表查找有收费对象的关联建筑
            if(CollectionUtils.isEmpty(relationIdsList)){
                log.warn("{}{}计费规则没有关联建筑,自动计费动作完成",tBsChargingRules.getRuleName(),type);
                continue;
            }
            //查找费用项公式
            List<TBsChargeType> tBsChargingTypeList = tBsChargeTypeMapper.selectChargeType(tBsChargingRules.getId());
            if(CollectionUtils.isEmpty(tBsChargingTypeList)){
                log.warn("{}{}计费规则没有收费项,自动计费动作完成",tBsChargingRules.getRuleName(),type);
                continue;
            }
            //每一个规则下的每一个建筑都需要通过这个规则下所有的费用项进行计算费用
            for(String ruleationId:relationIdsList){
                boolean bool = billingWaterElectFee(companyId,scheme,ruleationId,type,tBsChargingRules.getRuleName(),currentTotalBill,tBsChargingTypeList,lastBillDate,nextTotalId,insertList,updateList);
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

        //计算完毕之后,生成下个月的总账单 , 账单的上期数据由上方的总账单传入
        TBsChargeBillTotal nextBillTotal = new TBsChargeBillTotal();
        nextBillTotal.setId(nextTotalId);
        nextBillTotal.setAuditStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
        nextBillTotal.setChargingType(BillingEnum.TYPE_MANUAL.getIntV());
        nextBillTotal.setProjectId(project.getProjectId());
        nextBillTotal.setType(scheme.getSchemeType());
        nextBillTotal.setLastOwedFee(0.0);
        nextBillTotal.setIsRebilling(BillingEnum.IS_REBILLING_NO.getIntV());
        nextBillTotal.setCmacIsBilling(BillingEnum.common_account_is_not_bill.getIntV());
        nextBillTotal.setCreateId(project.getModifyId());
        nextBillTotal.setCreateTime(new Date());
        nextBillTotal.setModifyTime(new Date());
        nextBillTotal.setModifyId(project.getModifyId());
        nextBillTotal.setSchemeId(scheme.getId());
        nextBillTotal.setLastTotalId(currentTotalBill.getId());
        //获取本期的总金额
        if((boolean)returnMap.get("isInsert")){
            tBsChargeBillTotalMapper.insertChargeBillTotal(currentTotalBill);
        }else{
            tBsChargeBillTotalMapper.update(currentTotalBill);
        }
        tBsChargeBillTotalMapper.insertChargeBillTotal(nextBillTotal);
        if(type.equals(BillingEnum.ACCOUNT_TYPE_ELECT.getIntV())){
            project.setElectStatus(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV());
        }
        if(type.equals(BillingEnum.ACCOUNT_TYPE_WATER.getIntV())){
            project.setWaterStatus(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV());
        }

        project.setCurrentFee(CommonUtils.null2Double(project.getCurrentFee()) + CommonUtils.null2Double(currentTotalBill.getCurrentFee()));
        project.setLastOwedFee(CommonUtils.null2Double(project.getLastOwedFee()) +CommonUtils.null2Double(currentTotalBill.getLastOwedFee()));
        project.setTotalFee(CommonUtils.null2Double(project.getTotalFee()) + CommonUtils.null2Double(currentTotalBill.getTotalFee()));

        tBsProjectMapper.update(project);
        log.info("{}自动计费: 自动计费完成. 项目: {}",type,project.toString());
    }

    private boolean billingWaterElectFee (String companyId,
                                          TBsChargingScheme scheme,
                                          String ruleationId,
                                          Integer type,
                                          String ruleName,
                                          TBsChargeBillTotal currentTotalBill,
                                          List<TBsChargeType> tBsChargingTypeList,
                                          Date lastBillDate,
                                          String nextTotalId,
                                          List<TBsChargeBillHistory> insertList,
                                          List<TBsChargeBillHistory> updateList
    )
    {
        //根据收费对象ID查找表，如果表不存在或者表不为收费对象，则跳过
        if(type.equals(BillingEnum.ACCOUNT_TYPE_ELECT.getIntV())){
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
        if(type.equals(BillingEnum.ACCOUNT_TYPE_WATER.getIntV())){
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
        if(type.equals(BillingEnum.ACCOUNT_TYPE_ELECT.getIntV())){
            paramMap.put("meterType", 1); //电表
        }
        if(type.equals(BillingEnum.ACCOUNT_TYPE_WATER.getIntV())){
            paramMap.put("meterType", 0); //水表
        }
        paramMap.put("projectId", currentTotalBill.getProjectId());
        paramMap.put("lastBillTime", lastBillDate);
        paramMap.put("relationBuilding", ruleationId);
        List<Map<String,Object>> resultDetailMap = tcMeterDataMapper.getCountAndFeeObjByProjct(paramMap);
        //得到产权变更未计费的表读数
        List<Map<String, Object>> changeAssetData =tcOrderCompleteMapper.getNoBill(ruleationId,currentTotalBill.getProjectId());
        //这里还需要找产权变更的抄表
        if(CommonUtils.isEmpty(resultDetailMap) && CommonUtils.isEmpty(changeAssetData)){
            log.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"规则名:"+ruleName+"对用的建筑编号:"+ruleationId+"没有找到抄表数据,请检查!"));
            return false;
        }
        boolean returnMessage1=true;
        boolean returnMessage2=true;
        if(CommonUtils.isNotEmpty(resultDetailMap)){
            returnMessage1= calculationByUseCount(currentTotalBill,ruleationId,resultDetailMap,tBsChargingTypeList,nextTotalId,insertList,updateList,type,companyId,scheme);
        }
        if(CommonUtils.isNotEmpty(changeAssetData)){
            returnMessage2=calculationByUseCount(currentTotalBill,ruleationId,changeAssetData,tBsChargingTypeList,nextTotalId,insertList,updateList,type,companyId,scheme);
        }
        //计费玩之后，更改那些产权变更工单为已经计费状态
        for(Map<String, Object> mapComplete:changeAssetData){
            String orderCompleteId = String.valueOf(mapComplete.get("id"));
            TcOrderComplete tcOrderComplete = tcOrderCompleteMapper.findById(orderCompleteId);
            tcOrderComplete.setIsAlreadyBilling(TcOrderComplaintAndCompleteEnum.IS_BILLING_YES.getIntV());
            tcOrderCompleteMapper.singleUpdate(tcOrderComplete);
        }
        if(returnMessage1==true && returnMessage2 ==true){
            return true;
        }else{
            return false;
        }
    }

    private Map<String,Object> hasTotalBillInCurrentCycle(TBsProject project ,Integer type,TBsChargingScheme scheme){
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

            log.info(type.equals(BillingEnum.ACCOUNT_TYPE_ELECT.getIntV())?"电费计费: 当前未找到本月的计费总账单,需要重新生成. 项目: {} ":"水费计费: 当前未找到本月的计费总账单,需要重新生成. 项目: {} ",project.toString());
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

    private TBsChargingScheme hasCanBillingScheme(TBsProject entity ,Integer type){
        //2. 获取该项目下, 正在启用的scheme
        TBsChargingScheme paramScheme = new TBsChargingScheme();
        if(type.equals(BillingEnum.ACCOUNT_TYPE_ELECT.getIntV())){
            paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_ELECT.getIntV());	//电费scheme
        }
        if(type.equals(BillingEnum.ACCOUNT_TYPE_WATER.getIntV())){
            paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_WATER.getIntV());	//水费scheme
        }
        paramScheme.setProjectId(entity.getProjectId());	//project code
        paramScheme.setChargingType(0);
        TBsChargingScheme scheme = tBsChargingSchemeMapper.findUsingScheme(paramScheme);

        if(CommonUtils.isEmpty(scheme)){
            log.warn("{}计费:  该项目未找到可用的{}计费方案,无法{}计费. 项目: {}","自动","自动",entity.toString());
            return null;

        }else{
            if(BillingEnum.TYPE_AUTO.getIntV() == scheme.getChargingType()){
                return scheme;
            }else{
                log.warn("{}自动计费: 该项目可用的{}计费方案为手动计费,不参与自动计费, 自动计费流程停止 . 项目: {}",type,"自动",entity.toString());
                return null;
            }
        }
    }

    private Boolean calculationByUseCount( TBsChargeBillTotal currentTotalBill, String ruleationId,
                                           List<Map<String,Object>> resultDetailMap,List<TBsChargeType> tBsChargingTypeList,
                                           String nextTotalId, List<TBsChargeBillHistory> insertList,
                                           List<TBsChargeBillHistory> updateList,Integer type,String companyId,TBsChargingScheme scheme){
        boolean returnMessage = true;
        //查找判断本月的该建筑的数据是否已经生成,若未生成,则生成本月的
        TBsChargeBillHistory paramBill = new TBsChargeBillHistory();
        paramBill.setProjectId(currentTotalBill.getProjectId());
        paramBill.setBuildingCode(ruleationId);
        paramBill.setChargeTotalId(currentTotalBill.getId());
        List<TBsChargeBillHistory> histories = tBsChargeBillHistoryMapper.findCurrentDetailBill(paramBill);

        TBsChargeBillHistory currentDetailBill =null;
        boolean isInsert = false;
        if(CommonUtils.isNotEmpty(histories)){
            currentDetailBill =  histories.get(0);
            currentDetailBill.setModifyId(currentDetailBill.getModifyId());
            currentDetailBill.setModifyTime(new Date());
        }else{
            //这里新增一个第一次计费，如果之前用户就有欠费的情况
            if(type.equals(BillingEnum.ACCOUNT_TYPE_ELECT.getIntV())){
                type = 4; //电表
            }else if(type.equals(BillingEnum.ACCOUNT_TYPE_WATER.getIntV())) {
                type = 3;
            }

            TBsAssetAccount account = tBsAssetAccountMapper.lookupByBuildCodeAndType(ruleationId, type);
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

        //如果这里是虚拟表，这里费用不需要累加，（项目里只计子表，不计总表虚拟表，因为虚拟总表的用量是其下各个子表的用量之和）
//		currentDetailBill.get
        if(currentTotalBill.getType().equals(4)){//电表
            //查找是否是虚拟电表总表
            ElectMeter electMeter = tcElectMeterMapper.findMByBuildCodeAndProjectId(currentDetailBill.getProjectId(),currentDetailBill.getBuildingCode());
            if(!CommonUtils.isEmpty(electMeter)){
//				currentTotalBill.setCurrentFee(CommonUtils.null2Double(currentTotalBill.getCurrentFee()) + currentDetailBill.getCurrentFee());	//总金额累计
//				currentTotalBill.setTotalFee(CommonUtils.null2Double(currentTotalBill.getTotalFee()) + currBillFee);
                TBsChargeBillHistory nextBill = createNextBill(ruleationId,nextTotalId,(String)resultDetailMap.get(0).get("buildingFullName"),currentTotalBill.getProjectId(),currentDetailBill);
                currentDetailBill.setBillingTime(new Date());
                if(isInsert){
                    insertList.add(currentDetailBill);
                }else{
                    updateList.add(currentDetailBill);
                }
                insertList.add(nextBill);
                sendMessageQueue(insertList,updateList);
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
                if(isInsert){
                    insertList.add(currentDetailBill);
                }else{
                    updateList.add(currentDetailBill);
                }
                insertList.add(nextBill);
                sendMessageQueue(insertList,updateList);
                return returnMessage;
            }
        }

        //计算本月账单
        Double currentFee = 0.0;
        //费用项
        try {
//			Calculator calculator = new Calculator(); //由于不用计算需要安装MATLAB的运行环境；且运行环境较大；本业务不需要做较复杂的数值计算;故弃用MATLAB来计算；改用解析js脚本
            List<FeeItemDetail> list = new ArrayList<FeeItemDetail>();
            for(TBsChargeType tbChargType:tBsChargingTypeList){
                //得到公式
                Double CountValue =0.0;
                String feeItem="";
                Double minCriticalpoint = tbChargType.getMinCriticalpoint();
                Double maxCriticalpoint = tbChargType.getMaxCriticalpoint();
                String formulaInfoValue= tbChargType.getFormulaInfoValue(); //一个公式里面只能有一个计费项目
                if(StringUtils.isBlank(formulaInfoValue)){
                    log.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"公式为空,不能计算，请先维护公式!"));
                    continue;
                }
                for(int i=0;i<resultDetailMap.size();i++){

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
//					//置空公式然后得到新的运算式子
//					formulaInfoValue="";
//					for(int j=0;j<formulaArray.length;j++){
//						formulaInfoValue = formulaInfoValue+formulaArray[j];
//					}
//					Object[] obj = calculator.formulaCalculation(1, formulaInfoValue,userCount,peakCount,vallyCount,commonCount);
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
                //这里因为，double计算可能会丢失精度，导致后续四舍五入不准确，一般正常的水电费单价在四位小数左右，对于丢失精度的四舍五入保留前十位即可
                currentFee = BigDecimalUtils.add(currentFee, CountValue);
                currentFee = new BigDecimal(currentFee).setScale(10,BigDecimal.ROUND_HALF_UP).doubleValue();
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
            currentDetailBill.setFeeItemDetail(JSON.toJSONString(list)); //设置各个费用项组成的json字符串
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
                listTempCurrentFee= JSON.parseArray(temporaryBill, TempCurrentFee.class);
                TempCurrentFee tempCurrentFee = new TempCurrentFee();
                tempCurrentFee.setId(CommonUtils.getUUID());
                tempCurrentFee.setCurrentFee(currentDetailBill.getCurrentFee());
                listTempCurrentFee.add(tempCurrentFee);
                currentDetailBill.setTemporaryBill(JSON.toJSONString(listTempCurrentFee)); //记录这次临时计费
            };


            currentDetailBill.setBillingTime(new Date());
            currentTotalBill.setCurrentFee(BigDecimalUtils.add(CommonUtils.null2Double(currentTotalBill.getCurrentFee()), currentDetailBill.getCurrentFee()));	//总金额累计
            currentTotalBill.setTotalFee(BigDecimalUtils.add(CommonUtils.null2Double(currentTotalBill.getTotalFee()), currBillFee));
            currentDetailBill.setTax(CommonUtils.getTax(BigDecimalUtils.sub(currBillFee,CommonUtils.null2Double(currentDetailBill.getLateFee())), scheme.getTaxRate())); //税金计算
            int chargingType = BillingEnum.TYPE_AUTO.getIntV(); //设置计费类型
            currentTotalBill.setChargingType(chargingType);
            //计算该户以前欠费在本月需要计算的违约金    //每天定时任务刷违约金 此处不再计算


            //生成下月的数据
            TBsChargeBillHistory nextBill = createNextBill(ruleationId,nextTotalId,(String)resultDetailMap.get(0).get("buildingFullName"),currentTotalBill.getProjectId(),currentDetailBill);
//			TBsChargeBillHistory nextBill = new TBsChargeBillHistory();
//			nextBill.setBuildingCode(ruleationId);
//			nextBill.setChargeTotalId(nextTotalId);
//			nextBill.setCreateId(currentDetailBill.getCreateId());
//			nextBill.setCreateTime(new Date());
//			nextBill.setFullName((String)resultDetailMap.get(0).get("buildingFullName"));
//			nextBill.setId(CommonUtils.getUUID());
//			nextBill.setLastBillId(currentDetailBill.getId());
//			nextBill.setIsUsed(0);
//			nextBill.setLateFee(0.0);
//			nextBill.setLastBillFee(currentDetailBill.getCurrentBillFee());
//			nextBill.setProjectId(currentTotalBill.getProjectId());

            if(isInsert){
                insertList.add(currentDetailBill);
            }else{
                updateList.add(currentDetailBill);
            }
            insertList.add(nextBill);
            sendMessageQueue(insertList,updateList);
//			if(insertList.size() >= BATCH_ADD_COUNT){
//				BillingSupEntity e = new BillingSupEntity(insertList, null);
//				log.info(getFeeType(meterFlag)+"{}计费: 批量插入元数据 , 数据: {}, 发送至消息队列开始. ",oprStr, e.toString());
//				sendMessage(companyId,e);
//				insertList.clear();
//				log.info(getFeeType(meterFlag)+"{}计费: 批量插入元数据 , 数据: {}, 发送至消息队列完成. ", oprStr, e.toString());
//			}
//			if(updateList.size() >= BATCH_UPDATE_COUNT){
//				BillingSupEntity e = new BillingSupEntity(null, updateList);
//				log.info(getFeeType(meterFlag)+"{}计费: 批量修改元数据 , 数据: {}, 发送至消息队列开始. ",oprStr,e.toString());
//				sendMessage(companyId,e);
//				updateList.clear();
//				log.info(getFeeType(meterFlag)+"计费: 批量修改元数据 , 数据: {}, 发送至消息队列完成. ", oprStr,e.toString());
//			}
        } catch (ECPBusinessException e) {
            log.info(String.format("当前时间 : %s , 计算异常 -> %s" ,CommonUtils.getDateStr(),e.getMessage()));
            return false;
        }
        return returnMessage;
    }

    private void sendMessageQueue(List<TBsChargeBillHistory> insertList, List<TBsChargeBillHistory> updateList) {
        if(insertList.size() >= BATCH_ADD_COUNT){
            tBsChargeBillHistoryMapper.batchInsert(insertList);
        }
        if(updateList.size() >= BATCH_UPDATE_COUNT){
            tBsChargeBillHistoryMapper.batchUpdate(updateList);
        }
    }

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
}
