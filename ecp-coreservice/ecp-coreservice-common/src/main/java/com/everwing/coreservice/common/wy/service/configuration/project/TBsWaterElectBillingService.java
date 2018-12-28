package com.everwing.coreservice.common.wy.service.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingRules;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsGetRuleBuilding;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsRuleBuildingRelation;

public interface TBsWaterElectBillingService {

	BaseDto listPageRulesBySchemeId(String companyId,TBsChargingRules entity);
	
	MessageMap addOrEditRuleInfo(String companyId,TBsChargingRules entity);
	
	MessageMap deleteRulesByIds(String companyId,String ids);
	
	MessageMap updateRule(String companyId,TBsChargingRules entity);
	
	/**
	 * 根据Id查询规则和对应关联建筑
	 */
	BaseDto getRuleById(WyBusinessContext ctx,String Id);
	
	public BaseDto getFilterZeeTree(WyBusinessContext ctx,TBsGetRuleBuilding ruleBuilding);
	
	public BaseDto getReaBuildByChargRuleId(WyBusinessContext ctx,TBsRuleBuildingRelation tbsRuleRela);
	
	/**
	 * 批量删除规则
	 */
	BaseDto batchRuleByIds(WyBusinessContext ctx,TBsChargingRules tBsChargingRules);
	
	/**
	 * 根据ruleId查询规则
	 */
	BaseDto getFeeRuleById(WyBusinessContext ctx,String ruleId);
}
