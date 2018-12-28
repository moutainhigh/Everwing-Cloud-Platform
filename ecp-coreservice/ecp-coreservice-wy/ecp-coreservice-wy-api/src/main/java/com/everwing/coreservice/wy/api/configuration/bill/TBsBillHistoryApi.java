package com.everwing.coreservice.wy.api.configuration.bill;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.service.configuration.bill.TBsBillHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tBsBillHistoryApi")
public class TBsBillHistoryApi {

	@Autowired
	private TBsBillHistoryService tBsBillHistoryService;

	public RemoteModelResult<BaseDto> listPageData(String companyId,TBsChargeBillHistory entity) {
		return new RemoteModelResult<BaseDto>(this.tBsBillHistoryService.listPageData(companyId,entity));
	}

	public RemoteModelResult<BaseDto> listPageInCustomerService(String companyId,TBsChargeBillHistory entity) {
		return new RemoteModelResult<BaseDto>(this.tBsBillHistoryService.listPageInCustomerService(companyId,entity));
	}
	
}
