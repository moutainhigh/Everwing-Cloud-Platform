package com.everwing.server.wy.api.controller;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.CompanyApproval;
import com.everwing.coreservice.common.platform.entity.generated.CompanyApprovalExample;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.platform.api.AccountApi;
import com.everwing.coreservice.platform.api.CommonQueryApi;
import com.everwing.coreservice.platform.api.FastDFSApi;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 公司
 *
 * @author DELL shiny
 * @create 2017/7/4
 */
@RestController
@RequestMapping("{version}/company")
public class ApiCompanyController {

    private static final Logger logger= LogManager.getLogger(ApiCompanyController.class);

    @Autowired
    private AccountApi accountApi;

    @Autowired
    private CommonQueryApi commonQueryApi;

    @Autowired
    private FastDFSApi fastDFSApi;

    @PostMapping("register")
    @ApiVersion(1.0)
    public LinphoneResult register(String companyName, String companyLocation, String companyAddress,
                                   String bizRegistryLicenseNum, String logoFileId, String bizSaleLicenseFileId, String orgCodeFileId,
                                   String taxLicenseFileId, String propertyCertFileId,HttpServletRequest request){
        Account account= JSON.parseObject((String) request.getAttribute("account"),Account.class);
        RemoteModelResult remoteModelResult=accountApi.checkCanRegisterWyCompany(account.getAccountId(),account.getMobile());
        if(remoteModelResult.isSuccess()) {
            CompanyApproval companyApproval = new CompanyApproval();
            companyApproval.setCompanyName(companyName);
            companyApproval.setCompanyLocation(companyLocation);
            companyApproval.setCompanyAddress(companyAddress);
            companyApproval.setBizRegistryLicenseNum(bizRegistryLicenseNum);
            companyApproval.setLogoFileId(logoFileId);
            companyApproval.setBizSaleLicenseFileId(bizSaleLicenseFileId);
            companyApproval.setOrgCodeFileId(orgCodeFileId);
            companyApproval.setTaxLicenseFileId(taxLicenseFileId);
            companyApproval.setPropertyCertFileId(propertyCertFileId);
            // 查询是否已存在审核数据
            CompanyApprovalExample example = new CompanyApprovalExample();
            example.createCriteria().andAccountIdEqualTo(account.getAccountId());
            CompanyApproval approval = null;
            approval = commonQueryApi.selectOneByExample(CompanyApproval.class, example).getModel();
            if (approval == null) {// 插入
                companyApproval.setCreateDate(new Date());
                companyApproval.setCompanyApprovalId(UUID.randomUUID().toString());
                companyApproval.setStatus(Dict.COMPANY_APPROVAL_STATUS_PROCESSING.getIntValue());
                companyApproval.setAccountId(account.getAccountId());
                int count = commonQueryApi.insertSelective(companyApproval).getModel();
                if (count == 1) {
                    logger.debug("提交公司待审核信息成功!");
                    return new LinphoneResult(ReturnCode.API_RESOLVE_SUCCESS);
                }
            } else {// 更新
                approval.setBizRegistryLicenseNum(companyApproval.getBizRegistryLicenseNum());
                approval.setBizSaleLicenseFileId(companyApproval.getBizSaleLicenseFileId());
                approval.setCompanyAddress(companyApproval.getCompanyAddress());
                approval.setCompanyLocation(companyApproval.getCompanyLocation());
                approval.setCompanyName(companyApproval.getCompanyName());
                approval.setLogoFileId(companyApproval.getLogoFileId());
                approval.setOrgCodeFileId(companyApproval.getOrgCodeFileId());
                approval.setPropertyCertFileId(companyApproval.getPropertyCertFileId());
                approval.setPropertyCertAuditTxt(companyApproval.getPropertyCertAuditTxt());
                approval.setTaxLicenseFileId(companyApproval.getTaxLicenseFileId());
                approval.setStatus(Dict.COMPANY_APPROVAL_STATUS_PROCESSING.getIntValue());
                int count = commonQueryApi.updateByPrimaryKey(approval).getModel();
                if (count == 1) {
                    logger.debug("提交公司待审核信息成功,已查询到公司审核信息(修改)!");
                    return new LinphoneResult(ReturnCode.API_RESOLVE_SUCCESS);
                }
            }
        }else {
            logger.info("此账号不可注册公司");
            return new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
        }
        logger.debug("提交公司待审核信息失败!");
        return new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
    }

