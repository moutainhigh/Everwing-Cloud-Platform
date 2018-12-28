package com.everwing.coreservice.common.dynamicreports.service.system;/**
 * Created by wust on 2018/1/31.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUser;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUserQO;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/31
 * @author wusongti@lii.com.cn
 */
public interface UserService {
    /**
     * 分页查询
     * @param tRightsUserQO
     * @return
     */
    BaseDto listPage(TRightsUserQO tRightsUserQO);

    /**
     * 指定条件查询
     * @param tRightsUserQO
     * @return
     */
    BaseDto findByCondition(TRightsUserQO tRightsUserQO);

    /**
     * 保存
     * @param tRightsUser
     * @return
     */
    MessageMap save(TRightsUser tRightsUser);

    /**
     * 批量保存
     * @param tRightsUsers
     * @return
     */
    MessageMap batchSave(List<TRightsUser> tRightsUsers);

    /**
     * 删除
     * @param id
     * @return
     */
    MessageMap delete(String id);
}
