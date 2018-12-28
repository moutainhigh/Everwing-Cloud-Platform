package com.everwing.coreservice.wy.api.building;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectMeterSearch;
import com.everwing.coreservice.common.wy.service.TcSurfaceElectService.TcSurfaceElectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TcSurfaceElectApi {
    @Autowired
    private TcSurfaceElectService  tcSurfaceElectService;


    /**
     * 查询
     * @param ctx
     * @param
     * @return
     * @throws Exception
     */
    public RemoteModelResult<BaseDto> listPageElectMeterInfos(WyBusinessContext ctx, TcElectMeterSearch tcElectMeter)  {
        BaseDto pageResultDto = tcSurfaceElectService.listPageElectMeterInfos(ctx,tcElectMeter);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(pageResultDto);
        return result;
    }

    /**
     * 查询表级别为非终端表
     * @param ctx
     * @param
     * @return
     * @throws Exception
     */
    public RemoteModelResult<BaseDto> listPageParentInfos(WyBusinessContext ctx, TcElectMeterSearch tcElectMeter)  {
        BaseDto pageResultDto = tcSurfaceElectService.listPageParentInfos(ctx,tcElectMeter);
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
        MessageMap messageMap =tcSurfaceElectService.delete(ctx,id);
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
    public RemoteModelResult modify(WyBusinessContext  ctx, TcElectMeter  tcElectMeter) {
        MessageMap messageMap = tcSurfaceElectService.modify(ctx,tcElectMeter);
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
    public RemoteModelResult<MessageMap> add(WyBusinessContext ctx,TcElectMeter  tcElectMeter) {
        MessageMap messageMap =tcSurfaceElectService.add(ctx,tcElectMeter);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(messageMap);
        return result;

    }

}
