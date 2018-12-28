package com.everwing.coreservice.wy.core.service.impl.cust.enterprise;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.wy.common.enums.ImportExportEnum;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNew;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNewImportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.common.wy.service.cust.enterprise.EnterpriseCustImportService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.task.TcEnterpriseImportTask;
import com.everwing.coreservice.wy.core.utils.ImportExportUtils;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.enterprisecust.EnterpriseCustNewMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.person.PersonCustNewMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysProjectMapper;
import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.factory.DefinitionFactory;
import com.everwing.myexcel.factory.xml.XMLDefinitionFactory4commonImport;
import com.everwing.myexcel.resolver.poi.POIExcelResolver4commonImport;
import com.everwing.myexcel.result.ExcelImportResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


@Service("enterpriseCustServiceImportImpl")
public class EnterpriseCustServiceImportImpl extends POIExcelResolver4commonImport implements EnterpriseCustImportService{
    static Logger logger = LogManager.getLogger(EnterpriseCustServiceImportImpl.class);

    @Autowired
    private ImportExportMapper importExportMapper;

    @Autowired
    private EnterpriseCustNewMapper enterpriseCustNewMapper;

    @Autowired
    private PersonCustNewMapper personCustNewMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private FastDFSApi fastDFSApi;

    @Autowired
    private TSysProjectMapper tSysProjectMapper;


    @Override
    public void doImport(WyBusinessContext ctx, TSysImportExportSearch tSysImportExportRequest) {
        if(StringUtils.isBlank(tSysImportExportRequest.getProjectCode())){
            throw new ECPBusinessException("参数[projectCode]不能空");
        }

        if(StringUtils.isBlank(tSysImportExportRequest.getBatchNo())){
            throw new ECPBusinessException("参数[batchNo]不能空");
        }

        TSysImportExportSearch condition = new TSysImportExportSearch();
        condition.setBatchNo(tSysImportExportRequest.getBatchNo());
        List<TSysImportExportList> tSysImportExportListList = importExportMapper.findByCondtion(condition);
        if(CollectionUtils.isEmpty(tSysImportExportListList)){
            throw new ECPBusinessException("没有文件上传记录，请先上传文件");
        }
        TSysImportExportList tSysImportExportListExist = tSysImportExportListList.get(0);
        try {
            RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.loadFilePathById(tSysImportExportListExist.getUploadFileId());
            if(remoteModelResult.isSuccess()){
                UploadFile uploadFile = remoteModelResult.getModel();
                logger.info("上传的文件{}",uploadFile);
                URL url = new URL(uploadFile.getPath());
                HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
                uc.connect();
                super.excelInputStream = uc.getInputStream();
            }
        } catch (Exception e) {
            logger.error(e);
            throw new ECPBusinessException("导入失败，读取文件失败："+e.getMessage());
        }


        TSysProjectSearch tSysProjectSearch = new TSysProjectSearch();
        tSysProjectSearch.setCode(tSysImportExportRequest.getProjectCode());
        List<TSysProjectList>  tSysProjectLists = tSysProjectMapper.findByCondition(tSysProjectSearch);
        if(CollectionUtils.isNotEmpty(tSysProjectLists)){
            ctx.setProjectId(tSysProjectLists.get(0).getProjectId());
            ctx.setProjectCode(tSysProjectLists.get(0).getCode());
            ctx.setProjectName(tSysProjectLists.get(0).getName());
        }


        /**
         * 耗时多的业务丢到线程池
         */
        try {

            Runnable task = new TcEnterpriseImportTask(ctx,this,tSysImportExportRequest.getBatchNo());
            threadPoolTaskExecutor.execute(task);
        } catch (Exception e) {
            throw new ECPBusinessException("导入失败："+e.getMessage());
        }
    }

