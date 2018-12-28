package com.everwing.coreservice.common.wy.service.configuration.task;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;

import java.util.List;

public interface WaterElectRebillingService {

	MessageMap waterElectRebilling(String companyId, List<String> ids,String userId,int meterType);
	
	BaseDto rebillingCorrect(String companyId, TBsRebillingInfo entity,Integer type);
	
	void singleRebilling(String companyId, TBsRebillingInfo entity,Integer type);
	
	MessageMap waterElectReBillingOpr(String companyId, List<String> ids,String userId,Integer type);
	
}
