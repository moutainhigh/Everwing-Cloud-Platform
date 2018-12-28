package com.everwing.coreservice.wy.core.service.impl.product;/**
 * Created by wust on 2018/1/15.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.wy.common.enums.ImportExportEnum;
import com.everwing.coreservice.common.wy.entity.system.importExport.Excel;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.service.common.ComplexReportExportService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.utils.ImportExportUtils;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.myexcel.ExcelParameters;
import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.factory.DefinitionFactory;
import com.everwing.myexcel.factory.xml.XMLDefinitionFactory4complexExport;
import com.everwing.myexcel.resolver.poi.POIExcelResolver4complexExport;
import com.everwing.myexcel.result.ExcelExportResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 *
 * Function:
 * Reason:每一个导出业务需要自己实现ComplexReportExportService接口，
 *        不用在这个类里面写你的代码，这个demo随时可能删除
 * Date:2018/1/15
 * @author wusongti@lii.com.cn
 */
@Service("complexReportExportDemoServiceImpl")
public class ComplexReportExportDemoServiceImpl extends POIExcelResolver4complexExport implements ComplexReportExportService {
    static Logger logger = LogManager.getLogger(ComplexReportExportDemoServiceImpl.class);

    @Autowired
    private ImportExportMapper importExportMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private FastDFSApi fastDFSApi;

    /**
     * 这个方法可以完全复制，不要修改
     * @param ctx
     * @param excel
     * @return
     */
    @Override
    public MessageMap exportFixedExcel(WyBusinessContext ctx, Excel excel) {
        MessageMap mm = new MessageMap();

        // 批次号，时间戳+5位随机数，按照概率，一毫秒发生重复的概率为（1/10 * 1/10 * 1/10 * 1/10 * 1/10） = 1/100000
        String batchNo = ImportExportEnum.Export.name().toUpperCase() + new DateTime().toString("yyyyMMddHHmmssSSS") + (new Random()).nextInt(9) + (new Random()).nextInt(9) + (new Random()).nextInt(9) + (new Random()).nextInt(9) + (new Random()).nextInt(9);
        TSysImportExport tSysImportExport = new TSysImportExport();
        tSysImportExport.setBatchNo(batchNo);
        tSysImportExport.setFileName(batchNo + "." + excel.getExcelVersion());
        tSysImportExport.setModuleDescription(excel.getModuleDescription());
        tSysImportExport.setFileSize("");
        tSysImportExport.setFileType(excel.getExcelVersion());
        tSysImportExport.setCreaterId(ctx.getUserId());
        tSysImportExport.setCreaterName(ctx.getStaffName());
        tSysImportExport.setStartTime(new Date());
        tSysImportExport.setOperationType(ImportExportEnum.Export.name());
        tSysImportExport.setStatus(ImportExportEnum.doing.name());
        List<TSysImportExport> listInsert = new ArrayList<>(1);
        listInsert.add(tSysImportExport);
        importExportMapper.batchInsert(listInsert);

        try {
            excelParameters = new ExcelParameters();
            excelParameters.setBatchNo(batchNo);
            excelParameters.setParameters(excel.getParameters());
            excelParameters.setXmlName(excel.getXmlName());
            Runnable task = new ComplexReportExportServiceImplTask();
            threadPoolTaskExecutor.execute(task);
        } catch (Exception e) {
            logger.error("导出失败："+e);
            throw new ECPBusinessException("导出失败："+e.getMessage());
        }
        return mm;
    }

    /**
     * 该方法体可以复制，不用修改
     * @return
     */
    @Override
    protected ExcelDefinitionReader getExcelDefinition() {
        DefinitionFactory definitionReaderFactory = new XMLDefinitionFactory4complexExport("importExport/export/xml/" + excelParameters.getXmlName() + ".xml");
        return definitionReaderFactory.createExcelDefinitionReader();
    }

