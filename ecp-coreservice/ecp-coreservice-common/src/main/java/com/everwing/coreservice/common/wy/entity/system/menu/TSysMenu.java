package com.everwing.coreservice.common.wy.entity.system.menu;

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:菜单实体类
 * Reason:该实体类和数据库字段一一对应
 * Date:2017-4-10 09:44:23
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TSysMenu extends BaseEntity {

    private static final long serialVersionUID = -235718065260255368L;
    private String menuId;			//菜单id
	private String menuName;		//菜单名称
	private String menuDesc;        //菜单描述
    private String menuUrl;			//菜单指向的url
	private String menuPermission;	//菜单需要的权限,如user:list
	private Integer menuLevel;		//菜单的层次级别
	private Integer menuOrder;      //菜单的排序
	private String menuImg;         //菜单指向的图片
    private String pId;				//父级菜单id

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

    public Integer getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(Integer menuLevel) {
        this.menuLevel = menuLevel;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public String getMenuImg() {
        return menuImg;
    }

    public void setMenuImg(String menuImg) {
        this.menuImg = menuImg;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    @Override
    public String toString() {
        return "TSysMenu{" +
                "menuId='" + menuId + '\'' +
                ", menuName='" + menuName + '\'' +
                ", menuDesc='" + menuDesc + '\'' +
                ", menuUrl='" + menuUrl + '\'' +
                ", menuPermission='" + menuPermission + '\'' +
                ", menuLevel=" + menuLevel +
                ", menuOrder=" + menuOrder +
                ", menuImg='" + menuImg + '\'' +
                ", pId='" + pId + '\'' +
                '}';
    }
}
