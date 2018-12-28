package com.everwing.coreservice.wy.core.service.impl.business.pay;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfo;
import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfoImport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.service.business.pay.TBspayInfoImportService;
import com.everwing.coreservice.common.wy.service.sys.TSysLookupService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.factory.DefinitionFactory;
import com.everwing.myexcel.factory.xml.XMLDefinitionFactory4commonImport;
import com.everwing.myexcel.resolver.poi.POIExcelResolver4commonImport;
import com.everwing.myexcel.result.ExcelImportResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TBsPayInfoSericeImportlmpl  extends POIExcelResolver4commonImport implements TBspayInfoImportService   {

    static Logger logger = LogManager.getLogger(TBsPayInfoSericeImportlmpl.class);

    @Autowired
    private FastDFSApi fastDFSApi;
    @Autowired
    private ImportExportMapper importExportMapper;
    @Autowired
    private TcBuildingMapper tcBuildingMapper;
    @Autowired
    private TSysLookupService tSysLookupService;
    @Override
    public void importTcbuilding(TSysImportExportSearch tSysImportExportRequest, String singleStr, String isNotSkipLateFee) {
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

    }

    @Override
    public List<TBsPayInfo> importTcbuildingCallback( TSysImportExportSearch tSysImportExportRequest) {
//        MessageMap mm = new MessageMap();
//        List<TBsPayInfoImport> tBsPayInfoImportsListInsert = new ArrayList<>();
//
//        List<TBsPayInfoImport> tBsPayInfoImportsListModify = new ArrayList<>();
        ExcelImportResult excelImportResult = null;
        try{

            /**
             * 1.读取excel数据
             */
            excelImportResult = super.readExcel();

            /**
             * 2.处理业务数据
             */
            Map<String, List<?>> listMap = excelImportResult.getListMap();
           List<TBsPayInfoImport> tBsPayInfoImports= (List<TBsPayInfoImport>)listMap.get(0);
            List<TBsPayInfo> tBsPayInfoList =new ArrayList<>();
           for (TBsPayInfoImport tBsPayInfoImport:tBsPayInfoImports){
               TBsPayInfo tBsPayInfo=new TBsPayInfo();
//                String buildingCode=tcBuildingMapper.findHouseCodeBYBuildingCode(tBsPayInfoImport.getHouseCode());
//                tBsPayInfo.setBuildingCode(buildingCode);  合版删除，请修复
                tBsPayInfo.setWyAmount(tBsPayInfoImport.getPayWy());
                tBsPayInfo.setWaterAmount(tBsPayInfoImport.getPayWater());
                tBsPayInfo.setBtAmount(tBsPayInfoImport.getPayBt());
                tBsPayInfo.setElectAmount(tBsPayInfoImport.getPayele());
                tBsPayInfo.setPayType(1);
                tBsPayInfoList.add(tBsPayInfo);

           }
            return tBsPayInfoList;
        }catch (Exception e){
            e.printStackTrace();

        }
        return null;

    }


    @Override
    protected ExcelDefinitionReader getExcelDefinition() {
        String xmlFullPath = "importExport/import/xml/finace_payinfo.xml";
        DefinitionFactory definitionReaderFactory = new XMLDefinitionFactory4commonImport(xmlFullPath);
        return definitionReaderFactory.createExcelDefinitionReader();
    }

    @Override
    protected String getLookupItemCodeByName(String lookupCode, String parentCode, String name) {
        return null;
    }

}
