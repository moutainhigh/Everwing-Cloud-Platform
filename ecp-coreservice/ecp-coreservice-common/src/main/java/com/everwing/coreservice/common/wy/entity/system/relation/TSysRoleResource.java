package com.everwing.coreservice.common.wy.entity.system.relation;

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:角色资源实体类
 * Reason:该实体类和数据库字段一一对应
 * Date:2017-4-10 09:44:23
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TSysRoleResource extends BaseEntity{

	private static final long serialVersionUID = 7501639182354744727L;
	private String id;
	private String roleId;		//角色id
	private String srcId;		//资源id, 当srcType为m时,为菜单id,为r时,指向资源id
	private String srcType;		//资源类型,m为菜单,r为资源
	
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
