package com.everwing.coreservice.common.wy.dto;

import java.io.Serializable;

/**
 * Created by zhugeruifei on 17/8/28.
 */
public class ProjectGatingDTO implements Serializable{

    private static final long serialVersionUID = -1539217483004956421L;

    private String projectId;

    private String projectName;

    private String projectAddress;

    private int mkCount;

    private int mkOnLineCount;

    public ProjectGatingDTO() {
    }

    public ProjectGatingDTO(String projectId, String projectName, String projectAddress, int mkCount, int mkOnLineCount) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectAddress = projectAddress;
        this.mkCount = mkCount;
        this.mkOnLineCount = mkOnLineCount;
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

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public int getMkCount() {
        return mkCount;
    }

    public void setMkCount(int mkCount) {
        this.mkCount = mkCount;
    }

    public int getMkOnLineCount() {
        return mkOnLineCount;
    }

    public void setMkOnLineCount(int mkOnLineCount) {
        this.mkOnLineCount = mkOnLineCount;
    }

    @Override
    public String toString() {
        return "ProjectGatingDTO{" +
                "projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectAddress='" + projectAddress + '\'' +
                ", mkCount=" + mkCount +
                ", mkOnLineCount=" + mkOnLineCount +
                '}';
    }
}
