package com.everwing.coreservice.wy.api.sys;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartment;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartmentSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
@Component
public class TSysDepartmentApi {
    @Autowired
    private TSysDepartmentService tSysDepartmentService;

    public RemoteModelResult<BaseDto> listPage(WyBusinessContext ctx, TSysDepartmentSearch condition){
        BaseDto baseDto = tSysDepartmentService.listPage(ctx,condition);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(baseDto);
        return result;
    }

    public RemoteModelResult<MessageMap> save(WyBusinessContext ctx, TSysDepartment entity){
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tSysDepartmentService.save(ctx,entity));
        return result;
    }

    public RemoteModelResult<MessageMap> delete(WyBusinessContext ctx, TSysDepartment entity){
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tSysDepartmentService.delete(ctx,entity));
        return result;
    }

    public RemoteModelResult<BaseDto> loadDeptsByProjectId(String projectId) {
        String companyId= WyBusinessContext.getContext().getCompanyId();
        BaseDto baseDto=tSysDepartmentService.findByCompanyIdAndProjectId(companyId,projectId);
        RemoteModelResult<BaseDto> remoteModelResult=new RemoteModelResult<>();
        remoteModelResult.setModel(baseDto);
        return remoteModelResult;
    }
}
