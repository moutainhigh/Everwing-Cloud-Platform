package com.everwing.coreservice.common.admin.entity.cust.personbuilding;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @TODO 物业平台客户资产绑定关系 admin平台存储表
 * @author wsw
 * @createTime 2017年4月26日14:52:24
 */
public class PersonBuilding implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String personBuildingId;	//主键
	private String custId;				//个人客户id
	private String enterpriseId;		//企业客户id
	private String buildingId;			//建筑id tc_building表的主键
	private String buildingCode;		//建筑code
	private Integer state;				//状态 0启用 1禁用
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date relationDate;			//关联时间
	public String getPersonBuildingId() {
		return personBuildingId;
	}
	public void setPersonBuildingId(String personBuildingId) {
		this.personBuildingId = personBuildingId;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getRelationDate() {
		return relationDate;
	}
	public void setRelationDate(Date relationDate) {
		this.relationDate = relationDate;
	}
	public String getBuildingCode() {
		return buildingCode;
	}
	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}
	@Override
	public String toString() {
		return "PersonBuilding [personBuildingId=" + personBuildingId
				+ ", custId=" + custId + ", enterpriseId=" + enterpriseId
				+ ", buildingId=" + buildingId + ", buildingCode="
				+ buildingCode + ", state=" + state + ", relationDate="
				+ relationDate + "]";
	}
	
	
	
	
	
	
}
