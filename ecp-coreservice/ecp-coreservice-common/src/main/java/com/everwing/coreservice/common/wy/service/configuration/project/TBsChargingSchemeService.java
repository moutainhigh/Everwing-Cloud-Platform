package com.everwing.coreservice.common.wy.service.configuration.project;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;


import java.util.List;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;

import java.util.List;

public interface TBsChargingSchemeService {

/*	BaseDto listPageRulesBySchemeId(String companyId,TBsChargingRules entity);*/
	
	MessageMap addChargingScheme(String companyId,TBsChargingScheme entity);

	BaseDto editedScheme(String companyId, TBsChargingScheme scheme);

	BaseDto listPageSchemes(String companyId, TBsChargingScheme scheme);

	BaseDto stopSchemes(String companyId, List<TBsChargingScheme> schemes);

	BaseDto findUsingScheme(String companyId, TBsChargingScheme scheme);
	
	/**
	 * 根据项目编号和方案类型查找出未失效且已经启用的方案
	 * @param req
	 * @param schemeType
	 * @param projectId
	 * @return
	 */
	BaseDto getChargingScheme(WyBusinessContext ctx, String schemeType,String projectId);
}
