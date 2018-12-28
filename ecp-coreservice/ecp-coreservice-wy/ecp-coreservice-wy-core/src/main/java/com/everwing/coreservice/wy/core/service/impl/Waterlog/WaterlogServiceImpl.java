package com.everwing.coreservice.wy.core.service.impl.Waterlog;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterlog;
import com.everwing.coreservice.common.wy.service.Waterlog.WaterlogService;
import com.everwing.coreservice.wy.dao.mapper.materlog.WaterlogMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("waterlogServiceImpl")
public class WaterlogServiceImpl implements WaterlogService{
    private static final Logger log = Logger.getLogger(WaterlogServiceImpl.class);

    @Autowired
    WaterlogMapper  waterlogMapper;

    @Override
    public BaseDto loadWaterlogListPage(WyBusinessContext ctx, TcWaterlog tcWaterlog) {
        List<TcWaterlog> list = waterlogMapper.listPage(tcWaterlog);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(tcWaterlog.getPage());
        return baseDto ;
    }

    @Override
    public void insert(WyBusinessContext ctx,TcWaterlog  tcWaterlog) {
        tcWaterlog.setOperator(ctx.getStaffName());
        waterlogMapper.insert(tcWaterlog);
    }
}
