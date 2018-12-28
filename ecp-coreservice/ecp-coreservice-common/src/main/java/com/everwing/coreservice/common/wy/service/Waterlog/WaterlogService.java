package com.everwing.coreservice.common.wy.service.Waterlog;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterlog;

public interface WaterlogService {

    BaseDto loadWaterlogListPage(WyBusinessContext ctx, TcWaterlog  tcWaterlog);

    void insert(WyBusinessContext ctx, TcWaterlog  tcWaterlog);
}
