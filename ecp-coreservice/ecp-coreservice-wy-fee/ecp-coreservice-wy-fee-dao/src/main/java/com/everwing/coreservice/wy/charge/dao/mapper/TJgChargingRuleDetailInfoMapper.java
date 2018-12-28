package com.everwing.coreservice.wy.charge.dao.mapper;

import com.everwing.coreservice.common.wy.charging.entity.TJgChargingRuleDetailInfo;

import java.util.List;

public interface TJgChargingRuleDetailInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(TJgChargingRuleDetailInfo record);

    TJgChargingRuleDetailInfo selectByPrimaryKey(String id);

    List<TJgChargingRuleDetailInfo> selectAll();

    int updateByPrimaryKey(TJgChargingRuleDetailInfo record);
}