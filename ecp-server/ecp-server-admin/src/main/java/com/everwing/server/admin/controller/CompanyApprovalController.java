package com.everwing.server.admin.controller;

import com.everwing.coreservice.common.admin.util.PageBean;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.CompanyApproval;
import com.everwing.coreservice.wy.api.sys.TSysRoleApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/companyApproval")
public class CompanyApprovalController extends BaseController {

	@Autowired
	private TSysRoleApi tSysRoleApi;
	
	@GetMapping("/main")
	public String index() {
		return toAdminView("company_approval");
	}
	
	@PostMapping("/list")
	public @ResponseBody String list(PageBean pageBean) {
		addParamToPageBean(pageBean, "query_content", "status");
		return renderAdminJson(commonAdminApi.listCompanyApprovalByPage(pageBean));
	}


	/**
	 * @description 公司审核
	 */
	@PostMapping("/update")
	public @ResponseBody String update(CompanyApproval companyApproval) {
		CompanyApproval model = getModel(commonQueryApi.selectByPrimaryKey(CompanyApproval.class, companyApproval.getCompanyApprovalId()));
		if(Dict.COMPANY_APPROVAL_STATUS_SUCCESS.getIntValue() == companyApproval.getStatus()){
			
			model.setCompanyAddressAuditTxt(null);
			model.setBizRegistryLicenseNumAuditTxt(null);
			model.setBizSaleLicenseAuditTxt(null);
			model.setTaxLicenseAuditTxt(null);
			model.setOrgCodeAuditTxt(null);
			model.setPropertyCertAuditTxt(null);
			model.setStatus(Dict.COMPANY_APPROVAL_STATUS_SUCCESS.getIntValue());
			handleResult(commonQueryApi.updateByPrimaryKey(model));
			
			//创建公司
			companyApi.createCompany(model);
		}else{
			model.setCompanyAddressAuditTxt(companyApproval.getCompanyAddressAuditTxt());
			model.setCompanyNameAuditTxt(companyApproval.getCompanyNameAuditTxt());
			model.setLogoAuditTxt(companyApproval.getLogoAuditTxt());
			model.setBizRegistryLicenseNumAuditTxt(companyApproval.getBizRegistryLicenseNumAuditTxt());
			model.setBizSaleLicenseAuditTxt(companyApproval.getBizSaleLicenseAuditTxt());
			model.setTaxLicenseAuditTxt(companyApproval.getTaxLicenseAuditTxt());
			model.setOrgCodeAuditTxt(companyApproval.getOrgCodeAuditTxt());
			model.setPropertyCertAuditTxt(companyApproval.getPropertyCertAuditTxt());
			model.setStatus(Dict.COMPANY_APPROVAL_STATUS_FAIL.getIntValue());
			model.setCreateDate(new Date());
			handleResult(commonQueryApi.updateByPrimaryKey(model));
		}
		
		return renderSuccess();
	}
}