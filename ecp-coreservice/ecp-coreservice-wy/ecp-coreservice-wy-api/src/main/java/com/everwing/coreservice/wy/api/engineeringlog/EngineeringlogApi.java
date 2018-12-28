package com.everwing.coreservice.wy.api.engineeringlog;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.property.Engineering.Engineeringlog;
import com.everwing.coreservice.common.wy.service.engineeringlog.EngineeringlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EngineeringlogApi {
    @Autowired
    private EngineeringlogService engineeringlogService;

    public RemoteModelResult<BaseDto> loadEngineeringlogListPage(WyBusinessContext ctx, Engineeringlog engineeringlog) {
        BaseDto pageResultDto =engineeringlogService.loadEngineeringlogListPage(ctx,engineeringlog);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(pageResultDto);
        return result;
    }

    public RemoteModelResult insert(WyBusinessContext ctx, Engineeringlog engineeringlog) {
        engineeringlogService.insert(ctx,engineeringlog);
        RemoteModelResult result = new RemoteModelResult<>();
        return result;
    }
}
