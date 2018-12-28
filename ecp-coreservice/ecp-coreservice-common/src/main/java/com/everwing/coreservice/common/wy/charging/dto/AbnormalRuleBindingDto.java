package com.everwing.coreservice.common.wy.charging.dto;

import com.everwing.coreservice.common.wy.charging.entity.TJfRuleBuildingRelationInfo;

public class AbnormalRuleBindingDto extends TJfRuleBuildingRelationInfo {

    private String hosueCode;

    private Integer buildingType;

    private String buildingFullName;

    private String name;

    private String phone;

    private String projectName;

    private String projectId;

    private Integer chargingType;

    public String getHosueCode() {
        return hosueCode;
    }

    public void setHosueCode(String hosueCode) {
        this.hosueCode = hosueCode;
    }

    public Integer getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(Integer buildingType) {
        this.buildingType = buildingType;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getChargingType() {
        return chargingType;
    }

    public void setChargingType(Integer chargingType) {
        this.chargingType = chargingType;
    }
}
