package com.everwing.coreservice.wy.core.service.impl.business.meterdata;/**
 * Created by wust on 2017/7/24.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.ImportExportEnum;
import com.everwing.coreservice.common.wy.common.enums.MeterEnum;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.service.business.meterdata.TcElectMeterReadingImportService;
import com.everwing.coreservice.common.wy.service.business.meterdata.TcMeterDataService;
import com.everwing.coreservice.common.wy.service.sys.TSysLookupService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.task.TcElectMeterReadingImportTask;
import com.everwing.coreservice.wy.dao.mapper.business.electmeter.TcElectMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingschedule.TcReadingScheduleMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingtask.TcReadingTaskMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterOperRecordMapper;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.coreservice.wy.dao.mapper.order.complaint.TcOrderComplaintMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysLookupMapper;
import com.everwing.myexcel.definition.ExcelDefinitionReader;
import com.everwing.myexcel.factory.DefinitionFactory;
import com.everwing.myexcel.factory.xml.XMLDefinitionFactory4commonImport;
import com.everwing.myexcel.resolver.poi.POIExcelResolver4commonImport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * Function:
 * Reason:
 * Date:2017/7/24
 * @author wusongti@lii.com.cn
 */
@Service("tcElectMeterReadingImportServiceImpl")
public class TcElectMeterReadingImportServiceImpl extends POIExcelResolver4commonImport implements TcElectMeterReadingImportService {
    private static final Logger log = Logger.getLogger(TcElectMeterReadingImportServiceImpl.class);

    @Autowired
    private ImportExportMapper importExportMapper;

    @Autowired
    private TSysLookupMapper tSysLookupMapper;

    private WyBusinessContext ctx;

    @Autowired
    private TcMeterDataMapper tcMeterDataMapper;

    @Autowired
    private TcWaterMeterOperRecordMapper tcWaterMeterOperRecordMapper;

    @Autowired
    private TcReadingTaskMapper tcReadingTaskMapper;

    @Autowired
    private TcReadingScheduleMapper tcReadingScheduleMapper;

    @Autowired
    private TcWaterMeterMapper tcWaterMeterMapper;

    @Autowired
    private TcElectMeterMapper tcElectMeterMapper;

    @Autowired
    private TcOrderComplaintMapper tcOrderComplaintMapper;

    @Autowired
    private TcMeterDataService tcMeterDataService;

    @Autowired
    private TSysLookupService tSysLookupService;
    
    @Autowired
    private FastDFSApi fastDFSApi;
    

    public MessageMap importElectMeterReading(WyBusinessContext ctx,String batchNo,String excelPath,String taskId){
        this.ctx = ctx;
        MessageMap mm = new MessageMap();

        
      //采用分布式文件服务器方式来做
        //通过batchNo查询uploadFileId信息作为参数传递给文件服务器
        TSysImportExportList tSysImportExportListExist = null;
        TSysImportExportSearch condition = new TSysImportExportSearch();
        condition.setBatchNo(batchNo);
        List<TSysImportExportList> tSysImportExportListList = importExportMapper.findByCondtion(condition);
        if(CollectionUtils.isNotEmpty(tSysImportExportListList)){
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
            Callable task = new TcElectMeterReadingImportTask(this,ctx,batchNo,excelPath,taskId);
            Future<MessageMap> messageMapFuture = executorService.submit(task);
            mm = messageMapFuture.get();
        } catch (Exception e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(e.getMessage());
        }
        executorService.shutdown();
        return mm;
    }


