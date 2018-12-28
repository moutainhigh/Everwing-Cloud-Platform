package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.platform.entity.generated.CompanyApproval;
import com.everwing.coreservice.common.platform.entity.generated.CompanyExample;
import com.everwing.coreservice.platform.api.util.ServiceResources;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyApi extends ServiceResources{

    public RemoteModelResult<Company> createCompany(CompanyApproval companyApproval) {
    	return companyService.createCompany(companyApproval);
    }
    
    public RemoteModelResult<List<Company>> queryAllCompany(){
    	return commonService.selectListByExample(Company.class, new CompanyExample());
    }

    public RemoteModelResult<Company> queryCompany(String companyId){
    	return commonService.selectByPrimaryKey(Company.class, companyId);
    }

	public RemoteModelResult<Company> queryCompanyByUserLoginName(String accountName, int type) {
		Account account = accountService.queryByAccountName(accountName, type).getModel();
		return commonService.selectByPrimaryKey(Company.class, account.getCompanyId());
	}

    
    public RemoteModelResult<Company> queryCompanyByUserLoginNameAndPsw(String accountName, int type, String psw) {
    	logger.info("CompanyApi.queryCompanyByUserLoginNameAndPsw()");
    	Account account = accountService.queryByAccountNameAndPsw(accountName, type, psw).getModel();
    	return commonService.selectByPrimaryKey(Company.class, account.getCompanyId());
    }
}