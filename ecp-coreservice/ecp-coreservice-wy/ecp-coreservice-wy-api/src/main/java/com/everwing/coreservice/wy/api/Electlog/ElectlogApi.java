package com.everwing.coreservice.wy.api.Electlog;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectlog;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterlog;
import com.everwing.coreservice.common.wy.service.Electlog.ElectlogService;
import com.everwing.coreservice.common.wy.service.Waterlog.WaterlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ElectlogApi {
    @Autowired
    private ElectlogService  electlogService;

    public RemoteModelResult<BaseDto> loadElectlogListPage(WyBusinessContext ctx, TcElectlog tcElectlog) {
        BaseDto pageResultDto =electlogService.loadElectlogListPage(ctx,tcElectlog);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(pageResultDto);
        return result;
    }

    public RemoteModelResult insert(WyBusinessContext ctx, TcElectlog tcElectlog) {
        electlogService.insert(ctx,tcElectlog);
        RemoteModelResult result = new RemoteModelResult<>();
        return result;
    }
}
