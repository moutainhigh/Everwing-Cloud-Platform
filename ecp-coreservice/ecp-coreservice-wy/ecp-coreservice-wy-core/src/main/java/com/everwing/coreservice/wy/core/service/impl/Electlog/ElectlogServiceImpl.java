package com.everwing.coreservice.wy.core.service.impl.Electlog;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectlog;
import com.everwing.coreservice.common.wy.service.Electlog.ElectlogService;
import com.everwing.coreservice.wy.dao.mapper.materlog.ElectlogMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("electlogServiceImpl")
public class ElectlogServiceImpl implements ElectlogService{
    private static final Logger log = Logger.getLogger(ElectlogServiceImpl.class);

    @Autowired
    ElectlogMapper   electlogMapper;

    @Override
    public BaseDto loadElectlogListPage(WyBusinessContext ctx, TcElectlog  tcElectlog) {
        List<TcElectlog> list = electlogMapper.listPage(tcElectlog);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(tcElectlog.getPage());
        return baseDto ;
    }

    @Override
    public void insert(WyBusinessContext ctx,TcElectlog  tcElectlog) {
        tcElectlog.setOperator(ctx.getStaffName());
        electlogMapper.insert(tcElectlog);
    }
}
