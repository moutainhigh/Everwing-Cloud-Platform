package com.everwing.coreservice.wy.core.service.impl.engineeringlog;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.operation.Operationlog;
import com.everwing.coreservice.common.wy.entity.property.Engineering.Engineeringlog;
import com.everwing.coreservice.common.wy.service.engineeringlog.EngineeringlogService;
import com.everwing.coreservice.common.wy.service.operation.OperationService;
import com.everwing.coreservice.wy.dao.mapper.engineeringlog.EngineeringlogMapper;
import com.everwing.coreservice.wy.dao.mapper.operation.OperationMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("engineeringlogServiceImpl")
public class EngineeringlogServiceImpl implements EngineeringlogService{
    private static final Logger log = Logger.getLogger(EngineeringlogServiceImpl.class);

    @Autowired
    EngineeringlogMapper  engineeringlogMapper;

    @Override
    public BaseDto loadEngineeringlogListPage(WyBusinessContext ctx, Engineeringlog  engineeringlog) {
        List<Engineeringlog> list = engineeringlogMapper.listPage(engineeringlog);

        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(engineeringlog.getPage());
        return baseDto ;
    }

    @Override
    public void insert(WyBusinessContext ctx,Engineeringlog engineeringlog) {
        engineeringlog.setOperator(ctx.getStaffName());
        engineeringlogMapper.insert(engineeringlog);
    }
}
