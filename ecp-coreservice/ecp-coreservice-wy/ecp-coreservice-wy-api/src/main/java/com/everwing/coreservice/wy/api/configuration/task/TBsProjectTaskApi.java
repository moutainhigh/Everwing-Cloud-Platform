package com.everwing.coreservice.wy.api.configuration.task;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.service.configuration.cmac.CmacBillingService;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsProjectService;
import com.everwing.coreservice.common.wy.service.configuration.task.TBsProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tBsProjectTaskApi")
public class TBsProjectTaskApi {

	
	@Autowired
	private TBsProjectTaskService tBsProjectTaskService;
	
	@Autowired
	private TBsProjectService tBsProjectService;
	
	@Autowired
	private CmacBillingService  cmacBillingService;

	public RemoteModelResult<BaseDto> testGenNextProject(String companyId) {
		return new RemoteModelResult<BaseDto>(this.tBsProjectTaskService.genNextProject(companyId));
	}

	public RemoteModelResult<BaseDto> testCmacBilling(String companyId) {
		return new RemoteModelResult<BaseDto>(this.cmacBillingService.billing(companyId, new TBsProject()));
	}

	public RemoteModelResult<BaseDto> testAutoFlushProject(String companyId) {
		return new RemoteModelResult<BaseDto>(this.tBsProjectService.autoFlush(companyId));
	}
	
	
	
}
