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
public class TRightsResource extends BaseEntity implements java.io.Serializable{
    private static final long serialVersionUID = -5666803273627248565L;

    //field
    /** 资源id **/
    private String srcId;
    /** 菜单id **/
    private String menuId;
    /** 资源名,按钮名 **/
    private String srcName;
    /** 资源描述 **/
    private String srcDesc;
    /** 权限标识 **/
    private String srcPermission;
    /** 权限url **/
    private String srcUrl;

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getSrcDesc() {
        return srcDesc;
    }

    public void setSrcDesc(String srcDesc) {
        this.srcDesc = srcDesc;
    }

    public String getSrcPermission() {
        return srcPermission;
    }

    public void setSrcPermission(String srcPermission) {
        this.srcPermission = srcPermission;
    }

    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }
}