    public MessageMap electMeterReadingImport(WyBusinessContext ctx,List<TcMeterData> tcElectMeterReadinglist,String taskId){
        this.ctx = ctx;
        MessageMap messageMap = new MessageMap();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        List<TcMeterData> newUpdate = new ArrayList<TcMeterData>();
        StringBuffer error = new StringBuffer();
        try {
            paramMap.put("taskId", taskId);
            paramMap.put("meterType", MeterEnum.RECORD_METERTYPE_ELECT.getIntValue());
            if(tcElectMeterReadinglist.size()>500){
                int num=tcElectMeterReadinglist.size()/500;
                for(int i=0;i<num;i++){
                    //分批查询出数据信息
                    List<TcMeterData> bachList=new ArrayList<>();
                    if(i==num){
                        paramMap.put("list", tcElectMeterReadinglist.subList(i*500,tcElectMeterReadinglist.size() ));//一次500条
                    }else{
                        paramMap.put("list", tcElectMeterReadinglist.subList(i*500, (i+1)*500));//一次500条
                    }
                    bachList=tcMeterDataMapper.getMeterDataForImport(paramMap);//完整的抄表数据
                    newUpdate.addAll(bachList);
                }
            }else{
                paramMap.put("list", tcElectMeterReadinglist);
                newUpdate=tcMeterDataMapper.getMeterDataForImport(paramMap);//完整的抄表数据
            }
            String  remark="";
            for (TcMeterData tcMeterData : newUpdate) {
                for (TcMeterData tcM : tcElectMeterReadinglist) {
                    //水表编号相同的获取excel中最新的本次读数
                    if(tcM.getMeterCode().equals(tcMeterData.getMeterCode())){
                        remark = tcM.getRemark();
                        if(CommonUtils.isNotEmpty(remark)&&remark.length()>100){ //备注只能存放
                            error.append("表编号为:["+tcM.getMeterCode()+"]的备注最多只能填写100个字符");
                            break;
                        }
                        tcMeterData.setTotalReading(tcM.getTotalReading());
                        tcMeterData.setPeakReading(tcM.getPeakReading());
                        tcMeterData.setVallyReading(tcM.getVallyReading());
                        tcMeterData.setCommonReading(tcM.getCommonReading());
                        tcMeterData.setRemark(remark);
                        break;
                    }
                }
            }
            int badInfo=tcElectMeterReadinglist.size()-newUpdate.size();//未能在数据库查询到的数据条数
            BaseDto baseDto= tcMeterDataService.batchModify(ctx.getCompanyId(),newUpdate,"1");
            if(badInfo==0){//
                messageMap.setFlag(baseDto.getMessageMap().getFlag());
                messageMap.setMessage(baseDto.getMessageMap().getMessage());
                if(baseDto.getMessageMap().getFlag().equals(MessageMap.INFOR_SUCCESS)){
                    messageMap.setObj(ImportExportEnum.succeed.name());
                }
                if(baseDto.getMessageMap().getFlag().equals(MessageMap.INFOR_WARNING)){
                    messageMap.setObj(ImportExportEnum.partial_success.name());
                }
                if(baseDto.getMessageMap().getFlag().equals(MessageMap.INFOR_ERROR)){
                    messageMap.setObj(ImportExportEnum.failed.name());
                }

            }
            if(newUpdate.size()==0){ //全部失败
                messageMap.setFlag(MessageMap.INFOR_ERROR);
                messageMap.setMessage("导入失败!错误:可能所有导入的数据不存在,可能任务还没有产生;请耐心等待或者["+error.toString()+"]失败了["+tcElectMeterReadinglist.size()+"]条");
                messageMap.setObj(ImportExportEnum.failed.name());
            }
            if(newUpdate.size()>0 && newUpdate.size()<tcElectMeterReadinglist.size()){//部分成功
                messageMap.setFlag(MessageMap.INFOR_WARNING);
                messageMap.setMessage("导入部分成功;有部分数据不存在，可能已经被删除或者["+error.toString()+"]成功了["+newUpdate.size()+"]条;失败了["+(tcElectMeterReadinglist.size()-newUpdate.size())+"]条");

            }

        } catch (Exception e) {
            log.info(e.getMessage());
            messageMap.setFlag(MessageMap.INFOR_ERROR);
            messageMap.setMessage("导入失败");
        }
        return messageMap;
    }


    @Override
    protected ExcelDefinitionReader getExcelDefinition() {
        DefinitionFactory definitionReaderFactory = new XMLDefinitionFactory4commonImport("importExport/import/xml/elect_meter_reading.xml");
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
}
