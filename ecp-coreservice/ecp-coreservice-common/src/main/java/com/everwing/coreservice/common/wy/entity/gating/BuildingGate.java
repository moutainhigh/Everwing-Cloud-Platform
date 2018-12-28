package com.everwing.coreservice.common.wy.entity.gating;

import java.io.Serializable;
/**
 * 
 * @TODO 门控机-房屋关联中间表
 * @author wsw
 * @createTime 2017年4月25日13:54:10
 *
 */
public class BuildingGate implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
    //房屋id
    private String buildingId;
    //门控机账号ID
    private String gateId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	
	public String getGateId() {
		return gateId;
	}
	public void setGateId(String gateId) {
		this.gateId = gateId;
	}
	@Override
	public String toString() {
		return "BuildingGate [id=" + id + ", buildingId=" + buildingId
				+ ", gateId=" + gateId + "]";
	}
    


}