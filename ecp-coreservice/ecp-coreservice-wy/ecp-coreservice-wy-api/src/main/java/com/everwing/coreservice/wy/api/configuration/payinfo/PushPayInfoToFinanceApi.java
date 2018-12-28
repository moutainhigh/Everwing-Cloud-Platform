package com.everwing.coreservice.wy.api.configuration.payinfo;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.service.configuration.payinfo.PushPayInfoToFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("pushPayInfoToFinanceApi")
@SuppressWarnings("rawtypes")
public class PushPayInfoToFinanceApi {

	@Autowired
	private PushPayInfoToFinanceService pushPayInfoToFinanceService;

	
	public RemoteModelResult<BaseDto> doPushPayInfo(String companyId) {
		return new RemoteModelResult<BaseDto>(this.pushPayInfoToFinanceService.doPushPayInfo(companyId));
	}

	public RemoteModelResult<BaseDto> tellWCPushEnd(String companyId) {
		return new RemoteModelResult<BaseDto>(this.pushPayInfoToFinanceService.tellWCPushEnd(companyId));
	}
	
}
