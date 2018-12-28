package com.everwing.coreservice.wy.dao.mapper.materlog;

import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectlog;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterlog;

import java.util.List;

public interface ElectlogMapper {
    List<TcElectlog> listPage(TcElectlog  tcElectlog);

    void insert(TcElectlog  tcElectlog);
}
