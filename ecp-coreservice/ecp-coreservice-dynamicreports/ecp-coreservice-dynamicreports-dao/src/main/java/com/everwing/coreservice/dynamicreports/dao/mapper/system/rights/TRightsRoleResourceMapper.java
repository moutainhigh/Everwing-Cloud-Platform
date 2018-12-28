package com.everwing.coreservice.dynamicreports.dao.mapper.system.rights;/**
 * Created by wust on 2018/1/29.
 */

import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRoleResource;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/29
 * @author wusongti@lii.com.cn
 */
public interface TRightsRoleResourceMapper {
    /**
     * 查询指定条件的信息
     * @param tRightsRoleResource
     * @return
     */
    List<TRightsRoleResource> findByCondition(TRightsRoleResource tRightsRoleResource);

    /**
     * 根据角色分组菜单
     * @return
     */
    List<TRightsRoleResource> groupMenuIdByRoleId();

    /**
     * 批量插入
     * @param tRightsRoleResources
     * @return
     */
    int batchInsert(List<TRightsRoleResource> tRightsRoleResources);

    /**
     * 根据角色删除
     * @param roleId
     * @return
     */
    int deleteByRoleId(String roleId);

    /**
     * 根据角色批量删除
     * @param roleIds
     * @return
     */
    int batchDeleteByRoleId(List<String> roleIds);

    /**
     * 删除指定类型下面指定的资源
     * @param srcType
     * @param srcId
     * @return
     */
    int deleteByTypeAndSrcId(String srcType,String srcId);

    /**
     * 删除指定角色下面指定的资源
     * @param roleId
     * @param srcId
     * @return
     */
    int deleteByRoleIdAndSrcId(String roleId,String srcId);
}
