package com.everwing.coreservice.common.wy.service.engineeringlog;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.property.Engineering.Engineeringlog;

public interface EngineeringlogService {

    BaseDto loadEngineeringlogListPage(WyBusinessContext ctx, Engineeringlog  engineeringlog);

    void insert(WyBusinessContext ctx, Engineeringlog  engineeringlog);
}
