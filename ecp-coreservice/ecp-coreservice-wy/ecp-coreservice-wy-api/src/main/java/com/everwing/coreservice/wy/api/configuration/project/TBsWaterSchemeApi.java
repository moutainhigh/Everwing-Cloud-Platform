package com.everwing.coreservice.wy.api.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsWaterSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("tBsWaterSchemeApi")
public class TBsWaterSchemeApi {

	
	@Autowired
	private TBsWaterSchemeService tBsWaterSchemeService;
	
	
	public RemoteModelResult<BaseDto> selectWaterSchemeInfo(String companyId,TBsChargingScheme scheme) {
		return new RemoteModelResult<BaseDto>(this.tBsWaterSchemeService.selectWaterSchemeInfo(companyId,scheme));
	}
	
	public RemoteModelResult<MessageMap> manualChargingForWater(String companyId,String projectId,String userId) {
		return new RemoteModelResult<MessageMap>(this.tBsWaterSchemeService.manualChargingForWater(companyId,projectId,userId));
	}

}
