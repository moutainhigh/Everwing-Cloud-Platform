package com.everwing.coreservice.common.wy.entity.delivery;

import java.io.Serializable;
import java.util.Date;


/***
 * @describe  银账交割的员工组表javaBean
 * @author qhc
 * @ date 2017-08-31 
 */
public class TJgStaffGrop implements Serializable{

	private static final long serialVersionUID = 4783251247782072957L;


	/**主键**/
	private String id;
	
	/**用户id**/
	private String userId;
	
	/**员工姓名**/
	private String staffName;
	
	/**角色名称**/
	private String roleName;
	
	/**创建时间**/
	private Date createTime;
	
	/**上级id 关联本表id**/
	private String createUser;
	
	/**创建人**/
	private String pid;
	
	/** 项目id **/
	private String projectId;
	
	/** 项目名称 **/
	private String projectName;
	
	/** 角色级别 **/
	private Integer roleLevel;
	
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

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(Integer roleLevel) {
		this.roleLevel = roleLevel;
	}

	@Override
	public String toString() {
		return "TJgStaffGrop [id=" + id + ", userId=" + userId + ", staffName="
				+ staffName + ", roleName=" + roleName + ", createTime="
				+ createTime + ", createUser=" + createUser + ", pid=" + pid
				+ ", projectId=" + projectId + ", projectName=" + projectName
				+ ", roleLevel=" + roleLevel + "]";
	}


	
}
