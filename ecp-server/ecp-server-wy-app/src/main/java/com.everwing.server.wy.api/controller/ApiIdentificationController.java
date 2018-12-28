package com.everwing.server.wy.api.controller;

import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.platform.api.IdentityApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实名认证
 * @author DELL shiny
 * @create 2017/7/4
 */
@RestController
@RequestMapping("{version}/identification")
public class ApiIdentificationController {

    @Autowired
    private IdentityApi identityApi;

    @PostMapping("add")
    @ApiVersion(1.0)
    public LinphoneResult add(String accountId,String identityCode,String identityType,String realName,String mobile){
        return identityApi.addIdentityInfo(accountId,identityCode,identityType,realName,mobile);
    }

    @PostMapping("modify")
    @ApiVersion(1.0)
    public LinphoneResult modify(String identityId,String accountId,String identityCode,
                            String identityType,String realName,String mobile){
        return identityApi.modifyIdentityInfo(identityId,accountId,identityCode,identityType,realName,mobile);
    }

    @PostMapping("queryByAccountId")
    @ApiVersion(1.0)
    public LinphoneResult queryByAccountId(String accountId){
        return new LinphoneResult(identityApi.getByAccountId(accountId));
    }

}
