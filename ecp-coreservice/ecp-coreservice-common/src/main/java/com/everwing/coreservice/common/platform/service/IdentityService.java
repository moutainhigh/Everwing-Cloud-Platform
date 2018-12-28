package com.everwing.coreservice.common.platform.service;

import com.everwing.coreservice.common.dto.LinphoneResult;

import java.util.List;
import java.util.Map;

public interface IdentityService {
    /**
     * 添加资产身份信息
     * @param accountId 账号唯一id
     * @param identityCode 证件号码
     * @param identityType 证件类型
     * @param realName 真实姓名
     * @param mobile 手机号码
     * @return 操作是否成功
     */
    LinphoneResult insert(String accountId, String identityCode, String identityType, String realName, String mobile);

    /**
     * 修改资产身份信息
     * @param id 资产身份信息id
     * @param accountId 账号唯一id
     * @param identityCode 证件号码
     * @param identityType 证件类型
     * @param realName 真实姓名
     * @param mobile 手机号码
     * @return 操作是否成功
     */
    LinphoneResult update(String id, String accountId, String identityCode, String identityType, String realName, String mobile);

    /**
     * 根据资产身份id查询资产身份信息
     * @param accountId 登录账号id
     * @return 资产身份信息
     */
    List<Map<String,String>> queryByAccountId(String accountId);

    /**
     * 删除资产身份信息
     * @param identityId 资产身份信息
     */
    boolean deleteByKey(String identityId);

    /**
     * 根据资产身份Id查询资产身份信息
     * @param identityId 资产身份id
     * @return
     */
    LinphoneResult queryById(String identityId);


}
