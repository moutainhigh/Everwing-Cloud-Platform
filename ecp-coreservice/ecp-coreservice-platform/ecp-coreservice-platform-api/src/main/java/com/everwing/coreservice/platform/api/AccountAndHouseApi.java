package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.extra.AccountAndHouseData;
import com.everwing.coreservice.platform.api.util.ServiceResources;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountAndHouseApi extends ServiceResources{


	public RemoteModelResult<Void> batchAdd(List<AccountAndHouseData> dataList) {
		return accountAndHouseService.batchAdd(dataList);
	}
	
	public RemoteModelResult<Void> batchDelete(List<AccountAndHouseData> dataList) {
		return accountAndHouseService.batchDelete(dataList);
	}
	
}