package com.everwing.coreservice.common.wy.entity.order;

import java.io.Serializable;

public class TcOrderChangeAssetComplaint implements Serializable{

	private static final long serialVersionUID = 206272945282221234L;
	
	/**项目编号**/
	private String projectId;
	/**项目名称**/
	private String projectName;
	
	/** 客户名**/
	private String custName;
	/**收费对象**/
	private String buildCode;
	
	/**类型 0水表   1电表**/
	int type;
	
	/**负责人ID**/
	private String readingPersonId;

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

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getBuildCode() {
		return buildCode;
	}

	public void setBuildCode(String buildCode) {
		this.buildCode = buildCode;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getReadingPersonId() {
		return readingPersonId;
	}

	public void setReadingPersonId(String readingPersonId) {
		this.readingPersonId = readingPersonId;
	}
	
}
