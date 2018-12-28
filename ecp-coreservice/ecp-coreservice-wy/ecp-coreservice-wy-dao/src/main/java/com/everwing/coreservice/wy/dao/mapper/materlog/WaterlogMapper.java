package com.everwing.coreservice.wy.dao.mapper.materlog;

import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterlog;

import java.util.List;

public interface WaterlogMapper {
    List<TcWaterlog> listPage(TcWaterlog tcWaterlog);

    void insert(TcWaterlog tcWaterlog);
}
