package com.everwing.coreservice.common.wy.service.business.watermeter;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcHydroMeterOperationRecord;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;

import java.util.List;

public interface TcWaterMeterService {
	
	public BaseDto<String, Object> listPageWaterMeterInfos(String companyId,TcWaterMeter tcWaterMeter);
	
	public MessageMap addWaerMeterInfo(String companyId,TcWaterMeter tcWaterMeter);
	
	public MessageMap updateWaerMeterInfo(String companyId,TcWaterMeter tcWaterMeter);
	
	public MessageMap deleteWaterMeterInfos(String companyId,String waterMeterCodes);
	
	public MessageMap replaceWaerMeterByOne(String companyId,TcHydroMeterOperationRecord tcHydroMeterOperationRecord);
	
	public MessageMap startStopWaerMeterByOne(String companyId,TcHydroMeterOperationRecord tcHydroMeterOperationRecord,String meterId,int state);
	
	public BaseDto<String, Object> loadEnclosureInfoByid(String companyId,TcWaterMeter tcWaterMeter);
	
	public BaseDto<String, Object> listPageLoadMeterReadingRecordById(String companyId,TcWaterMeter tcWaterMeter);
	
	public BaseDto<String, Object> listPageloadWaterMeterForChange(String companyId,TcWaterMeter tcWaterMeter);
	
	public MessageMap checkWaterMeterCode(String companyId,String code);
	


	public BaseDto countMeters(String companyId, TcWaterMeter entity);
	
	public BaseDto<String, Object> listPageWaterMeterByLevel(String companyId,TcWaterMeter tcWaterMeter);
	
	public BaseDto<String, Object> loadBuildingAndMeter(String companyId,String projectId,String meterType);

	public List<TcWaterMeter> findsByBuildingCode(String companyId, String buildingCode);


	BaseDto listPageWaterMeterByBuildingCode(WyBusinessContext ctx, TcWaterMeter tcWaterMeter);
}
