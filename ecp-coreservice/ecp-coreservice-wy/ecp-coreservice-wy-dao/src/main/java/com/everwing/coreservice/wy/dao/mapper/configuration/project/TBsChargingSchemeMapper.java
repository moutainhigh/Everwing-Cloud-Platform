package com.everwing.coreservice.wy.dao.mapper.configuration.project;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;
/**
 * @describe （物业，本体，水费，电费）方案相关
 * @author QHC
 *
 */
public interface TBsChargingSchemeMapper {

	/**
	 * 
	 * @param schemeType 查询方案类型
	 * @return List<TBsChargingScheme>
	 */
	List<TBsChargingScheme> selectSchemeInfo(TBsChargingScheme scheme);
	
	/**
	 * @describe 查询历史方案信息（含当前使用的方案）
	 * @param schemeType 查询方案类型
	 * @param queryStr  模糊查询字符串
	 * @return List<TBsChargingScheme>
	 */
	List<TBsChargingScheme> listPageSchemeInfos(Map<String, String> paraMap);
	
	/**
	 * 单个新增
	 * @param tbsCharingScheme
	 * @return 插入条数
	 */
	int insert(TBsChargingScheme tbsCharingScheme) throws DataAccessException;
	
	
	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	int batchInsert(List<TBsChargingScheme> list) throws DataAccessException;
	
	/**
	 * 修改
	 * @param tbsCharingScheme
	 * @return
	 */
	int updateSchemeInfo(TBsChargingScheme tbsCharingScheme) throws DataAccessException;

	List<TBsChargingScheme> listPageSchemes(TBsChargingScheme scheme);

	TBsChargingScheme findUsingScheme(TBsChargingScheme scheme);

	int stopSchemes(List<TBsChargingScheme> schemes) throws DataAccessException;

	List<TBsChargingScheme> findConfilcScheme(TBsChargingScheme scheme);
	
	/**
	 * 根据方案类型和项目编号查找出未失效且已经启用的方案
	 * @param schemeType
	 * @param projectId
	 * @return
	 */
	TBsChargingScheme getChargSchByTypeAndProjectId(String schemeType,String projectId);

	/**
	 * @TODO 根据总单获取scheme
	 * @param chargeTotalId
	 * @return
	 */
	TBsChargingScheme selectByTotalId(String chargeTotalId);

	List<Map<String, Object>> findCurrentRate(String projectId);

	void autoStopScheme() throws DataAccessException;
	
	/**
	 * 根据id查询
	 */
	TBsChargingScheme findById(String id);

	Double findTaxRate(@Param("schemeType") Integer type, @Param("buildingCode") String buildingCode);
	
	/**
	 * 根据项目id查询税率
	 */
	List<Map<String, String>> getTaxRateByProject(String projectId);
	
	
}
