package com.everwing.coreservice.wy.charge.dao.mapper;

import com.everwing.coreservice.common.wy.charging.entity.TJgChargingRuleInfo;

import java.util.List;

public interface TJgChargingRuleInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(TJgChargingRuleInfo record);

    TJgChargingRuleInfo selectByPrimaryKey(String id);

    List<TJgChargingRuleInfo> selectAll();

    int updateByPrimaryKey(TJgChargingRuleInfo record);
}