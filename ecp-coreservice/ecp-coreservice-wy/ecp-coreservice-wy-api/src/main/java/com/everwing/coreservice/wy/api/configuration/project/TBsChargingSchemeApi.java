package com.everwing.coreservice.wy.api.configuration.project;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsChargingSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("tBsChargingSchemeApi")
public class TBsChargingSchemeApi {

	
	@Autowired
	private TBsChargingSchemeService tBsChargingSchemeService;
	
	/*
	public RemoteModelResult<BaseDto> listPageRulesBySchemeId(String companyId,TBsChargingRules entity) {
		return new RemoteModelResult<BaseDto>(this.tBsWaterElectBillingService.listPageRulesBySchemeId(companyId,entity));
	}
	*/
	
	
	public RemoteModelResult<MessageMap> addChargingScheme (String companyId,TBsChargingScheme entity) {
		return new RemoteModelResult<MessageMap>(this.tBsChargingSchemeService.addChargingScheme(companyId,entity));
	}

	public RemoteModelResult<BaseDto> editedScheme(String companyId,TBsChargingScheme scheme) {
		return new RemoteModelResult<BaseDto>(this.tBsChargingSchemeService.editedScheme(companyId,scheme));
	}

	public RemoteModelResult<BaseDto> listPageSchemes(String companyId,TBsChargingScheme scheme) {
		return new RemoteModelResult<BaseDto>(this.tBsChargingSchemeService.listPageSchemes(companyId,scheme));
	}

	public RemoteModelResult<BaseDto> findUsingScheme(String companyId,TBsChargingScheme scheme) {
		return new RemoteModelResult<BaseDto>(this.tBsChargingSchemeService.findUsingScheme(companyId,scheme));
	}

	public RemoteModelResult<BaseDto> stopSchemes(String companyId,List<TBsChargingScheme> schemes) {
		return new RemoteModelResult<BaseDto>(this.tBsChargingSchemeService.stopSchemes(companyId,schemes));
	}
	
	public RemoteModelResult<BaseDto> getChargingScheme(WyBusinessContext ctx, String schemeType, String projectId){
		return new RemoteModelResult<BaseDto>(this.tBsChargingSchemeService.getChargingScheme(ctx, schemeType, projectId));
	}
	
}
