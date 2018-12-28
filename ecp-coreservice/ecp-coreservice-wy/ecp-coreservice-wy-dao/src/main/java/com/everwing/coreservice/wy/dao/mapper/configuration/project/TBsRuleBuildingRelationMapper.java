package com.everwing.coreservice.wy.dao.mapper.configuration.project;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsRuleBuildingRelation;

import java.util.List;
import java.util.Map;
/**
 * @describe （物业，本体，水费，电费）方案相关
 * @author QHC
 *
 */
public interface TBsRuleBuildingRelationMapper {

	/**
	 * 根据规则ID查询建筑关联关系
	 * @param ruleId
	 * @return
	 */
	List<TBsRuleBuildingRelation> selectAssetsByRuleId(String ruleId);
	
	/**
	 * 根据ruleId查询BuildingCode
	 * @param ruleId
	 * @return
	 */
	List<String> getBuildingCodeByRuleId(Map<String,Object> paramMap);
	
	int batchInsert(List<TBsRuleBuildingRelation> datas);
	
	/**
	 * 根据id批量修改
	 * @return
	 */
	int batchDelRules(List<String> ids);
	
	//根据关联规则id删除其下面关联的建筑信息
	int deleteRelationBuilding(String ruleId);
	
	List<String> selectIdsByRuleId(String ruleId);
	
	/**
	 * 根据规则id和建筑类型查询
	 * @param ruleId
	 * @param buildingType
	 * @return
	 */
	List<TBsRuleBuildingRelation> getRelationByRuleIdAndType(String ruleId,String buildingType);
	
	/**
	 * 根据规则编号和父ID查询
	 * @param ruleId
	 * @param pId
	 * @return
	 */
	List<TBsRuleBuildingRelation> getRuleIdAndPid(String ruleId,String pId);
	
	/**
	 * 根据buildingCode和ruleId查询
	 */
	TBsRuleBuildingRelation getRelationByBuilCodeAndRuleId(String buildingCode,String ruleId);
	
	/**
	 * 根据规则Id查询
	 */
	int delByRuleId(List<String> ids);
	
	/**
	 * 这里会将一些父节点存储进来，因为一个收费对象，水费和电费可能都要收取
	 */
	List<TBsRuleBuildingRelation> findByBuildCodeAndType(String buildCode,String type);
	
	List<TBsRuleBuildingRelation> getReaBuildByChargRuleId(TBsRuleBuildingRelation ruleBuilding);

	List<TBsRuleBuildingRelation> selectByBuildingCode(String buildingCode);
}
