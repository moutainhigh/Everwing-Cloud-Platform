package com.everwing.coreservice.common.platform.service;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.extra.AccountAndHouseData;

import java.util.List;

public interface AccountAndHouseService {
	
	
	public RemoteModelResult<Void> batchAdd(List<AccountAndHouseData> dataList);
	
	public RemoteModelResult<Void> batchDelete(List<AccountAndHouseData> dataList);
}
