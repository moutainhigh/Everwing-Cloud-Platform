package com.everwing.coreservice.wy.api.common;/**
 * Created by wust on 2018/12/19.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodataSearch;
import com.everwing.coreservice.common.wy.service.common.SynchrodataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/12/19
 * @author wusongti@lii.com.cn
 */
@Component
public class SynchrodataApi {
    @Autowired
    private SynchrodataService synchrodataService;

    public RemoteModelResult<BaseDto> listPage(WyBusinessContext ctx, TSynchrodataSearch tSynchrodataSearch) {
        RemoteModelResult<BaseDto> baseDtoRemoteModelResult = new RemoteModelResult<>();
        BaseDto baseDto = synchrodataService.listPage(ctx,tSynchrodataSearch);
        baseDtoRemoteModelResult.setModel(baseDto);
        return baseDtoRemoteModelResult;
    }

    public RemoteModelResult<MessageMap> syncData(WyBusinessContext ctx, List<String> tSynchrodatas) {
        RemoteModelResult<MessageMap> baseDtoRemoteModelResult = new RemoteModelResult<>();
        MessageMap messageMap = synchrodataService.syncData(ctx,tSynchrodatas);
        baseDtoRemoteModelResult.setModel(messageMap);
        return baseDtoRemoteModelResult;
    }
}
