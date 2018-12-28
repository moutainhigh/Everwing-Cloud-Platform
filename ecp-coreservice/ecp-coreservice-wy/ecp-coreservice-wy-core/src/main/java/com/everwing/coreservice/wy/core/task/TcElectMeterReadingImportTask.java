package com.everwing.coreservice.wy.core.task;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.service.business.meterdata.TcElectMeterReadingImportService;
import com.everwing.myexcel.resolver.ExcelResolver;
import com.everwing.myexcel.result.ExcelImportResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class TcElectMeterReadingImportTask  implements Callable{
	
	
	private String excelPath;
    private String batchNo;
    private static final Logger log = Logger.getLogger(TcElectMeterReadingImportTask.class);
	private WyBusinessContext ctx;
	private TcElectMeterReadingImportService tcElectMeterReadingImportService;
	private String taskId;//导入的抄表结果数据关联的任务id

    private ExcelResolver abstractExcel;
	
	private static String excelId = "elect_meter_reading";
	public TcElectMeterReadingImportTask(ExcelResolver abstractExcel, WyBusinessContext ctx, String batchNo, String excelPath, String taskId){
		this.ctx=ctx;
		this.batchNo=batchNo;
		this.excelPath = excelPath;
		this.taskId = taskId;
        tcElectMeterReadingImportService = SpringContextHolder.getBean("tcElectMeterReadingImportServiceImpl");
		this.abstractExcel = abstractExcel;
	}
	
	
	
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
         * 3.處理业务数据
         */
        Map<String, List<?>> listMap = excelImportResult.getListMap();
        List<TcMeterData> tcElectMeterReadinglist = (List<TcMeterData>)listMap.get("0"); // 获取第1个sheet里面的数据
        if(CollectionUtils.isEmpty(tcElectMeterReadinglist)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("找不到Excel数据呢，请检查一下模板数据是否按照规定格式设置");
        }else{
        	MessageMap messageMap  =tcElectMeterReadingImportService.electMeterReadingImport(ctx,tcElectMeterReadinglist,taskId);
        	mm = messageMap;
        }
		return mm;
	}
	
	
	

}
