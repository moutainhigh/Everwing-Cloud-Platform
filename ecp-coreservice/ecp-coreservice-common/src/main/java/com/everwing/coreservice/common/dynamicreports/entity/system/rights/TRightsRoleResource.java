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
public class TRightsRoleResource extends BaseEntity implements java.io.Serializable{
    private static final long serialVersionUID = -2369816751390698824L;
    //field
    /** 主键id **/
    private String id;
    /** 角色id **/
    private String roleId;
    /** 菜单资源id , 当菜单资源类型为m时 , 指向菜单id当菜单资源类型为r时 , 指向资源id **/
    private String srcId;
    /** 菜单资源类型 , 当值为m时, 指向菜单表当值为r时 , 指向按钮资源表 **/
    private String srcType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public String getSrcType() {
        return srcType;
    }

    public void setSrcType(String srcType) {
        this.srcType = srcType;
    }
}
