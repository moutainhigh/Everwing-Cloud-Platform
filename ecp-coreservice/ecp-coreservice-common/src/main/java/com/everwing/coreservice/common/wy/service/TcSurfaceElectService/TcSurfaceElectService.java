package com.everwing.coreservice.common.wy.service.TcSurfaceElectService;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectMeterSearch;


public interface TcSurfaceElectService {

    /**
     * 查询电表
     * @param ctx
     * @param
     * @return
     */
    public BaseDto listPageElectMeterInfos(WyBusinessContext ctx, TcElectMeterSearch tcElectMeter);


    /**
     * 查询电表级别为非终端表
     * @param ctx
     * @param
     * @return
     */
    public BaseDto listPageParentInfos(WyBusinessContext ctx, TcElectMeterSearch tcElectMeter);

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
    MessageMap modify(WyBusinessContext ctx, TcElectMeter  tcElectMeter);

    /**新增
     * i
     * @param ctx
     * @param
     * @return
     */
    MessageMap add(WyBusinessContext ctx, TcElectMeter  tcElectMeter);
}
