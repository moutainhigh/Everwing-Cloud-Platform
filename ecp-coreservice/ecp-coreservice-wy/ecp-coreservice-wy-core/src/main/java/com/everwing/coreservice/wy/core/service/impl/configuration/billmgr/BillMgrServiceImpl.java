package com.everwing.coreservice.wy.core.service.impl.configuration.billmgr;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.AnnexEnum;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.billmgr.AllBill;
import com.everwing.coreservice.common.wy.entity.configuration.billmgr.Bill;
import com.everwing.coreservice.common.wy.entity.configuration.project.*;
import com.everwing.coreservice.common.wy.entity.cust.TBcCollection;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.service.configuration.billmgr.BillMgrService;
import com.everwing.coreservice.common.wy.utils.SysConfig;
import com.everwing.coreservice.platform.api.IdGenApi;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import com.everwing.coreservice.wy.core.utils.FtlUtils;
import com.everwing.coreservice.wy.dao.mapper.business.electmeter.TcElectMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillTotalMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.owed.TBsOwedHistoryMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargeTypeMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargingRulesMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsRuleBuildingRelationMapper;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service("billMgrService")
public class BillMgrServiceImpl extends Resources implements BillMgrService {

    private static final Logger logger = LogManager.getLogger(BillMgrServiceImpl.class);
    @Autowired
    TcElectMeterMapper tcElectMeterMapper;
    @Autowired
    TcMeterDataMapper tcMeterDataMapper;
    @Autowired
    TBsChargeBillTotalMapper tBsChargeBillTotalMapper;

    @Autowired
    private TBsChargeTypeMapper tBsChargeTypeMapper;

    @Autowired
    private TBsRuleBuildingRelationMapper tBsRuleBuildingRelationMapper;

    @Autowired
    private TBsChargingRulesMapper tBsChargingRulesMapper;

    @Autowired
    private TBsOwedHistoryMapper tBsOwedHistoryMapper;

    /**
     * 投递按项目生成账单
     */
    @Value("${queue.task2Wy.bill.gen.key}")
    private String route_key_gen_bill;

    @Value("${queue.task2Wy.bill.file.upload.key}")
    private String route_key_bill_upload_key;

    @Autowired
    private SysConfig sysConfig;

    @Autowired
    private IdGenApi idGenApi;

