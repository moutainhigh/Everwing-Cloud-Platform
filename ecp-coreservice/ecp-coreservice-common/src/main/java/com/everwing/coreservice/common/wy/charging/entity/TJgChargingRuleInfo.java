package com.everwing.coreservice.common.wy.charging.entity;

import java.util.Date;

public class TJgChargingRuleInfo {
    private String id;

    private String billingItemId;

    private String ruleDescription;

    private String projectId;

    private String projectName;

    private Date createTime;

    private String createId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillingItemId() {
        return billingItemId;
    }

    public void setBillingItemId(String billingItemId) {
        this.billingItemId = billingItemId;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }
}