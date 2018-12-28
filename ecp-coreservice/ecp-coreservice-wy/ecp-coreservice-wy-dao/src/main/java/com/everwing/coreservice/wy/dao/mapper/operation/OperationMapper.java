package com.everwing.coreservice.wy.dao.mapper.operation;

import com.everwing.coreservice.common.wy.entity.operation.Operationlog;

import java.util.List;

public interface OperationMapper {
    List<Operationlog> listPage(Operationlog operationlog);

    void insert(Operationlog operationlog);
}
