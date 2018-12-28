package com.everwing.coreservice.common.platform.service;

import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;

public interface BuildingService {
    /**
     * 通过手机号码获取资产
     * @param accountId 用户登录唯一标识
     * @param mobile 手机号码
     * @return 统一返回格式(资产集合)
     */
    LinphoneResult queryByMobile(String accountId, String mobile);

    /**
     * 通过buildingId查询资产信息
     * @param buildingId 条件
     * @return 资产信息
     */
    RemoteModelResult queryByBuildingId(String buildingId);
}