    /**
     * 获取业务数据，你想要组装业务数据
     * @return
     */
    @Override
    protected Map<String, Object> getBusinessData() {
        Map<String, Object> datas = new HashMap<>(100);
        datas.put("companyName","深圳市翔恒科技开发有限公司");
        datas.put("projectName","一号工程");
        datas.put("statisticalInterval","2018-01-01 至 2018-12-31");
        datas.put("sk_cash_wy","200");
        datas.put("sk_cash_bt","200");
        datas.put("sk_cash_sf","200");
        datas.put("sk_cash_df","200");
        datas.put("sk_cash_jm","200");
        datas.put("sk_cash_fxhj","200");
        datas.put("sk_cash_zhj","200");

        datas.put("sk_charge_wy","200");
        datas.put("sk_charge_bt","200");
        datas.put("sk_charge_sf","200");
        datas.put("sk_charge_df","200");
        datas.put("sk_charge_jm","200");
        datas.put("sk_charge_fxhj","200");
        datas.put("sk_charge_zhj","200");

        datas.put("sk_bank_wy","200");
        datas.put("sk_bank_bt","200");
        datas.put("sk_bank_sf","200");
        datas.put("sk_bank_df","200");
        datas.put("sk_bank_jm","200");
        datas.put("sk_bank_fxhj","200");
        datas.put("sk_bank_zhj","200");

        datas.put("sk_ali_wy","200");
        datas.put("sk_ali_bt","200");
        datas.put("sk_ali_sf","200");
        datas.put("sk_ali_df","200");
        datas.put("sk_ali_jm","200");
        datas.put("sk_ali_fxhj","200");
        datas.put("sk_ali_zhj","200");

        datas.put("sk_weixin_wy","200");
        datas.put("sk_weixin_bt","200");
        datas.put("sk_weixin_sf","200");
        datas.put("sk_weixin_df","200");
        datas.put("sk_weixin_jm","200");
        datas.put("sk_weixin_fxhj","200");
        datas.put("sk_weixin_zhj","200");
        return datas;
    }

    /**
     * 该类可以复制，不用修改（除了类名）
     */
    class ComplexReportExportServiceImplTask implements Runnable{
        @Override
        public void run() {
            MessageMap mm = new MessageMap();
            TSysImportExportSearch tSysImportExportSearch = new TSysImportExportSearch();
            tSysImportExportSearch.setBatchNo(excelParameters.getBatchNo());
            List<TSysImportExportList> tSysImportExportLists =  importExportMapper.findByCondtion(tSysImportExportSearch);
            logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>开始导出，获取到的记录{}",tSysImportExportLists);
            if(CollectionUtils.isNotEmpty(tSysImportExportLists)){
                TSysImportExportList tSysImportExportList = tSysImportExportLists.get(0);

                File tempFile = null;
                FileOutputStream fos = null;
                try {
                    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>准备放到临时文件夹");
                    String tempFilePath = ImportExportUtils.createTempFile("attachment",tSysImportExportList.getBatchNo(),tSysImportExportList.getFileType());
                    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>准备放到临时文件夹，",tempFilePath);
                    tempFile = new File(tempFilePath);
                    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>放到临时文件夹，",tempFilePath);
                    excelParameters.setFileType(tSysImportExportList.getFileType());
                    ExcelExportResult excelExportResult = createWorkbook();
                    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>创建工作薄完成");
                    Workbook wb = excelExportResult.getWorkbook();
                    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>得到工作薄工作薄",wb!=null);
                    fos = new FileOutputStream(tempFile);
                    wb.write(fos);
                    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>写入文件流");
                } catch (Exception e) {
                    mm.setFlag(MessageMap.INFOR_ERROR);
                    mm.setMessage(e.getMessage());
                    logger.error(e);
                }finally {
                    if(fos != null){
                        try {
                            fos.close();
                        } catch (IOException e) {
                            mm.setFlag(MessageMap.INFOR_ERROR);
                            mm.setMessage(e.getMessage());
                        }
                    }

                    try {
                        logger.info("开始上传文件到文件服务器。。。。");
                        RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.uploadFile(tempFile);
                        if(remoteModelResult.isSuccess()){
                            UploadFile uploadFile = remoteModelResult.getModel();
                            if(uploadFile != null){
                                tSysImportExportList.setUploadFileId(uploadFile.getUploadFileId());
                                tSysImportExportList.setFileSize((uploadFile.getSize() / 1024) + "KB");
                            }
                        }else{
                            logger.error(remoteModelResult.getMsg());
                        }
                        logger.info("上传文件到文件服务器完成。。。。");
                    } catch (Exception e) {
                        mm.setFlag(MessageMap.INFOR_ERROR);
                        mm.setMessage(e.getMessage());
                        logger.error(e);
                    }finally {
                        // 修改导出状态
                       if(MessageMap.INFOR_SUCCESS.equals(mm.getFlag())){
                            tSysImportExportList.setStatus(ImportExportEnum.succeed.name());
                        }else{
                            tSysImportExportList.setStatus(ImportExportEnum.failed.name());
                        }
                        importExportMapper.modify(tSysImportExportList);

                        // 上传日志
                        ImportExportUtils.uploadMessage(importExportMapper,fastDFSApi,tSysImportExportList.getBatchNo(),mm.getMessage());

                        logger.info("导出完成。。。。");
                    }
                }
            }else {
                logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>根据批次号查不到记录{}",excelParameters.getBatchNo());
            }
        }
    }
}
