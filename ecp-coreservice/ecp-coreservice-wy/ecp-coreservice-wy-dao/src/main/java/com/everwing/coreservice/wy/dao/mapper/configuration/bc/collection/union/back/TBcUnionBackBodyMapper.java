package com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.union.back;

import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.TBcUnionCollectionBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.back.TBcUnionBackBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TBcUnionBackBodyMapper {

	List<TBcUnionBackBody> listPage(TBcUnionBackBody body);

	TBcUnionCollectionBody findByHeadIdAndBuildingCode(String headId,String buildingCode);

	List<TBcUnionCollectionBody> findByObj(TBcUnionCollectionBody param);

	int batchInsert(List<TBcUnionBackBody> bodies);

	int update(TBcUnionBackBody bb);

	TBcUnionBackBody findByHeadIdAndOrderNo(@Param("headId") String headId,@Param("orderNo") String orderNo);

	Integer countTotalCountByTotalId(String id);

	List<String> findCollingDatasByTotalId(@Param("totalId")String id, @Param("buildingCodes") List<String> buildingCodes);

	Map<String, Object> getSuccessDatasByTotalId(@Param("totalId") String id);

}
