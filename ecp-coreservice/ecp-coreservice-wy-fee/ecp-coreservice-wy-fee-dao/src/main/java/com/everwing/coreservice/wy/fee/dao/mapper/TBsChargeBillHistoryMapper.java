package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

public interface TBsChargeBillHistoryMapper {


	int updateBillHistory(TBsChargeBillHistory entity) throws DataAccessException;

	TBsChargeBillHistory findById(String billId);

	TBsChargeBillHistory selectNotBillingByObj(@Param("buildingCode") String buildingCode, @Param("type") Integer type);

}
