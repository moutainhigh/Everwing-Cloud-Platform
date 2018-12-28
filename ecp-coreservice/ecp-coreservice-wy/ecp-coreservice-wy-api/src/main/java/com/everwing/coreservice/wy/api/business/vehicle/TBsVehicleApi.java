package com.everwing.coreservice.wy.api.business.vehicle;/**
 * Created by wust on 2017/8/1.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicle;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicleList;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicleSearch;
import com.everwing.coreservice.common.wy.service.business.vehicle.TBsVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/1
 * @author wusongti@lii.com.cn
 */
@Component
public class TBsVehicleApi {
    @Autowired
    private TBsVehicleService tBsVehicleService;

    public RemoteModelResult<BaseDto> listPage(WyBusinessContext ctx, TVehicleSearch tBsVehicleSearch){
        BaseDto baseDto = tBsVehicleService.listPage(ctx,tBsVehicleSearch);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(baseDto);
        return result;
    }

    public RemoteModelResult<List<TVehicleList>> findByCondition(WyBusinessContext ctx, TVehicleSearch tBsVehicleSearch){
        List<TVehicleList> tBsVehicleLists = tBsVehicleService.findByCondition(ctx,tBsVehicleSearch);
        RemoteModelResult<List<TVehicleList>> result = new RemoteModelResult<>();
        result.setModel(tBsVehicleLists);
        return result;
    }

    public RemoteModelResult<MessageMap> insert(WyBusinessContext ctx,TVehicle tBsVehicle){
        MessageMap mm = tBsVehicleService.insert(ctx,tBsVehicle);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(mm);
        return result;
    }

    public RemoteModelResult<MessageMap> update(WyBusinessContext ctx,TVehicle tBsVehicle){
        MessageMap mm = tBsVehicleService.update(ctx,tBsVehicle);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(mm);
        return result;
    }

    public RemoteModelResult<MessageMap> delete(WyBusinessContext ctx,TVehicle tBsVehicle){
        MessageMap mm = tBsVehicleService.delete(ctx,tBsVehicle);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(mm);
        return result;
    }
}
