package com.everwing.coreservice.common.platform.service;

import com.everwing.coreservice.common.dto.LinphoneResult;

public interface AuthorizationService {

    LinphoneResult authorization(String authorizedAccountCode, String authorizeeAccountCode, String startTime, String endTime, String buildingId);

    LinphoneResult queryAuthorized(String accountCode);

    LinphoneResult queryAuthorizee(String accountCode);

    LinphoneResult removeAuthorizee(String authorizedAccountCode, String authorizeeAccountCodes, String buildingId);
}
