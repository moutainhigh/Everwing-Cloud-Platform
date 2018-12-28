package com.everwing.server.linphone.controller;

import com.everwing.coreservice.common.constant.ResponseCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.platform.api.IdentityApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{version}/identification")
public class IdentityController {

    @Autowired
    private IdentityApi identityApi;

    @PostMapping(value = "/add")
    @ApiVersion(1.0)
    public LinphoneResult add(String accountId, String identityCode, String identityType, String realName, String mobile){
        return identityApi.addIdentityInfo(accountId,identityCode,identityType,realName,mobile);
    }

    @PostMapping(value = "modify")
    @ApiVersion(1.0)
    public LinphoneResult modify(String identityId, String accountId, String identityCode, String identityType, String realName, String mobile){
        return identityApi.modifyIdentityInfo(identityId,accountId,identityCode,identityType,realName,mobile);
    }

    @PostMapping(value = "queryByAccountId")
    @ApiVersion(1.0)
    public LinphoneResult queryByAccountId(String accountId){
        return new LinphoneResult(identityApi.getByAccountId(accountId));
    }

    @PostMapping(value = "queryById")
    @ApiVersion(1.0)
    public LinphoneResult queryById(String identityId){
        return identityApi.getById(identityId);
    }

    @PostMapping(value = "delete")
    @ApiVersion(1.0)
    public LinphoneResult delete(String identityId, String accountId){
        identityApi.remove(identityId);
        return new LinphoneResult(ResponseCode.RESOLVE_SUCCESS);
    }
}
