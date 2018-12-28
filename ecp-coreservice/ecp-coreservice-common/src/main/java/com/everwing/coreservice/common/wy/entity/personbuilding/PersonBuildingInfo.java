package com.everwing.coreservice.common.wy.entity.personbuilding;

import java.io.Serializable;

/**
 * 绑定关系数据
 *
 */
public class PersonBuildingInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2266547899308536392L;
	
	
	private String id;
	private String houseCode;
	private String buildingCode;
	private String buildingName;
	private String buildingFullName;
	private String buildingType;
	private String pid;
	private String custName;
	private String registerPhone;
	private String cardNum;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHouseCode() {
		return houseCode;
	}
	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}
	public String getBuildingCode() {
		return buildingCode;
	}
	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getBuildingFullName() {
		return buildingFullName;
	}
	public void setBuildingFullName(String buildingFullName) {
		this.buildingFullName = buildingFullName;
	}
	public String getBuildingType() {
		return buildingType;
	}
	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getRegisterPhone() {
		return registerPhone;
	}
	public void setRegisterPhone(String registerPhone) {
		this.registerPhone = registerPhone;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	@Override
	public String toString() {
		return "PersonBuildingInfo [id=" + id + ", houseCode=" + houseCode
				+ ", buildingCode=" + buildingCode + ", buildingName="
				+ buildingName + ", buildingFullName=" + buildingFullName
				+ ", buildingType=" + buildingType + ", pid=" + pid
				+ ", custName=" + custName + ", registerPhone=" + registerPhone
				+ ", cardNum=" + cardNum + "]";
	}
	
	
	
	
	
	
	

}
