package com.everwing.coreservice.common.wy.dto;

import java.io.Serializable;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-07-29 16:35
 **/

public class BuildingAndCustDTO implements Serializable {

    private String houseCode;

    private String buildingFullName;

    private String buildType;

    private String buildingId;

    private String ownersName;

    private String ownersMobile;

    private String companyId;

    private String projectId;

    public BuildingAndCustDTO() {
    }

    public BuildingAndCustDTO(String houseCode, String buindingFullName, String buildType, String buildingId, String ownersName, String ownersMobile, String companyId, String projectId) {
        this.houseCode = houseCode;
        this.buildingFullName = buindingFullName;
        this.buildType = buildType;
        this.buildingId = buildingId;
        this.ownersName = ownersName;
        this.ownersMobile = ownersMobile;
        this.companyId = companyId;
        this.projectId = projectId;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getBuildingFullName() {
        return buildingFullName;
    }

    public void setBuildingFullName(String buildingFullName) {
        this.buildingFullName = buildingFullName;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getOwnersName() {
        return ownersName;
    }

    public void setOwnersName(String ownersName) {
        this.ownersName = ownersName;
    }

    public String getOwnersMobile() {
        return ownersMobile;
    }

    public void setOwnersMobile(String ownersMobile) {
        this.ownersMobile = ownersMobile;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "BuildingAndCustDTO{" +
                "houseCode='" + houseCode + '\'' +
                ", buildingFullName='" + buildingFullName + '\'' +
                ", buildType='" + buildType + '\'' +
                ", buildingId='" + buildingId + '\'' +
                ", ownersName='" + ownersName + '\'' +
                ", ownersMobile='" + ownersMobile + '\'' +
                ", companyId='" + companyId + '\'' +
                ", projectId='" + projectId + '\'' +
                '}';
    }
}
