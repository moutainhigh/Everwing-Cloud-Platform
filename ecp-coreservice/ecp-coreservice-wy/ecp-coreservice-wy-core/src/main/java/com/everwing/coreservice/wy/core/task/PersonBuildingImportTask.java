package com.everwing.coreservice.wy.core.task;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingImportBean;
import com.everwing.coreservice.common.wy.service.personbuilding.PersonbuildingService;
import com.everwing.myexcel.resolver.ExcelResolver;
import com.everwing.myexcel.result.ExcelImportResult;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class PersonBuildingImportTask implements Callable{
	
	private WyBusinessContext ctx;

	private String batchNo;

	private String excelPath;

	private static String excelId = "person_building";

	private PersonbuildingService personbuildingService;
	
	private String projectId;

	private ExcelResolver abstractExcel;

	public PersonBuildingImportTask(ExcelResolver abstractExcel, WyBusinessContext ctx, String batchNo, String excelPath,String projectId) {
		this.ctx = ctx;
		this.batchNo = batchNo;
		this.excelPath = excelPath;
		this.abstractExcel = abstractExcel;
		this.projectId = projectId;
		this.personbuildingService = SpringContextHolder.getBean("personbuildingService");
	}

	@Override
	public Object call() throws Exception {

		//导入老数据
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
        List<PersonBuildingImportBean> pbImportBeans = (List<PersonBuildingImportBean>)listMap.get("0"); // 获取第1个sheet里面的数据
        if(CollectionUtils.isEmpty(pbImportBeans)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("找不到Excel数据呢，请检查一下模板数据是否按照规定格式设置");
        }else{
        	MessageMap msg= personbuildingService.importData(ctx, batchNo, pbImportBeans, projectId);
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
