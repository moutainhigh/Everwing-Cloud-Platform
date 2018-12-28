package com.everwing.coreservice.common.wy.service.TcConstruction;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.property.Engineering.TcConstruction;
import com.everwing.coreservice.common.wy.entity.property.Engineering.TcConstructionSearch;


/**
 * @ClassName: TcConstructionService
 * @Description:
 * @Author:Ck
 * @Date: 2018/7/31 10:30
 **/
public  interface   TcConstructionService {
    /**
     * 查询工程施工
     * @param ctx
     * @param
     * @return
     */
      public  BaseDto  loadbyConstructionlistPage(WyBusinessContext ctx, TcConstructionSearch  tcConstruction);

    /**
     * 查询未关联水电的工程施工
     */
    public  BaseDto  loadbyWaterElectlistPage(WyBusinessContext ctx, TcConstructionSearch  tcConstruction);

    /**
     * 查询历史施工
     * @param ctx
     * @param
     * @return
     */
    public  BaseDto  loadbyEhistorylistPage(WyBusinessContext ctx, TcConstructionSearch  tcConstruction);

        /**
         * 删除工程施工
         * @param
         * @param  id
         * @return
         */
       MessageMap delete(WyBusinessContext ctx, String id);


        /**
         * 修改工程施工
         * @param ctx
         * @param  tcConstruction
         * @return
         */
        MessageMap  modify(WyBusinessContext ctx, TcConstruction  tcConstruction);

    /**
     * 修改状态为完工
     * @param ctx
     * @param  tcConstruction
     * @return
     */
    MessageMap  reviseComplete(WyBusinessContext ctx, TcConstruction  tcConstruction);

    /**
     * 修改状态为暂停
     * @param ctx
     * @param  tcConstruction
     * @return
     */
    MessageMap  suspend(WyBusinessContext ctx, TcConstruction  tcConstruction);
    /**
     * 修改状态为施工中
     * @param ctx
     * @param  tcConstruction
     * @return
     */
    MessageMap  startUp(WyBusinessContext ctx, TcConstruction  tcConstruction);
    /**
     * 修改延期时间
     * @param ctx
     * @param  tcConstruction
     * @return
     */
    MessageMap  editDelay(WyBusinessContext ctx, TcConstruction  tcConstruction);
        /**
         * 新增工程施工
         * @param ctx
         * @param
         * @return
         */
        MessageMap  add(WyBusinessContext ctx, TcConstruction  tcConstruction);
}
