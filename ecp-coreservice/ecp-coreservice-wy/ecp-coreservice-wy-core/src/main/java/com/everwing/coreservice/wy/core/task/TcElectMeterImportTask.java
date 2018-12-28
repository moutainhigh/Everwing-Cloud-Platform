package com.everwing.coreservice.wy.core.task;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.wy.common.enums.ImportExportEnum;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeterImport;
import com.everwing.coreservice.common.wy.service.business.electmeter.TcElectMeterImportService;
import com.everwing.myexcel.resolver.ExcelResolver;
import com.everwing.myexcel.result.ExcelImportResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


public class TcElectMeterImportTask implements Callable{

	   private WyBusinessContext ctx;
       private String excelPath;
       private String batchNo;
       private TcElectMeterImportService tcElectMeterImportService;
       private static final Logger log = Logger.getLogger(TcElectMeterImportTask.class);

	private ExcelResolver abstractExcel;

    // excelId一定要对应basedata_building.xml里面excel节点的id值
	   private static String excelId = "elect_meter";
	   public TcElectMeterImportTask(ExcelResolver abstractExcel, WyBusinessContext ctx, String batchNo, String excelPath){
//	        tcElectMeterMapper=((TcElectMeterMapper)SpringContextHolder.getBean("tcElectMeterMapper"));
//	        projectMapper=((ProjectMapper)SpringContextHolder.getBean("projectMapper"));
//	        tcBuildingMapper=((TcBuildingMapper)SpringContextHolder.getBean("tcBuildingMapper"));
//	        staffMapper=((StaffMapper)SpringContextHolder.getBean("staffMapper"));
		   	
	        this.ctx = ctx;
	        this.excelPath = excelPath;
	        this.batchNo = batchNo;
//	        Map<String,TcElectMeterServiceImpl> map = SpringContextHolder.getBeanList(TcElectMeterService.class);
//	        tcElectMeterServiceImpl = (map.get("tcElectMeterServiceImpl"));
		   tcElectMeterImportService = SpringContextHolder.getBean("tcElectMeterImportServiceImpl");

		   this.abstractExcel = abstractExcel;
	    }

 


	@Override
	public MessageMap call() throws ECPBusinessException {
		
		MessageMap mm = new MessageMap();

        /**
         * 1.从输入流中得到业务数据
         */
        ExcelImportResult excelImportResult = null;
        try {
            excelImportResult = this.abstractExcel.readExcel();
        } catch (ECPBusinessException e) {
            log.info(CommonUtils.log(e.getMessage()));
            throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
        }
        
        /**
         * 2.處理业务数据
         */
        Map<String, List<?>> listMap = excelImportResult.getListMap();
        List<ElectMeterImport> tcElectMeterlist = (List<ElectMeterImport>)listMap.get("0"); // 获取第1个sheet里面的数据
        if(CollectionUtils.isEmpty(tcElectMeterlist)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("找不到Excel数据呢，请检查一下模板数据是否按照规定格式设置");
        }else{
        	try {
        		MessageMap returnMM = tcElectMeterImportService.importElect(ctx,tcElectMeterlist);
            	if(returnMM.getFlag().equals(MessageMap.INFOR_WARNING)){
            		mm.setFlag(MessageMap.INFOR_WARNING);
            		mm.setMessage(returnMM.getMessage());
            		mm.setObj(ImportExportEnum.partial_success.name());
            	}
            	if(returnMM.getFlag().equals(MessageMap.INFOR_SUCCESS)){
            		mm.setFlag(MessageMap.INFOR_SUCCESS);
            		mm.setMessage(returnMM.getMessage());
            		mm.setObj(ImportExportEnum.succeed.name());
            	}
            	if(returnMM.getFlag().equals(MessageMap.INFOR_ERROR)){
            		mm.setFlag(MessageMap.INFOR_ERROR);
            		mm.setMessage(returnMM.getMessage());
            		mm.setObj(ImportExportEnum.failed.name());
            	}
			} catch (Exception e) {
				log.info(CommonUtils.log(e.getMessage()));
				throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
			}
        
        }
		return mm;
	}
	

}
