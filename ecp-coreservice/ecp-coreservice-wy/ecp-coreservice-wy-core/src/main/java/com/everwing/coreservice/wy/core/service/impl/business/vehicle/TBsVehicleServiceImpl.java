package com.everwing.coreservice.wy.core.service.impl.business.vehicle;/**
 * Created by wust on 2017/8/1.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicle;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicleList;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicleSearch;
import com.everwing.coreservice.common.wy.service.business.vehicle.TBsVehicleService;
import com.everwing.coreservice.wy.dao.mapper.property.TVehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/1
 * @author wusongti@lii.com.cn
 */
@Service("tBsVehicleServiceImpl")
public class TBsVehicleServiceImpl implements TBsVehicleService {

    @Autowired
    private TVehicleMapper tBsVehicleMapper;

    @Override
    public BaseDto listPage(WyBusinessContext ctx, TVehicleSearch tBsVehicleSearch) {
        List<TVehicleList> list = tBsVehicleMapper.listPage(tBsVehicleSearch);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(tBsVehicleSearch.getPage());
        return baseDto ;
    }

    @Override
    public List<TVehicleList> findByCondition(WyBusinessContext ctx, TVehicleSearch tBsVehicleSearch) {
        return tBsVehicleMapper.findByCondition(tBsVehicleSearch);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap insert(WyBusinessContext ctx,TVehicle tBsVehicle) {
        MessageMap mm = new MessageMap();
        tBsVehicleMapper.insert(tBsVehicle);
        return mm;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap update(WyBusinessContext ctx,TVehicle tBsVehicle) {
        MessageMap mm = new MessageMap();
        tBsVehicleMapper.update(tBsVehicle);
        return mm;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap delete(WyBusinessContext ctx,TVehicle tBsVehicle) {
        MessageMap mm = new MessageMap();
        tBsVehicleMapper.delete(tBsVehicle);
        return mm;
    }
}
