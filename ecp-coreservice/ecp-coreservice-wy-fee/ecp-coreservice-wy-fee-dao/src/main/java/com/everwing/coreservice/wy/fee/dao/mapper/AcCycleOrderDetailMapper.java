package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.dto.OrderDetailInfoDto;
import com.everwing.coreservice.common.wy.fee.entity.AcCycleOrderDetail;

import java.util.List;
import java.util.Map;

public interface AcCycleOrderDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcCycleOrderDetail record);

    int insertSelective(AcCycleOrderDetail record);

    AcCycleOrderDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcCycleOrderDetail record);

    int updateByPrimaryKey(AcCycleOrderDetail record);

    List<Map<String,Object>> selectBySearchObj(OrderDetailInfoDto orderDetailInfoDto);
}