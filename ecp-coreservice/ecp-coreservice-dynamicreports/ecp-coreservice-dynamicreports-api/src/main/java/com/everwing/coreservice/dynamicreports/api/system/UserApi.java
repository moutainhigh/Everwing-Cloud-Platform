package com.everwing.coreservice.dynamicreports.api.system;/**
 * Created by wust on 2018/1/31.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUser;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUserQO;
import com.everwing.coreservice.common.dynamicreports.service.system.UserService;
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
public class UserApi {
    @Autowired
    private UserService userService;

    public RemoteModelResult<BaseDto> listPage(TRightsUserQO tRightsUserQO){
        BaseDto baseDto = userService.listPage(tRightsUserQO);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(baseDto);
        return result;
    }

    /**
     * 指定条件查询
     * @param tRightsUserQO
     * @return
     */
    public RemoteModelResult<BaseDto> findByCondition(TRightsUserQO tRightsUserQO){
        BaseDto baseDto = userService.findByCondition(tRightsUserQO);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(baseDto);
        return result;
    }

    /**
     * 保存
     * @param tRightsUser
     * @return
     */
    public RemoteModelResult<MessageMap> save(TRightsUser tRightsUser){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(userService.save(tRightsUser));
        return remoteModelResult;
    }

    /**
     * 批量保存
     * @param tRightsUsers
     * @return
     */
    public RemoteModelResult<MessageMap> batchSave(List<TRightsUser> tRightsUsers){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(userService.batchSave(tRightsUsers));
        return remoteModelResult;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public RemoteModelResult<MessageMap> delete(String id){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(userService.delete(id));
        return remoteModelResult;
    }
}
