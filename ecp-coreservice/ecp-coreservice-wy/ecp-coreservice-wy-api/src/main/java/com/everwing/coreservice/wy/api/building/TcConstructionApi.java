package com.everwing.coreservice.wy.api.building;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.property.Engineering.TcConstruction;
import com.everwing.coreservice.common.wy.entity.property.Engineering.TcConstructionSearch;
import com.everwing.coreservice.common.wy.service.TcConstruction.TcConstructionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: TcConstructionApi
 * @Author:Ck
 * @Date: 2018/7/31
 **/

@Component
public class TcConstructionApi {
    @Autowired
    private TcConstructionService tcConstructionService;


    /**
     * 查询
     *
     * @param ctx
     * @param tcConstruction
     * @return
     * @throws Exception
     */

    public RemoteModelResult<BaseDto> loadbyConstructionlistPage(WyBusinessContext ctx, TcConstructionSearch tcConstruction) {
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(tcConstructionService.loadbyConstructionlistPage(ctx, tcConstruction));
        return result;
    }

    /**
     * 查询未关联水电的工程施工
     *
     * @param ctx
     * @param tcConstruction
     * @return
     * @throws Exception
     */

    public RemoteModelResult<BaseDto> loadbyWaterElectlistPage(WyBusinessContext ctx, TcConstructionSearch tcConstruction) {
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(tcConstructionService.loadbyWaterElectlistPage(ctx, tcConstruction));
        return result;
    }

    /**
     * 查询历史施工
     *
     * @param ctx
     * @param tcConstruction
     * @return
     * @throws Exception
     */
    public RemoteModelResult<BaseDto> loadbyEhistorylistPage(WyBusinessContext  ctx, TcConstructionSearch tcConstruction) {
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(tcConstructionService.loadbyEhistorylistPage(ctx, tcConstruction));
        return result;
    }
    /**
     * 删除
     *
     * @param
     * @param id
     * @return
     * @throws Exception
     */

    public RemoteModelResult<MessageMap> delete(WyBusinessContext ctx, String id) {
        MessageMap messageMap = tcConstructionService.delete(ctx, id);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;
    }


    /**
     * 修改
     *
     * @param ctx
     * @param tcConstruction
     * @return
     * @throws Exception
     */
    public RemoteModelResult modify(WyBusinessContext  ctx, TcConstruction tcConstruction) {
        MessageMap messageMap = tcConstructionService.modify(ctx, tcConstruction);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;

    }


    /**
     * 修改状态为完工
     *
     * @param ctx
     * @param tcConstruction
     * @return
     * @throws Exception
     */
    public RemoteModelResult reviseComplete(WyBusinessContext ctx, TcConstruction tcConstruction) {
        MessageMap messageMap = tcConstructionService.reviseComplete(ctx, tcConstruction);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;

    }

    /**
     * 修改状态为暂停
     *
     * @param ctx
     * @param tcConstruction
     * @return
     * @throws Exception
     */
    public RemoteModelResult suspend(WyBusinessContext  ctx, TcConstruction tcConstruction) {
        MessageMap messageMap = tcConstructionService.suspend(ctx, tcConstruction);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;

    }

    /**
     * 修改状态为施工中
     *
     * @param ctx
     * @param tcConstruction
     * @return
     * @throws Exception
     */
    public RemoteModelResult startUp(WyBusinessContext ctx, TcConstruction tcConstruction) {
        MessageMap messageMap = tcConstructionService.startUp(ctx, tcConstruction);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;
    }


        /**
         * 修改延期时间
         *
         * @param ctx
         * @param tcConstruction
         * @return
         * @throws Exception
         */
    public RemoteModelResult editDelay(WyBusinessContext  ctx, TcConstruction tcConstruction) {
        MessageMap messageMap = tcConstructionService.editDelay(ctx, tcConstruction);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;

    }
    public RemoteModelResult<MessageMap> add(WyBusinessContext ctx, TcConstruction tcConstruction) {
        MessageMap messageMap = tcConstructionService.add(ctx, tcConstruction);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;

    }
}
