package com.everwing.autotask.core.service.impl.bill;

import com.alibaba.fastjson.JSON;
import com.everwing.autotask.core.dao.*;
import com.everwing.autotask.core.service.bill.WyAutoBillingService;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.BigDecimalUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.stall.TcStall;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

/**
 * 物业自动计费逻辑
 *
 * @author DELL shiny
 * @create 2018/6/5
 */
@Log4j2
@Service
public class WyAutoBillingServiceImpl implements WyAutoBillingService {

    @Value("${batch_add_max_count}")
    private int BATCH_ADD_COUNT;

    @Value("${batch_update_max_count}")
    private int BATCH_UPDATE_COUNT;

    @Autowired
    private TBsProjectMapper tBsProjectMapper;

    @Autowired
    private TBsChargingSchemeMapper tBsChargingSchemeMapper;

    @Autowired
    private TcBuildingMapper tcBuildingMapper;

    @Autowired
    private TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;

    @Autowired
    private TBsAssetAccountMapper tBsAssetAccountMapper;

    @Autowired
    private TBsOwedHistoryMapper tBsOwedHistoryMapper;

    @Autowired
    private TcStallMapper tcStallMapper;

    @Autowired
    private TBsChargeBillTotalMapper tBsChargeBillTotalMapper;

    @Override
    public void autoBilling(String companyId, Integer type) {
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
        List<TBsProject> projects = tBsProjectMapper.findCanBillingProjects(paramMap);		//获取可计费的项目
        if(projects.isEmpty()){
            log.warn("当前物业公司下,未找到可计费的项目. 计费完成 . companyId : {}",companyId);
            return ;
        }
        for(TBsProject project:projects){
            String feeStr = returnFeeStr(type);
            log.info("{}自动计费: 开始自动计费 .  项目: {}",feeStr,project.toString());

            if(null == project){
                log.warn("{}自动计费: 项目为空, 自动计费完成. 自动计费流程停止. ",feeStr);
                return ;
            }

            //1. 获取该项目下可用的物业管理费计费方案
            TBsChargingScheme scheme = hasCanBillingScheme(project, true,type,feeStr);
            if(null == scheme){
                log.warn("{}自动计费: 当前项目下未找到可供计费的资产 . 自动计费流程停止. 项目: {} ",feeStr, project.toString());
                return ;
            }


            //2. 获取本月的总账单,若未生成,则生成一张新的总账单
            String nextTotalId = CommonUtils.getUUID();
            Map<String,Object> returnMap = hasTotalBillInCurrentCycle(project, scheme);

            TBsChargeBillTotal currentTotalBill = (TBsChargeBillTotal) returnMap.get("entity");
            //根据本次的总账单，检查上一个总账单是否有审核，如果是未审核的状态则不能做本次的计费;加上这个判断限制主要是为了防止重复扣费
            String lastTotalId= currentTotalBill.getLastTotalId();
            if(StringUtils.isNotBlank(lastTotalId)){
                //如果上期总账单编号Id是空；则说明系统第一次计费，故没有上期总账单;
                TBsChargeBillTotal  lastBillTotal = tBsChargeBillTotalMapper.findTbsTotalbyId(lastTotalId);
                if(CommonUtils.isNotEmpty(lastBillTotal)){
                    if(CommonUtils.isNotEmpty(lastBillTotal.getAuditStatus()) && lastBillTotal.getAuditStatus() !=1){
                        log.info("总账单编号为{}的总账单还未审核，不能做计费!,自动计费停止.......",lastTotalId);
                        return ;
                    }
                }
            }
            //3. 获取该项目下所有需要计费的建筑
            List<TcBuilding> buildingList = tcBuildingMapper.findChargeBuildingByProjectCode(project.getProjectId());
            if(CommonUtils.isEmpty(buildingList)){
                log.warn("{}自动计费: 当前项目下未找到可供计费的资产. 自动计费流程停止.  项目:{}",feeStr,project.toString());
                return ;
            }

            List<TBsChargeBillHistory> insertList = new ArrayList<TBsChargeBillHistory>();
            List<TBsChargeBillHistory> updateList = new ArrayList<TBsChargeBillHistory>();

            Date lastBillDate = (Date) returnMap.get("lastBillDate");
            for(TcBuilding building : buildingList){
                billingCommonFee(companyId, building, currentTotalBill, scheme, lastBillDate, nextTotalId, insertList, updateList,"自动",type,feeStr);
            }

            //本期总账单: 本期总费用  + 上期欠费
            currentTotalBill.setTotalFee(currentTotalBill.getLastOwedFee() + currentTotalBill.getCurrentFee());

            tBsChargeBillHistoryMapper.batchInsert(insertList);

            tBsChargeBillHistoryMapper.batchUpdate(updateList);

            //计算完毕之后,生成下个月的总账单 , 账单的上期数据由上方的总账单传入
            TBsChargeBillTotal nextBillTotal = new TBsChargeBillTotal();
            nextBillTotal.setId(nextTotalId);
            nextBillTotal.setAuditStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
            nextBillTotal.setChargingType(BillingEnum.TYPE_AUTO.getIntV());
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
            nextBillTotal.setLastOwedFee(currentTotalBill.getTotalFee());	//下月总账单才生成的时候, 默认上月欠费为上月的总账单

            //获取本期的总金额

            if((boolean) returnMap.get("isInsert")){
                tBsChargeBillTotalMapper.insertChargeBillTotal(currentTotalBill);
            }else{
                tBsChargeBillTotalMapper.update(currentTotalBill);
            }
            tBsChargeBillTotalMapper.insertChargeBillTotal(nextBillTotal);


            tBsProjectMapper.update(assemTBsProject(project, currentTotalBill, type));

            log.info("{}自动计费: 自动计费完成. 项目: {}",feeStr,project.toString());
        }
    }

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

