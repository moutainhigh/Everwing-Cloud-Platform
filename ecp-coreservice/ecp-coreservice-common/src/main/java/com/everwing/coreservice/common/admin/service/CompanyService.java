package com.everwing.coreservice.common.admin.service;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.platform.entity.generated.CompanyApproval;

public interface CompanyService {
	
	public RemoteModelResult<Company> createCompany(CompanyApproval companyApproval);
}
