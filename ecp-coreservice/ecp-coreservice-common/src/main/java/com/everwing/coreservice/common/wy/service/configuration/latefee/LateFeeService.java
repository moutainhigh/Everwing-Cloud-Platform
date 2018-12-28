package com.everwing.coreservice.common.wy.service.configuration.latefee;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;

public interface LateFeeService {

	BaseDto billLateFee(String companyId, TBsProject project);
}
