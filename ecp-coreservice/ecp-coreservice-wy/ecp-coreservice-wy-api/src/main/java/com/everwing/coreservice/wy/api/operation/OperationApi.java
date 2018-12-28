package com.everwing.coreservice.wy.api.operation;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.operation.Operationlog;
import com.everwing.coreservice.common.wy.service.operation.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OperationApi {
    @Autowired
    private OperationService operationService;

    public RemoteModelResult<BaseDto> loadOperationListPage(WyBusinessContext ctx, Operationlog operationlog) {
        BaseDto pageResultDto = operationService.loadOperationListPage(ctx,operationlog);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(pageResultDto);
        return result;
    }

    public RemoteModelResult insert(WyBusinessContext ctx, Operationlog operationlog) {
        operationService.insert(ctx,operationlog);
        RemoteModelResult result = new RemoteModelResult<>();
        return result;
    }
}
