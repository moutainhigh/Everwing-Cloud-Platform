package com.everwing.coreservice.common.wy.entity.system.relation;

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:用户角色实体类
 * Reason:该实体类和数据库字段一一对应
 * Date:2017-4-10 09:44:23
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TSysUserRole extends BaseEntity{


	private static final long serialVersionUID = 5217181623883783120L;
	private String id ;
	private String userId;		//用户id
	private String roleId;		//角色id
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public TSysUserRole(String userId, String roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}
	
	public TSysUserRole() {
	}
	
	public TSysUserRole(String userId){
		this.userId = userId;
	}
	
	
	
	
	
	
}
