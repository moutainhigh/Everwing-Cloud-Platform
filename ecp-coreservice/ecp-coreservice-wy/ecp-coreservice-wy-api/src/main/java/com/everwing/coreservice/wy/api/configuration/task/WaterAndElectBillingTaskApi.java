package com.everwing.coreservice.wy.api.configuration.task;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.service.configuration.task.WaterAndElectBillingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("electFeeBillingTaskApi")
public class WaterAndElectBillingTaskApi {

	@Autowired
	private WaterAndElectBillingTaskService waterAndElectBillingTaskService;
	/**
	 * 手动计费
	 */
	public RemoteModelResult<BaseDto> manualElectBilling(String companyId,String flag,TBsProject entity){
		
		return new RemoteModelResult<BaseDto>(this.waterAndElectBillingTaskService.manualElectBilling(companyId,flag,entity));
	}
	
	/**
	 * 自动计费
	 */
	public RemoteModelResult<BaseDto> autoBilling(String companyId,String meterFlag){
		return new RemoteModelResult<BaseDto>(this.waterAndElectBillingTaskService.autoBilling(companyId, meterFlag));
	}
	
}
