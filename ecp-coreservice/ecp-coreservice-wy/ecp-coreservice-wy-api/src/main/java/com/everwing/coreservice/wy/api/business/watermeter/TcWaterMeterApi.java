package com.everwing.coreservice.wy.api.business.watermeter;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcHydroMeterOperationRecord;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.service.business.watermeter.TcWaterMeterImportService;
import com.everwing.coreservice.common.wy.service.business.watermeter.TcWaterMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("waterMeterApi")
public class TcWaterMeterApi {

	
	@Autowired
	private TcWaterMeterService waterMeterService;

	@Autowired
	private TcWaterMeterImportService tcWaterMeterImportService;
	
	
	public RemoteModelResult<BaseDto<String, Object>> listPageWaterMeterInfos(String companyId,TcWaterMeter tcWaterMeter) {
		return new RemoteModelResult<BaseDto<String,Object>>(this.waterMeterService.listPageWaterMeterInfos(companyId,tcWaterMeter));
	}
	
	public RemoteModelResult<MessageMap> addWaerMeterInfo(String companyId,TcWaterMeter tcWaterMeter) {
		return new RemoteModelResult<MessageMap>(this.waterMeterService.addWaerMeterInfo(companyId,tcWaterMeter));
	}
	
	
	public RemoteModelResult<MessageMap> updateWaerMeterInfo(String companyId,TcWaterMeter tcWaterMeter) {
		return new RemoteModelResult<MessageMap>(this.waterMeterService.updateWaerMeterInfo(companyId,tcWaterMeter));
	}
	
	
	public RemoteModelResult<MessageMap> startStopWaerMeterByOne(String companyId,TcHydroMeterOperationRecord tcHydroMeterOperationRecord,String meterId,int state) {
		return new RemoteModelResult<MessageMap>(this.waterMeterService.startStopWaerMeterByOne(companyId,tcHydroMeterOperationRecord,meterId,state));
	}
	
	
	public RemoteModelResult<MessageMap> deleteWaterMeterInfos(String companyId,String ids) {
		return new RemoteModelResult<MessageMap>(this.waterMeterService.deleteWaterMeterInfos(companyId,ids));
	}
	
	
	public RemoteModelResult<MessageMap> replaceWaerMeterByOne(String companyId,TcHydroMeterOperationRecord tcHydroMeterOperationRecord) {
		return new RemoteModelResult<MessageMap>(this.waterMeterService.replaceWaerMeterByOne(companyId,tcHydroMeterOperationRecord));
	}
	
	public RemoteModelResult<BaseDto<String, Object>> loadEnclosureInfoByid(String companyId,TcWaterMeter tcWaterMeter) {
		return new RemoteModelResult<BaseDto<String,Object>>(this.waterMeterService.loadEnclosureInfoByid(companyId,tcWaterMeter));
	}
	
	
	public RemoteModelResult<BaseDto<String, Object>> listPageLoadMeterReadingRecordById(String companyId,TcWaterMeter tcWaterMeter) {
		return new RemoteModelResult<BaseDto<String,Object>>(this.waterMeterService.listPageLoadMeterReadingRecordById(companyId,tcWaterMeter));
	}
	
	public RemoteModelResult<BaseDto<String, Object>> listPageloadWaterMeterForChange(String companyId,TcWaterMeter tcWaterMeter) {
		return new RemoteModelResult<BaseDto<String,Object>>(this.waterMeterService.listPageloadWaterMeterForChange(companyId,tcWaterMeter));
	}
	
	public RemoteModelResult<MessageMap> checkWaterMeterCode(String companyId,String code) {
		return new RemoteModelResult<MessageMap>(this.waterMeterService.checkWaterMeterCode(companyId,code));
	}
	
	
	public RemoteModelResult<BaseDto<String, Object>> listPageWaterMeterByLevel(String companyId,TcWaterMeter waterMeter) {
		return new RemoteModelResult<BaseDto<String,Object>>(this.waterMeterService.listPageWaterMeterByLevel(companyId,waterMeter));
	}
	
	
	 /**
     * 导入
     * @param ctx
     * @param batchNo
     * @param excelPath
     * @return
     */
	public RemoteModelResult<MessageMap> importWaterMeter(WyBusinessContext ctx, String batchNo, String excelPath){
		MessageMap messageMap =tcWaterMeterImportService.importWaterMeter(ctx,batchNo,excelPath);
	    return new RemoteModelResult<MessageMap>(messageMap);
	}

	public RemoteModelResult<BaseDto> countMeters(String companyId,TcWaterMeter entity) {
		return new RemoteModelResult<BaseDto>(this.waterMeterService.countMeters(companyId,entity));
	}
	
	public RemoteModelResult<BaseDto> loadBuildingAndMeter(String companyId,String projectId,String meterType) {
		return new RemoteModelResult<BaseDto>(this.waterMeterService.loadBuildingAndMeter(companyId,projectId,meterType));
	}

	public  RemoteModelResult<List<TcWaterMeter>> findsByBuildingCode(String companyId, String buildingCode){

		return new RemoteModelResult<List<TcWaterMeter>>(this.waterMeterService.findsByBuildingCode(companyId, buildingCode));
	}


	public RemoteModelResult<BaseDto> listPageWaterMeterByBuildingCode(WyBusinessContext ctx, TcWaterMeter tcWaterMeter) {
		RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
		result.setModel(waterMeterService.listPageWaterMeterByBuildingCode(ctx, tcWaterMeter));
		return result;
	}
}
