package com.everwing.coreservice.dynamicreports.dao.mapper.system.rights;/**
 * Created by wust on 2018/1/29.
 */

import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsMenu;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsMenuQO;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsMenuVO;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/29
 * @author wusongti@lii.com.cn
 */
public interface TRightsMenuMapper {

    /**
     * 获取指定条件的数据
     * @param tReportMenuQO
     * @return
     */
    List<TRightsMenuVO> findByCondition(TRightsMenuQO tReportMenuQO);

    /**
     * 超级管理员：获取非白名单菜单集合
     * @return
     */
    List<TRightsMenuVO> findNonWhiteListByAdmin();

    /**
     * 超级管理员：获取白名单菜单集合
     * @return
     */
    List<TRightsMenuVO> findWhiteListByAdmin();

    /**
     * 根据角色获取非白名单菜单集合
     * @param tReportMenuQO
     * @return
     */
    List<TRightsMenuVO> findNonWhiteListByRole(TRightsMenuQO tReportMenuQO);

    /**
     * 根据角色获取白名单菜单集合
     * @param tReportMenuQO
     * @return
     */
    List<TRightsMenuVO> findWhiteListByRole(TRightsMenuQO tReportMenuQO);

    /**
     * 根据角色获取菜单树
     * @param roleId
     * @return
     */
    List<Map> findMenuTreeByRoleId(String roleId);

    /**
     * 插入一条记录
     * @param tReportMenu
     * @return
     */
    int insert(TRightsMenu tReportMenu);

    /**
     * 批量插入记录
     * @param tReportMenus
     * @return
     */
    int batchInsert(List<TRightsMenu> tReportMenus);


    /**
     * 全部删除数据
     * @return
     */
    int deleteAll();
}
