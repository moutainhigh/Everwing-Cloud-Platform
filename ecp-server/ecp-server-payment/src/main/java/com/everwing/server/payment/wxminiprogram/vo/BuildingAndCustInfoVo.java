package com.everwing.server.payment.wxminiprogram.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: zgrf
 * @Description: 资产和业主信息
 * @Date: Create in 2018-10-17 10:54
 **/

public class BuildingAndCustInfoVo implements Serializable {

    private String buildId;

    private String buildingsAddress;

    private String buildingType;

    private String houseCode;

    private String buildingFullName;

    private String buildingProperty;

    private String constructionArea;

    private String comprisingConstructionArea;

    private String managementUnitPrice;

    private String maintenanceUnitPrice;

    private List<Map<String,String>> owers;

    public BuildingAndCustInfoVo() {
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getBuildingsAddress() {
        return buildingsAddress;
    }

    public void setBuildingsAddress(String buildingsAddress) {
        this.buildingsAddress = buildingsAddress;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
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

    public String getBuildingProperty() {
        return buildingProperty;
    }

    public void setBuildingProperty(String buildingProperty) {
        this.buildingProperty = buildingProperty;
    }

    public String getConstructionArea() {
        return constructionArea;
    }

    public void setConstructionArea(String constructionArea) {
        this.constructionArea = constructionArea;
    }

    public String getComprisingConstructionArea() {
        return comprisingConstructionArea;
    }

    public void setComprisingConstructionArea(String comprisingConstructionArea) {
        this.comprisingConstructionArea = comprisingConstructionArea;
    }

    public String getManagementUnitPrice() {
        return managementUnitPrice;
    }

    public void setManagementUnitPrice(String managementUnitPrice) {
        this.managementUnitPrice = managementUnitPrice;
    }

    public String getMaintenanceUnitPrice() {
        return maintenanceUnitPrice;
    }

    public void setMaintenanceUnitPrice(String maintenanceUnitPrice) {
        this.maintenanceUnitPrice = maintenanceUnitPrice;
    }

    public List<Map<String, String>> getOwers() {
        return owers;
    }

    public void setOwers(List<Map<String, String>> owers) {
        this.owers = owers;
    }

    @Override
    public String toString() {
        return "BuildingAndCustInfoVo{" +
                "buildingsAddress='" + buildingsAddress + '\'' +
                ", buildingType='" + buildingType + '\'' +
                ", houseCode='" + houseCode + '\'' +
                ", buildingFullName='" + buildingFullName + '\'' +
                ", buildingProperty='" + buildingProperty + '\'' +
                ", constructionArea='" + constructionArea + '\'' +
                ", comprisingConstructionArea='" + comprisingConstructionArea + '\'' +
                ", managementUnitPrice='" + managementUnitPrice + '\'' +
                ", maintenanceUnitPrice='" + maintenanceUnitPrice + '\'' +
                ", owers=" + owers +
                '}';
    }

}
