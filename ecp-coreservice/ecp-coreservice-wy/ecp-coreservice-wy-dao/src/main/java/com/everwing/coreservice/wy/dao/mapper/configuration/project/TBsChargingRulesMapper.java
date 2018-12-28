package com.everwing.coreservice.wy.dao.mapper.configuration.project;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingRules;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsGetRuleBuilding;

import java.util.List;
import java.util.Map;
/**
 * @describe （物业，本体，水费，电费）方案相关
 * @author QHC
 *
 */
public interface TBsChargingRulesMapper {

	List<TBsChargingRules> listPageRulesBySchemeId(TBsChargingRules entity);
	
	/**
	 * 单个新增
	 * @param tbsCharingScheme
	 * @return
	 */
	int insertRule(TBsChargingRules tBsChargingRules);
	
	
	/**
	 * 修改
	 * @param tbsCharingScheme
	 * @return
	 */
	int updateChargeRuleType(TBsChargingRules tBsChargingRules);
	
	/**
	 * 根据id批量修改
	 * @return
	 */
	int batchDelRules(List<String> ids);
	
	/**
	 * 根据名称和方案id来查找
	 */
	
	TBsChargingRules getByNameAndSchemId(String ruleName,String chargingSchemeId);
	
	/**
	 * 根据ID和方案ID查找规则和对应的关联建筑
	 * @param Id
	 * @param schemId
	 * @return
	 */
	 List<TBsChargingRules> getById(String Id);
	 
	 /**
	  * 根据方案编码查找规则
	  * @param chargingSchemeId
	  * @return
	  */
	 List<TBsChargingRules> getTBsChargingRulesBySchemeId(String chargingSchemeId);
	 
	 
	 /**
	  * 过滤后查找建筑
	  */
	 List<Map<String,Object>> getFilterZeeTree(TBsGetRuleBuilding ruleBuilding);
	 
	 /**
	  * 根据ID进行批量删除
	  */
	 int delByRuleId(List<String> ids);
	 
	 /**
	  * 根据ruleId查询
	  */
	 TBsChargingRules getChargeRuleById(String ruleId);
}
