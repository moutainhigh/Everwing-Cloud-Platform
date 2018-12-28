package com.everwing.coreservice.wy.core.service.impl.sys;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.enums.ImportExportEnum;
import com.everwing.coreservice.common.wy.entity.system.importExport.Excel;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysImportExportService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.task.CommonExportTask;
import com.everwing.coreservice.wy.core.utils.ImportExportUtils;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.myexcel.ExcelParameters;
import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.factory.DefinitionFactory;
import com.everwing.myexcel.factory.xml.XMLDefinitionFactory4commonExport;
import com.everwing.myexcel.resolver.poi.POIExcelResolver4commonExport;
import com.everwing.myexcel.result.ExcelExportResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * Function:excel业务实现类
 * Reason:目标：一次编写，到处复用
 * Date:2017/4/26
 * @author wusongti@lii.com.cn
 */
@Service("tSysImportExportServiceImpl")
public class TSysImportExportServiceImpl extends POIExcelResolver4commonExport implements TSysImportExportService {
    static Logger logger = LogManager.getLogger(TSysImportExportServiceImpl.class);

    @Autowired
    private ImportExportMapper importExportMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private FastDFSApi fastDFSApi;


    @Override
    public BaseDto listPage(WyBusinessContext ctx, TSysImportExportSearch condition) {
        List<TSysImportExportList> list = importExportMapper.listPage(condition);

        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(condition.getPage());
        return baseDto ;
    }

    @Override
    public List<TSysImportExportList> findByCondtion(String companyId, TSysImportExportSearch condition) {
        List<TSysImportExportList> list = importExportMapper.findByCondtion(condition);
        return list ;
    }


    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap batchInsert(String companyId, List<TSysImportExport> list) {
        MessageMap mm = new MessageMap();
        importExportMapper.batchInsert(list);
        return mm;
    }


    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap modify(TSysImportExport entity) {
        MessageMap mm = new MessageMap();
        importExportMapper.modify(entity);
        return mm;
    }

    
    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap modify(String companyId,TSysImportExport entity) {
        MessageMap mm = new MessageMap();
        importExportMapper.modify(entity);
        return mm;
    }
    

    @Override
    public MessageMap exportExcel(WyBusinessContext ctx, Excel excel) {
        MessageMap mm = new MessageMap();

        /**
         * 耗时多的业务使用异步
         */
        try {
            excelParameters = new ExcelParameters();
            excelParameters.setBatchNo(excel.getBatchNo());
            excelParameters.setParameters(excel.getParameters());
            excelParameters.setXmlName(excel.getXmlName());

            Runnable task = new CommonExportTask(ctx);
            threadPoolTaskExecutor.execute(task);
        } catch (Exception e) {
            logger.error("导出失败："+e);
            throw new ECPBusinessException("导出失败："+e.getMessage());
        }
        return mm;
    }

    @Override
    public void exportExcelCallback(WyBusinessContext ctx) {
        MessageMap mm = new MessageMap();
        mm.setFlag(MessageMap.INFOR_SUCCESS);

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
                ExcelExportResult excelExportResult = super.createWorkbook();
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
                        mm.setFlag(MessageMap.INFOR_ERROR);
                        mm.setMessage(remoteModelResult.getMsg());
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

    @Override
    protected ExcelDefinitionReader getExcelDefinition() {
        DefinitionFactory definitionReaderFactory = new XMLDefinitionFactory4commonExport("importExport/export/xml/" + excelParameters.getXmlName() + ".xml");
        return definitionReaderFactory.createExcelDefinitionReader();
    }


    /**
     * 根据参数查询要导出的数据
     * @param parseParameters
     * @return
     */
    @Override
    protected List<Map<String, Object>> findBySql(Map<String, Object> parseParameters) {
        return importExportMapper.findBySql(parseParameters);
    }

    @Override
    protected String getLookupItemNameByCode(String parentCode, String code) {
        WyBusinessContext ctx = WyBusinessContext.getContext();
        return DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),parentCode,code);
    }
}