    @SuppressWarnings("rawtypes")
    @Override
    public BaseDto genBill(String companyId, TBsProject project) {
        if (CommonUtils.isEmpty(project)) {
            logger.error("账单生成:  传入参数为空 , 无法生成账单. ");
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "传入参数为空,无法生成账单"));
        }

        TBsProject tBsProject = this.tBsProjectMapper.findById(project.getId());
        if (tBsProject.getIsGenBill() == BillingEnum.bill_is_gen_yes.getIntV()) {
            logger.warn("账单生成:  当前项目的账单已经生成,无法继续生成. ");
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "当前项目的账单已经生成,无法继续生成. "));
        }
        String configPath = sysConfig.getBillFtlPath() + File.separator + sysConfig.getBillFtlFileName();
        try {
            File file = ResourceUtils.getFile("classpath:" + configPath);
        } catch (FileNotFoundException e) {
            logger.error("账单生成: 未找到可用的jasper模板, 账单生成失败. ");
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "未找到可用的freemark模板, 账单生成失败."));
        }

        //开始投递到消息队列
        logger.info("账单生成: 开始组装数据,进行消息队列投递. ");
        MqEntity entity = new MqEntity();
        entity.setOpr(BillingEnum.mq_gen_bill_manaul_first.getIntV());
        entity.setCompanyId(companyId);
        entity.setData(tBsProject);
        this.amqpTemplate.convertAndSend(route_key_gen_bill, entity);
        logger.info("账单生成: 数据组装完毕, 投递至消息队列完成, route_key:{} , 数据:{}", route_key_gen_bill, entity.toString());

        //投递至 genBillFirstByManaul

        return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS, "账单开始异步生成,请稍后查看. "));
    }

    /**********************************************************   生成账单       **********************************************************/

    /**
     * @TODO 第一次手动生成账单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void genBillFirstByManaul(String companyId, TBsProject project) {

        if (project.getIsGenBill() == BillingEnum.bill_is_gen_yes.getIntV()) {
            logger.warn("账单生成:  当前项目的账单已经生成,无法继续生成. ");
            return;
        }

        List<TcBuildingList> buildings = getDongList(project.getProjectId());
        if (CommonUtils.isEmpty(buildings)) {
            logger.warn("账单生成: 当前项目下未找到含有栋座的建筑, 生成失败. ");
            return;
        }

        //生成账单
        genBillByBuildings(companyId, buildings, project, BillingEnum.TYPE_MANUAL.getIntV());
    }

    /**
     * @param buildings
     * @TODO 按栋座生成账单
     */
    private void genBillByBuildings(String companyId, List<TcBuildingList> buildings, TBsProject project, int flag) {

        String title = (flag == BillingEnum.mq_gen_bill_manaul_first.getIntV()) ? "账单手动生成" :
                (flag == BillingEnum.mq_gen_bill_auto.getIntV()) ? "账单自动生成" : "账单重新生成";
        TBsChargingScheme paramscheme = new TBsChargingScheme();
        paramscheme.setProjectId(project.getProjectId());
        paramscheme.setSchemeType(BillingEnum.SCHEME_TYPE_WY.getIntV());
        TBsChargingScheme scheme = tBsChargingSchemeMapper.findUsingScheme(paramscheme);
        boolean wyIsTaonei = (null != scheme && scheme.getChargingArea() == 1) ? true : false;

        //获取当前项目下,本次所有的scheme的税率
        Map<String, Double> rateMap = initRateMap(project.getProjectId());

        List<Map<String, Object>> feeList = new ArrayList<Map<String, Object>>();
        for (TcBuilding dong : buildings) {
            //获取该栋座下面 本周期内生成账单的建筑集合
            List<TcBuilding> chargeBuildings = this.tcBuildingMapper.findHasBillsBuildings(project.getProjectId(), project.getBillingTime(), dong.getBuildingCode());
            if (CommonUtils.isEmpty(chargeBuildings)) {
                logger.warn("{}: 当前栋座下不含已生成账单建筑. 数据:{}", title, dong.toString());
                continue;
            }
            solveChargeBuildings(chargeBuildings,title,wyIsTaonei,rateMap,feeList,companyId,dong,project,flag);
        }

        //获取未打包的不符合结构的计费建筑
        List<TcBuilding> allBillBuildings=tcBuildingMapper.findAllUnzipedBuildings(project.getProjectId(),project.getBillingTime());
        solveChargeBuildings(allBillBuildings,title,wyIsTaonei,rateMap,feeList,companyId,null,project,flag);
    }

    private void solveChargeBuildings(List<TcBuilding> chargeBuildings,String title,boolean wyIsTaonei,Map<String, Double> rateMap,List<Map<String, Object>> feeList,String companyId,TcBuilding dong,TBsProject project,int flag){
        if(CommonUtils.isEmpty(chargeBuildings)){
            logger.warn("无需生成账单数据!{}",chargeBuildings);
        }
        Map<String, Object> map = null;
        List<String> chargeBuildingCodes=new ArrayList<>(chargeBuildings.size());
        for (TcBuilding building : chargeBuildings) {
            //分别找出各个建筑的物业管理费,本体基金以及水电费
            chargeBuildingCodes.add(building.getBuildingCode());
            List<TBsChargeBillHistory> histories = this.tBsChargeBillHistoryMapper.findCurrentBillByBuildingCode(building.getBuildingCode());

            if (CommonUtils.isEmpty(histories)) {
                logger.warn("{}: 当前建筑下不含已计费账单. 数据:{}", title, building.toString());
                continue;
            }
            map = assemData2Map(histories, building, wyIsTaonei, rateMap);
            if (null != map) {
                boolean billFlag= (boolean) map.get("billFlag");
                if(billFlag) {
                    feeList.add(map);
                }
            }
        }

        if (!CollectionUtils.isEmpty(feeList)) {
            logger.info("{}:账单数据组装完毕 , 投递至消息队列进行文件读写. routeKey:{}", title, route_key_bill_gen);
            MqEntity entity = new MqEntity();
            entity.setCompanyId(companyId);
            entity.setData(JSON.toJSONString(feeList));
            if(dong!=null) {
                entity.setSupAttr(dong.getBuildingFullName() + "-" + dong.getBuildingCode());
            }else {
                entity.setSupAttr("其它-其它");
            }
            entity.setProjectId(project.getId());
            entity.setProjectName(project.getProjectName());
            entity.setOpr(flag);
            entity.setBuildingCodes(chargeBuildingCodes);
            this.amqpTemplate.convertAndSend(route_key_bill_gen, entity);

            //投递至 BillWriteListener

            logger.info("{}: 账单数据组装完毕 , 投递至消息队列进行文件读写完成 . routeKey:{},数据:{}", title, route_key_bill_gen, entity.toString());
            feeList.clear();
        }
    }

    /**
     * @param projectId
     * @return
     */
    private Map<String, Double> initRateMap(String projectId) {
        Map<String, Double> rateMap = new HashMap<String, Double>();
        rateMap.put(Constants.BILLING_WY_STR, 0.0);
        rateMap.put(Constants.BILLING_BT_STR, 0.0);
        rateMap.put(Constants.BILLING_WATER_STR, 0.0);
        rateMap.put(Constants.BILLING_ELECT_STR, 0.0);

        List<Map<String, Object>> result = this.tBsChargingSchemeMapper.findCurrentRate(projectId);
        if (CommonUtils.isNotEmpty(result)) {
            for (String key : rateMap.keySet()) {
                for (Map<String, Object> m : result) {
                    if (CommonUtils.isEquals(key, CommonUtils.null2String(m.get("schemeType")))) {
                        rateMap.put(key, CommonUtils.null2Double(m.get("taxRate")));
                        break;
                    }
                }
            }
        }
        return rateMap;
    }

    /**
     * @TODO 组装数据到map
     */
    private Map<String, Object> assemData2Map(List<TBsChargeBillHistory> histories,
                                              TcBuilding building,
                                              boolean wyIsTaonei,
                                              Map<String, Double> rateMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean billFlag=false;
        AllBill allBill = new AllBill();
        allBill.setArea(CommonUtils.null2Double(building.getBuildingArea()));
        allBill.setBillCode("ZD" + this.idGenApi.queryMaxId(4).getModel().getId());
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        allBill.setBillGenTime(year + "-" + month);
        allBill.setCurrBilling(0.0);            //本期应付总额      只计算currentTotalBill > 0的
        allBill.setCurrTotalBill(0.0);          //本期产生费用总和
        allBill.setLastTotalBill(0.0);            //上期欠费金额      只计算小于0的  lastTotalBill - lastPayed > 0 的
        allBill.setTotalLateFee(0.0);            //违约金
        //allBill.setTotalDkFee(0.0);
        //allBill.setLastTotalPayed(0.0);

        TBcCollection collection = this.collectionMapper.findByBuildingCode(building.getBuildingCode());
        if (null != collection) {
            allBill.setIsCollection("是");
        } else {
            allBill.setIsCollection("否");
        }
        String buildingCode=building.getBuildingCode();
        PersonCustNew cust = this.personCustNewMapper.findNamesByBuildingCode(buildingCode);
        allBill.setCustName(cust.getName());
        allBill.setCustCode(cust.getCustCode());
        allBill.setFullName(building.getBuildingFullName());
        Double totalLateFee=CommonUtils.null2Double(tBsOwedHistoryMapper.findSumLateFeeByBuildingCode(buildingCode));
        allBill.setTotalLateFee(totalLateFee);
        double lastTotalBill = CommonUtils.null2Double(allBill.getLastTotalBill());
        double currBilling= CommonUtils.null2Double(allBill.getCurrBilling());
        boolean isEmptyBill = false;
        for (TBsChargeBillHistory history : histories) {
            //region for
            double currTotalBill = CommonUtils.null2Double(allBill.getCurrTotalBill());
            double currentFee = CommonUtils.null2Double(history.getCurrentFee());
            double shareFee = CommonUtils.null2Double(history.getShareFee());
            double lastBillFee = CommonUtils.null2Double(history.getLastBillFee());
//			double totalDkFee = CommonUtils.null2Double(allBill.getTotalDkFee());
            double ncd = CommonUtils.null2Double(history.getNoCommonDesummoney());
            double cd = CommonUtils.null2Double(history.getCommonDesummoney());

            //            double totalLateFee = CommonUtils.null2Double(allBill.getTotalLateFee());
            //double lateFee = CommonUtils.null2Double(history.getLateFee());
//			double lastTotalPayed = CommonUtils.null2Double(allBill.getLastTotalPayed());
            double lastPayed = CommonUtils.null2Double(history.getLastPayed());
            double currentQf = 0.0;

            if (lastBillFee > lastPayed) {
                allBill.setLastTotalBill(CommonUtils.getScaleNumber(lastTotalBill + lastBillFee - lastPayed, 2));
            }
            Double typeLateFee=CommonUtils.null2Double(tBsOwedHistoryMapper.findSumByBuildingCodeAndType(buildingCode,history.getType()));
            Bill bill = new Bill();
            double lastNeedPay=0.0;
            double dk=CommonUtils.getScaleNumber(cd+ncd+lastPayed,2);
            if(dk!=0){
                double left=dk-CommonUtils.null2Double(typeLateFee);
                if(left>0){
                    typeLateFee=0.0;
                    double calculate=CommonUtils.getScaleNumber(left-lastBillFee,2);
                    if(calculate>0){
                        lastNeedPay=0.0;
                    }else {
                        lastNeedPay=CommonUtils.getScaleNumber(Math.abs(calculate),2);
                    }
                }else {
                    typeLateFee=Math.abs(left);
                    lastNeedPay=lastBillFee;
                }
            }else {
                lastNeedPay=lastBillFee;
            }
            bill.setLateFee(typeLateFee);
            bill.setLastBillFee(lastBillFee);
            bill.setShareFee(shareFee);
            bill.setWyPrice(building.getUnitWyPrice());
            bill.setBtPrice(building.getUnitBtPrice());
            bill.setLateFee(typeLateFee);
            //本期欠费总额 , 如果大于0 才计入欠费
            if(lastNeedPay==0.0){
                currentQf = CommonUtils.getScaleNumber(lastBillFee-lastPayed + currentFee + shareFee + CommonUtils.null2Double(typeLateFee)-cd-ncd, 2);
                if (currentQf > 0) {
                    bill.setTotal(currentQf);
                } else {
                    bill.setTotal(0.0);
                }
            }else {
                currentQf = CommonUtils.getScaleNumber(lastNeedPay + currentFee + shareFee + CommonUtils.null2Double(typeLateFee), 2);
                if (currentQf > 0) {
                    bill.setTotal(currentQf);
                } else {
                    bill.setTotal(0.0);
                }
            }

            if (BillingEnum.ACCOUNT_TYPE_WY.getIntV() == history.getType() || BillingEnum.ACCOUNT_TYPE_BT.getIntV() == history.getType()) {
                //bill.setLateFee(lateFee);
                bill.setLastPayed(lastPayed);
                bill.setCurrFee(currentFee);
                bill.setLastBillFee(lastBillFee);
                bill.setCurrPayed(CommonUtils.getScaleNumber(ncd + cd, 2));
                bill.setCurrBillFee(history.getCurrentBillFee());
                bill.setLastUnPay(CommonUtils.getScaleNumber(Math.abs(lastNeedPay), 2));

				/*bill.setTaxRate(
						(BillingEnum.ACCOUNT_TYPE_WY.getIntV() == history.getType()) ? 
							CommonUtils.null2Double(rateMap.get(Constants.BILLING_WY_STR))
						:
							CommonUtils.null2Double(rateMap.get(Constants.BILLING_BT_STR))	
						);*/

                map.put((BillingEnum.ACCOUNT_TYPE_WY.getIntV() == history.getType() ? Constants.BILLING_WY_STR : Constants.BILLING_BT_STR), bill);

                if(!billFlag){
                    billFlag=true;
                }
                isEmptyBill = true;

            } else if (BillingEnum.ACCOUNT_TYPE_WATER.getIntV() == history.getType() || BillingEnum.ACCOUNT_TYPE_ELECT.getIntV() == history.getType()) {

                //判断当前表是否为C级子表, 如果为C级子表, 则不生成pdf账单(只生成M级的聚合账单pdf文件)
                Integer meterLevel = this.tcWaterMeterMapper.findMeterLevelBySomeParams(building.getProjectId(), building.getBuildingCode(), history.getType());

                if (Constants.METER_TYPE_CHILDREN == meterLevel) {
                    //相等,则为子表, 本条数据不再计入账单
                    continue;
                }
                if(!billFlag){
                    billFlag=true;
                }
                //bill.setLateFee(lateFee);
                bill.setLastPayed(lastPayed);
                bill.setCurrPayed(CommonUtils.getScaleNumber(ncd + cd, 2));
                bill.setCurrFee(currentFee);
                bill.setLastBillFee(lastBillFee);
                bill.setLastUnPay(CommonUtils.getScaleNumber(Math.abs(lastNeedPay), 2));
				/*bill.setTaxRate(
						(BillingEnum.ACCOUNT_TYPE_WY.getIntV() == history.getType()) ? 
							CommonUtils.null2Double(rateMap.get(Constants.BILLING_WY_STR))
						:
							CommonUtils.null2Double(rateMap.get(Constants.BILLING_BT_STR))	
						);*/


                String buildCode = history.getBuildingCode();
                String projectId = history.getProjectId();

                if (BillingEnum.ACCOUNT_TYPE_WATER.getIntV() == history.getType()) {
                    if (StringUtils.isNotBlank(buildCode)) {
                        TcWaterMeter waterMeter = this.tcWaterMeterMapper.findByBuildingCode(projectId, buildCode);
                        if (CommonUtils.isNotEmpty(waterMeter)) {
                            double rate = waterMeter.getRate();
                            if (CommonUtils.isNotEmpty(rate)) {
                                bill.setRate(String.valueOf(rate));//倍率
                            }
                        }
                    }
                } else if (BillingEnum.ACCOUNT_TYPE_ELECT.getIntV() == history.getType()) {
                    if (StringUtils.isNotBlank(buildCode)) {
                        ElectMeter electMeter = this.tcElectMeterMapper.getElectMeterByReationId(projectId, buildCode);
                        if (CommonUtils.isNotEmpty(electMeter)) {
                            float rate = electMeter.getRate();
                            if (CommonUtils.isNotEmpty(rate)) {
                                bill.setRate(String.valueOf(rate));//倍率
                            }
                        }
                    }
                }
                pickupPrice(projectId,buildCode,bill,history.getType(),history.getBillingTime());
                Integer meterType = (history.getType() == 3 ? 0 : 1); //0是水   1是电
                //查找上期总读数，本期总读数及总用量
                Map<String, Object> mapResult = getmeterData(history, meterType);
                if (CommonUtils.isNotEmpty(mapResult)) {
                    bill.setLastReading(CommonUtils.null2String(mapResult.get("lastTotalReading")));
                    bill.setTotalReading(CommonUtils.null2String(mapResult.get("totalReading")));
                    bill.setUseCount(CommonUtils.null2Double(mapResult.get("useCount")));
                    Object o=mapResult.get("rate");
                    if(o!=null){
                        bill.setRate(o.toString());
                    }
                }

                map.put(BillingEnum.ACCOUNT_TYPE_WATER.getIntV() == history.getType() ? Constants.BILLING_WATER_STR : Constants.BILLING_ELECT_STR, bill);

                isEmptyBill = true;
            }
            //endregion
            lastTotalBill=CommonUtils.getScaleNumber(lastTotalBill+bill.getLastUnPay(),2);
            currBilling=CommonUtils.getScaleNumber(currBilling+bill.getTotal(),2);
        }
        allBill.setLastTotalBill(lastTotalBill);
        allBill.setCurrBilling(currBilling);
        allBill.setCurrTotalBill(CommonUtils.getScaleNumber(allBill.getCurrBilling()-allBill.getLastTotalBill()-allBill.getTotalLateFee(),2));
        if (!isEmptyBill) {
            return null;
        }
        map.put(Constants.BILLING_ALL_STR, allBill);
        map.put("billFlag",billFlag);
        map.put("fullName", building.getBuildingFullName());
        //update by shiny 2018-4-8 add other side of the bill start
        //add postCode from project
        TSysProject tSysProject = tSysProjectMapper.findByCode(building.getProjectId());
        map.put("postCode", tSysProject.getZipCode());
        map.put("houseCode", building.getHouseCode());
        //update by shiny 2018-4-8 add other side of the bill end
        return map;
    }

    //得的读数表的总读数等信息
    private Map<String, Object> getmeterData(TBsChargeBillHistory history, Integer meterType) {
        Map<String, Object> map = null;
        String totalProjectId = history.getChargeTotalId();
        TBsChargeBillTotal tBsChargeBillTotal = tBsChargeBillTotalMapper.findTbsTotalbyId(totalProjectId);
        if (StringUtils.isNotBlank(history.getProjectId()) && CommonUtils.isNotEmpty(tBsChargeBillTotal) && CommonUtils.isNotEmpty(tBsChargeBillTotal.getBillingTime())) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("meterType", meterType);
            paramMap.put("projectId", history.getProjectId());
            paramMap.put("relationBuilding", history.getBuildingCode());
            paramMap.put("state", 0);//电表启用状态
            Date lastBillTime=tBsChargeBillHistoryMapper.findLastBillTime(history.getId());
            paramMap.put("lastBillTime", lastBillTime);
            paramMap.put("billTime",history.getBillingTime());
            List<Map<String, Object>> rusultMap = this.tcMeterDataMapper.getCountAndFeeObjByProject(paramMap);
            if (!CollectionUtils.isEmpty(rusultMap)) {
                map = rusultMap.get(0);
                Object object=map.get("meterLevel");
                if(null!=object){
                    Integer meterLevel=Integer.valueOf(object.toString());
                    if(meterLevel.equals(11)){
                        map.put("lastTotalReading","详询管理处");
                        map.put("totalReading","详询管理处");
                        map.put("rate","详询管理处");
                    }
                }
            }
        }
        return map;
    }


    /**
     * @param projectId
     * @return
     * @TODO 获取栋座的建筑
     */
    private List<TcBuildingList> getDongList(String projectId) {
        TcBuildingSearch condition = new TcBuildingSearch();
        condition.setProjectId(projectId);
        condition.setBuildingType(LookupItemEnum.buildingType_dongzuo.getStringValue());
        return this.tcBuildingMapper.findByCondition(condition);
    }

    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("rawtypes")
    @Override
    public BaseDto reGenBill(String companyId, TBsProject project) {

        MessageMap msgMap = null;

        //project_id , searchTime,
        List<String> times = CommonUtils.str2List(project.getSearchTime(), Constants.STR_COMMA);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("projectId", project.getProjectId());
        paramMap.put("times", times);
        List<TBsProject> projects = this.tBsProjectMapper.findCanReGenBillingProject(paramMap);

        if (CommonUtils.isEmpty(projects)) {

            msgMap = new MessageMap(MessageMap.INFOR_WARNING, "未找到可重新生成账单的项目.");
            logger.info("账单重新计费: 未找到可重新生成账单的项目. 传入数据:{}. ", project.toString());
            return new BaseDto(msgMap);
        } else if (projects.size() < times.size()) {

            msgMap = new MessageMap(MessageMap.INFOR_WARNING, "有部分项目无法重新生成账单. 后台开始异步生成符合条件的账单. ");
            logger.info("账单重新计费: 有部分项目无法重新生成账单. 传入数据:{}. ", project.toString());

        } else {

            msgMap = new MessageMap(MessageMap.INFOR_SUCCESS, "重新生成账单在后台异步开始,请稍后查看. ");
            logger.info("账单重新计费: 重新生成账单在后台生成账单. 传入数据:{}. ", project.toString());

        }


        for (TBsProject p : projects) {

            logger.info("账单重新计费: 开始组装数据发送至消息队列. ");
            MqEntity entity = new MqEntity();
            entity.setOpr(BillingEnum.mq_gen_bill_manaul_regen.getIntV());
            entity.setCompanyId(companyId);
            entity.setProjectId(p.getProjectId());
            entity.setProjectName(p.getProjectName());
            entity.setData(p);
            this.amqpTemplate.convertAndSend(route_key_gen_bill, entity);
            logger.info("账单重新计费: 数据组装并发送至消息队列完成.  routeKey:{}, 数据:{} .", route_key_gen_bill, entity.toString());

            //投递至 reGenBillByManaul
        }

        return new BaseDto(msgMap);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void autoGenBill(String companyId, TBsProject project) {
        Template template = new FtlUtils().getFtlConfig(sysConfig.getBillFtlPath(), sysConfig.getBillFtlFileName());
        if (template == null) {
            logger.error("账单生成: 未找到可用的freemark模板, 账单生成失败. ");
            return;
        }

        if (project.getIsGenBill() == BillingEnum.bill_is_gen_yes.getIntV()) {
            logger.warn("账单生成:  当前项目的账单已经生成,无法继续生成. ");
            return;
        }

        List<TcBuildingList> buildings = getDongList(project.getProjectId());
        if (CommonUtils.isEmpty(buildings)) {
            logger.warn("账单生成: 当前项目下未找到含有栋座的建筑, 生成失败. ");
            return;
        }

        //生成账单
        genBillByBuildings(companyId, buildings, project, BillingEnum.TYPE_AUTO.getIntV());

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void reGenBillByManaul(String companyId, TBsProject project) {


        if (project.getIsGenBill() == BillingEnum.bill_is_gen_no.getIntV()) {
            logger.warn("账单重新生成:  当前项目的账单尚未生成,无法重新生成. ");
            return;
        }

        List<TcBuildingList> buildings = getDongList(project.getProjectId());
        if (CommonUtils.isEmpty(buildings)) {
            logger.warn("账单重新生成: 当前项目下未找到含有栋座的建筑, 生成失败. ");
            return;
        }

        //将该项目之前的账单无效化
        Annex annex = new Annex();
        annex.setRelationId(project.getId());
        annex.setAnnexTime(CommonUtils.getDateStr(project.getBillingTime()));
        annex.setIsUsed(AnnexEnum.annex_is_used_no.getIntV());
        this.annexMapper.updateAnnex(annex);

        //生成账单
        genBillByBuildings(companyId, buildings, project, BillingEnum.mq_gen_bill_manaul_regen.getIntV());
    }

    @Override
    public BaseDto zipBill(String companyId, TBsProject project) {
		
		/*File file = new File("E:\\file\\bill\\深圳市翔恒科技开发有限公司\\桃源峰景园");
		
		for(File cDir : file.listFiles()){
			PDFUtils.getInstance().combinePdfs(cDir.getAbsolutePath(), "E:\\file\\bill_zip\\深圳市翔恒科技开发有限公司\\桃源峰景园", 
											  cDir.getName()+".pdf");
		}*/

        MqEntity e = new MqEntity();
        e.setCompanyId(companyId);
        e.setProjectId(project.getProjectId());
        e.setSupAttr(project.getId());
        e.setOpr(BillingEnum.mq_gen_bill_manaul_first.getIntV());
        this.amqpTemplate.convertAndSend(route_key_bill_upload_key, e);

        return new BaseDto(new MessageMap(null, "账单开始打包"));
    }

    private void pickupPrice(String projectId,String buildCode,Bill bill,Integer type,Date billTime){
        TBsChargingScheme paramScheme = new TBsChargingScheme();
        if(type.equals(BillingEnum.SCHEME_TYPE_WATER.getIntV())) {
            paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_WATER.getIntV());    //水费scheme
        }else if(type.equals(BillingEnum.SCHEME_TYPE_ELECT.getIntV())){
            paramScheme.setSchemeType(BillingEnum.SCHEME_TYPE_ELECT.getIntV());
        }
        paramScheme.setProjectId(projectId);    //project code
        TBsChargingScheme scheme = tBsChargingSchemeMapper.findUsingScheme(paramScheme);
        if (CommonUtils.isEmpty(scheme)) {
            logger.error("未找到计费方案无法查找单价!");
        } else{
            List<TBsChargingRules> rules = tBsChargingRulesMapper.getTBsChargingRulesBySchemeId(scheme.getId());
            List<TBsRuleBuildingRelation> buildingRelations = tBsRuleBuildingRelationMapper.selectByBuildingCode(buildCode);
            if (CommonUtils.isEmpty(buildingRelations)) {
                logger.info("未找到计费规则,无法查找单价!");
            } else {
                String ruleId = null;
                for (TBsRuleBuildingRelation relation : buildingRelations) {
                    if (null != ruleId) {
                        break;
                    }
                    for (TBsChargingRules rule : rules) {
                        if (rule.getId().equals(relation.getChargingRuleId())) {
                            ruleId = rule.getId();
                            break;
                        }
                    }
                }
                if (ruleId!=null) {
                    List<TBsChargeType> tBsChargingTypeList = tBsChargeTypeMapper.selectChargeType(ruleId);
                    if (tBsChargingTypeList == null) {
                        logger.error("未找到收费项，无法查找单价!");
                    } else {
                        TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
                        paramTotal.setProjectId(projectId);
                        paramTotal.setType(scheme.getSchemeType());
                        List<TBsChargeBillTotal> totals = tBsChargeBillTotalMapper.findCurrentBillTotal(paramTotal);
                        TBsChargeBillTotal currentTotalBill = null;
                        Date lastBillDate = scheme.getStartUsingDate();
                        if (CommonUtils.isNotEmpty(totals)) {
                            if (totals.size() == 1) {
                                currentTotalBill = totals.get(0);
                            } else if (totals.size() > 1) {
                                //这里两个总单 只有两种 一个总单，计费时间不为空，且切分砖头未2    另外一个总单 计费时间为空，且然后计费状态为0  是未计费状态
                                for (TBsChargeBillTotal totalBill : totals) {
                                    Date billtime = totalBill.getBillingTime();
                                    Integer billStatus = totalBill.getBillStatus();
                                    if (CommonUtils.isNotEmpty(billtime) && billStatus.equals(BillingEnum.BILL_STATUS_APART.getIntV())) {
                                        currentTotalBill = totalBill; //这个表示部分计费的总单
                                        break;
                                    } else {
                                        currentTotalBill = totalBill;
                                    }
                                }
                            }
                            TBsChargeBillTotal lastBill = tBsChargeBillTotalMapper.selectById(currentTotalBill.getLastTotalId());
                            if (null != lastBill) {
                                TBsChargeBillTotal lastBillOld = tBsChargeBillTotalMapper.selectById(lastBill.getLastTotalId());
                                if (null != lastBillOld) {
                                    lastBillDate = lastBillOld.getBillingTime();
                                }
                            }
                            Map<String, Object> paramMap = new HashMap<String, Object>();
                            if(type.equals(BillingEnum.SCHEME_TYPE_WATER.getIntV())) {
                                paramMap.put("meterType", 0); //水表
                            }else if(type.equals(BillingEnum.SCHEME_TYPE_ELECT.getIntV())){
                                paramMap.put("meterType", 1); //电表
                            }
                            paramMap.put("projectId", projectId);
                            paramMap.put("lastBillTime", lastBillDate);
                            paramMap.put("relationBuilding", buildCode);
                            paramMap.put("billTime",billTime);
                            List<Map<String, Object>> resultDetailMap = tcMeterDataMapper.getCountAndFeeObjByProject(paramMap);
                            if (resultDetailMap != null && resultDetailMap.size() == 1) {
                                for (TBsChargeType chargeType : tBsChargingTypeList) {
                                    Double maxCriticalpoint = chargeType.getMaxCriticalpoint();
                                    Double minCriticalpoint = chargeType.getMinCriticalpoint();
                                    Double userCount = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(resultDetailMap.get(0).get("useCount")) ? "0" : resultDetailMap.get(0).get("useCount")));
                                    if (null == minCriticalpoint || null == maxCriticalpoint) {
                                        continue;
                                    }
                                    if ((minCriticalpoint == 0 && maxCriticalpoint == 0) || (userCount >= minCriticalpoint && userCount <= maxCriticalpoint)) {
                                        //chargeType 满足
                                        logger.info("满足条件的收费项目:charge_type:{}", JSON.toJSONString(chargeType));
                                        setBillPrice(chargeType,bill);
                                    }
                                    if (minCriticalpoint != 0 && maxCriticalpoint == 0) {
                                        if (userCount <= minCriticalpoint) {
                                            continue;
                                        }else{
                                            logger.info("满足条件的收费项目:charge_type:{}", JSON.toJSONString(chargeType));
                                            setBillPrice(chargeType,bill);
                                        }
                                    }
                                    if (minCriticalpoint == 0 && maxCriticalpoint != 0) {
                                        if (userCount > maxCriticalpoint) {
                                            continue;
                                        }else {
                                            logger.info("满足条件的收费项目:charge_type:{}", JSON.toJSONString(chargeType));
                                            setBillPrice(chargeType,bill);
                                        }
                                    }
                                    if (minCriticalpoint != 0 && maxCriticalpoint != 0) {
                                        if (userCount <= minCriticalpoint || userCount > maxCriticalpoint) {
                                            continue;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private void  setBillPrice(TBsChargeType chargeType,Bill bill){
        String formulaInfo = chargeType.getFormulaInfo();
        if (formulaInfo != null) {
            String[] constantArray = formulaInfo.split(" ");
            for (int i = 0; i < constantArray.length; i++) {
                String item = constantArray[i];
                if (item.indexOf("标准水费") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setWaterPrice(Double.parseDouble(keyValue[1]));
                    }
                }
                if (item.indexOf("超额水费一") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setWaterPrice1(Double.parseDouble(keyValue[1]));
                    }
                }
                if (item.indexOf("超额水费二") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setWaterPrice2(Double.parseDouble(keyValue[1]));
                    }
                }
                if (item.indexOf("污水处理单价") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setPollutedPrice(Double.parseDouble(keyValue[1]));
                    }
                }
                if (item.indexOf("污水处理费一") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setPollutedPrice1(Double.parseDouble(keyValue[1]));
                    }
                }
                if (item.indexOf("污水处理费二") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setPollutedPrice2(Double.parseDouble(keyValue[1]));
                    }
                }
                if (item.indexOf("垃圾处理费") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setRubbishPrice(Double.parseDouble(keyValue[1]));
                    }
                }
                if (item.indexOf("垃圾处理费一") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setRubbishPrice1(Double.parseDouble(keyValue[1]));
                    }
                }
                if (item.indexOf("垃圾处理费二") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setRubbishPrice2(Double.parseDouble(keyValue[1]));
                    }
                }
                if (item.indexOf("电费计费单价") != -1) {
                    String[] keyValue = item.split(":");
                    if (keyValue != null && keyValue.length == 2) {
                        bill.setElectPrice(Double.parseDouble(keyValue[1]));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        double dk=CommonUtils.getScaleNumber(1540.40,2);
        System.out.println(dk);
        double left=dk-CommonUtils.null2Double(11.32);
        System.out.println(left);
        if(left>0) {
            double calculate = CommonUtils.getScaleNumber(left - 3614.00, 2);
            System.out.println(calculate);
            System.out.println(CommonUtils.getScaleNumber(Math.abs(calculate),2));
            System.out.println(CommonUtils.getScaleNumber(Math.abs(calculate), 2));
        }
    }
}
