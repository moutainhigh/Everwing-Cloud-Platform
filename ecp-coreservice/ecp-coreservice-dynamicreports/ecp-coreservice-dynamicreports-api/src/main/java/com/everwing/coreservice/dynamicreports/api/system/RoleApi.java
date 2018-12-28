package com.everwing.coreservice.dynamicreports.api.system;/**
 * Created by wust on 2018/1/31.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRole;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRoleQO;
import com.everwing.coreservice.common.dynamicreports.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/31
 * @author wusongti@lii.com.cn
 */
@Component
public class RoleApi {
    @Autowired
    private RoleService roleService;

    public RemoteModelResult<BaseDto> listPage(TRightsRoleQO tRightsRoleQO){
        BaseDto baseDto = roleService.listPage(tRightsRoleQO);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(baseDto);
        return result;
    }

    /**
     * 指定条件查询
     * @param tRightsRoleQO
     * @return
     */
    public RemoteModelResult<BaseDto> findByCondition(TRightsRoleQO tRightsRoleQO){
        BaseDto baseDto = roleService.findByCondition(tRightsRoleQO);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(baseDto);
        return result;
    }

    /**
     * 保存
     * @param tRightsRole
     * @return
     */
    public RemoteModelResult<MessageMap> save(TRightsRole tRightsRole){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(roleService.save(tRightsRole));
        return remoteModelResult;
    }

    /**
     * 批量保存
     * @param tRightsRoles
     * @return
     */
    public RemoteModelResult<MessageMap> batchSave(List<TRightsRole> tRightsRoles){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(roleService.batchSave(tRightsRoles));
        return remoteModelResult;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public RemoteModelResult<MessageMap> delete(String id){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(roleService.delete(id));
        return remoteModelResult;
    }
}