    @Override
    public MessageMap importCallback(WyBusinessContext ctx, String batchNo) {
            // 切换数据源
            WyBusinessContext.getContext().setCompanyId(ctx.getCompanyId());

            MessageMap mm = new MessageMap();

            int allCount = 0;   // 导入且有数据的sheet数量

            ExcelImportResult excelImportResult = null;

            try {
                // 1.读取excel数据
                excelImportResult = super.readExcel();

                // 2.处理业务数据
                Map<String, List<?>> listMap = excelImportResult.getListMap();

                List<EnterpriseCustNewImportList> enterpriseCustNewImportLists = (List<EnterpriseCustNewImportList>)listMap.get("0"); // 获取第1个sheet里面的数据


                MessageMap messageMap = null;
                if(!CollectionUtils.isEmpty(enterpriseCustNewImportLists)){
                    allCount ++;
                    messageMap = importEnterPrise(ctx, enterpriseCustNewImportLists);
                }


                int successCount = 0;
                int errorCount = 0;
                int partialSuccessCount = 0;
                if(messageMap != null){
                    if(MessageMap.INFOR_ERROR.equals(messageMap.getFlag())){
                        errorCount ++;
                    }else if(MessageMap.INFOR_WARNING.equals(messageMap.getFlag())){
                        partialSuccessCount ++;
                    }else{
                        successCount ++;
                    }
                }



                if(partialSuccessCount > 0){
                    mm.setObj(ImportExportEnum.partial_success.name());
                }else{
                    if(allCount > 0 && (allCount == successCount)){
                        mm.setObj(ImportExportEnum.succeed.name());
                    }else if(allCount == errorCount){
                        mm.setObj(ImportExportEnum.failed.name());
                    }else {
                        mm.setObj(ImportExportEnum.partial_success.name());
                    }
                }

                mm.setMessage(messageMap.getMessage());
            } catch (Exception e){
                mm.setFlag(MessageMap.INFOR_ERROR);
                mm.setMessage(e.getMessage());
            }
            return mm;
    }

    @Override
    public void after(WyBusinessContext ctx, MessageMap mm, String batchNo) {
        TSysImportExportSearch condition = new TSysImportExportSearch();
        condition.setBatchNo(batchNo);
        List<TSysImportExportList> tSysImportExportListList = importExportMapper.findByCondtion(condition);
        TSysImportExportList tSysImportExportListExist = tSysImportExportListList.get(0);

        String stauts = "";
        if(MessageMap.INFOR_SUCCESS.equals(mm.getFlag())){
            if(mm.getObj() != null){
                stauts = mm.getObj().toString();
            }else{
                stauts = ImportExportEnum.succeed.name();
            }
            TSysImportExport tSysImportExport = new TSysImportExport();
            BeanUtils.copyProperties(tSysImportExportListExist,tSysImportExport);
            tSysImportExport.setStatus(stauts);
            importExportMapper.modify(tSysImportExport);
        }else{
            if(mm.getObj() != null){
                stauts = mm.getObj().toString();
            }else{
                stauts = ImportExportEnum.failed.name();
            }
            TSysImportExport tSysImportExport = new TSysImportExport();
            BeanUtils.copyProperties(tSysImportExportListExist,tSysImportExport);
            tSysImportExport.setStatus(stauts);
            tSysImportExport.setEndTime(new Date());
            importExportMapper.modify(tSysImportExport);
        }

        // 上传导入结果到文件服务器
        ImportExportUtils.uploadMessage(importExportMapper,fastDFSApi,tSysImportExportListExist.getBatchNo(),mm.getMessage());


    }

    private MessageMap importEnterPrise(WyBusinessContext ctx, List<EnterpriseCustNewImportList> enterpriseCustNewImportLists) {
        MessageMap mm = new MessageMap();



        Map<String, Object> map = getImportEnterPrise(ctx, enterpriseCustNewImportLists);
        List<EnterpriseCustNew> newList = (List<EnterpriseCustNew>) map.get("newEnterpriseCustList");

        int commitSize = 1000;//默认每次提交数量
        // 新增
        if (CollectionUtils.isNotEmpty(newList)) {
            int size = newList.size();
            if (size <= commitSize) {
                this.enterpriseCustNewMapper.batchadd(newList);
            } else {
                if (size % commitSize == 0) {
                    int count = size / commitSize;
                    for (int i = 0; i < count; i++) {
                        int fromIndex = i * commitSize;
                        int toIndex = (i + 1) * commitSize;
                        enterpriseCustNewMapper.batchadd(newList.subList(fromIndex, toIndex));
                    }
                } else {
                    int endIndex = 0;
                    int count = size / commitSize;
                    for (int i = 0; i < count; i++) {
                        int fromIndex = i * commitSize;
                        int toIndex = (i + 1) * commitSize;
                        endIndex = toIndex;
                        enterpriseCustNewMapper.batchadd(newList.subList(fromIndex, toIndex));
                    }
                    enterpriseCustNewMapper.batchadd(newList.subList(endIndex, size));
                }
            }
        }

        Integer successCount = (Integer) map.get("successCount");
        Integer failCount = (Integer) map.get("failCount");
        StringBuffer error = (StringBuffer) map.get("error");
        if (failCount > 0 && successCount > 0) { //部分导入成功
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("企业客户部分导入成功;成功了" + successCount + "条;失败" + failCount + "条;" + error.toString());
        }
        if (failCount == 0 && successCount == enterpriseCustNewImportLists.size()) {//全部导入成功
            mm.setFlag(MessageMap.INFOR_SUCCESS);
            mm.setMessage("全部导入成功");
        }
        if (successCount == 0 && failCount == enterpriseCustNewImportLists.size()) {//全部导入失败
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("企业客户全部导入失败;失败了" + failCount + "条;" + error.toString());
        }
        return mm;
    }

