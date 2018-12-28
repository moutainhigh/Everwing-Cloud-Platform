package com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.union;

import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.TBcUnionCollectionHead;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TBcUnionCollectionHeadMapper {

	List<TBcUnionCollectionHead> listPage(TBcUnionCollectionHead head);

	List<TBcUnionCollectionHead> findAllByObj(TBcUnionCollectionHead head);

	int insert(TBcUnionCollectionHead paramObj);

	int update(TBcUnionCollectionHead unionHead);

	int updateShopNum(@Param("projectId") String projectId, @Param("shopNum") String shopNum , @Param("modifyId") String modifyId);

	TBcUnionCollectionHead findByTotalId(String totalId);

	TBcUnionCollectionHead findTheEarliestHeadByTotalId(String totalId);

}
