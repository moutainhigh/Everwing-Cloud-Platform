package com.everwing.coreservice.wy.dao.mapper.engineeringlog;

import com.everwing.coreservice.common.wy.entity.property.Engineering.Engineeringlog;

import java.util.List;

public interface EngineeringlogMapper {
    List<Engineeringlog> listPage(Engineeringlog  engineeringlog);

    void insert(Engineeringlog  engineeringlog);
}
