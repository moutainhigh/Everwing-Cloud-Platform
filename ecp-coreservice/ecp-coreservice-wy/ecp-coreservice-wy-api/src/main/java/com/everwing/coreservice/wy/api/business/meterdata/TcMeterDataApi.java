package com.everwing.coreservice.wy.api.business.meterdata;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.service.business.meterdata.TcElectMeterReadingImportService;
import com.everwing.coreservice.common.wy.service.business.meterdata.TcMeterDataService;
import com.everwing.coreservice.common.wy.service.business.meterdata.TcWaterMeterReadingImportService;
import com.everwing.coreservice.common.wy.service.quartz.business.MeterDataTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TcMeterDataApi {

	
	@Autowired
	private TcMeterDataService tcMeterDataService;
	
	@Autowired
	private MeterDataTaskService meterDataTaskService;

	@Autowired
	private TcWaterMeterReadingImportService tcWaterMeterReadingImportService;

	@Autowired
	private TcElectMeterReadingImportService tcElectMeterReadingImportService;
	
	public RemoteModelResult<BaseDto> listPageDatas(String companyId,TcMeterData tcMeterData) {
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.listPageDatas(companyId,tcMeterData));
	}

	public RemoteModelResult<BaseDto> batchModify(String companyId, List<TcMeterData> datas,String operationType) {
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.batchModify(companyId,datas,operationType));
	}

	public RemoteModelResult<BaseDto> listPageDatasToAudit(String companyId,TcMeterData data) {
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.listPageDatasToAudit(companyId,data));
	}

	public RemoteModelResult<BaseDto> batchAudit(String companyId,List<TcMeterData> datas,Integer auditStatus) {
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.batchAudit(companyId,datas,auditStatus));
	}

	
	public RemoteModelResult<MessageMap> importElectMeterReading(WyBusinessContext ctx, String batchNo, String excelPath, int type, String taskId){
		if(0 == type){
			return new RemoteModelResult<MessageMap>(this.tcWaterMeterReadingImportService.importWaterMeterReading(ctx, batchNo, excelPath,taskId));
		}else{
			return new RemoteModelResult<MessageMap>(this.tcElectMeterReadingImportService.importElectMeterReading(ctx, batchNo, excelPath,taskId));
		}
	}

	
	public RemoteModelResult<BaseDto> listPageReadingRecords(String companyId,TcMeterData tcMeterData) {
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.listPageReadingRecords(companyId,tcMeterData));
	}
	
	public RemoteModelResult<BaseDto> modifyReading(String companyId,TcMeterData data) {
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.modifyReading(companyId,data));
	}
	
	
	public RemoteModelResult<BaseDto> modifyReadingNew(String companyId,TcMeterData data) {
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.modifyReadingNew(companyId,data));
	}
	
	
	public RemoteModelResult<BaseDto> listPageHistories(WyBusinessContext ctx,TcMeterData tcMeterData){
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.listPageHistories(ctx, tcMeterData));
	}

	public RemoteModelResult<BaseDto> findByBuildingCode(String companyId,String code) {
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.findByBuildingCode(companyId, code));
	}
	
	public RemoteModelResult<BaseDto> selectAbnormalData(String companyId,TcMeterData meterData) {
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.selectAbnormalData(companyId,meterData));
	}
	
	public RemoteModelResult<BaseDto> selectAbnormalElectData(String companyId,TcMeterData meterData){
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.selectAbnormalElectData(companyId, meterData));
	}
	
	public RemoteModelResult<BaseDto> getReadingInfoByYear(String companyId,Map<String, String> paramMap) {
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.getReadingInfoByYear(companyId,paramMap));
	}
	
	public RemoteModelResult<BaseDto> listPageReadingInfoByYear(String companyId,TcMeterData tcMeterData,int serviceType) {
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.listPageReadingInfoByYear(companyId,tcMeterData,serviceType));
	}

	public RemoteModelResult<BaseDto> findByTaskIdAndBuildingCode(String companyId, String code, String taskId) {
		return new RemoteModelResult<BaseDto>(this.tcMeterDataService.findByTaskIdAndBuildingCode(companyId,code,taskId));
	}

	public RemoteModelResult<BaseDto> autoCompleteTask(String companyId,String companyStr) {
		return new RemoteModelResult<BaseDto>(this.meterDataTaskService.autoCompleteTask(companyId,companyStr));
	}

    /**------------wyapp---------------*/
	public RemoteModelResult<List<TcMeterData>> queryByTaskIds(String companyId,List<String> taskIds,int meterType) {
        RemoteModelResult<List<TcMeterData>> remoteModelResult = new RemoteModelResult<List<TcMeterData>>();
        if(taskIds==null||taskIds.size()==0){
        	remoteModelResult.setCode(MessageMap.INFOR_ERROR);
        	remoteModelResult.setMsg("暂无任务数据!");
        	return remoteModelResult;
		}
        remoteModelResult.setModel(tcMeterDataService.queryByTaskIds(companyId,taskIds,meterType));
        return remoteModelResult;
    }

    public RemoteModelResult<List<TcMeterData>> queryByTaskId(String companyId,String taskId,int meterType) {
        RemoteModelResult<List<TcMeterData>> remoteModelResult = new RemoteModelResult<List<TcMeterData>>();
        remoteModelResult.setModel(tcMeterDataService.queryByTaskId(companyId,taskId,meterType));
        return remoteModelResult;
    }

    public RemoteModelResult<List<TcMeterData>> queryByDescription(String companyId,String description,int meterType) {
        RemoteModelResult<List<TcMeterData>> remoteModelResult = new RemoteModelResult<List<TcMeterData>>();
        remoteModelResult.setModel(tcMeterDataService.queryByDescription(companyId,description,meterType));
        return remoteModelResult;
    }

    public RemoteModelResult<List<TcMeterData>> queryYearData(String companyId,String meterCode,int meterType,String year) {
        RemoteModelResult<List<TcMeterData>> remoteModelResult = new RemoteModelResult<List<TcMeterData>>();
        remoteModelResult.setModel(tcMeterDataService.queryYearData(companyId,meterCode,meterType,year));
        return remoteModelResult;
    }

    public RemoteModelResult<Integer> modifyWMeterData(String companyId,String taskId,String accountId,String meterCode,String totalReading,String fileId) {
        RemoteModelResult<Integer> remoteModelResult = new RemoteModelResult<Integer>();
        remoteModelResult.setModel(tcMeterDataService.modifyWMeterData(companyId,taskId,accountId,meterCode,totalReading,fileId));
        return remoteModelResult;
    }

    public RemoteModelResult<Integer> modifyEMeterData(String companyId,String taskId,String accountId,String meterCode,String totalReading,
                                                       String peakrReading,String vallyReading,String commonReading, String fileId) {
        RemoteModelResult<Integer> remoteModelResult = new RemoteModelResult<Integer>();
        remoteModelResult.setModel(tcMeterDataService.modifyEMeterData(companyId,taskId,accountId,meterCode,totalReading,peakrReading,vallyReading,commonReading,fileId));
        return remoteModelResult;
    }
  
    public RemoteModelResult<BaseDto> checkMeterData(String companyId,TcMeterData t) {
    	
    	return new RemoteModelResult<BaseDto>(this.tcMeterDataService.checkMeterData(companyId,t));
    	
	}
    
    public RemoteModelResult<BaseDto> modifyTotalReadingById (String companyId, TcMeterData data) {
    	
    	return new RemoteModelResult<BaseDto>(this.tcMeterDataService.modifyTotalReadingById (companyId,data));
    	
	}
    
    public RemoteModelResult<BaseDto> getLastTaskMterData(String companyId, TcMeterData data){
    	
    	return new RemoteModelResult<BaseDto>(this.tcMeterDataService.getLastTaskMterData(companyId,data));
    }
    
    public RemoteModelResult<List<TcMeterData>> queryMeterByYear(String companyId,String meterCode,int meterType, String year){
        RemoteModelResult<List<TcMeterData>> remoteModelResult = new RemoteModelResult<List<TcMeterData>>();
        remoteModelResult.setModel(tcMeterDataService.queryMeterByYear(companyId,meterCode,meterType,year));
        return remoteModelResult;
    }
    public RemoteModelResult<BaseDto> modifyMeterDataStatus(String companyId,TcMeterData tcMeterData){
    	
    	return new RemoteModelResult<BaseDto>(this.tcMeterDataService.modifyMeterDataStatus(companyId,tcMeterData));
    }
   
}
