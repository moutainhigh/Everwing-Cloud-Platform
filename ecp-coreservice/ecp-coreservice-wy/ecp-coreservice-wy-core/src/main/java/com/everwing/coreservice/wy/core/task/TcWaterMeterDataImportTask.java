package com.everwing.coreservice.wy.core.task;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.service.business.meterdata.TcWaterMeterReadingImportService;
import com.everwing.myexcel.resolver.ExcelResolver;
import com.everwing.myexcel.result.ExcelImportResult;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


/**
 * @descrbe 水表抄表结果数据导入操作
 * @author QHC
 *
 */
public class TcWaterMeterDataImportTask implements Callable{
	
    private WyBusinessContext ctx;

    private String batchNo;
    
	private String excelPath;
	
	private static String excelId = "water_meter_data";
	
	private TcWaterMeterReadingImportService tcWaterMeterReadingImportService;
	
	private String taskId;//导入的抄表结果数据关联的任务id

    private ExcelResolver abstractExcel;

    public TcWaterMeterDataImportTask(ExcelResolver abstractExcel, WyBusinessContext ctx, String batchNo, String excelPath, String taskId){
        this.ctx = ctx;
        this.batchNo = batchNo;
        this.excelPath = excelPath;
        this.taskId=taskId;
        tcWaterMeterReadingImportService=SpringContextHolder.getBean("tcWaterMeterReadingImportServiceImpl");
        this.abstractExcel = abstractExcel;
    }
	   
	
	@Override
	public MessageMap call() throws Exception {
		
		MessageMap mm = new MessageMap();


        /**
         * 2.从输入流中得到业务数据
         */
        ExcelImportResult excelImportResult = null;
        try {
            excelImportResult = this.abstractExcel.readExcel();
        } catch (ECPBusinessException e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(e.getMessage());
            throw e;
        }
        
        /**
         * 3.处理业务数据
         */
        Map<String, List<?>> listMap = excelImportResult.getListMap();
        List<TcMeterData> tcWaterMeterDatalist = (List<TcMeterData>)listMap.get("0"); // 获取第1个sheet里面的数据
        if(CollectionUtils.isEmpty(tcWaterMeterDatalist)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("找不到Excel数据呢，请检查一下模板数据是否按照规定格式设置");
        }else{

        	MessageMap msg= tcWaterMeterReadingImportService.importWaterMeterReading(ctx,tcWaterMeterDatalist,taskId);
        	RemoteModelResult<MessageMap> remoteModelResult =new RemoteModelResult<MessageMap>(msg);
        	if (remoteModelResult.isSuccess()){
                mm = remoteModelResult.getModel();
            }else{
                mm.setFlag(MessageMap.INFOR_ERROR);
                mm.setMessage(remoteModelResult.getMsg());
            }
        }
		return mm;
	}

}
