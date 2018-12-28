package com.everwing.coreservice.wy.api.sys;/**
 * Created by wust on 2018/8/6.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.system.code.TSysCode;
import com.everwing.coreservice.common.wy.entity.system.code.TSysCodeSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/8/6
 * @author wusongti@lii.com.cn
 */
@Component
public class TSysCodeApi {
    @Autowired
    private TSysCodeService tSysCodeService;

    public RemoteModelResult<List<TSysCode>> findByCondition(WyBusinessContext ctx, TSysCodeSearch tSysCodeSearch){
        List<TSysCode> tSysCodes = tSysCodeService.findByCondition(ctx,tSysCodeSearch);
        RemoteModelResult<List<TSysCode>> result = new RemoteModelResult<>();
        result.setModel(tSysCodes);
        return result;
    }


    public RemoteModelResult<Integer> insert(WyBusinessContext ctx, TSysCode tSysCode){
        int numbers = tSysCodeService.insert(ctx,tSysCode);
        RemoteModelResult<Integer> result = new RemoteModelResult<>();
        result.setModel(numbers);
        return result;
    }

    public RemoteModelResult<Integer> update(WyBusinessContext ctx, TSysCode tSysCode){
        int numbers = tSysCodeService.update(ctx,tSysCode);
        RemoteModelResult<Integer> result = new RemoteModelResult<>();
        result.setModel(numbers);
        return result;
    }
}
