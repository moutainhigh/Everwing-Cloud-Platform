package com.everwing.coreservice.common.wy.fee.entity;


import com.everwing.coreservice.common.Page;

import java.io.Serializable;
import java.util.List;

public class FinaceAccount implements Serializable {
    private static final long serialVersionUID = 9147041429742911786L;
    private String projectId;

    private String houseCode;
    private String buildingFullName;
    private String userName;
    private Double minMoney;
    private Double maxMoney;
    //单纯的接收一个参数
    private String staffCode;

    private List<String> projectList;

    private Page page;

    public List<String> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<String> projectList) {
        this.projectList = projectList;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
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

    public String getBuildingFullName() {
        return buildingFullName;
    }

    public void setBuildingFullName(String buildingFullName) {
        this.buildingFullName = buildingFullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(Double minMoney) {
        this.minMoney = minMoney;
    }

    public Double getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(Double maxMoney) {
        this.maxMoney = maxMoney;
    }

    @Override
    public String toString() {
        return "FinaceAccount{" +
                "projectId='" + projectId + '\'' +

                ", houseCode='" + houseCode + '\'' +
                ", buildingFullName='" + buildingFullName + '\'' +
                ", userName='" + userName + '\'' +
                ", minMoney=" + minMoney +
                ", maxMoney=" + maxMoney +
                '}';
    }


}
