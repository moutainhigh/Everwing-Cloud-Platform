package com.everwing.coreservice.common.wy.charging.entity;

import java.util.Date;

public class TJfChargingItemBaseInfo {
    private String id;

    /**
     *计费项名称
     */
    private String chargingItemName;

    /**
     *计费项编号，这里对应账户的1 物业，2本体，3水等，方便的关联
     */
    private Integer chargingType;

    /**
     *描述计费项是否使用中，0 使用  1 停用
     */
    private Integer isUsed;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *开启时间，做一个记录
     */
    private Date startUsedTime;

    /**
     *停止时间，有可能中间会停用，做个记录
     */
    private Date stopTime;

    /**
     *创建人
     */
    private String createId;

    /**
     *项目id
     */
    private String projectId;

    /**
     *项目名称
     */
    private String projectName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChargingItemName() {
        return chargingItemName;
    }

    public void setChargingItemName(String chargingItemName) {
        this.chargingItemName = chargingItemName;
    }

    public Integer getChargingType() {
        return chargingType;
    }

    public void setChargingType(Integer chargingType) {
        this.chargingType = chargingType;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartUsedTime() {
        return startUsedTime;
    }

    public void setStartUsedTime(Date startUsedTime) {
        this.startUsedTime = startUsedTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
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
}