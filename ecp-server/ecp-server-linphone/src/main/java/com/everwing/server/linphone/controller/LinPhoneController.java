package com.everwing.server.linphone.controller;

import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.platform.api.LinPhoneApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{version}")
public class LinPhoneController {

    @Autowired
    private LinPhoneApi linPhoneApi;

    @PostMapping(value="checkUpdate")
    @ApiVersion(1.0)
    public LinphoneResult checkUpdate(String version, String type){
        return linPhoneApi.checkUpdate(version,type);
    }

    @PostMapping(value = "modifyDoorPassword")
    @ApiVersion(1.0)
    public LinphoneResult modifyDoorPassword(String buildingCode, String password){
        return linPhoneApi.modifyDoorPassword(buildingCode,password);
    }

}
