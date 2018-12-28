package com.everwing.coreservice.wy.api.configuration.task;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.service.configuration.task.WyBillingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("wyBillingTaskApi")
public class WyBillingTaskApi {

	@Autowired
	private WyBillingTaskService wyBillingTaskService;

	public RemoteModelResult<BaseDto> manaulBilling(String companyId, TBsProject entity,Integer type) {
		return new RemoteModelResult<BaseDto>(this.wyBillingTaskService.manualBilling(companyId, entity,type));
	}
	
	
	public RemoteModelResult<BaseDto> autoBilling(String companyId,Integer type) {
		return new RemoteModelResult<BaseDto>(this.wyBillingTaskService.autoBilling(companyId,type)); 
	}


	public RemoteModelResult<BaseDto> reBillingOpr(String companyId,List<String> ids,String userId, Integer type) {
		return new RemoteModelResult<BaseDto>(this.wyBillingTaskService.reBillingOpr(companyId,ids,userId,type)); 
	}
	
	
}
