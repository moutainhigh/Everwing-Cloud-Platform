package com.everwing.coreservice.wy.core.service.impl.operation;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.operation.Operationlog;
import com.everwing.coreservice.common.wy.service.operation.OperationService;
import com.everwing.coreservice.wy.dao.mapper.operation.OperationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import java.util.List;

@Service("OperationServiceImpl")
public class OperationServiceImpl implements OperationService{
    private static final Logger log = Logger.getLogger(OperationServiceImpl.class);

    @Autowired OperationMapper operationMapper;

    @Override
    public BaseDto loadOperationListPage(WyBusinessContext ctx, Operationlog operationlog) {
        List<Operationlog> list = operationMapper.listPage(operationlog);

        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(operationlog.getPage());
        return baseDto ;
    }

    @Override
    public void insert(WyBusinessContext ctx, Operationlog operationlog) {
        operationlog.setOperator(ctx.getStaffName());
        operationMapper.insert(operationlog);
    }
}
