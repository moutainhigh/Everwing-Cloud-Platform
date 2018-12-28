package com.everwing.coreservice.wy.charge.dao.mapper;

import com.everwing.coreservice.common.wy.charging.entity.TJfChargingScheme;

import java.util.List;

public interface TJfChargingSchemeMapper {
    int deleteByPrimaryKey(String id);

    int insert(TJfChargingScheme record);

    TJfChargingScheme selectByPrimaryKey(String id);

    List<TJfChargingScheme> selectAll();

    int updateByPrimaryKey(TJfChargingScheme record);
}