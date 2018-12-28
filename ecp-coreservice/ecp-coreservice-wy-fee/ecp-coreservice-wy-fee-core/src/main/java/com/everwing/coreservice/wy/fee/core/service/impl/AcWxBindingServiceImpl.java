package com.everwing.coreservice.wy.fee.core.service.impl;

import com.everwing.coreservice.common.wy.fee.entity.AcWxBinding;
import com.everwing.coreservice.common.wy.fee.service.AcWxBindingService;
import com.everwing.coreservice.wy.fee.dao.mapper.AcWxBindingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-07-21 16:19
 **/
@Service
public class AcWxBindingServiceImpl implements AcWxBindingService {

    @Autowired
    private AcWxBindingMapper acWxBindingMapper;

    @Override
    public String bindOpenIdAndUserId(String companyId, String openId) {
        acWxBindingMapper.insert(openId);
        return acWxBindingMapper.queryByOpenId(openId).getUserId();
    }

    @Override
    public int bindUserAndMobile(String companyId, String userId, String mobile) {
        return acWxBindingMapper.update(userId,mobile);
    }

    @Override
    public AcWxBinding getAcWxBindingByUserId(String companyId, String userId) {
        return acWxBindingMapper.queryByUserId(userId);
    }
}
