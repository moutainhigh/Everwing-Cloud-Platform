package com.everwing.server.linphone.controller;

import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.wy.api.common.WyCommonApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 费用相关
 *
 * @author DELL shiny
 * @create 2018/4/4
 */
@RestController
@RequestMapping("{version}/fee")
public class FeeController {

    @Autowired
    private WyCommonApi wyCommonApi;

    @RequestMapping("buildingArrearsQuota")
    @ApiVersion(1.0)
    public LinphoneResult buildingArrearsQuota(String companyId,String[] buildingCodes){
        RemoteModelResult result=wyCommonApi.queryArrearageByBuildingCode(companyId,buildingCodes);
        if(result.isSuccess()){
            return new LinphoneResult(result.getModel());
        }else {
            return new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
        }
    }
}