    @PostMapping("approvalInfo")
    @ApiVersion(1.0)
    public LinphoneResult approvalInfo(HttpServletRequest request){
        // 当前登录账号
        Account account= JSON.parseObject((String) request.getAttribute("account"),Account.class);
        // 查询审核数据
        CompanyApprovalExample example = new CompanyApprovalExample();
        example.createCriteria().andAccountIdEqualTo(account.getAccountId());
        RemoteModelResult<CompanyApproval> data = commonQueryApi.selectOneByExample(CompanyApproval.class, example);
        if(data.getModel() == null){
            logger.debug("未查到公司注册数据审核信息!");
            return new LinphoneResult(ReturnCode.API_RESOLVE_SUCCESS);
        }
        logger.debug("查询公司注册数据审核信息成功!");
        return new LinphoneResult(data.getModel());
    }

    @PostMapping("modify")
    @ApiVersion(1.0)
    public LinphoneResult modify(HttpServletRequest request,String companyName,String companyLocation,String companyAddress,
                            String bizRegistryLicenseNum,String logoFileId,String bizSaleLicenseFileId,String orgCodeFileId,
                            String taxLicenseFileId,String propertyCertFileId,String companyId){
        Account account= JSON.parseObject((String) request.getAttribute("account"),Account.class);
        CompanyApproval companyApproval = new CompanyApproval();
        companyApproval.setCompanyApprovalId(companyId);
        companyApproval.setCompanyName(companyName);
        companyApproval.setCompanyLocation(companyLocation);
        companyApproval.setCompanyAddress(companyAddress);
        companyApproval.setBizRegistryLicenseNum(bizRegistryLicenseNum);
        companyApproval.setLogoFileId(logoFileId);
        companyApproval.setBizSaleLicenseFileId(bizSaleLicenseFileId);
        companyApproval.setOrgCodeFileId(orgCodeFileId);
        companyApproval.setTaxLicenseFileId(taxLicenseFileId);
        companyApproval.setPropertyCertFileId(propertyCertFileId);
        companyApproval.setAccountId(account.getAccountId());
        int count=commonQueryApi.updateByPrimaryKeySelective(companyApproval).getModel();
        if(count==1){
            logger.debug("{}修改公司注册信息成功！",companyId);
            return new LinphoneResult(ReturnCode.API_RESOLVE_SUCCESS);
        }
        logger.debug("{}修改公司注册信息失败，未找到公司注册信息!",companyId);
        return new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
    }

    @PostMapping("info")
    @ApiVersion(1.0)
    public LinphoneResult info(HttpServletRequest request){
        Account account= JSON.parseObject((String) request.getAttribute("account"),Account.class);
        CompanyApprovalExample example = new CompanyApprovalExample();
        example.createCriteria().andAccountIdEqualTo(account.getAccountId()).andStatusEqualTo(1);
        List<CompanyApproval> companyApprovalList = commonQueryApi.selectListByExample(CompanyApproval.class, example).getModel();
        if(companyApprovalList==null){
            logger.debug("未查询到注册公司信息!");
        }
        logger.debug("查询注册信息成功!");
        return new LinphoneResult(companyApprovalList);
    }

    @PostMapping("uploadFile")
    @ApiVersion(1.0)
    public LinphoneResult uploadFile(MultipartFile uploadFile) throws Exception {
        RemoteModelResult<UploadFile> remoteModelResult=fastDFSApi.uploadFile(uploadFile);
        if(remoteModelResult.isSuccess()) {
            UploadFile file=remoteModelResult.getModel();
            if(StringUtils.isNotEmpty(file.getUploadFileId())) {
                logger.debug("上传文件成功!");
                HashMap<String,String> hashMap=new HashMap<>(1);
                hashMap.put("fileId",file.getUploadFileId());
                return new LinphoneResult(hashMap);
            }
        }
        logger.debug("上传文件失败!");
        return new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
    }
}
