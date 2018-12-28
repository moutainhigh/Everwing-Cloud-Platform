package com.everwing.coreservice.common.platform.entity.extra;

import java.io.Serializable;

public class AuthorizationInfoDto implements Serializable{

    private String authorizedAccountCode;

    private String authorizedAccountName;

    private String authorizeeAccountCode;

    private String authorizeeAccountName;

    private String buildingId;

    private String buildingName;

    private String buildingCode;

    private String projectId;

    private String projectName;

    private String gatingId;

    private String gatingCode;

    private String gatingAddress;

    public String getAuthorizeeAccountCode() {
        return authorizeeAccountCode;
    }

    public void setAuthorizeeAccountCode(String authorizeeAccountCode) {
        this.authorizeeAccountCode = authorizeeAccountCode;
    }

    public String getAuthorizeeAccountName() {
        return authorizeeAccountName;
    }

    public void setAuthorizeeAccountName(String authorizeeAccountName) {
        this.authorizeeAccountName = authorizeeAccountName;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGatingId() {
        return gatingId;
    }

    public void setGatingId(String gatingId) {
        this.gatingId = gatingId;
    }

    public String getGatingCode() {
        return gatingCode;
    }

    public void setGatingCode(String gatingCode) {
        this.gatingCode = gatingCode;
    }

    public String getGatingAddress() {
        return gatingAddress;
    }

    public void setGatingAddress(String gatingAddress) {
        this.gatingAddress = gatingAddress;
    }

    public String getAuthorizedAccountCode() {
        return authorizedAccountCode;
    }

    public void setAuthorizedAccountCode(String authorizedAccountCode) {
        this.authorizedAccountCode = authorizedAccountCode;
    }

    public String getAuthorizedAccountName() {
        return authorizedAccountName;
    }

    public void setAuthorizedAccountName(String authorizedAccountName) {
        this.authorizedAccountName = authorizedAccountName;
    }
}
