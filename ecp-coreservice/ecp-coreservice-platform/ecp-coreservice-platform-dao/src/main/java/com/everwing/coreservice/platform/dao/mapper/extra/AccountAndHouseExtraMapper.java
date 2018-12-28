package com.everwing.coreservice.platform.dao.mapper.extra;

import com.everwing.coreservice.common.platform.entity.generated.AccountAndHouse;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AccountAndHouseExtraMapper {
	int batchDelete(List<AccountAndHouse> list);
	
	int delete(AccountAndHouse accountAndHouse);
	
	int batchInsert(List<AccountAndHouse> list);
	
	int deleteByAccountId(@Param("accountId") String accountId);

}