package com.everwing.server.wy.api.controller;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.AccountDto;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.platform.api.AccountApi;
import com.everwing.utils.TokenGenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


/**
 * 用户controller
 * @author DELL shiny
 * @create 2017/7/4
 */
@RestController
@RequestMapping("{version}")
public class ApiUserController {

    @Autowired
    private AccountApi accountApi;

    @Value("${wyVerifySMSKeyPrefix}")
    private String wySMSCodePrefix;

    @Value("${wySMSKeyCountPrefix}")
    private String wySMSKeyCountPrefix;

    @Value("${linphoneVerifySMSMaxCount}")
    private Integer linphoneSMSMaxCount;

    @Value("${wyTokenKeyPrefix}")
    private String wyTokenKeyPrefix;

    @Value("${wyTokenExpireTime}")
    private int wyTokenExpireTime;

    @PostMapping("/register")

    @ApiVersion(1.0)
    public LinphoneResult register(String mobile, String password, String verificationCode, String sourceType) {
        Account account=new Account();
        account.setPassword(password);
        account.setAccountName(mobile);
        account.setType(Dict.ACCOUNT_TYPE_STAFF.getIntValue());
        account.setSourceType(sourceType);
        RemoteModelResult<HashMap> remoteModelResult=accountApi.registerApi(account,verificationCode,mobile,wySMSCodePrefix);
        if(remoteModelResult.isSuccess()){
            return new LinphoneResult(remoteModelResult.getModel());
        }
        return new LinphoneResult(remoteModelResult);
    }

    @PostMapping("verificationCode")
    @ApiVersion(1.0)
    public LinphoneResult verificationCode(String mobile){
        RemoteModelResult<HashMap<String,String>> remoteModelResult=accountApi.getVerifyCodeApi(mobile,wySMSCodePrefix,wySMSKeyCountPrefix,linphoneSMSMaxCount);
        if(remoteModelResult.isSuccess()){
            return new LinphoneResult(remoteModelResult.getModel());
        }
        return new LinphoneResult(remoteModelResult);
    }

    @PostMapping("login")
    @ApiVersion(1.0)
    public LinphoneResult login(String mobile,String password,String sourceType){
        RemoteModelResult<AccountDto> remoteModelResult=accountApi.loginApi(mobile,password,Dict.ACCOUNT_TYPE_STAFF.getIntValue(),sourceType,wyTokenKeyPrefix,wyTokenExpireTime);
        if(remoteModelResult.isSuccess()){
            AccountDto accountDto=remoteModelResult.getModel();
            RemoteModelResult canRegisterResult=accountApi.checkCanRegisterWyCompany(accountDto.getAccountId(),accountDto.getMobile());
            HashMap<String,String> hashMap=new HashMap<>(9);
            hashMap.put("accountName",accountDto.getAccountName());
            hashMap.put("accountCode",accountDto.getAccountCode());
            hashMap.put("accountId",accountDto.getAccountId());
            hashMap.put("token",accountDto.getToken());
            hashMap.put("icon","");
            hashMap.put("nickName","");
            hashMap.put("sex","");
            hashMap.put("signatures","");
            //已入职的不可注册
            if(canRegisterResult.isSuccess()&& !StringUtils.isNotEmpty(accountDto.getCompanyId())){
                hashMap.put("registerCompany","y");
            }else {
                hashMap.put("registerCompany","n");
            }
            return new LinphoneResult(hashMap);
        }
        return new LinphoneResult(remoteModelResult);
    }

    @PostMapping("resetPassword")
    @ApiVersion(1.0)
    public LinphoneResult resetPassword(String accountName,String password,String mobile,String verificationCode){
        RemoteModelResult remoteModelResult=accountApi.resetPasswordApi(accountName,password,mobile,Dict.ACCOUNT_TYPE_STAFF.getIntValue(),verificationCode,wySMSCodePrefix);
        if(remoteModelResult.isSuccess()){
            return new LinphoneResult(ReturnCode.API_RESOLVE_SUCCESS);
        }
        return new LinphoneResult(remoteModelResult);
    }

    @PostMapping("modifyPassword")
    @ApiVersion(1.0)
    public LinphoneResult modifyPassword(HttpServletRequest request,String accountId, String password){
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        RemoteModelResult remoteModelResult=accountApi.modifyPasswordApi(account,password);
        if(remoteModelResult.isSuccess()){
            return new LinphoneResult(ReturnCode.API_RESOLVE_SUCCESS);
        }
        return new LinphoneResult(remoteModelResult);
    }

    @PostMapping("wlResetMobile")
    @ApiVersion(1.0)
    public LinphoneResult resetPhone(HttpServletRequest request,String newMobile,String verificationCode){
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        String OldMobile = account.getMobile();
        RemoteModelResult remoteModelResult = accountApi.resetMobile(account.getAccountId(),newMobile,OldMobile,verificationCode,wySMSCodePrefix);
        if(remoteModelResult.isSuccess()){
            return new LinphoneResult(ReturnCode.API_RESOLVE_SUCCESS);
        }else {
            return new LinphoneResult(remoteModelResult);
        }
    }

}