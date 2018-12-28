package com.everwing.coreservice.common.wy.entity.configuration.assetaccount;

import java.io.Serializable;
import java.util.Date;

public class TBsAssetAccount implements Serializable{

	private static final long serialVersionUID = 1000945027969621035L;
	
	/**主键**/
	private String id; 
	/**建筑编号**/
	private String buildingCode;
	/**账户类型**/
	private Integer type;
	/**账户余额**/
	private Double accountBalance;
	/**使用状态**/
	private Integer useStatus;
	/**项目编码**/
	private String projectId;
	/**项目名称**/
	private String projectName;
	/**创建时间**/
	private Date createTime;
	/**创建人编码**/
	private String createId;
	/**创建人名称**/
	private String createName;
	/**修改人编码**/
	private String modifyId;
	/**修改人名称**/
	private String modifyName;
	/**建筑全称**/
	private String fullName;
	/**修改时间**/
	private Date modifyTime;
	
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBuildingCode() {
		return buildingCode;
	}
	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Double getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}
	public Integer getUseStatus() {
		return useStatus;
	}
	public void setUseStatus(Integer useStatus) {
		this.useStatus = useStatus;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getModifyId() {
		return modifyId;
	}
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	public String getModifyName() {
		return modifyName;
	}
	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}
	
	
}
