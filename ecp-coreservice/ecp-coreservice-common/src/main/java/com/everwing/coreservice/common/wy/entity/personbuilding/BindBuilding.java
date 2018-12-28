package com.everwing.coreservice.common.wy.entity.personbuilding;

import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNew;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;

import java.io.Serializable;
import java.util.Date;

/**
 * 用于客户资产绑定用导入导出的实体类
 *
 */
public class BindBuilding implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2700564066347957024L;
	/**-------- tc_person_building --------*/
	private String personBuildingId;	//主键
	private String custId;				//客户id
	private String enterpriseId;		//企业客户id
//	private String buildingId;	        //建筑Code
	private Byte state;					//建筑状态
	private String custType;			//客户类型
	private Byte enterpriseCallType;	//
	private String accessory;			//
	private Date relationDate;			//关联日期
	
	/*后去去掉了buildding_stauctureid表，实体有些变动*/
	private String custName;            //个人客户姓名
	private String enterpriseName;      //企业客户名称
	
	/**-------- tc_building --------*/
	private String buildingId;			//建筑id
	private String buildingCode;		//建筑Code
	private String buildingName;		//建筑名
	private String buildingFullName;	//建筑全名
	private String pId;					//父级建筑Code
	private Double buildingArea;		//建筑面积
	private Double totalArea;			//总面积
	private Double completionArea;		//完成面积
	private String projectId;			//项目id
	private String parcelId;			//宗地id
	private String buildingAccount;		//建筑注册账号
	private String buildingType;		//建筑类型
	
	
	/**-------- tc_person_cust --------*/
	private PersonCustNew personCust;
	
	/**-------- tc_enterprise_cust --------*/
	private EnterpriseCustNew enterpriseCust;
	
	
	
	public PersonCustNew getPersonCust() {
		return personCust;
	}
	public void setPersonCust(PersonCustNew personCust) {
		this.personCust = personCust;
	}
	public EnterpriseCustNew getEnterpriseCust() {
		return enterpriseCust;
	}
	public void setEnterpriseCust(EnterpriseCustNew enterpriseCust) {
		this.enterpriseCust = enterpriseCust;
	}
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
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
	public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public Byte getEnterpriseCallType() {
		return enterpriseCallType;
	}
	public void setEnterpriseCallType(Byte enterpriseCallType) {
		this.enterpriseCallType = enterpriseCallType;
	}
	public String getAccessory() {
		return accessory;
	}
	public void setAccessory(String accessory) {
		this.accessory = accessory;
	}
	public Date getRelationDate() {
		return relationDate;
	}
	public void setRelationDate(Date relationDate) {
		this.relationDate = relationDate;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
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
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public Double getBuildingArea() {
		return buildingArea;
	}
	public void setBuildingArea(Double buildingArea) {
		this.buildingArea = buildingArea;
	}
	public Double getTotalArea() {
		return totalArea;
	}
	public void setTotalArea(Double totalArea) {
		this.totalArea = totalArea;
	}
	public Double getCompletionArea() {
		return completionArea;
	}
	public void setCompletionArea(Double completionArea) {
		this.completionArea = completionArea;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getParcelId() {
		return parcelId;
	}
	public void setParcelId(String parcelId) {
		this.parcelId = parcelId;
	}
	public String getBuildingAccount() {
		return buildingAccount;
	}
	public void setBuildingAccount(String buildingAccount) {
		this.buildingAccount = buildingAccount;
	}
	public String getBuildingType() {
		return buildingType;
	}
	
	public void setBuildingType(String buildingType) {
		
		if("house".equals(buildingType))
			this.buildingType = "住宅";
		else if("parkspace".equals(buildingType))
			this.buildingType ="车位";
		else if("store".equals(buildingType))
			this.buildingType = "商铺";
		else
			this.buildingType = buildingType;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
