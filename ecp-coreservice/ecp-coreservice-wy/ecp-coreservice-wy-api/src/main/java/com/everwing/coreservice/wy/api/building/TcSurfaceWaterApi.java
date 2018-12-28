package com.everwing.coreservice.wy.api.building;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeterSearch;
import com.everwing.coreservice.common.wy.service.TcSurfaceWater.TcSurfaceWaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TcSurfaceWaterApi {
    @Autowired
    private TcSurfaceWaterService   tcSurfaceWaterService;


    /**
     * 查询
     * @param ctx
     * @param
     * @return
     * @throws Exception
     */
    public RemoteModelResult<BaseDto> listPageWaterMeterInfos(WyBusinessContext ctx, TcWaterMeterSearch tcWaterMeter)  {
        BaseDto pageResultDto = tcSurfaceWaterService.listPageWaterMeterInfos(ctx,tcWaterMeter);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(pageResultDto);
        return result;
    }


    /**
     * 水表结构图
     * @param ctx
     * @param
     * @return
     * @throws Exception
     */
    public RemoteModelResult<BaseDto> listPageWaterTree(WyBusinessContext ctx, TcWaterMeterSearch tcWaterMeterSearch)  {
        BaseDto pageResultDto = tcSurfaceWaterService.listPageWaterTree(ctx,tcWaterMeterSearch);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(pageResultDto);
        return result;
    }
    /**
     * 查询父表编码为非终端表
     * @param ctx
     * @param
     * @return
     */
    public RemoteModelResult<BaseDto> listPageParentCodeInfos(WyBusinessContext ctx, TcWaterMeterSearch tcWaterMeter)  {
        BaseDto pageResultDto = tcSurfaceWaterService.listPageParentCodeInfos(ctx,tcWaterMeter);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(pageResultDto);
        return result;
    }

    /**
     * 删除
     * @param ctx
     * @param id
     * @return
     */
    public RemoteModelResult<MessageMap> delete(WyBusinessContext ctx, String id){
        MessageMap messageMap =tcSurfaceWaterService.delete(ctx,id);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;
    }


    /**
     * 修改
     *
     * @param ctx
     * @param
     * @return
     * @throws Exception
     */
    public RemoteModelResult modify(WyBusinessContext  ctx, TcWaterMeter  tcWaterMeter) {
        MessageMap messageMap = tcSurfaceWaterService.modify(ctx, tcWaterMeter);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;

    }



    /**
     * 新增
     *
     * @param ctx
     * @param
     * @return
     * @throws Exception
     */
    public RemoteModelResult<MessageMap> add(WyBusinessContext ctx,TcWaterMeter tcWaterMeter) {
        MessageMap messageMap =tcSurfaceWaterService.add(ctx,tcWaterMeter);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;

    }

}
