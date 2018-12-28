package com.everwing.coreservice.common.wy.service.TcSurfaceWater;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeterSearch;

public interface TcSurfaceWaterService {

    /**
     * 查询水表
     * @param ctx
     * @param
     * @return
     */
    public BaseDto listPageWaterMeterInfos(WyBusinessContext ctx, TcWaterMeterSearch tcWaterMeter);


    /**
     * 水表结构图
     * @param ctx
     * @param
     * @return
     */
    public BaseDto listPageWaterTree(WyBusinessContext ctx, TcWaterMeterSearch tcWaterMeterSearch);
    /**
     * 查询父表编码为非终端表
     * @param ctx
     * @param
     * @return
     */
    public BaseDto listPageParentCodeInfos(WyBusinessContext ctx, TcWaterMeterSearch tcWaterMeter);
    /**
     * 删除
     * @param ctx
     * @param id
     * @return
     */
    MessageMap delete(WyBusinessContext ctx, String id);

    /**
     * 修改
     * @param ctx
     * @param
     * @return
     */
    MessageMap modify(WyBusinessContext ctx, TcWaterMeter tcWaterMeter);

    /**新增
     * i
     * @param ctx
     * @param
     * @return
     */
    MessageMap add(WyBusinessContext ctx, TcWaterMeter tcWaterMeter);
}
