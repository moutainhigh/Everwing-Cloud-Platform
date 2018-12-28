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
public class TRightsRole extends BaseEntity implements java.io.Serializable{
    private static final long serialVersionUID = 6518013539619042249L;
    //field
    /** 角色ID **/
    private String roleId;
    /** 编码 **/
    private String code;
    /** 角色名 **/
    private String roleName;
    /** 角色描述 **/
    private String roleDesc;
    /** 状态； **/
    private String status;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
