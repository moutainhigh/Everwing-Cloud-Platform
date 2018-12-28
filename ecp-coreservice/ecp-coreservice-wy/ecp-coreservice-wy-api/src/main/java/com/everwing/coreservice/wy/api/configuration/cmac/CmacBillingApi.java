package com.everwing.coreservice.wy.api.configuration.cmac;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.service.configuration.cmac.CmacBillingService;
import com.everwing.coreservice.common.wy.service.configuration.latefee.LateFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("cmacBillingApi")
public class CmacBillingApi {

	@Autowired
	private CmacBillingService cmacBillingService;
	
	@Autowired
	private LateFeeService lateFeeService;

	public RemoteModelResult<BaseDto> testDk(String companyId,TBsProject project) {
		return new RemoteModelResult<BaseDto>(this.cmacBillingService.billing(companyId, project));
	}

	public RemoteModelResult<BaseDto> testLateFee(String companyId,TBsProject project) {
		return new RemoteModelResult<BaseDto>(this.lateFeeService.billLateFee(companyId,project));
	}
	
	
}
