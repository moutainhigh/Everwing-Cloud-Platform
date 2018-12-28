package com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.jrl;

import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.jrl.TBcJrlBody;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;


public interface TBcJrlBodyMapper {

	List<TBcJrlBody> listPage(TBcJrlBody body);
	
	TBcJrlBody findByHeadId(String headId);

	List<TBcJrlBody> findByObj(TBcJrlBody param);

	int update(TBcJrlBody body) throws DataAccessException;

	int batchInsert(List<TBcJrlBody> list) throws DataAccessException;
	
	int batchUpdate(List<TBcJrlBody> list) throws DataAccessException;

	TBcJrlBody findByHeadIdAndAgreementNo(@Param("headId") String headId, @Param("agreementNo") String agreementNo);
	
	List<TBcJrlBody> findFailedBodies(TBcJrlBody param);

	int insert(TBcJrlBody body);

	Integer countByHeadIdAndType(@Param("headId") String headId, @Param("type") Integer type);

	Double sumAmountByHeadIdAndType(@Param("headId") String headId, @Param("type") Integer type);

	Double sumTotalAmountByTotalId(String id);

	Integer countTotalCountByTotalId(String id);

	List<String> findCollingDatasByTotalId(@Param("totalId") String id, @Param("buildingCodes") List<String> buildingCodes);

	Map<String, Object> getSuccessDatasByTotalId(@Param("totalId") String id);
	
	TBcJrlBody getCollBodyByTotalIdAndBuildingCoded(@Param("totalId") String totalId, @Param("buildingCode") String buildingCode);
	
	
	
	
}
