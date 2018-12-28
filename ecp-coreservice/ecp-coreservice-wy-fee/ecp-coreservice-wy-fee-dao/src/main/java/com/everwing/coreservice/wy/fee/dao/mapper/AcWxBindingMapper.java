package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AcWxBinding;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-07-21 16:24
 **/

public interface AcWxBindingMapper {

    void insert(@Param("openId") String openId);

    int update(@Param("userId") String userId,@Param("mobile") String mobile);

    AcWxBinding queryByUserId(@Param("userId") String userId);

    AcWxBinding queryByOpenId(@Param("openId") String openId);

}
