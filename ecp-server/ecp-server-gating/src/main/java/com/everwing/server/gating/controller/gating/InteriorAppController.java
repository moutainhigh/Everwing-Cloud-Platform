package com.everwing.server.gating.controller.gating;

import com.everwing.coreservice.common.constant.ResponseCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.platform.api.AccountApi;
import com.everwing.coreservice.platform.api.LinPhoneApi;
import com.everwing.coreservice.wy.api.gating.GatingApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 室内机接口
 */
@RestController
@RequestMapping("/indoorpad/api/{version}")
public class InteriorAppController {

    @Autowired
    private GatingApi gatingApi;

    @Autowired
    private AccountApi accountApi;

    @Autowired
    private LinPhoneApi linPhoneApi;

    @PostMapping("login")
    @ApiVersion(1.0)
    public LinphoneResult login(String houseAccount, String password){
        Account account=accountApi.queryByAccountName(houseAccount, Dict.ACCOUNT_TYPE_HOUSE.getIntValue()).getModel();
        if(account==null){
            return new LinphoneResult(ResponseCode.LOGIN_FAILURE);
        }
        return gatingApi.interiorLogin(account);
    }

    @PostMapping("checkUpdate")
    @ApiVersion(1.0)
    public LinphoneResult checkUpdate(String version, String type){
        return linPhoneApi.checkUpdate(version,type);
    }
}
