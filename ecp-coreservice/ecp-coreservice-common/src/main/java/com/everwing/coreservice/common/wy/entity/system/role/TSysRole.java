package com.everwing.coreservice.common.wy.entity.system.role;


import com.everwing.coreservice.common.BaseEntity;

/**
 * 岗位持久化对象
 */
public class TSysRole extends BaseEntity {
    private static final long serialVersionUID = -5359529581823032427L;
    private String roleId;		// 角色ID
    private String code;
	private String roleName;	// 角色名称
	private String roleDesc;    // 角色描述
	private String status;		// 启用状态

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return this.roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return super.toString() + "\nTSysRole{" +
                "roleId='" + roleId + '\'' +
                ", code='" + code + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleDesc='" + roleDesc + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