    private String returnFeeStr(Integer type){
        return (type == 1) ? Constants.BILLING_WY : Constants.BILLING_BT;
    }

    private TBsChargingScheme hasCanBillingScheme(TBsProject entity , boolean isAuto, Integer type , String feeStr){
        TBsChargingScheme paramScheme = new TBsChargingScheme();
        if(type == 1){
            paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_WY.getIntV());	//物业管理费用scheme
        }else if(type == 2){
            paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_BT.getIntV());    //本体基金用scheme
        }
        paramScheme.setProjectId(entity.getProjectId());	//project code
        TBsChargingScheme scheme = tBsChargingSchemeMapper.findUsingScheme(paramScheme);

        String str = "自动";
        if(CommonUtils.isEmpty(scheme)){
            log.warn("{}{}计费:  该项目未找到可用的物业管理费计费方案,无法{}计费. 项目: {}",feeStr,str,str,entity.toString());
            return null;

        }else{
            if(isAuto){	//自动
                if(BillingEnum.TYPE_AUTO.getIntV() == scheme.getChargingType()){
                    return scheme;
                }else{
                    log.warn("{}自动计费: 该项目可用的物业管理费计费方案为手动计费,不参与自动计费, 自动计费流程停止 . 项目: {}",feeStr,entity.toString());
                    return null;
                }
            }else{//手动
                if(BillingEnum.TYPE_MANUAL.getIntV() == scheme.getChargingType()){
                    return scheme;
                }else{
                    log.warn("{}手动计费: 该项目可用的物业管理费计费方案为自动计费,不参与手动计费, 手动计费流程停止. 项目: {}",feeStr,entity.toString());
                    return null;
                }
            }
        }
    }
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
            log.info("物业管理费计费: 当前未找到本月的计费总账单,需要重新生成. 项目: {} ",project.toString());
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
            String feeStr
    )
    {

        //查找判断本月的该建筑的数据是否已经生成,若未生成,则生成本月的

        TBsChargeBillHistory paramBill = new TBsChargeBillHistory();
        paramBill.setProjectId(currentTotalBill.getProjectId());
        paramBill.setBuildingCode(building.getBuildingCode());
        paramBill.setChargeTotalId(currentTotalBill.getId());

        //查询是否有收费记录(两种收费方式)
        List<TBsChargeBillHistory> histories = tBsChargeBillHistoryMapper.findCurrentDetailBill(paramBill);

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
            TBsAssetAccount account = tBsAssetAccountMapper.lookupByBuildCodeAndType(building.getBuildingCode(), type);
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
        Double currentFee = bill(building, lastBillDate, currentDetailBill, scheme,type,isWhole);

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
            List<Map<String,Object>> ohs = tBsOwedHistoryMapper.findSomeInfoByAccountId(account.getId());
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

        if(insertList.size() >= BATCH_ADD_COUNT){
            tBsChargeBillHistoryMapper.batchInsert(insertList);
            insertList.clear();
            log.info("{}{}计费: 批量插入元数据 , 数据500: {}, 处理完成. ", feeStr , oprStr, JSON.toJSONString(insertList));
        }
        if(updateList.size() >= BATCH_UPDATE_COUNT){
            tBsChargeBillHistoryMapper.batchUpdate(updateList);
            updateList.clear();
            log.info("{}{}计费: 批量修改元数据 , 数据500: {}, 发送至消息队列完成. ", feeStr , oprStr,JSON.toJSONString(updateList));
        }
        return true;
    }

    private Double bill(TcBuilding building,Date lastBillDate , TBsChargeBillHistory currentDetailBill,TBsChargingScheme scheme,Integer type,int isWhole){

        double currentFee = 0.0;

        if(LookupItemEnum.buildingType_parkspace.getStringValue().equals(building.getBuildingType())){
            //车位
            TcStall stall=tcStallMapper.findByBuildingCode(building.getBuildingCode());
            //车位分为三种车位，只有私家车位收取物业管理费，并且没有本体基金
            if(type == 1 && "003".equals(stall.getStallType())) {
                currentFee=stall.getAdministrativeExpenese();//单位元/月
            }
        }else{
            //房屋 / 商铺 / 公建共用
            double price = (type == 1 ? CommonUtils.null2Double(building.getUnitWyPrice()) : CommonUtils.null2Double(building.getUnitBtPrice()));

            double area = CommonUtils.null2Double((0 ==  scheme.getChargingArea())? building.getBuildingArea():building.getUsableArea());

            if(CommonUtils.isEmpty(price) || CommonUtils.isEmpty(area)){
                currentFee = 0.0;
            }else{
                //现在还没有 入伙时间先给定
                currentFee = calculationOfWYAmount(area,price,null,isWhole);
            }
        }
        return currentFee;
    }

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
}
