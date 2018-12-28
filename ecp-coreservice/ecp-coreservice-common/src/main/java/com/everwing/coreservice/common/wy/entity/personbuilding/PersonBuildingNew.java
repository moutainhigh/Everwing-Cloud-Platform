package com.everwing.coreservice.common.wy.entity.personbuilding;

import com.everwing.coreservice.common.BaseEntity;
import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNewSearch;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "PersonBuildingNew")
public class PersonBuildingNew extends BaseEntity implements Cloneable{

	private static final long serialVersionUID = -7020055037493517716L;

	private String personBuildingId;  //客户房屋关系id

    private String custId;   //个人客户id

    private String enterpriseId;//企业id

    private String buildingId;//建筑id
    
    private String buildingCode; //建筑code

    private Byte state;//关联状态(0开启，1禁用)

    private String custType;//客户类型

    private Byte enterpriseCallType;//企业呼叫人状态

    private String accessory ;  //  上传附件信息
    
    private PersonCustNew personCustNew; //个人客户对象
    
    private EnterpriseCustNewSearch enterpriseCustNew; //企业客户对象
    
    private Page page;
    
    private Date relationDate;//关联时间
    
    //查出其他需要字段
    private String buildingFullName;	//建筑全名
    
    private String name;	//客户名字
    
    private String enterpriseName;	//企业客户名字

	private String projectId;

	private String projectName;
    
	public Object clone(){  
		PersonBuildingNew sc = null;  
        try  
        {  
            sc = (PersonBuildingNew) super.clone();  
        } catch (CloneNotSupportedException e){  
            e.printStackTrace();  
        }  
        return sc;  
    }


	public String getBuildingFullName() {
		return buildingFullName;
	}


	public void setBuildingFullName(String buildingFullName) {
		this.buildingFullName = buildingFullName;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEnterpriseName() {
		return enterpriseName;
	}


	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
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


	public String getBuildingId() {
		return buildingId;
	}


	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
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


	public PersonCustNew getPersonCustNew() {
		return personCustNew;
	}


	public void setPersonCustNew(PersonCustNew personCustNew) {
		this.personCustNew = personCustNew;
	}


	public EnterpriseCustNewSearch getEnterpriseCustNew() {
		return enterpriseCustNew;
	}


	public void setEnterpriseCustNew(EnterpriseCustNewSearch enterpriseCustNew) {
		this.enterpriseCustNew = enterpriseCustNew;
	}


	public Page getPage() {
		return page;
	}


	public void setPage(Page page) {
		this.page = page;
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

	public String getProjectId() { return projectId; }

	public void setProjectId(String projectId) { this.projectId = projectId; }

	public String getProjectName() { return projectName; }

	public void setProjectName(String projectName) { this.projectName = projectName; }

	@Override
	public String toString() {
		return "PersonBuildingNew [personBuildingId=" + personBuildingId
				+ ", custId=" + custId + ", enterpriseId=" + enterpriseId
				+ ", buildingId=" + buildingId + ", buildingCode="
				+ buildingCode + ", state=" + state + ", custType=" + custType
				+ ", enterpriseCallType=" + enterpriseCallType + ", accessory="
				+ accessory + ", personCustNew=" + personCustNew
				+ ", enterpriseCustNew=" + enterpriseCustNew + ", page=" + page
				+ ", relationDate=" + relationDate + ", buildingFullName="
				+ buildingFullName + ", name=" + name + ", enterpriseName="
				+ enterpriseName + "]";
	}

	
	
}