        private Map<String,Object> getImportEnterPrise(WyBusinessContext ctx, List<EnterpriseCustNewImportList> enterpriseCustNewImportLists) {
            Map<String,Object> map =new HashMap<String,Object>();
            List<EnterpriseCustNew> newEnterpriseCustList = new ArrayList<EnterpriseCustNew>();
            StringBuffer error = new StringBuffer();
            Integer successCount=0;
            Integer failCount =0;
            try {

                // 生成的企业客户编码
                String UnitNumber = enterpriseCustNewMapper.selectCustCode();


                for(int i=0;i<enterpriseCustNewImportLists.size();i++){
                    EnterpriseCustNewImportList enterpriseCustNew = enterpriseCustNewImportLists.get(i);
                    if(enterpriseCustNew.getSuccessFlag()){
                        int count = this.enterpriseCustNewMapper.selectByTradingNum(enterpriseCustNew.getTradingNumber());
                        if(count > 0){
                            error.append("企业客户["+enterpriseCustNew.getEnterpriseName()+"]在系统中已存在营业资格证").append("\n");
                            failCount++;
                            continue;
                        }

                        count  = this.enterpriseCustNewMapper.selectByName(enterpriseCustNew.getEnterpriseName());
                        if(count > 0){
                            error.append("企业客户["+enterpriseCustNew.getEnterpriseName()+"]在系统中已存在该企业姓名").append("\n");
                            failCount++;
                            continue;
                        }

//                        if(CommonUtils.isNotEmpty(enterpriseCustNew.getRepresentative())){
//                            String Custid = this.personCustNewMapper.SeachIdByName(enterpriseCustNew.getRepresentative());
//                            if(CommonUtils.isNotEmpty(Custid)){
//                                enterpriseCustNew.setRepresentative(Custid);
//                            }else{
//                                error.append("企业客户["+enterpriseCustNew.getEnterpriseName()+"]在系统中无此法人代表").append("\n");
//                                failCount++;
//                                continue;
//                            }
//                        } 合版修改请自行修复





                        if(CommonUtils.isNotEmpty(enterpriseCustNew)){
                            enterpriseCustNew.setCreateId(ctx.getUserId());
                            enterpriseCustNew.setCreateTime(new Date());
                            enterpriseCustNew.setCompanyId(ctx.getCompanyId());
                            UnitNumber = String.valueOf(Integer.parseInt(UnitNumber) + 1);
                            enterpriseCustNew.setUnitNumber('C' + UnitNumber);//企业编号
                            newEnterpriseCustList.add(enterpriseCustNew);
                            successCount++;
                        }
                    }else{
                        error.append(enterpriseCustNew.getErrorMessage()).append("\n");
                        failCount++;
                    }

                }
                //编辑的校验
                map.put("error", error);
                map.put("newEnterpriseCustList", newEnterpriseCustList);
                map.put("successCount", successCount);
                map.put("failCount", failCount);
                return map;
            } catch (Exception e) {
                map.put("newEnterpriseCustList", new ArrayList<EnterpriseCustNew>());
                logger.info(CommonUtils.log(e.getMessage()));
                map.put("successCount", 0);
                map.put("failCount", enterpriseCustNewImportLists.size());
                map.put("error", error.append(e.getMessage()));
            }


            return map;

    }


    @Override
    protected ExcelDefinitionReader getExcelDefinition() {
        DefinitionFactory definitionReaderFactory = new XMLDefinitionFactory4commonImport("importExport/import/xml/enterprise_customer.xml");
        return definitionReaderFactory.createExcelDefinitionReader();
    }

    @Override
    protected String getLookupItemCodeByName(String lookupCode, String parentCode, String name) {
        WyBusinessContext ctx = WyBusinessContext.getContext();
        return DataDictionaryUtil.getLookupItemCodeByParentCodeAndName(ctx.getCompanyId(),parentCode,name);
    }
}
