package com.everwing.coreservice.wy.api.business.electmeter;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ChangeElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeterSearch;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.service.business.electmeter.TcElectMeterImportService;
import com.everwing.coreservice.common.wy.service.business.electmeter.TcElectMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tcElectMeterApi")
public class TcElectMeterApi {
	
	@Autowired
	private TcElectMeterService tcElectMeterService;

	@Autowired
	private TcElectMeterImportService tcElectMeterImportService;
	
	
	public RemoteModelResult<BaseDto> addelectmeter(String companyId,ElectMeter electMeter){
		return new RemoteModelResult<BaseDto>(tcElectMeterService.addelectmeter(companyId, electMeter));
	}
	
	public RemoteModelResult<BaseDto> listPageElectmeterByCompany(String companyId,ElectMeterSearch electMeterSearch){
		return new RemoteModelResult<BaseDto>(tcElectMeterService.listPageElectmeterByCompany(companyId, electMeterSearch));
	}
	
	public RemoteModelResult<BaseDto> startOrstopElectMeter(String companyId,ElectMeter electMeter){
		return new RemoteModelResult<BaseDto>(tcElectMeterService.startOrstopElectMeter(companyId, electMeter));
	}
	
	public RemoteModelResult<BaseDto> editSave(String companyId,ElectMeter electMeter){
		return new RemoteModelResult<BaseDto>(tcElectMeterService.editSave(companyId, electMeter));
	}
	
	public RemoteModelResult<BaseDto> delElect(String companyId,ElectMeter electMeter){
		return new RemoteModelResult<BaseDto>(tcElectMeterService.delElect(companyId, electMeter));
	}
	
	public RemoteModelResult<BaseDto> changeElectMeter(String companyId,ChangeElectMeter changeElectMeter){
		return new RemoteModelResult<BaseDto>(tcElectMeterService.changeElectMeter(companyId, changeElectMeter));
	}
	
	public RemoteModelResult<BaseDto> startOrStop(String companyId,ElectMeter electMeter){
		return new RemoteModelResult<BaseDto>(tcElectMeterService.startOrStop(companyId, electMeter));
	}
	
	public RemoteModelResult<MessageMap> importElectMeter(WyBusinessContext ctx, String batchNo,String excelPath){
		return new RemoteModelResult<MessageMap>(tcElectMeterImportService.importElectMeter(ctx, batchNo, excelPath));
	}
	
	public RemoteModelResult<BaseDto> countMeters(String companyId,ElectMeter entity) {
		return new RemoteModelResult<BaseDto>(this.tcElectMeterService.countMeters(companyId,entity));
	}
	
	public RemoteModelResult<BaseDto> listPageFilterRelationBuild(WyBusinessContext ctx, TcBuildingSearch entity){
		return new RemoteModelResult<BaseDto>(this.tcElectMeterService.listPageFilterRelationBuild(ctx, entity));
	}
	public RemoteModelResult<List<ElectMeter>> findsByBuildingCode(String companyId, String buildingCode){
		
		return new RemoteModelResult(this.tcElectMeterService.findsByBuildingCode(companyId, buildingCode));
	}

    public RemoteModelResult<BaseDto> listPageElectMeterByBuildingCode(WyBusinessContext ctx, ElectMeterSearch electMeterSearch) {
		RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
		result.setModel(tcElectMeterService.listPageElectMeterByBuildingCode(ctx, electMeterSearch));
		return result;
    }
}
