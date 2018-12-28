package com.everwing.autotask.core.service.impl.bill;

import com.alibaba.fastjson.JSON;
import com.everwing.autotask.core.dao.*;
import com.everwing.autotask.core.service.AnnexService;
import com.everwing.autotask.core.service.CompanyService;
import com.everwing.autotask.core.service.FastDFSService;
import com.everwing.autotask.core.service.ProjectService;
import com.everwing.autotask.core.service.bill.BillMgrService;
import com.everwing.autotask.core.utils.ReportUtils;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
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
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 账单
 *
 * @author DELL shiny
 * @create 2018/6/4
 */
@Log4j2
@Service
public class BillMgrServiceImpl implements BillMgrService{

    @Value("${bill_file_path}")
    private String billFilePath;

    @Value("${bill_file_name}")
    private String billFileName;

    @Value("${bill_zip_path}")
    private String billZipPath;

    @Autowired
    private TcBuildingMapper tcBuildingMapper;

    @Autowired
    private TBsChargingSchemeMapper tBsChargingSchemeMapper;

    @Autowired
    private TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private PersonCustNewMapper personCustNewMapper;

    @Autowired
    private TBsOwedHistoryMapper tBsOwedHistoryMapper;

    @Autowired
    private TcWaterMeterMapper tcWaterMeterMapper;

    @Autowired
    private TcElectMeterMapper tcElectMeterMapper;

    @Autowired
    private TBsChargingRulesMapper tBsChargingRulesMapper;

    @Autowired
    private TBsRuleBuildingRelationMapper tBsRuleBuildingRelationMapper;

    @Autowired
    private TBsChargeTypeMapper tBsChargeTypeMapper;

    @Autowired
    private TBsChargeBillTotalMapper tBsChargeBillTotalMapper;

    @Autowired
    private TcMeterDataMapper tcMeterDataMapper;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private FastDFSService fastDFSService;

    @Autowired
    private TBsProjectMapper tBsProjectMapper;

    @Autowired
    private AnnexService annexService;

    @Autowired
    private TSysProjectMapper tSysProjectMapper;

    @Override
    public void autoGenBill(String companyId, TBsProject project) {
        if (project.getIsGenBill() == BillingEnum.bill_is_gen_yes.getIntV()) {
            log.warn("账单生成:  当前项目的账单已经生成,无法继续生成. ");
            return;
        }
        List<TcBuildingList> buildings = getDongList(project.getProjectId());
        if (CommonUtils.isEmpty(buildings)) {
            log.warn("账单生成: 当前项目下未找到含有栋座的建筑, 生成失败. ");
            return;
        }
        genBillByBuildings(companyId, buildings, project, BillingEnum.TYPE_MANUAL.getIntV());
    }

    private List<TcBuildingList> getDongList(String projectId) {
        TcBuildingSearch condition = new TcBuildingSearch();
        condition.setProjectId(projectId);
        condition.setBuildingType(LookupItemEnum.buildingType_dongzuo.getStringValue());
        return tcBuildingMapper.findByCondition(condition);
    }

