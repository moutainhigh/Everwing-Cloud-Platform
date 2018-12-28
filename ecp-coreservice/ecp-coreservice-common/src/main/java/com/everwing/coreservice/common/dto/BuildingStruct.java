package com.everwing.coreservice.common.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 建筑结构
 */
public class BuildingStruct implements Serializable{

    private String companyId;

    private String projectId;

    private List<Map<String,String>> buildings;

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

    public List<Map<String, String>> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Map<String, String>> buildings) {
        this.buildings = buildings;
    }
}
