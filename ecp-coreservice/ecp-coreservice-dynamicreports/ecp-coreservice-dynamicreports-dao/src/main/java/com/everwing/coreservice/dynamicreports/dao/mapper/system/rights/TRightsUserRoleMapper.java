package com.everwing.coreservice.dynamicreports.dao.mapper.system.rights;/**
 * Created by wust on 2018/1/29.
 */

import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUserRole;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/29
 * @author wusongti@lii.com.cn
 */
public interface TRightsUserRoleMapper {
    /**
     * 查询指定条件的信息
     * @param tRightsUserRole
     * @return
     */
    List<TRightsUserRole> findByCondition(TRightsUserRole tRightsUserRole);


    /**
     * 批量插入
     * @param tRightsUserRole
     * @return
     */
    int batchInsert(List<TRightsUserRole> tRightsUserRole);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    int batchDelete(List<String> ids);

    /**
     * 根据用户批量删除
     * @param userIds
     * @return
     */
    int batchDeleteByUserId(List<String> userIds);
}
