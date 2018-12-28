package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.NoExceptionProxy;
import com.everwing.coreservice.common.platform.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingApi {

    @Autowired
    private BuildingService buildingService;
    /**
     * 通过手机号码获取资产
     * @param accountId 用户登录唯一标识
     * @param mobile 手机号码
     * @return 统一返回结果(资产集合)
     */
    @NoExceptionProxy
    public LinphoneResult getByMobile(String accountId, String mobile) {
        return buildingService.queryByMobile(accountId,mobile);
    }

    public RemoteModelResult getByBuildingId(String buildingId) {
        return buildingService.queryByBuildingId(buildingId);
    }
}
