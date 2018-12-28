package com.everwing.coreservice.wy.api.sys;/**
 * Created by wust on 2018/3/20.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.system.areas.TSysAreasList;
import com.everwing.coreservice.common.wy.entity.system.areas.TSysAreasSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysAreasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/3/20
 * @author wusongti@lii.com.cn
 */
@Component
public class TSysAreasApi {
    @Autowired
    private TSysAreasService tSysAreasService;

    public RemoteModelResult<List<TSysAreasList>> findByCondition(WyBusinessContext ctx, TSysAreasSearch tSysAreasSearch){
        List<TSysAreasList> tSysAreasLists = tSysAreasService.findByCondition(ctx,tSysAreasSearch);
        RemoteModelResult<List<TSysAreasList>> result = new RemoteModelResult<>();
        result.setModel(tSysAreasLists);
        return result;
    }
}
