package com.everwing.coreservice.wy.api.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingRules;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsGetRuleBuilding;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsRuleBuildingRelation;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsWaterElectBillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("tBsWaterElectBillingApi")
public class TBsWaterElectBillingApi {

	
	@Autowired
	private TBsWaterElectBillingService tBsWaterElectBillingService;
	
	
	public RemoteModelResult<BaseDto> listPageRulesBySchemeId(String companyId,TBsChargingRules entity) {
		return new RemoteModelResult<BaseDto>(this.tBsWaterElectBillingService.listPageRulesBySchemeId(companyId,entity));
	}
	
	public RemoteModelResult<MessageMap> addOrEditRuleInfo(String companyId,TBsChargingRules entity) throws ECPBusinessException{
		return new RemoteModelResult<MessageMap>(this.tBsWaterElectBillingService.addOrEditRuleInfo(companyId,entity));
	}
	
	
	public RemoteModelResult<MessageMap> deleteRulesByIds(String companyId,String ids) {
		return new RemoteModelResult<MessageMap>(this.tBsWaterElectBillingService.deleteRulesByIds(companyId,ids));
	}
	
	
	public RemoteModelResult<MessageMap> updateRule(String companyId,TBsChargingRules entity) {
		return new RemoteModelResult<MessageMap>(this.tBsWaterElectBillingService.updateRule(companyId,entity));
	}
	
	/**
	 * 根据规则ID查询规则和对应的建筑
	 */
	public RemoteModelResult<BaseDto> getRuleById(WyBusinessContext ctx,String Id){
		
		return new RemoteModelResult<BaseDto>(this.tBsWaterElectBillingService.getFeeRuleById(ctx, Id));
	}
	
	/**
	 * 查询过滤后的建筑
	 */
	public RemoteModelResult<BaseDto> getFilterZeeTree(WyBusinessContext ctx, TBsGetRuleBuilding ruleBuilding){
		
		return new RemoteModelResult<BaseDto>(this.tBsWaterElectBillingService.getFilterZeeTree(ctx, ruleBuilding));
	}
	
	/**
	 * 根据规则ID异步查询计费关联建筑
	 */
	public RemoteModelResult<BaseDto> getReaBuildByChargRuleId(WyBusinessContext ctx,TBsRuleBuildingRelation tbsRuleRelation){
		
		return new RemoteModelResult<BaseDto>(tBsWaterElectBillingService.getReaBuildByChargRuleId(ctx, tbsRuleRelation));
	}
	
	
  /**
   * 批量删除规则
   */
	public RemoteModelResult<BaseDto> batchRuleByIds(WyBusinessContext ctx,TBsChargingRules tBsChargingRules){
		return new RemoteModelResult<BaseDto>(this.tBsWaterElectBillingService.batchRuleByIds(ctx, tBsChargingRules));
	}
	
	/**
	 * 根据Id查询
	 */
	public RemoteModelResult<BaseDto>getFeeRuleById(WyBusinessContext ctx,String ruleId){
		return new RemoteModelResult<BaseDto>(tBsWaterElectBillingService.getFeeRuleById(ctx,ruleId));
	}
}
