package com.everwing.coreservice.wy.api.WaterlogApi;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterlog;
import com.everwing.coreservice.common.wy.service.Waterlog.WaterlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaterlogApi {
    @Autowired
    private WaterlogService  waterlogService;

    public RemoteModelResult<BaseDto> loadWaterlogListPage(WyBusinessContext ctx, TcWaterlog  tcWaterlog) {
        BaseDto pageResultDto =waterlogService.loadWaterlogListPage(ctx,tcWaterlog);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(pageResultDto);
        return result;
    }

    public RemoteModelResult insert(WyBusinessContext ctx, TcWaterlog  tcWaterlog) {
        waterlogService.insert(ctx,tcWaterlog);
        RemoteModelResult result = new RemoteModelResult<>();
        return result;
    }
}