    private void genBillByBuildings(String companyId, List<TcBuildingList> buildings, TBsProject project, int flag) {

        String title = "账单自动生成";
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
            List<TcBuilding> chargeBuildings = tcBuildingMapper.findHasBillsBuildings(project.getProjectId(), project.getBillingTime(), dong.getBuildingCode());

            if (CommonUtils.isEmpty(chargeBuildings)) {
                log.warn("{}: 当前栋座下不含已生成账单建筑. 数据:{}", title, dong.toString());
                continue;
            }

            Map<String, Object> map = null;
            for (TcBuilding building : chargeBuildings) {
                //分别找出各个建筑的物业管理费,本体基金以及水电费
                List<TBsChargeBillHistory> histories = tBsChargeBillHistoryMapper.findCurrentBillByBuildingCode(building.getBuildingCode());

                if (CommonUtils.isEmpty(histories)) {
                    log.warn("{}: 当前建筑下不含已计费账单. 数据:{}", title, building.toString());
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
            billWrite(companyId,project.getId(),dong.getBuildingFullName() + "-" + dong.getBuildingCode(),feeList,project.getProjectName());
        }
    }

    private void billWrite(String companyId,String projectId,String dongStr,List<Map<String,Object>> coll,String projectName) {
        File jasperFile= null;
        try {
            jasperFile = ResourceUtils.getFile("classpath:"+billFilePath+File.separator+ billFileName);
        } catch (FileNotFoundException e) {
            log.error("未找到报表文件，失败");
            return;
        }
        if(!jasperFile.exists()){
            log.error("未找到账单模板,失败!");
            return;
        }
        Company company=companyService.queryCompany(companyId);
        TBsProject project = projectService.findById(companyId,projectId);

        if(project == null){
            log.warn("定时任务: [账单zip包扫描打包及上传]  未找到对应项目. 公司id : {} , 项目id : {} ", companyId, projectId);
            return;
        }

        String dongName = dongStr.split("-")[0];
        String dongCode = dongStr.split("-")[1];
        log.info("{}公司的:{}项目:{}栋数据准备完成！共:{}户",company.getCompanyName(),projectName,dongName,coll.size());
        String newPath = billZipPath + File.separator + company.getCompanyName() + File.separator + projectName;
        File folder=new File(newPath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String fileName = dongName + "-" + CommonUtils.getDateStr(new Date(), "yyyy年MM月") + ".pdf";
        String dongPdfFile=newPath+File.separator+fileName;
        if(!coll.isEmpty()&&coll.size()>0) {
            long start=System.currentTimeMillis(); //获取开始时间
            ReportUtils.generatePDF(jasperFile.getPath(), dongPdfFile, coll);
            long end=System.currentTimeMillis(); //获取结束时间
            log.info("栋PDF程序生成时间： "+(end-start)+"ms");
        }else {
            return;
        }
        log.info("{}公司的:{}项目pdf生成完成。路径:{}",company.getCompanyName(),projectName,newPath);

        //对项目下的本栋建筑的分单做打包完成标识
        TBsChargeBillHistory paramObj = new TBsChargeBillHistory();
        paramObj.setProjectId(project.getProjectId());
        paramObj.setBillingTime(project.getBillingTime());
        paramObj.setBuildingCode(dongCode);
        paramObj.setIsZipComplete(BillingEnum.bill_is_zip_yes.getIntV());
        tBsChargeBillHistoryMapper.updateZipCompleteByObj(paramObj); //对本栋打上完成标识

        //查询本项目下是否还有未打包的房屋
        int count = tBsChargeBillHistoryMapper.findNotZipByObj(paramObj);
        if(count == 0){
            billFileUpload(companyId,projectId);
        }
    }

    private void billFileUpload(String companyId,String projectId) {
        File file = new File(billZipPath);
        if(!file.exists() || CommonUtils.isEmpty(file.listFiles())){
            log.warn("定时任务: [账单zip包扫描打包及上传]  路径下未找到可打包的文件. 路径 : {}. ", billZipPath);
            return;
        }
        TBsProject project=projectService.findById(companyId,projectId);
        ZipOutputStream zos = null;
        FileInputStream fis = null;
        for(File companyFile : file.listFiles()){ //E:\file\bill_zip下所有的公司级别
            File[] projectFiles = companyFile.listFiles();  //E:\file\bill_zip\深圳市翔恒科技开发有限公司    下项目级别
            if(CommonUtils.isEmpty(projectFiles)){
                log.info("定时任务: [账单zip包扫描打包及上传]  路径下未找到可打包的项目文件. 路径:{} ", companyFile.getAbsolutePath());
                continue;
            }
            log.info("项目文件列表:{}",projectFiles);
            //桃源盛景园文件夹下
            for(File projectFile : projectFiles){
                log.info("开始将公司:{}下的:{}项目打包",companyFile.getName(),projectFile.getName());
                if(projectFile == null || projectFile.getName().endsWith(AnnexEnum.annex_file_type_zip.getStringV())){
                    continue;
                }
                log.info("projectFile:{}continue之后",projectFile);
                String zipFileName = projectFile.getName() + CommonUtils.getDateStr(project.getBillingTime(), "-yyyy年MM月");
                File zipProjectFile = new File(companyFile.getAbsolutePath() + File.separator + zipFileName + ".zip");

                try {
                    zos = new ZipOutputStream(new FileOutputStream(zipProjectFile)); //公司目录下

                    for(File zipFile : projectFile.listFiles()){
                        ZipEntry entry = new ZipEntry(zipFile.getName());
                        zos.putNextEntry(entry);

                        fis = new FileInputStream(zipFile);
                        byte[] b = new byte[fis.available()];
                        fis.read(b);
                        zos.write(b);
                        fis.close();
                        b = null;
                        fis = null;
                        zos.flush();
                    }
                    zos.flush();
                    zos.close();
                    zos = null;

                    //2.上传

                    //2.1 同步等待压缩包上传并修改附件状态, 异步修改项目的账单状态生成状态将本项目的is_gen_bill修改成已生成状态
                    TBsProject tBsProject = new TBsProject();
                    tBsProject.setId(projectId);
                    tBsProject.setIsGenBill(BillingEnum.bill_is_gen_yes.getIntV());
                    tBsProjectMapper.updateGenBill(tBsProject);
                    log.info("修改项目状态完成");
                    //2.2 上传
                    UploadFile uploadFile=fastDFSService.uploadFile(zipProjectFile);

                    //2.2.1 组装数据,插入物业公司ts_annex
                    Annex annex = new Annex();
                    annex.setAnnexId(uploadFile.getUploadFileId());
                    annex.setAnnexName(zipFileName);
                    annex.setAnnexType(AnnexEnum.annex_type_zip.getIntV());
                    annex.setCompanyId(companyId);
                    annex.setRelationId(project.getProjectId());
                    annex.setAnnexAddress(uploadFile.getPath());
                    annex.setFileType(AnnexEnum.annex_file_type_zip.getStringV());
                    annex.setProjectId(project.getProjectId());
                    annex.setAnnexTime(CommonUtils.getDateStr());
                    annex.setIsUsed(AnnexEnum.annex_is_used_yes.getIntV());
                    annex.setMd5(uploadFile.getMd5());
                    annex.setUploadFileId(uploadFile.getUploadFileId());
                    this.annexService.addAnnex(companyId, annex);

                    //2.2.2 删除该项目及项目下所有文件
                    projectFile.delete();
                    if(null != projectFile && projectFile.exists()){
                        FileUtils.forceDelete(projectFile);
                    }

                    if(null != zipProjectFile && zipProjectFile.exists()){
                        FileUtils.forceDeleteOnExit(zipProjectFile);
                    }
                    log.info("zip 上传完成");
                } catch ( Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    private Map<String, Double> initRateMap(String projectId) {
        Map<String, Double> rateMap = new HashMap<String, Double>();
        rateMap.put(Constants.BILLING_WY_STR, 0.0);
        rateMap.put(Constants.BILLING_BT_STR, 0.0);
        rateMap.put(Constants.BILLING_WATER_STR, 0.0);
        rateMap.put(Constants.BILLING_ELECT_STR, 0.0);

        List<Map<String, Object>> result = tBsChargingSchemeMapper.findCurrentRate(projectId);
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

    private Map<String, Object> assemData2Map(List<TBsChargeBillHistory> histories,
                                              TcBuilding building,
                                              boolean wyIsTaonei,
                                              Map<String, Double> rateMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean billFlag=false;
        AllBill allBill = new AllBill();
        allBill.setArea(CommonUtils.null2Double(building.getBuildingArea()));
        allBill.setBillCode("ZD" + System.nanoTime());
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
                    double calculate=CommonUtils.getScaleNumber(left-lastBillFee,0);
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
            log.error("未找到计费方案无法查找单价!");
        } else{
            List<TBsChargingRules> rules = tBsChargingRulesMapper.getTBsChargingRulesBySchemeId(scheme.getId());
            List<TBsRuleBuildingRelation> buildingRelations = tBsRuleBuildingRelationMapper.selectByBuildingCode(buildCode);
            if (CommonUtils.isEmpty(buildingRelations)) {
                log.info("未找到计费规则,无法查找单价!");
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
                if (!ruleId.isEmpty()) {
                    List<TBsChargeType> tBsChargingTypeList = tBsChargeTypeMapper.selectChargeType(ruleId);
                    if (tBsChargingTypeList == null) {
                        log.error("未找到收费项，无法查找单价!");
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
                                        log.info("满足条件的收费项目:charge_type:{}", JSON.toJSONString(chargeType));
                                        setBillPrice(chargeType,bill);
                                    }
                                    if (minCriticalpoint != 0 && maxCriticalpoint == 0) {
                                        if (userCount <= minCriticalpoint) {
                                            continue;
                                        }else{
                                            log.info("满足条件的收费项目:charge_type:{}", JSON.toJSONString(chargeType));
                                            setBillPrice(chargeType,bill);
                                        }
                                    }
                                    if (minCriticalpoint == 0 && maxCriticalpoint != 0) {
                                        if (userCount > maxCriticalpoint) {
                                            continue;
                                        }else {
                                            log.info("满足条件的收费项目:charge_type:{}", JSON.toJSONString(chargeType));
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
}
