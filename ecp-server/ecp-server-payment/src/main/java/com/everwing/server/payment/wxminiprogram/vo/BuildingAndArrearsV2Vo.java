package com.everwing.server.payment.wxminiprogram.vo;

import java.io.Serializable;

/**
 * @Author: zgrf
 * @Description: 建筑信息以及该建筑的缴费总额版本2.0
 * @Date: Create in 2018-10-15 16:38
 **/

public class BuildingAndArrearsV2Vo implements Serializable {

    private String buildingFullName;

    private String buildType;

    private String houseCode;

    private Double arrears;

    private String projectName;

    public BuildingAndArrearsV2Vo() {
    }

    public BuildingAndArrearsV2Vo(String buildingFullName, String buildType, String houseCode, Double arrears, String projectName) {
        this.buildingFullName = buildingFullName;
        this.buildType = buildType;
        this.houseCode = houseCode;
        this.arrears = arrears;
        this.projectName = projectName;
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

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public Double getArrears() {
        return arrears;
    }

    public void setArrears(Double arrears) {
        this.arrears = arrears;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "BuildingAndArrearsV2Vo{" +
                "buildingFullName='" + buildingFullName + '\'' +
                ", buildType='" + buildType + '\'' +
                ", houseCode='" + houseCode + '\'' +
                ", arrears=" + arrears +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
