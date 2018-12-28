package com.everwing.coreservice.common.wy.entity.configuration.project;

public class TBsGetRuleBuilding implements java.io.Serializable{

	private static final long serialVersionUID = 7512958863507754232L;
	
	/**建筑编码**/
	private String buildCode;
	/**建筑名称**/
	private String buildName;
	/**建筑全称**/
	private String buildFullName;
	/**建筑类型**/
	private String buildType;
	/**父编号**/
	private String pId;
	/**方案编号**/
	private String chargingSchemeId;
	/**项目编号**/
	private String projectId;
	
	
	public String getBuildCode() {
		return buildCode;
	}
	public void setBuildCode(String buildCode) {
		this.buildCode = buildCode;
	}
	public String getBuildName() {
		return buildName;
	}
	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}
	public String getBuildFullName() {
		return buildFullName;
	}
	public void setBuildFullName(String buildFullName) {
		this.buildFullName = buildFullName;
	}
	public String getBuildType() {
		return buildType;
	}
	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getChargingSchemeId() {
		return chargingSchemeId;
	}
	public void setChargingSchemeId(String chargingSchemeId) {
		this.chargingSchemeId = chargingSchemeId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	

}
