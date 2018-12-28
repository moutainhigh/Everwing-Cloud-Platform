package com.everwing.coreservice.common.wy.fee.service;

import com.everwing.coreservice.common.wy.fee.entity.AcWxBinding;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-07-21 16:21
 **/

public interface AcWxBindingService {

    /**
     * 绑定微信小程序登录后返回的openId,返回系统生成的UserId
     * @param openId
     * @return userId
     */
    public String bindOpenIdAndUserId(String companyId,String openId);

    /**
     * 绑定微信小程序userId和手机号码
     * @param userId
     * @param mobile
     * @return
     */
    public int bindUserAndMobile(String companyId,String userId,String mobile);

    /**
     * 通过userId查询对应的小程序绑定信息
     * @param userId
     * @return
     */
    public AcWxBinding getAcWxBindingByUserId(String companyId,String userId);
}
