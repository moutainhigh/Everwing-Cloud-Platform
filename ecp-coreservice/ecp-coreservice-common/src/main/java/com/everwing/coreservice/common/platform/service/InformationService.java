package com.everwing.coreservice.common.platform.service;

import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.Account;

public interface InformationService {

    /**
     * 修改用户手机号码 同时修改preson_cust表
     * @param accountCode
     * @return
     */
    RemoteModelResult getIdentity(String accountCode);

    /**
     * 修改用户的身份证号与姓名
     * @param account
     * @param name
     * @param id
     * @return
     */
    LinphoneResult setIdentity(Account account, String name, String id);
}
