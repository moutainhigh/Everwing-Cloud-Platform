package com.everwing.coreservice.wy.core.task;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.importEntity.TBsAssetAccountImportBean;
import com.everwing.coreservice.common.wy.service.configuration.tbcassetacount.TBsAssetAccountServie;
import com.everwing.myexcel.resolver.ExcelResolver;
import com.everwing.myexcel.result.ExcelImportResult;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@SuppressWarnings("rawtypes")
public class TBsAssetAccountImportTask implements Callable {

	private WyBusinessContext ctx;

	private String batchNo;

	private String excelPath;

	private static String excelId = "water_meter";

	private TBsAssetAccountServie tBsAssetAccountServie;
	
	private String oprUserId;

	private ExcelResolver abstractExcel;

	public TBsAssetAccountImportTask(ExcelResolver abstractExcel,WyBusinessContext ctx, String batchNo, String excelPath, String oprUserId) {
		this.ctx = ctx;
		this.batchNo = batchNo;
		this.excelPath = excelPath;
		tBsAssetAccountServie = SpringContextHolder.getBean("tBsAssetAccountServieImpl");
		this.abstractExcel = abstractExcel;
		this.oprUserId = oprUserId;
	}

	@SuppressWarnings("unchecked")
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
        List<TBsAssetAccountImportBean> accountImportBeans = (List<TBsAssetAccountImportBean>)listMap.get("0"); // 获取第1个sheet里面的数据
        if(CollectionUtils.isEmpty(accountImportBeans)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("找不到Excel数据呢，请检查一下模板数据是否按照规定格式设置");
        }else{
        	MessageMap msg= tBsAssetAccountServie.importDatas(ctx, batchNo, accountImportBeans, oprUserId);
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
