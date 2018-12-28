package com.everwing.coreservice.wy.core.task;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.service.business.watermeter.TcWaterMeterImportService;
import com.everwing.myexcel.resolver.ExcelResolver;
import com.everwing.myexcel.result.ExcelImportResult;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


/**
 * @descrbe 水表基础数据执行导入操作时，实现模板
 * @author QHC
 *
 */
public class TcWaterMeterImportTask implements Callable{
	
    private WyBusinessContext ctx;

    private String batchNo;
    
	private String excelPath;
	
	private static String excelId = "water_meter";
	
	private TcWaterMeterImportService tcWaterMeterImportService;

    private ExcelResolver abstractExcel;
	

    public TcWaterMeterImportTask(ExcelResolver abstractExcel, WyBusinessContext ctx, String batchNo, String excelPath){
        this.ctx = ctx;
        this.batchNo = batchNo;
        this.excelPath = excelPath;
        tcWaterMeterImportService=SpringContextHolder.getBean("tcWaterMeterImportServiceImpl");
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
        List<TcWaterMeter> tcWaterMeterlist = (List<TcWaterMeter>)listMap.get("0"); // 获取第1个sheet里面的数据
        if(CollectionUtils.isEmpty(tcWaterMeterlist)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("找不到Excel数据呢，请检查一下模板数据是否按照规定格式设置");
        }else{
        	MessageMap msg= tcWaterMeterImportService.importWaterMeterInfo(ctx, batchNo, tcWaterMeterlist);
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
