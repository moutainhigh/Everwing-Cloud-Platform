package com.everwing.coreservice.common.wy.service.Electlog;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectlog;

public interface ElectlogService {

    BaseDto loadElectlogListPage(WyBusinessContext ctx, TcElectlog  tcElectlog);

    void insert(WyBusinessContext ctx, TcElectlog  tcElectlog);
}
