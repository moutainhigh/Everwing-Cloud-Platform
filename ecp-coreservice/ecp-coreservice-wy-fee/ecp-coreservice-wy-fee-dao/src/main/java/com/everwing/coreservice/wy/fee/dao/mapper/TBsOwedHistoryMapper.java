package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;

import java.util.List;

public interface TBsOwedHistoryMapper {


	
	int update(TBsOwedHistory entity);

	List<TBsOwedHistory> findAllByAccountId(String accountId);
}
