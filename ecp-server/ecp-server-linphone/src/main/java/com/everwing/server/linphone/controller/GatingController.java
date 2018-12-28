package com.everwing.server.linphone.controller;

import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.platform.api.PlatformGatingApi;
import com.everwing.coreservice.wy.api.gating.GatingApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{version}/gating")
public class GatingController {

    @Autowired
    private PlatformGatingApi platformGatingApi;

    @Autowired
    private GatingApi gatingApi;

    @PostMapping("queryByMobile")
    @ApiVersion(1.0)
    public LinphoneResult queryByMobile(String accountId, String mobile){
        return platformGatingApi.getByMobile(accountId,mobile);
    }

    @PostMapping("gatingList")
    @ApiVersion(1.0)
    public LinphoneResult gatingList(String buildingId){
        return platformGatingApi.gatingList(buildingId);
    }


    /**
     * 通过公司ID和项目ID获取门口机以及门口机所在位置的楼栋结构。
     * @param companyId
     * @param projectId
     * @return
     */
    @PostMapping("getGatingStruct")
    @ApiVersion(1.0)
    public LinphoneResult getGatingStruct(String companyId,String projectId){
        RemoteModelResult RemoteModelResult = gatingApi.getGatingStruct(companyId,projectId);
        return new LinphoneResult(RemoteModelResult);
    }


    @PostMapping("getBuildingsByApartmentId")
    @ApiVersion(1.0)
    public LinphoneResult getBuildingsByApartmentId(String companyId,String projectId,String apartmentId){
        RemoteModelResult RemoteModelResult = gatingApi.getBuildingsByApartmentId(companyId,projectId,apartmentId);
        return new LinphoneResult(RemoteModelResult);
    }

    /**
     * 根据资产ID获取门口机数据
     * @param buildingId
     * @return
     */
    @PostMapping("getGatingDataByBuildingId")
    @ApiVersion(1.0)
    public LinphoneResult getGatingDataByBuildingId(String buildingId){
        RemoteModelResult Result = platformGatingApi.getCompanyByBuildingId(buildingId);
        String companyid = (String) Result.getModel();

        RemoteModelResult RemoteModelResult = gatingApi.getGatingDataByBuildingId(companyid,buildingId);
        return new LinphoneResult(RemoteModelResult);
    }

}
