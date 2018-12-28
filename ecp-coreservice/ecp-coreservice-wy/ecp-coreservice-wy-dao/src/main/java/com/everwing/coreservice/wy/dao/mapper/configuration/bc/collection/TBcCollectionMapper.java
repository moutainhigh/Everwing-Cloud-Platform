package com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection;

import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.TBcCollectionTotal;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.common.wy.entity.cust.person.TBcCillectionName;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TBcCollectionMapper {

	Map<String, Object> findIdPerYear(TBcCollectionTotal total);

	Map<String, Object> findFamilyCountPerYear(TBcCollectionTotal total);

	Map<String, Object> findTotalAmountPerYear(TBcCollectionTotal total);

	Map<String, Object> findCompleteCountPerYear(TBcCollectionTotal total);

	Map<String, Object> findCompleteAmountPerYear(TBcCollectionTotal total);

	Map<String, Object> findNotCompleteAmountPerYear(TBcCollectionTotal total);

	Map<String, Object> findStatusPerYear(TBcCollectionTotal total);

	Map<String, Object> findTotalPerYear(TBcProject entity);

	TBcCollectionTotal findById(String totalId);

	TBcCollectionTotal findCurrTotal(@Param("projectId") String projectId, @Param("collectionType") Integer unionFlag);

	int insert(TBcCollectionTotal total);

	int update(TBcCollectionTotal total);

	int updateCountAndAmountById(@Param("completeCount") Integer completeCount,
							     @Param("completeAmount") Double completeAmout, 
							     @Param("id") String id,
							     @Param("type") Integer type);

	int completeLastTotal();

	TBcCollectionTotal findRecentTotal(@Param("projectId") String projectId, @Param("collectionType") Integer unionFlag);

	TBcCollectionTotal findByProjectIdAndTime(@Param("projectId") String projectId, @Param("createTime") Date createTime);

	/**
	 * 催缴报表得银行信息
	 * @param projectId
	 * @return
	 */
	List<TBcCillectionName> findByTBcClillectionNameByProjectId(String projectId);
}
