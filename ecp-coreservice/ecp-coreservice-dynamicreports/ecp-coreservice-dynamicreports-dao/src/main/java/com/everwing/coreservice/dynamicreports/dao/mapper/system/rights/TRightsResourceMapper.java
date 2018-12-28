package com.everwing.coreservice.dynamicreports.dao.mapper.system.rights;/**
 * Created by wust on 2018/1/29.
 */

import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsResource;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsResourceQO;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsResourceVO;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/29
 * @author wusongti@lii.com.cn
 */
public interface TRightsResourceMapper {
    /**
     * 获取指定条件的数据
     * @param tReportResourceQO
     * @return
     */
    List<TRightsResourceVO> findByCondition(TRightsResourceQO tReportResourceQO);

    /**
     * 超级管理员：获取非白名单资源集合
     * @return
     */
    List<TRightsResourceVO> findNonWhiteListByAdmin();

    /**
     * 超级管理员：获取白名单资源集合
     * @return
     */
    List<TRightsResourceVO> findWhiteListByAdmin();

    /**
     * 非超级管理员：根据角色获取非白名单资源集合
     * @param tReportResourceQO
     * @return
     */
    List<TRightsResourceVO> findNonWhiteListByRole(TRightsResourceQO tReportResourceQO);

    /**
     * 非超级管理员：根据角色获取白名单资源集合
     * @param tReportResourceQO
     * @return
     */
    List<TRightsResourceVO> findWhiteListByRole(TRightsResourceQO tReportResourceQO);

    /**
     * 获取系统级别的白名单资源集合
     * @param tReportResourceQO
     * @return
     */
    List<TRightsResourceVO> findApplicationWhiteList(TRightsResourceQO tReportResourceQO);

    /**
     * 获取菜单级别下面的白名单资源集合
     * @param menuId
     * @return
     */
    List<TRightsResourceVO> findWhiteListByMenuId(String menuId);

    /**
     * 根据角色获取资源树
     * @param roleId
     * @return
     */
    List<Map> findResourceTreeByRoleId(String roleId);

    /**
     * 插入一条记录
     * @param tReportResource
     * @return
     */
    int insert(TRightsResource tReportResource);

    /**
     * 批量插入记录
     * @param tReportResources
     * @return
     */
    int batchInsert(List<TRightsResource> tReportResources);


    /**
     * 全部删除数据
     * @return
     */
    int deleteAll();
}
