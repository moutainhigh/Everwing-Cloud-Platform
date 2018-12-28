package com.everwing.coreservice.wy.core.service.impl.business.meterdata;/**
 * Created by wust on 2017/7/24.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.service.business.meterdata.TcWaterMeterReadingImportService;
import com.everwing.coreservice.common.wy.service.sys.TSysLookupService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.core.task.TcWaterMeterDataImportTask;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingtask.TcReadingTaskMapper;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
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
import java.util.*;
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
@Service("tcWaterMeterReadingImportServiceImpl")
public class TcWaterMeterReadingImportServiceImpl extends POIExcelResolver4commonImport implements TcWaterMeterReadingImportService {
    private static final Logger log = Logger.getLogger(TcWaterMeterReadingImportServiceImpl.class);


    private WyBusinessContext ctx;

    @Autowired
    private TcMeterDataMapper tcMeterDataMapper;



    @Autowired
    private TcReadingTaskMapper tcReadingTaskMapper;

    @Autowired
    private ImportExportMapper importExportMapper;

    @Autowired
    private TSysLookupService tSysLookupService;
    
    @Autowired
    private FastDFSApi fastDFSApi;



    public MessageMap importWaterMeterReading(WyBusinessContext ctx, String batchNo, String excelPath, String taskId) {
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
        // 你只需要实现这个类即可

        try {

            Callable task = new TcWaterMeterDataImportTask(this,ctx,batchNo,excelPath,taskId);
            Future<MessageMap> messageMapFuture = executorService.submit(task);
            mm = messageMapFuture.get();
        } catch (Exception e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(e.getMessage());
        }
        executorService.shutdown();

        while (true) {
            if (executorService.isTerminated()) {
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {

            }
        }

        executorService.shutdown();
        return mm;
    }


    /**
     * 抄表结果数据的导入主要是做update操作
     * 注意 问题：1.在未被审核前可填写和修改抄表结果   状态为：0
     *
     * @param tcWaterMeterDatalist
     * @return
     */
    public MessageMap importWaterMeterReading(WyBusinessContext ctx, List<TcMeterData> tcWaterMeterDatalist, String taskId){
        this.ctx = ctx;
        if(CommonUtils.isEmpty(tcWaterMeterDatalist)){
            return new MessageMap(MessageMap.INFOR_ERROR,"导入参数信息为空");
        }

        List<TcMeterData> dataList=new ArrayList<>();
        Map<String, Object> paraMap=new HashMap<>();
        paraMap.put("taskId", taskId);
        if(tcWaterMeterDatalist.size()>500){
            int num=tcWaterMeterDatalist.size()/500;
            for(int i=0;i<num;i++){
                //分批查询出数据信息
                List<TcMeterData> bachList=new ArrayList<>();
                if(i==num){
                    paraMap.put("list", tcWaterMeterDatalist.subList(i*500,tcWaterMeterDatalist.size() ));//一次500条
                }else{
                    paraMap.put("list", tcWaterMeterDatalist.subList(i*500, (i+1)*500));//一次500条
                }
                bachList=tcMeterDataMapper.getMeterDataForImport(paraMap);//完整的抄表数据（未审核）
                dataList.addAll(bachList);
            }
        }else{
            paraMap.put("list", tcWaterMeterDatalist);
            dataList=tcMeterDataMapper.getMeterDataForImport(paraMap);//完整的抄表数据
        }

        for (TcMeterData tcMeterData : dataList) {
            //单条处理
            for (TcMeterData tcM : tcWaterMeterDatalist) {
                //水表编号相同的获取excel中最新的本次读数
                if(tcM.getMeterCode().equals(tcMeterData.getMeterCode())){
                    tcMeterData.setTotalReading(tcM.getTotalReading());
                    break;
                }
            }

        }

        TcReadingTask task = this.tcReadingTaskMapper.getWaterTaskDetailById(taskId);	//获取此次任务对象
        //tcWaterMeterDatalist  导入excel中存在的抄表数据信息     dataList 查询出符合导入批量修改读数的数据信息
        if(CommonUtils.isEmpty(dataList)){
            return new MessageMap(MessageMap.INFOR_ERROR,"导入数据不符合要求");
        }else{
            int trueCount = 0;
            boolean flag = true;
            for(TcMeterData data : dataList){
                if(CommonUtils.isEmpty(data.getTotalReading())){
                    continue;
                }
                if(data.getTotalReading() != 0){  //待审批状态,且读数发生改变
                    trueCount ++ ;
                    data.setReadingTime(new Date());
                    //计算本次用量
                    if(1 == data.getIsReplaced()){
                        //若已经换过表 ,判断当前传入的数值,与数据库内的数值大小
                        TcMeterData beforeData = this.tcMeterDataMapper.getDataById(data.getId());
                        //当上次读数为空,即尚未读数时, 初始化的用量即为原用量
                        double initUseCount = (0 == beforeData.getTotalReading()) ?
                                beforeData.getUseCount() : beforeData.getUseCount() - (beforeData.getTotalReading() - beforeData.getLastTotalReading());
                        data.setUseCount(initUseCount + (data.getTotalReading() - data.getLastTotalReading()));
                        if(initUseCount > data.getUseCount()){
                            continue;
                        }
                    }else{
                        data.setUseCount(data.getTotalReading() - data.getLastTotalReading());
                    }
                    this.tcMeterDataMapper.modify(data);
                }
            }
        }

        //修改该任务的读数
        Map<String,Long> countMap = this.tcReadingTaskMapper.getReadedMetersCount(task.getId());
        int readedCount = (CommonUtils.isEmpty(countMap)) ? 0 : countMap.get("COUNT").intValue();
        task.setCompleteMeterCount(readedCount);
        task.setRemainMeterCount(task.getTotalMeterCount() - readedCount);
        this.tcReadingTaskMapper.update(task);

        int badInfo=tcWaterMeterDatalist.size()-dataList.size();//未能在数据库查询到的数据条数

//		BaseDto baseDto= batchModify(null,dataList,"0");

        MessageMap msg=new MessageMap();
        StringBuilder info=new StringBuilder();
        /*if(badInfo == 0) {
        	msg.setFlag(MessageMap.INFOR_SUCCESS);
        	msg.setMessage("倒入操作成功！"+info.toString());
        }else */
        if( badInfo>=0 && badInfo<tcWaterMeterDatalist.size()){
        	msg.setFlag(MessageMap.INFOR_SUCCESS);
        	if(badInfo == 0) {
        		msg.setMessage("倒入操作成功！"+info.toString());
        	}else {
        		info.append("有"+badInfo+"条导入的数据不符合要求，水表编号找不到");
        		msg.setMessage(info.toString());
        	}
            
        }else {
        	msg.setFlag(MessageMap.INFOR_ERROR);
        	info.append("导入失败！");
        	msg.setMessage(info.toString());
        }

        return msg;
    }

    @Override
    protected ExcelDefinitionReader getExcelDefinition() {
        DefinitionFactory definitionReaderFactory = new XMLDefinitionFactory4commonImport("importExport/import/xml/water_meter_data.xml");
        return definitionReaderFactory.createExcelDefinitionReader();
    }

    @Override
    protected String getLookupItemCodeByName(String lookupCode, String parentCode, String name) {
        WyBusinessContext ctx = WyBusinessContext.getContext();
        return DataDictionaryUtil.getLookupItemCodeByParentCodeAndName(ctx.getCompanyId(),parentCode,name);
    }
}
