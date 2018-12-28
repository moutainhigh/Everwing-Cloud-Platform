package com.everwing.coreservice.common.wy.service.operation;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.operation.Operationlog;

public interface OperationService {

    BaseDto loadOperationListPage(WyBusinessContext ctx, Operationlog operationlog);

    void insert(WyBusinessContext ctx, Operationlog operationlog);
}
