//package com.everwing.autotask.core.service.impl.bill;
//
//import com.everwing.autotask.core.dao.*;
//import com.everwing.autotask.core.datasource.DBContextHolder;
//import com.everwing.autotask.core.service.bill.WaterElectShareService;
//import com.everwing.coreservice.common.ThreadPool.ThreadPoolUtils;
//import com.everwing.coreservice.common.utils.BigDecimalUtils;
//import com.everwing.coreservice.common.utils.CommonUtils;
//import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
//import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
//import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
//import com.everwing.coreservice.common.wy.entity.configuration.project.*;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineManager;
//import javax.script.ScriptException;
//import java.util.*;
//
//import static jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType.DIGIT;
//
///**
// * @author DELL shiny
// * @create 2018/6/4
// */
//@Log4j2
//@Service
//public class WaterElectShareServiceImpl implements WaterElectShareService {
//
//    private static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
//
//    @Autowired
//    private TBsProjectMapper tBsProjectMapper;
//
//    @Autowired
//    private TBsChargingSchemeMapper tBsChargingSchemeMapper;
//
//    @Autowired
//    private TBsShareBasicInfoMapper tBsShareBasicInfoMapper;
//
//    @Autowired
//    private TBsChargeBillTotalMapper tBsChargeBillTotalMapper;
//
//    @Autowired
//    private TBsShareRelatedTaskMapper tBsShareRelatedTaskMapper;
//
//    @Autowired
//    private TcMeterDataMapper tcMeterDataMapper;
//
//    @Autowired
//    private TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;
//
//    @Override
//    public void doWaterElectShareBillingByCompany(String companyId, int meterType) {
//        Map<String,Object> paramMap = new HashMap<String,Object>();
////		paramMap.put("status", BillingEnum.STATUS_START.getIntV());						//项目启用状态
//        if(BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType){
//            paramMap.put("electStatus", BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV());		//电费种待计费状态
//            paramMap.put("schemeType", BillingEnum.SCHEME_TYPE_ELECT.getIntV());//计费方案可用状态
//        }
//        if(BillingEnum.SHARE_WATER_TYPE.getIntV() == meterType){
//            paramMap.put("waterStatus", BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV()); //电费种待计费状态
//            paramMap.put("schemeType", BillingEnum.SCHEME_TYPE_WATER.getIntV());//计费方案可用状态
//        }
////		paramMap.put("chargeType", BillingEnum.TYPE_AUTO.getIntV());						//计费方案类型
//        paramMap.put("isUsed", BillingEnum.IS_USED_USING.getIntV());
//        List<TBsProject> projects = tBsProjectMapper.findShareBillingProjects(paramMap);		//获取可计费的项目
//        if(projects.isEmpty()){
//            log.warn("当前物业公司下,未找到可计费的项目. 计费完成 . companyId : {}",companyId);
//        }
//
//        for (TBsProject tBsProject : projects) {
//            if(BillingEnum.SHARE_WATER_TYPE.getIntV() == meterType) {
//                log.info("*************开始对项目："+tBsProject.getProjectName()+"进行水费的分摊计费");
//                shareBilling(companyId,tBsProject,BillingEnum.SHARE_WATER_TYPE.getIntV());
//            }else if(BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType) {
//                log.info("*************开始对项目："+tBsProject.getProjectName()+"进行电费的分摊计费");
//                shareBilling(companyId,tBsProject,BillingEnum.SHARE_ELECT_TYPE.getIntV());
//            }
//        }
//        log.info("各个项目正在单独线程中执行自由的分摊任务，请稍后查看详情");
//    }
//
//    private void shareBilling(final String companyId, final TBsProject entity, final Integer meterType) {
//        String meterName=BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType ? "电费" : "水费";
//        log.info( "开始进行{}的分摊计费",meterName);
//        //项目信息
//        if(CommonUtils.isEmpty(entity.getId())){
//            log.warn("项目id为空");
//            return ;
//        }
//
//        //水电费的分摊在一起，所以类型一定要有
//        if(!( 0== meterType || 1 == meterType)){
//            log.warn("表类型错误！");
//            return ;
//        }
//        ThreadPoolUtils.getInstance().executeThread(new Runnable() {
//            @Override
//            public void run() {
//                DBContextHolder.setDBType(companyId);//切换数据源
//
//                String meterName=BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType ? "电费" : " 水费";
//                log.info("开始进行本次{}分摊费用",entity.getProjectName());
//                //1. 根据projectCode判断当前项目是否开启物业计费.只有计费开启了才存在分摊
//                TBsProject paramObj = new TBsProject();
//                paramObj.setId(entity.getId());
////				paramObj.setWaterStatus(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV());//水费计费后才可进行水费分摊
//                paramObj.setStatus(BillingEnum.STATUS_START.getIntV());		//项目启用
//
//                TBsProject tBsProject = tBsProjectMapper.findByObj(paramObj);
//                if(CommonUtils.isEmpty(tBsProject)){
//                    log.warn("该项目未开启计费,{}计费未启动或尚未计费,无法进行{}分摊.",entity.getProjectName(),meterName);
//                    return;
//                }
//
//                //2. 获取该项目下, 正在启用的scheme
//                TBsChargingScheme paramScheme = new TBsChargingScheme();
//                if(BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType){
//                    //电费
//                    paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_ELECT.getIntV());	//电费管理scheme
//                }else{
//                    //水费
//                    paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_WATER.getIntV());	//水费管理scheme
//                }
//
//                paramScheme.setProjectId(tBsProject.getProjectId());	//project code
//                TBsChargingScheme scheme = tBsChargingSchemeMapper.findUsingScheme(paramScheme);
//                if(CommonUtils.isEmpty(scheme)){
//                    log.warn("{}该项目未找到可用的{}计费方案,无法进行分摊",entity.getProjectName(),meterName);
//                    return;
//                }
//
//                //3. 获取该项目下, 正在启用且本月需要分摊的分摊信息（有可能会启用但本月不需要进行分摊）
//                List<String> shareinfosF=new ArrayList<>();
//                List<TBsShareBasicsInfo> shareInfos;
//                if(BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType ){
//                    shareInfos=tBsShareBasicInfoMapper.getUsedShareInfo(entity.getProjectId(),String.valueOf(BillingEnum.SHARE_TYPE_ELECT.getIntV()));
//                }else{
//                    shareInfos=tBsShareBasicInfoMapper.getUsedShareInfo(entity.getProjectId(),String.valueOf(BillingEnum.SHARE_TYPE_WATER.getIntV()));
//                }
//                if(CommonUtils.isEmpty(shareInfos)){
//                    log.warn( "{}该项目目前没有可用的{}分摊,不用进行分摊",entity.getProjectName(),meterName);
//                    return;
//                }
//
//                //查询本期总账单信息(分摊费用是要写入到账单元数据信息中，所以一定要计费了才又分摊)
//                TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
//                paramTotal.setProjectId(tBsProject.getProjectId());
//                paramTotal.setType(scheme.getSchemeType());
//                List<TBsChargeBillTotal> totals = tBsChargeBillTotalMapper.findCurrentBillTotalForShare(paramTotal);
//                if ( CommonUtils.isEmpty(totals) ){
//                    log.warn("{}本项目本周期尚未进行{}计费，还无法分摊",entity.getProjectName(),meterName);
//                    return;
//                }else{
//                    paramTotal=totals.get(0);//汇总数据只会有一条数据
//                }
//
//                log.info("项目开启了计费 && 有可用的计费方案 && 本周期已计费     可以进行本次的"+meterName+"分摊计费 ");
//
//                //开始分开计费。区分水电费
//                if(BillingEnum.SHARE_ELECT_TYPE.getIntV() == meterType){
//                    electShareBilling(entity,shareInfos,scheme,paramTotal,companyId);
//                }else{
//                    waterShareBilling(entity,shareInfos,scheme,paramTotal,companyId);
//                }
//
//
//            }
//        });
//        log.info("异步手动计费开始,请稍后查看详情.");
//        return ;
//    }
//    private void waterShareBilling(
//            TBsProject entity,List<TBsShareBasicsInfo> shareInfos,
//            TBsChargingScheme scheme,
//            TBsChargeBillTotal total,
//            String companyId){
//        log.info("{}开始本周期水费分摊的计算",entity.getProjectName());
//        //存在分摊方案信息  shareInfos  校验分摊任务，关联建筑信息（）
//        /**
//         *  分摊方案要么自动要么手动，存在多条
//         */
//        List<TBsShareRelatedTask> tasks=new ArrayList<>();
//        //账单元数据的集合信息，用于计算出水分分摊详情后的修改
//        List<TBsShareTaskBuilding> shareTaskBuilding=new ArrayList<>();
//        tasks= tBsShareRelatedTaskMapper.getShareTaskByIds(shareInfos);
//        if(CommonUtils.isEmpty(tasks)){
//            log.info("{}没有查询到可用的分摊任务信息",entity.getProjectName());
//            return ;
//        }
//
//        for (TBsShareBasicsInfo tBsShareBasicsInfo : shareInfos) {
//            //分摊方案下关联有分摊任务,查询分摊方案下面的分摊任务
//            for (TBsShareRelatedTask tBsShareRelatedTask : tasks) {
//                if(tBsShareRelatedTask.getShareBasicsId().equals(tBsShareBasicsInfo.getId())){
//                    shareTaskBuilding.add(getTbsShareTaskInfo(tBsShareBasicsInfo.getStartMode(),tBsShareRelatedTask,tBsShareBasicsInfo.getShareFrequency()));
//                }
//            }
//        }
//
//
//        if(shareTaskBuilding.size()==0){
//            log.info("{}没有查询到可用的分摊任务信息",entity.getProjectName());
//            return ;
//        }
//
//        //根据任务集合得到每个分摊任务的分摊金额，以及分摊的关联建筑信息
//        for (TBsShareTaskBuilding tBsShareTaskBuilding : shareTaskBuilding) {
//            if(BillingEnum.manaul_billing.getIntV() == tBsShareTaskBuilding.getStartMode()){
//                //自动  需要通过公式获取
//                if(CommonUtils.isEmpty(tBsShareTaskBuilding.getShareAmountFormula())){
//                    log.info("{}没有查询到可用的分摊公式信息",tBsShareTaskBuilding.getTaskId());
//                    continue;
//                }
//                String shareForFormula=tBsShareTaskBuilding.getShareAmountFormula();
//                //根据公式计算分摊的量
//                Double usedAmount= getTotalUsedAmountByCode(shareForFormula,String.valueOf(BillingEnum.SHARE_WATER_TYPE.getIntV()),tBsShareTaskBuilding.getShareFrequency());//
//                if(null == usedAmount ||  usedAmount <= 0){
//                    log.info("{}没有查询到可用的分摊公式信息",tBsShareTaskBuilding.getTaskId());
//                    continue;
//                }
//                //得到每个分摊任务的分摊量
//                tBsShareTaskBuilding.setShareAmount(usedAmount);
//            }else{
//                //手动   直接获取金额，获取任务的关联收费对象
//                if(CommonUtils.isEmpty(tBsShareTaskBuilding.getShareMoney())){
//                    log.info("{}没有查询到可用的分摊金额信息",tBsShareTaskBuilding.getTaskId());
//                    continue;
//                }
//
//            }
//            //获取分摊的关联建筑信息--区分按户和按用量
//            //分按户和按用量两种
//            if(BillingEnum.SHARE_BY_HOUSEHOLD.getIntV() == tBsShareTaskBuilding.getShareType()){
//                //按户--只用获取需要进行分摊的用户数量和关联建筑信息
//                List<TBsShareBuildingRelation> resultList= tBsShareRelatedTaskMapper.getRightBuilingInfos(tBsShareTaskBuilding.getTaskId());
//                if(CommonUtils.isEmpty(resultList)) continue;
//                tBsShareTaskBuilding.settBsRuleBuildingRelation(resultList);
//            }else{
//
//                //按用量--需要先进行用量的计算再进行分摊计算。同时获取关联建筑信息
//                //如果是第一次   需要拿一个分摊周期的数据，如果还没有这么多用初始化数据
//                Double totalUseAmount=tBsShareRelatedTaskMapper.getTotalUseAmountByTaskId(tBsShareTaskBuilding.getTaskId(),String.valueOf(BillingEnum.SHARE_WATER_TYPE.getIntV()),String.valueOf(tBsShareTaskBuilding.getShareFrequency()));
//                if(CommonUtils.isEmpty(totalUseAmount)) continue;
//                tBsShareTaskBuilding.setTotalUseAmount(totalUseAmount);
//                List<TBsShareBuildingRelation> resultListAuto= tBsShareRelatedTaskMapper.getUseAmountByBuilding(tBsShareTaskBuilding.getTaskId(),String.valueOf(tBsShareTaskBuilding.getShareFrequency()));
//                tBsShareTaskBuilding.settBsRuleBuildingRelation(resultListAuto);
//            }
//        }
//
//        //所有任务需要的数据已经拿到，剩下计算出来，然后得出需要修改的集合，丢入消息队列
//        log.info("{}本期水费分摊计费结束",entity.getProjectName());
//        if(doCalculationShare(shareTaskBuilding,total.getId(),companyId)) {
//            log.info("{}本次水费分摊计费成功",entity.getProjectName());
//            return ;
//        }else {
//            log.info("{}本次水费分摊计费失败",entity.getProjectName());
//            return ;
//        }
//    }
//
//    private void electShareBilling(TBsProject entity,List<TBsShareBasicsInfo> shareInfos, TBsChargingScheme scheme,
//            TBsChargeBillTotal total,
//            String companyId){
//
//        log.info("开始此{}项目本周期电费分摊的计算",entity.getProjectName());
//        //存在分摊方案信息  shareInfos  校验分摊任务，关联建筑信息（）
//        /**
//         *  分摊方案要么自动要么手动，存在多条
//         */
//        List<TBsShareRelatedTask> tasks=new ArrayList<>();
//        //账单元数据的集合信息，用于计算出水分分摊详情后的修改
//        List<TBsShareTaskBuilding> shareTaskBuilding=new ArrayList<>();
//        tasks= tBsShareRelatedTaskMapper.getShareTaskByIds(shareInfos);
//
//        if(CommonUtils.isEmpty(tasks)){
//            log.info("{}没有查询到可用的分摊任务信息",entity.getProjectName());
//            return ;
//        }
//
//        for (TBsShareBasicsInfo tBsShareBasicsInfo : shareInfos) {
//            //分摊方案下关联有分摊任务,查询分摊方案下面的分摊任务
//            for (TBsShareRelatedTask tBsShareRelatedTask : tasks) {
//                if(tBsShareRelatedTask.getShareBasicsId().equals(tBsShareBasicsInfo.getId())){
//                    shareTaskBuilding.add(getTbsShareTaskInfo(tBsShareBasicsInfo.getStartMode(),tBsShareRelatedTask,tBsShareBasicsInfo.getShareFrequency()));
//                }
//            }
//        }
//
//
//        if(shareTaskBuilding.size()==0){
//            log.info("{}没有查询到可用的分摊任务信息",entity.getProjectName());
//            return ;
//        }
//
//        //根据任务集合得到每个分摊任务的分摊金额，以及分摊的关联建筑信息
//        for (TBsShareTaskBuilding tBsShareTaskBuilding : shareTaskBuilding) {
//            if(BillingEnum.manaul_billing.getIntV() == tBsShareTaskBuilding.getStartMode()){
//                //自动  需要通过公式获取
//                if(CommonUtils.isEmpty(tBsShareTaskBuilding.getShareAmountFormula())){
//                    log.info("{}没有查询到可用的分摊公式信息",tBsShareTaskBuilding.getTaskId());
//                    continue;
//                }
//                String shareForFormula=tBsShareTaskBuilding.getShareAmountFormula();
//                //根据公式计算分摊的量
//                Double usedAmount= getTotalUsedAmountByCode(shareForFormula,String.valueOf(BillingEnum.SHARE_ELECT_TYPE.getIntV()),tBsShareTaskBuilding.getShareFrequency());
//
//                if(null == usedAmount ||  usedAmount <= 0){
//                    log.info("{}分摊金额小于0，请检查公式是否准确.",tBsShareTaskBuilding.getTaskId());
//                    continue;
//                }
//                //得到每个分摊任务的分摊量
//                tBsShareTaskBuilding.setShareAmount(usedAmount);
//            }else{
//                //手动   直接获取金额，获取任务的关联收费对象
//                if(CommonUtils.isEmpty(tBsShareTaskBuilding.getShareMoney())){
//                    log.info("{}没有查询到可用的分摊金额信息",tBsShareTaskBuilding.getTaskId());
//                    continue;
//                }
//
//            }
//
//            //获取分摊的关联建筑信息--区分按户和按用量
//            //分按户和按用量两种
//            if(BillingEnum.SHARE_BY_HOUSEHOLD.getIntV() == tBsShareTaskBuilding.getShareType()){
//                //按户--只用获取需要进行分摊的用户数量和关联建筑信息
//                List<TBsShareBuildingRelation> resultList= tBsShareRelatedTaskMapper.getRightBuilingInfos(tBsShareTaskBuilding.getTaskId());
//                if(CommonUtils.isEmpty(resultList)) continue;
//                tBsShareTaskBuilding.settBsRuleBuildingRelation(resultList);
//            }else{
//                //按用量--需要先进行用量的计算再进行分摊计算。同时获取关联建筑信息
//                //如果是第一次   需要拿一个分摊周期的数据，如果还没有这么多用初始化数据
//                Double totalUseAmount=tBsShareRelatedTaskMapper.getElectTotalUseAmountByTaskId(tBsShareTaskBuilding.getTaskId(),String.valueOf(BillingEnum.SHARE_ELECT_TYPE.getIntV()),String.valueOf(tBsShareTaskBuilding.getShareFrequency()));
//                if(CommonUtils.isEmpty(totalUseAmount)) continue;
//                tBsShareTaskBuilding.setTotalUseAmount(totalUseAmount);
//                List<TBsShareBuildingRelation> resultListAuto= tBsShareRelatedTaskMapper.getUseAmountByBuildingForElect(tBsShareTaskBuilding.getTaskId(),String.valueOf(tBsShareTaskBuilding.getShareFrequency()));
//                tBsShareTaskBuilding.settBsRuleBuildingRelation(resultListAuto);
//            }
//        }
//
//        //所有任务需要的数据已经拿到，剩下计算出来，然后得出需要修改的集合，丢入消息队列
//        log.info( "{}本期水费分摊计费结束",entity.getProjectName());
//        if(doCalculationShare(shareTaskBuilding,total.getId(),companyId)) {
//            log.info("{}本次水费分摊计费成功",entity.getProjectName());
//            return ;
//        }else {
//            log.info("{}本次水费分摊计费失败",entity.getProjectName());
//            return ;
//        }
//    }
//
//    private boolean doCalculationShare(List<TBsShareTaskBuilding> shareTaskBuilding,String chargeTotalId,String companyId){
//        log.info("======开始进行分摊计费====");
//        //一个资产可以关联进多个
//        //最终会使用的字段是房屋code，分摊金额，总账单id
//        Map<String, Double> resultMap=new HashMap<>();//存放所有的用户和各自分摊金额信息
//        for (TBsShareTaskBuilding tBsShareTaskBuilding : shareTaskBuilding) {
//            if(CommonUtils.isEmpty(tBsShareTaskBuilding.gettBsRuleBuildingRelation())){
//                log.info("{}下未发现可用建筑信息，无法分摊",tBsShareTaskBuilding.getTaskId());
//                continue;
//            }
//            if(BillingEnum.SHARE_BY_HOUSEHOLD.getIntV() == tBsShareTaskBuilding.getShareType()){
//                //按户--只用获取需要进行分摊的用户数量和关联建筑信息
//                int shareNumber=tBsShareTaskBuilding.gettBsRuleBuildingRelation().size();//需要进行分摊的用户数
//                Double shareMoney=tBsShareTaskBuilding.getShareMoney();//分摊总金额
//                Double shareMoneyEveryone= BigDecimalUtils.div(shareMoney, shareNumber, 2);//保留两位小数
//                for (TBsShareBuildingRelation relation : tBsShareTaskBuilding.gettBsRuleBuildingRelation()) {
//                    //每家分摊shareMoneyEveryone
//                    putShareMoneyToHouse(resultMap,relation.getRelationBuildingCode(),shareMoneyEveryone);
//                }
//            }else{
//                Double totalUseAmount=tBsShareTaskBuilding.getTotalUseAmount();//总用量
//                Double shareAmount=tBsShareTaskBuilding.getShareAmount();//需要分摊的总用量
//                Double price=tBsShareTaskBuilding.getSharePrice();//分摊的单价
//                if(CommonUtils.isEmpty(price)) {
//                    continue;//一定要有单价
//                }
//                Double shareMoney=BigDecimalUtils.mul(shareAmount, price);//分摊总金额
//                for (TBsShareBuildingRelation relation : tBsShareTaskBuilding.gettBsRuleBuildingRelation()) {
//                    //每家分摊shareMoneyEveryone
//                    Double useAmont=relation.getUseAmount();//需要分摊的每户的使用量
//                    Double shareMoneyEveryone=BigDecimalUtils.div(BigDecimalUtils.mul(shareMoney, useAmont),totalUseAmount, DIGIT);
//                    putShareMoneyToHouse(resultMap,relation.getRelationBuildingCode(),shareMoneyEveryone);
//                }
//            }
//        }
//
//        log.info("========计算出每户需要分摊的金额=========");
//        List<TBsChargeBillHistory> updateList=new ArrayList<>();
//        getUpdateListFormMap(resultMap,updateList,chargeTotalId,companyId);
//
//        //将要处理的信息放入消息队列之后，需要修改分摊基础信息的上次分摊时间
//        List<String> ids=new ArrayList<>();
//        for (TBsShareTaskBuilding shareB : shareTaskBuilding) {
//            if(CommonUtils.isNotEmpty(shareB.getShareBasicsId())) {
//                ids.add(shareB.getShareBasicsId());
//            }
//        }
//        if(ids.size()>0) {
//            tBsShareBasicInfoMapper.updateShareBasicInfo(ids);
//        }
//        log.info("======分摊计费结束，计费成功====");
//        return true;
//    }
//
//    private void getUpdateListFormMap(
//            Map<String, Double> resultMap,
//            List<TBsChargeBillHistory> updateList,
//            String chargeTotalId,
//            String companyId){
//
//        Iterator it=resultMap.entrySet().iterator();
//        while(it.hasNext()){
//            TBsChargeBillHistory cb=new TBsChargeBillHistory();
//            Map.Entry<String, Double> entry=(Map.Entry<String, Double>) it.next();
//            cb.setShareFee(entry.getValue());
//            cb.setBuildingCode(entry.getKey());
//            cb.setChargeTotalId(chargeTotalId);
//            updateList.add(cb);
//
//
//            //500条数据丢一次消息队列
//            if(updateList.size()>500){
//
//                log.info("总账单：{}的分摊计费进行批量修改",chargeTotalId);
//                for(TBsChargeBillHistory history : updateList){
//                    tBsChargeBillHistoryMapper.updateChargeHistoryForShare(history);
//                }
//            }
//        }
//
//        if(updateList.size()>0) {
//            log.info("总账单：{}的分摊计费进行批量修改",chargeTotalId);
//            for(TBsChargeBillHistory history : updateList){
//                tBsChargeBillHistoryMapper.updateChargeHistoryForShare(history);
//            }
//        }
//
//
//    }
//
//    private void putShareMoneyToHouse(Map<String, Double> resultMap,String relatinoBuilding,Double shareMoney){
//        //
//        if(resultMap.containsKey(relatinoBuilding)){
//            //此建筑存在于多个分摊
//            Double oldMoney= resultMap.get(relatinoBuilding);
//            Double newMoney=BigDecimalUtils.add(oldMoney,shareMoney);
//            resultMap.put(relatinoBuilding, newMoney);
//        }else{
//            resultMap.put(relatinoBuilding, shareMoney);
//        }
//    }
//
//    private TBsShareTaskBuilding getTbsShareTaskInfo(int startMode,TBsShareRelatedTask task,int shareFrequency){
//        TBsShareTaskBuilding tst=new TBsShareTaskBuilding();
//        tst.setStartMode(startMode);
//        tst.setShareAmount(task.getShareAmount());
//        tst.setShareAmountFormula(task.getShareAmountFormula());
//        tst.setShareBasicsId(task.getShareBasicsId());
//        tst.setShareType(task.getShareType());
//        tst.setTaskId(task.getId());
//        tst.setShareFrequency(shareFrequency);
//        tst.setShareMoney(task.getShareMoney());
//        tst.setSharePrice(task.getSharePrice());
//        return tst;
//    }
//
//    private Double getTotalUsedAmountByCode(String shareForFormula,String meterType,int shareFrequency){
//        log.info("=====通过公式得出需要分摊的用量=====");
//        //取出表编号，查出每个表的量，替换掉公式中的表编号进行计算(已#分割)
//        //example   #code1#+/-*/#code2#*#code3#....
//        String [] codes=shareForFormula.split("#");//去单数就可以得到水表号
//        for(int i=0;i<=codes.length / 2-1;i++){
//            String meterCode=codes[i*2+1];
//            //查询出此水表的读数
//            Double reading= tcMeterDataMapper.getLastMeterReadingByCode(meterCode,shareFrequency+"",meterType);
//            if(CommonUtils.isEmpty(reading)){
//                reading=0.0;
//            }
//            shareForFormula=shareForFormula.replace("#"+meterCode+"#",""+reading);
//        }
//        //循环替换后进行计算
//        try {
//            Double shareAmount= (Double) jse.eval(shareForFormula);
//            return shareAmount;
//        } catch (ScriptException e) {
//            e.printStackTrace();
//            log.info("计算分摊量信息失败");
//        }
//        return null;
//    }
//
//
//}
