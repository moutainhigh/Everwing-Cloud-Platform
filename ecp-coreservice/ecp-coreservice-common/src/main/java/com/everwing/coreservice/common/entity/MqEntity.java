package com.everwing.coreservice.common.entity;

import java.io.Serializable;
import java.util.List;

public class MqEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer opr;
	private Object data;
	private String companyId;
	private String userId;
	private String supAttr;
	private String projectId;
	private String projectName;
	private List<String> buildingCodes;
	public Integer getOpr() {
		return opr;
	}
	public void setOpr(Integer opr) {
		this.opr = opr;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSupAttr() {
		return supAttr;
	}
	public void setSupAttr(String supAttr) {
		this.supAttr = supAttr;
	}


	public List<String> getBuildingCodes() {
		return buildingCodes;
	}

	public void setBuildingCodes(List<String> buildingCodes) {
		this.buildingCodes = buildingCodes;
	}

	public MqEntity(Integer opr, Object data) {
		this.opr = opr;
		this.data = data;
	}
	
	public MqEntity() {}
	
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

	@Override
	public String toString() {
		return "MqEntity{" +
				"opr=" + opr +
				", data=" + data +
				", companyId='" + companyId + '\'' +
				", userId='" + userId + '\'' +
				", supAttr='" + supAttr + '\'' +
				", projectId='" + projectId + '\'' +
				", projectName='" + projectName + '\'' +
				", buildingCodes=" + buildingCodes +
				'}';
	}
}
