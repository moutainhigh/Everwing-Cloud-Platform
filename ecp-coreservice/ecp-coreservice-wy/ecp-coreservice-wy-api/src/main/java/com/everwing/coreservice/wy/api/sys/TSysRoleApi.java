package com.everwing.coreservice.wy.api.sys;/**
 * Created by wust on 2017/4/10.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.relation.PopMenuResourceTreeMap;
import com.everwing.coreservice.common.wy.entity.system.relation.TSysRoleResource;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRole;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRoleList;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRoleSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017-4-10 15:34:12
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
@Component
public class TSysRoleApi {
    @Autowired
    private TSysRoleService tSysRoleService;

    public RemoteModelResult<BaseDto> listPageRole(WyBusinessContext ctx, TSysRoleSearch condition){
        BaseDto baseDto = tSysRoleService.listPageRole(ctx,condition);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(baseDto);
        return result;
    }


    public RemoteModelResult<MessageMap> save(WyBusinessContext ctx, TSysRole entity){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(tSysRoleService.save(ctx,entity));
        return remoteModelResult;
    }

    public RemoteModelResult<BaseDto> findByGuid(String companyId,String guid){
        RemoteModelResult<BaseDto> remoteModelResult = new RemoteModelResult<>();
        BaseDto baseDto = new BaseDto();
        TSysRoleList role = tSysRoleService.findByGuid(companyId,guid);
        baseDto.setT(role);
        remoteModelResult.setModel(baseDto);
        return remoteModelResult;
    }

    public RemoteModelResult<MessageMap> delete(String companyId,String guids){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(tSysRoleService.delete(companyId,guids));
        return remoteModelResult;
    }

    public RemoteModelResult<MessageMap> changeStatus(WyBusinessContext ctx, TSysRole entity){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(tSysRoleService.changeStatus(ctx,entity));
        return remoteModelResult;
    }

    public RemoteModelResult<MessageMap> initResources(String companyId){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(tSysRoleService.initResources(companyId));
        return remoteModelResult;
    }

    public  RemoteModelResult<BaseDto> findResourceTreeByRoleId(String companyId, String toRoleId){
        RemoteModelResult<BaseDto> remoteModelResult = new RemoteModelResult<>();
        BaseDto baseDto = new BaseDto();
        List<PopMenuResourceTreeMap> popMenuResourceTreeMap = tSysRoleService.findResourceTreeByRoleId(companyId,toRoleId);
        baseDto.setLstDto(popMenuResourceTreeMap);
        remoteModelResult.setModel(baseDto);
        return remoteModelResult;
    }

    public RemoteModelResult<MessageMap> authorize(WyBusinessContext ctx, TSysRoleResource rr){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(tSysRoleService.authorize(ctx,rr));
        return remoteModelResult;
    }
}