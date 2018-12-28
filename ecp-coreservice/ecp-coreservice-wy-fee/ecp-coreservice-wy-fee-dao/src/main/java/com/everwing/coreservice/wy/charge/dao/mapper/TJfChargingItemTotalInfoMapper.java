package com.everwing.coreservice.wy.charge.dao.mapper;

import com.everwing.coreservice.common.wy.charging.entity.TJfChargingItemTotalInfo;

import java.util.List;

public interface TJfChargingItemTotalInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(TJfChargingItemTotalInfo record);

    TJfChargingItemTotalInfo selectByPrimaryKey(String id);

    List<TJfChargingItemTotalInfo> selectAll();

    int updateByPrimaryKey(TJfChargingItemTotalInfo record);
}