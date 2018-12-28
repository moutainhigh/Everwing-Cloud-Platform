package com.everwing.coreservice.wy.api.configuration.task;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;
import com.everwing.coreservice.common.wy.service.configuration.task.WaterElectRebillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("waterElectRebillingApi")
public class WaterElectRebillingApi {

	@Autowired
	private WaterElectRebillingService waterElectRebillingService;

	public RemoteModelResult<MessageMap> waterElectRebilling(String companyId,List<String> ids,String userId,int meterType) {
		return new RemoteModelResult<MessageMap>(this.waterElectRebillingService.waterElectReBillingOpr(companyId, ids, userId, meterType)); 
	}
	
	public RemoteModelResult<BaseDto> rebillingCorrect(String companyId,TBsRebillingInfo entity,Integer type) {
		return new RemoteModelResult<BaseDto>(this.waterElectRebillingService.rebillingCorrect(companyId,entity,type));
	}
	
	
}
