package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.exception.NoExceptionProxy;
import com.everwing.coreservice.common.platform.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationApi {

    @Autowired
    private AuthorizationService authorizationService;

    /**
     * 添加授权信息
     * @param authorizedAccountCode 授权人账户编码
     * @param authorizeeAccountCode 被授权人账号编码
     * @param startTime 授权开始时间
     * @param endTime  授权结束时间
     * @param buildingId 授权的建筑资产信息
     * @return 邻音通用返回结果
     */
    @NoExceptionProxy
    public LinphoneResult authorization(String authorizedAccountCode, String authorizeeAccountCode, String startTime, String endTime, String buildingId) {
        return authorizationService.authorization(authorizedAccountCode,authorizeeAccountCode,startTime,endTime,buildingId);
    }

    /**
     * 查询授权信息
     * @param accountCode 邻音APP用户账号编码
     * @return 邻音通用返回结果
     */
    @NoExceptionProxy
    public LinphoneResult getAuthorized(String accountCode) {
        return authorizationService.queryAuthorized(accountCode);
    }

    /**
     * 查询被授权信息
     * @param accountCode 邻音APP用户账号编码
     * @return 邻音通用返回结果
     */
    @NoExceptionProxy
    public LinphoneResult getAuthorizee(String accountCode) {
        return authorizationService.queryAuthorizee(accountCode);
    }

    @NoExceptionProxy
    public LinphoneResult deleteAuthorizee(String authorizedAccountCode, String authorizeeAccountCodes, String buildingId) {
        return authorizationService.removeAuthorizee(authorizedAccountCode,authorizeeAccountCodes,buildingId);
    }
}
