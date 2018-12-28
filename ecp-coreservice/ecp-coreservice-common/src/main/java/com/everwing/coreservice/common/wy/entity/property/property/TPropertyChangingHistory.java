package com.everwing.coreservice.common.wy.entity.property.property;

import com.everwing.coreservice.common.BaseEntity;

import java.util.Date;

public class TPropertyChangingHistory extends BaseEntity{
    private static final long serialVersionUID = -6207646434835884830L;
    private String id;

    private String buildingCode;

    private String buildingType;

    private String oldHolder;

    private String newHolder;

    private String oldHolderName;

    private String newHolderName;

    private String buildingCertificate;

    private Date subscribedDate;

    private String custFrom;
    
    private String buildingId;
    
    private String projectId;
    
    private String enterpriseNewId;
    
    private String enterpriseOldId;
    
    
    
    public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	
	

	public String getEnterpriseOldId() {
		return enterpriseOldId;
	}

	public void setEnterpriseOldId(String enterpriseOldId) {
		this.enterpriseOldId = enterpriseOldId;
	}

	public String getEnterpriseNewId() {
		return enterpriseNewId;
	}

	public void setEnterpriseNewId(String enterpriseNewId) {
		this.enterpriseNewId = enterpriseNewId;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingCode() {
        return this.buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getBuildingType() {
        return this.buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    public String getOldHolder() {
        return this.oldHolder;
    }

    public void setOldHolder(String oldHolder) {
        this.oldHolder = oldHolder;
    }

    public String getNewHolder() {
        return this.newHolder;
    }

    public void setNewHolder(String newHolder) {
        this.newHolder = newHolder;
    }

    public String getOldHolderName() {
        return this.oldHolderName;
    }

    public void setOldHolderName(String oldHolderName) {
        this.oldHolderName = oldHolderName;
    }

    public String getNewHolderName() {
        return this.newHolderName;
    }

    public void setNewHolderName(String newHolderName) {
        this.newHolderName = newHolderName;
    }

    public String getBuildingCertificate() {
        return this.buildingCertificate;
    }

    public void setBuildingCertificate(String buildingCertificate) {
        this.buildingCertificate = buildingCertificate;
    }

    public Date getSubscribedDate() {
        return this.subscribedDate;
    }

    public void setSubscribedDate(Date subscribedDate) {
        this.subscribedDate = subscribedDate;
    }

    public String getCustFrom() {
        return this.custFrom;
    }

    public void setCustFrom(String custFrom) {
        this.custFrom = custFrom;
    }
}