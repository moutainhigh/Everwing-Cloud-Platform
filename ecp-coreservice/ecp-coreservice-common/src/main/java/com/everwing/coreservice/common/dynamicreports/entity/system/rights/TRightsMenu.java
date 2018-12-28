package com.everwing.coreservice.common.dynamicreports.entity.system.rights;/**
 * Created by wust on 2018/1/29.
 */

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/29
 * @author wusongti@lii.com.cn
 */
public class TRightsMenu extends BaseEntity{
    private static final long serialVersionUID = 4517772813313284685L;
    //field
    /** 菜单id **/
    private String menuId;
    /** 菜单名 **/
    private String menuName;
    /** 菜单描述 **/
    private String menuDesc;
    /** 菜单url , 指定菜单跳转的url **/
    private String menuUrl;
    /** 菜单权限 , 该菜单的访问权限 **/
    private String menuPermission;
    /** 菜单层级 , 在同一个pid下 , 值越小层级越高 **/
    private int menuLevel;
    /** 菜单排序 , 在同一个pid下可重新排序 **/
    private int menuOrder;
    /** 菜单对应的图片 **/
    private String menuImg;
    /** 父级菜单id , 顶级菜单父id为空 **/
    private String pid;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuPermission() {
        return menuPermission;
    }

    public void setMenuPermission(String menuPermission) {
        this.menuPermission = menuPermission;
    }

    public int getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(int menuLevel) {
        this.menuLevel = menuLevel;
    }

    public int getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(int menuOrder) {
        this.menuOrder = menuOrder;
    }

    public String getMenuImg() {
        return menuImg;
    }

    public void setMenuImg(String menuImg) {
        this.menuImg = menuImg;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
