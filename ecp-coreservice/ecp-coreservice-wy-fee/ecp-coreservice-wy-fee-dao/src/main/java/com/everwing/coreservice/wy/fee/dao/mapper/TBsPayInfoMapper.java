package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfo;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface TBsPayInfoMapper {

	int batchInsert(List<TBsPayInfo> insertInfos) throws DataAccessException;

	int insert(TBsPayInfo record) throws DataAccessException;

}