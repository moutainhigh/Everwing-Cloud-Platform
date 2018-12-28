package com.everwing.coreservice.common.wy.entity.configuration.bsconstant;

import com.everwing.coreservice.common.Page;

import java.io.Serializable;
import java.util.Date;








public class TBsConstant implements Serializable{

	private static final long serialVersionUID = -1049313823476011419L;
	
	/**
	 * 主键
	 */
	private String id;
	
	/**
	 * 常量名
	 */
	private String billConstantName;
	
	/**
	 * 常量值
	 */
	private Double billConstantValue;
	
	/**
	 * 常量类型
	 */
	private Integer constantType;
	
	/**
	 * 项目编码
	 */
	private String projectId;
	
	/**
	 * 项目名称
	 */
	private String projectName;
	
	/**
	 * 创建人编码
	 */
	private String createId;
	
	/**
	 * 创建人名称
	 */
	private String createName;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 最后更新人编码
	 */
	private String lastUpdateId;
	
	/**
	 * 最后更新人名称
	 */
	private String lastUpdateName;
	
	private Page page;
	
	
	
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;

	
	
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBillConstantName() {
		return billConstantName;
	}

	public void setBillConstantName(String billConstantName) {
		this.billConstantName = billConstantName;
	}

	public Double getBillConstantValue() {
		return billConstantValue;
	}

	public void setBillConstantValue(Double billConstantValue) {
		this.billConstantValue = billConstantValue;
	}

	public Integer getConstantType() {
		return constantType;
	}

	public void setConstantType(Integer constantType) {
		this.constantType = constantType;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateId() {
		return lastUpdateId;
	}

	public void setLastUpdateId(String lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}

	public String getLastUpdateName() {
		return lastUpdateName;
	}

	public void setLastUpdateName(String lastUpdateName) {
		this.lastUpdateName = lastUpdateName;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	
}
