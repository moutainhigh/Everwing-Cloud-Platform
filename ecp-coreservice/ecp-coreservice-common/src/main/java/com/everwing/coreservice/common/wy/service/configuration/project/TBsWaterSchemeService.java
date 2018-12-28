package com.everwing.coreservice.common.wy.service.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;

public interface TBsWaterSchemeService {

	BaseDto selectWaterSchemeInfo(String companyId,TBsChargingScheme scheme);
	
	MessageMap manualChargingForWater(String companyId,String projectId,String userId);

}
