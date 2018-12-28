package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.fee.entity.AcAccount;

public interface AcAccountMapper {
    
    int updateByPrimaryKeyForAmount(AcAccount record);

    AcAccount selectByHouseCodeNew(String houseCodeNew);

}