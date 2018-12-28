package com.everwing.coreservice.common.wy.service.business.vehicle;/**
 * Created by wust on 2017/8/1.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicle;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicleList;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicleSearch;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/1
 * @author wusongti@lii.com.cn
 */
public interface TBsVehicleService {
    BaseDto  listPage(WyBusinessContext ctx,TVehicleSearch tBsVehicleSearch);

    List<TVehicleList> findByCondition(WyBusinessContext ctx, TVehicleSearch tBsVehicleSearch);

    MessageMap insert(WyBusinessContext ctx,TVehicle tBsVehicle);

    MessageMap update(WyBusinessContext ctx,TVehicle tBsVehicle);

    MessageMap delete(WyBusinessContext ctx,TVehicle tBsVehicle);
}
