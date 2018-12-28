package com.everwing.coreservice.common.wy.entity.business.meterrelation;

import java.io.Serializable;

/**
 * 表与建筑关联关系表
 *
 */
public class TcMeterRelation implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id; //主键id
	private String buildingCode; //建筑code
	private String relationId;	//关联id
	private Integer type;   //表关联类型 0 水表用, 1 电表用, 2任务用
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
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "TcMeterRelation [id=" + id + ", buildingCode=" + buildingCode
				+ ", relationId=" + relationId + ", type=" + type + "]";
	}

	
	
	
	
}
