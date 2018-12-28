package com.everwing.server.platform.controller.api;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.*;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.server.platform.constant.ResponseCode;
import com.everwing.server.platform.exception.ApiException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/comany")
public class CompanyController extends BaseApiController {
	
	@PostMapping("/approval/submitOrModify")
	public String approvalSubmit() throws Exception {
		CompanyApproval companyApproval = parseObjFromBody(CompanyApproval.class);
		String accountName = parseAccountNameFromBody();
		Integer type = parseTypeFromBody();
		
		
		// 校验表单数据
		CommonUtils.isAnyNull(companyApproval.getCompanyName(),
				companyApproval.getCompanyLocation(), companyApproval.getCompanyAddress(),
				companyApproval.getBizRegistryLicenseNum(), companyApproval.getLogoFileId(),
				companyApproval.getBizSaleLicenseFileId(), companyApproval.getOrgCodeFileId(),
				companyApproval.getTaxLicenseFileId());
		
		// 查询账号
		Account account = getModel(accountApi.queryByAccountName(accountName, type));
		// 查询是否已存在审核数据
		CompanyApprovalExample example = new CompanyApprovalExample();
		example.createCriteria().andAccountIdEqualTo(account.getAccountId());
		CompanyApproval model = null;
		try {
			model = getModel(commonQueryApi.selectOneByExample(CompanyApproval.class, example));
		} catch (Exception e) {
		}
		if (model == null) {// 插入
			companyApproval.setCompanyApprovalId(UUID.randomUUID().toString());
			companyApproval.setStatus(Dict.COMPANY_APPROVAL_STATUS_PROCESSING.getIntValue());
			companyApproval.setAccountId(account.getAccountId());
			checkSuccess(commonQueryApi.insertSelective(companyApproval));
		} else {// 更新
			model.setBizRegistryLicenseNum(companyApproval.getBizRegistryLicenseNum());
			model.setBizRegistryLicenseNumAuditTxt(null);
			model.setBizSaleLicenseFileId(companyApproval.getBizSaleLicenseFileId());
			model.setBizSaleLicenseAuditTxt(null);
			model.setCompanyAddress(companyApproval.getCompanyAddress());
			model.setCompanyAddressAuditTxt(null); 
			model.setCompanyLocation(companyApproval.getCompanyLocation());
			model.setCompanyLocationAuditTxt(null);
			model.setCompanyName(companyApproval.getCompanyName());
			model.setCompanyNameAuditTxt(null);
			model.setLogoFileId(companyApproval.getLogoFileId());
			model.setLogoAuditTxt(null);
			model.setOrgCodeFileId(companyApproval.getOrgCodeFileId());
			model.setOrgCodeAuditTxt(null);
			model.setPropertyCertFileId(companyApproval.getPropertyCertFileId());
			model.setPropertyCertAuditTxt(companyApproval.getPropertyCertAuditTxt());
			model.setTaxLicenseFileId(companyApproval.getTaxLicenseFileId());
			model.setTaxLicenseAuditTxt(null);
			model.setStatus(Dict.COMPANY_APPROVAL_STATUS_PROCESSING.getIntValue());
			checkSuccess(commonQueryApi.updateByPrimaryKey(model));
		}
		return renderSuccess();
	}
	
	@GetMapping("/approval/info")
	public String approval(String accountName,int type) {
		// 查询账号
		Account account = getModel(accountApi.queryByAccountName(accountName, type));
		// 查询审核数据
		CompanyApprovalExample example = new CompanyApprovalExample();
		example.createCriteria().andAccountIdEqualTo(account.getAccountId());
		RemoteModelResult<CompanyApproval> data = commonQueryApi.selectOneByExample(CompanyApproval.class, example);
		if(data.getModel() == null){
				throw new ApiException(ResponseCode.DATA_NOT_EXITS);
		}
		return renderJsonWithImgPath(data);
	}
	
	@GetMapping("/info")
	public String info(String accountName, int type) {
		// 查询账号
		Account account = getModel(accountApi.queryByAccountName(accountName, type));
		// 查询审核数据
		return renderJsonWithImgPath(commonQueryApi.selectByPrimaryKey(Company.class, account.getCompanyId()));
	}
	
	@GetMapping("/companyNameCheck/{companyName}")
	public String companyNameCheck(@PathVariable String companyName) {
		// 查询审核数据
		CompanyExample example = new CompanyExample();
		example.createCriteria().andCompanyNameEqualTo(companyName);
		Company company = null;
		try {
			company = getModel(commonQueryApi.selectOneByExample(Company.class, example));
		} catch (ApiException e) {
			throw e;
		} 
		
		return renderJson(company == null ? ResponseCode.SUCCESS : ResponseCode.COMPANY_EXISTED);
	}

}