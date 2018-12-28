package com.everwing.coreservice.wy.charge.dao.mapper;

import com.everwing.coreservice.common.wy.charging.entity.TJfAssetsChargingDetailInfo;

import java.util.List;

public interface TJfAssetsChargingDetailInfoMapper {

    int deleteByPrimaryKey(String id);

    int insert(TJfAssetsChargingDetailInfo record);

    TJfAssetsChargingDetailInfo selectByPrimaryKey(String id);

    List<TJfAssetsChargingDetailInfo> selectAll();

    int updateByPrimaryKey(TJfAssetsChargingDetailInfo record);
}