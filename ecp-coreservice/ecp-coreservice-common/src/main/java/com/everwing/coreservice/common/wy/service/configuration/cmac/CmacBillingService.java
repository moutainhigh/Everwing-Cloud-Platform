package com.everwing.coreservice.common.wy.service.configuration.cmac;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;

public interface CmacBillingService {

	BaseDto billing(String companyId, TBsProject project);

}
