package com.everwing.coreservice.dynamicreports.dao.mapper.system.rights;/**
 * Created by wust on 2018/1/29.
 */

import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRole;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRoleQO;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRoleVO;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/29
 * @author wusongti@lii.com.cn
 */
public interface TRightsRoleMapper {
    /**
     * 分页查询
     * @param tRightsRoleQO
     * @return
     */
    List<TRightsRoleVO> listPage(TRightsRoleQO tRightsRoleQO);

    /**
     * 查询指定条件的信息
     * @param tRightsRoleQO
     * @return
     */
    List<TRightsRoleVO> findByCondition(TRightsRoleQO tRightsRoleQO);

    int insert(TRightsRole tRightsRole);

    int batchInsert(List<TRightsRole> tRightsRoles);

    int modify(TRightsRole tRightsRole);

    int delete(String id);

    int batchDelete(List<String> ids);
}
