package com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.union;

import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.TBcUnionCollectionBody;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface TBcUnionCollectionBodyMapper {

	List<TBcUnionCollectionBody> listPage(TBcUnionCollectionBody body);
	
	List<TBcUnionCollectionBody> findAllByHeadId(String headId);

	int update(TBcUnionCollectionBody body);

	String findOrderNoByHeadId(String headId);

	int batchInsert(ArrayList<TBcUnionCollectionBody> list);

	TBcUnionCollectionBody findByHeadIdAndOrderId(@Param("headId") String headId, @Param("orderNo") String orderNo);
	
	List<TBcUnionCollectionBody> findFailedBodies(String headId);

	Integer countByHeadId(String id);

	int insert(TBcUnionCollectionBody body);

	Double sumAmountByHeadId(String headId);

	Double sumTotalAmountByTotalId(String id);
	
	List<TBcUnionCollectionBody> getThisCollectionInfo(String projectId);
	
	int batchUpdate( List< TBcUnionCollectionBody > list );

}
