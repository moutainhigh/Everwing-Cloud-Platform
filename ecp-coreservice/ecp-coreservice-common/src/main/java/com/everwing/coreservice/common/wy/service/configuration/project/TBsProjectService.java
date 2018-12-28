package com.everwing.coreservice.common.wy.service.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;

import java.util.List;

public interface TBsProjectService {

	BaseDto listPageProject(String companyId, TBsProject project);

	BaseDto findById(String companyId, String id);

	BaseDto update(String companyId, TBsProject project);

	BaseDto countProject(String companyId);

	BaseDto manualBilling(String companyId, List<TBsProject> projects);

	List<TBsProject> findCanBillingCmacProject(String companyId);

	BaseDto autoFlush(String companyId);

	BaseDto findCanGenBillProject(String companyId);

	BaseDto findCommonStatus(String companyId, String userId);
	
	BaseDto findByObj(String companyId, TBsProject project);

}
