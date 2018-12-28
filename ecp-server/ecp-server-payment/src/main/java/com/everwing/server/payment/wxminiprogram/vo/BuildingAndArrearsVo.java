package com.everwing.server.payment.wxminiprogram.vo;

import com.everwing.coreservice.common.wy.dto.BuildingAndCustDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zgrf
 * @Description: 建筑信息以及该建筑的缴费总额
 * @Date: Create in 2018-07-29 16:14
 **/

public class BuildingAndArrearsVo implements Serializable {

    private String houseCode;

    private String buildingFullName;

    private String buildType;

    private String buildingId;

    private String ownersName;

    private String ownersMobile;

    private String companyId;

    private String projectId;

    private BuildingAndCustDTO buindingAndCustDTO;

    private String projectName;

    private List<Map<String,String>> owners;

    private Double arrears;

    public BuildingAndArrearsVo() {
    }

    public BuildingAndArrearsVo(BuildingAndCustDTO buindingAndCustDTO) {
        this.buindingAndCustDTO = buindingAndCustDTO;
        this.houseCode = buindingAndCustDTO.getHouseCode();
        this.buildingFullName = buindingAndCustDTO.getBuildingFullName();
        this.buildType = buindingAndCustDTO.getBuildType();
        this.buildingId = buindingAndCustDTO.getBuildingId();
        this.ownersName = buindingAndCustDTO.getOwnersName();
        this.ownersMobile = buindingAndCustDTO.getOwnersMobile();
        this.companyId = buindingAndCustDTO.getCompanyId();
        this.projectId = buindingAndCustDTO.getProjectId();
    }

    public BuildingAndArrearsVo(String houseCode, String buildingFullName, String buildType, String buildingId, String ownersName, String ownersMobile, String companyId, String projectId, String projectName) {
        this.houseCode = houseCode;
        this.buildingFullName = buildingFullName;
        this.buildType = buildType;
        this.buildingId = buildingId;
        this.ownersName = ownersName;
        this.ownersMobile = ownersMobile;
        this.companyId = companyId;
        this.projectId = projectId;
        this.projectName = projectName;
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

    public void setBuildingFullName(String buindingFullName) {
        this.buildingFullName = buindingFullName;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public BuildingAndCustDTO getBuindingAndCustDTO() {
        return buindingAndCustDTO;
    }

    public void setBuindingAndCustDTO(BuildingAndCustDTO buindingAndCustDTO) {
        this.buindingAndCustDTO = buindingAndCustDTO;
    }

    public Double getArrears() {
        return arrears;
    }

    public void setArrears(Double arrears) {
        this.arrears = arrears;
    }

    public List<Map<String, String>> getOwners() {
        owners = new ArrayList<Map<String,String>>();
        Map<String,String> map = new HashMap<String, String>();
        map.put("name",ownersName);
        map.put("mobile",ownersMobile);
        owners.add(map);
        return owners;
    }

    public void setOwners(List<Map<String, String>> owners) {
        this.owners = owners;
    }

    @Override
    public String toString() {
        return "BuildingAndArrearsVo{" +
                "houseCode='" + houseCode + '\'' +
                ", buildingFullName='" + buildingFullName + '\'' +
                ", buildType='" + buildType + '\'' +
                ", buildingId='" + buildingId + '\'' +
                ", ownersName='" + ownersName + '\'' +
                ", ownersMobile='" + ownersMobile + '\'' +
                ", companyId='" + companyId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
