package com.everwing.coreservice.common.dynamicreports.service.system;/**
 * Created by wust on 2018/1/31.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRole;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRoleQO;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/31
 * @author wusongti@lii.com.cn
 */
public interface RoleService {
    /**
     * 分页查询
     * @param tRightsRoleQO
     * @return
     */
    BaseDto listPage(TRightsRoleQO tRightsRoleQO);

    /**
     * 指定条件查询
     * @param tRightsRoleQO
     * @return
     */
    BaseDto findByCondition(TRightsRoleQO tRightsRoleQO);

    /**
     * 保存
     * @param tRightsRole
     * @return
     */
    MessageMap save(TRightsRole tRightsRole);

    /**
     * 批量保存
     * @param tRightsRoles
     * @return
     */
    MessageMap batchSave(List<TRightsRole> tRightsRoles);

    /**
     * 删除
     * @param id
     * @return
     */
    MessageMap delete(String id);
}
