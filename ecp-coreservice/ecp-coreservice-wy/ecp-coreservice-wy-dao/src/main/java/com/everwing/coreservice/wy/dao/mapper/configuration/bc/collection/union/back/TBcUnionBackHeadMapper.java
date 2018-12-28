package com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.union.back;

import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.back.TBcUnionBackHead;

import java.util.List;

public interface TBcUnionBackHeadMapper {

	List<TBcUnionBackHead> listPage(TBcUnionBackHead head);

	List<TBcUnionBackHead> findByObj(TBcUnionBackHead paramHead);

	int insert(TBcUnionBackHead head);

}
