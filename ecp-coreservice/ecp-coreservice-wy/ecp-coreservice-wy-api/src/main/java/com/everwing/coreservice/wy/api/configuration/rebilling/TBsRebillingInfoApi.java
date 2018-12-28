package com.everwing.coreservice.wy.api.configuration.rebilling;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;
import com.everwing.coreservice.common.wy.service.configuration.rebilling.TBsRebillingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tBsRebillingApi")
public class TBsRebillingInfoApi {
	
	@Autowired
	private TBsRebillingInfoService tBsRebillingInfoService;

	public RemoteModelResult<BaseDto> listPageInfos(String companyId,TBsRebillingInfo entity) {
		return new RemoteModelResult<BaseDto>(this.tBsRebillingInfoService.listPageInfos(companyId,entity));
	}

	public RemoteModelResult<BaseDto> rebilling(String companyId,TBsRebillingInfo entity,Integer type) {
		return new RemoteModelResult<BaseDto>(this.tBsRebillingInfoService.rebilling(companyId,entity,type));
	}
	
	

}
