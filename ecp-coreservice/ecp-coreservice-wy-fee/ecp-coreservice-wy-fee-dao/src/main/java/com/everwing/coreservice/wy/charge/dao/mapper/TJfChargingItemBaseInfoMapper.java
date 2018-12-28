package com.everwing.coreservice.wy.charge.dao.mapper;

import com.everwing.coreservice.common.wy.charging.entity.TJfChargingItemBaseInfo;

import java.util.List;

public interface TJfChargingItemBaseInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(TJfChargingItemBaseInfo record);

    TJfChargingItemBaseInfo selectByPrimaryKey(String id);

    List<TJfChargingItemBaseInfo> selectAll();

    int updateByPrimaryKey(TJfChargingItemBaseInfo record);
}