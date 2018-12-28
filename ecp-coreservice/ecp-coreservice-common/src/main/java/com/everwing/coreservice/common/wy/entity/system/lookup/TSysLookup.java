package com.everwing.coreservice.common.wy.entity.system.lookup;


import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * @date Sep 16, 2015
 * @time 10:11:04 AM
 * @author Wusongti
 */
public class TSysLookup extends BaseEntity{


	private static final long serialVersionUID = -7401802938742745577L;
	private String lookupId;
	private String code;
	private String name;
	private String parentCode;
	private String status;
	private String description;
	private Integer itemOrder;
	private String projectCode;


	public String getLookupId() {
		return this.lookupId;
	}

	public void setLookupId(String lookupId) {
		this.lookupId = lookupId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentCode() {
		return this.parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getItemOrder() {
		return this.itemOrder;
	}

	public void setItemOrder(Integer itemOrder) {
		this.itemOrder = itemOrder;
	}

	public String getProjectCode() {
		return this.projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	@Override
	public String toString() {
		return super.toString() + "\nTSysLookup{" +
				"lookupId='" + lookupId + '\'' +
				", code='" + code + '\'' +
				", name='" + name + '\'' +
				", parentCode='" + parentCode + '\'' +
				", status='" + status + '\'' +
				", description='" + description + '\'' +
				", itemOrder='" + itemOrder + '\'' +
				", projectCode='" + projectCode + '\'' +
				'}';
	}
}
