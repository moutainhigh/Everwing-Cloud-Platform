package com.everwing.coreservice.common.wy.service.configuration.task;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.configuration.support.BillingSupEntity;

public interface WaterAndElectBillingTaskService {

	BaseDto manualElectBilling(String companyId,String flag,TBsProject entity);
	
	public void insert(String companyId, BillingSupEntity se);
	
	public BaseDto autoBilling(String companyId,String meterFlag);
	
	public void autoBillingByProject(String companyId, TBsProject entity,String meterTypeFlag);
	
}
