package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.Page;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


public class AbnormalChargeAcccountDto implements Serializable {

    private String buildingCode;

    private String projectId;

    private String houseCode;

    private String buildingName;

    private String wy;

    private String bt;

    private String water;

    private String elect;

    private BigDecimal common;

    private String ownerName;

    private String staffCode;

    private List<String> projectList;

    private Page page;

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getWy() {
        return wy;
    }

    public void setWy(String wy) {
        this.wy = wy;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getElect() {
        return elect;
    }

    public void setElect(String elect) {
        this.elect = elect;
    }

    public BigDecimal getCommon() {
        return common;
    }

    public void setCommon(BigDecimal common) {
        this.common = common;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public List<String> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<String> projectList) {
        this.projectList = projectList;
    }
}
