package com.everwing.coreservice.wy.dao.mapper.configuration.project;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargeType;

import java.util.List;
/**
 * @describe （物业，本体，水费，电费）方案相关
 * @author QHC
 *
 */
public interface TBsChargeTypeMapper {
	
	
	/**
	 * 分页查询
	 * @param ruleId
	 * @return
	 */
	List<TBsChargeType> listPageChargingType(TBsChargeType tBsChargeType);
	
	/**
	 * 根据规则查询
	 * @param ruleId
	 * @return
	 */

	List<TBsChargeType> selectChargeType(String ruleId);
	
	/**
	 * 根据收费规则和收费细项名称查询
	 * @param ruleId
	 * @param chargingName
	 * @return
	 */
	
	TBsChargeType getByRuleIdAndName(String ruleId,String chargingName);
	
	/**
	 * 单个新增
	 * @param tbsCharingScheme
	 * @return
	 */
	int insert(TBsChargeType tBsChargeType);
	
	
	/**
	 * 修改
	 * @param tbsCharingScheme
	 * @return
	 */
	int updateChargeType(TBsChargeType tBsChargeType);
	
	/**
	 * 根据id批量修改
	 * @return
	 */
	int batchDel(List<String> ids);
	
	/**
	 * 根据id批量修改
	 * @return
	 */
	int deleteByRuleId(String ruleId);
	
	
	/**
	 * 根据规则ID批量删除
	 */
	
	int delByRuleId(List<String> ids);
	
	/**
	 * 根据总单ID，项目编码，关联建筑编码查询查询收费项
	 */
	List<TBsChargeType>  findBytoIdAndBuildCodeAndProId(String totalId,String buildCode,String projectId);
}
