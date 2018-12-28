package com.everwing.coreservice.common.wy.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 物业APP我的工单查询DTO
 * Created by zhugeruifei on 17/10/14.
 */
public class MyWorkOrderDTO implements Serializable{

    private static final long serialVersionUID = -4062274700867381120L;
    private String orderCode;

    private Date createTime;

    private Date updateTime;

    private int status;

    private String description;

    private String buildingCode;

    private String buildingFullName;

    private String projectId;

    private String projectName;

    private String customerMobile;

    private String orderType1;

    private String orderType2;

    private String orderType3;

    private String principalSysUser;

    private int needCallback;

    private int sourceType;

    private int isUrgent;

    private String procesInstructions;

    public MyWorkOrderDTO() {
    }

    public MyWorkOrderDTO(String orderCode, Date createTime, Date updateTime, int status, String description, String buildingCode, String buildingFullName, String projectId, String projectName, String customerMobile, String orderType1, String orderType2, String orderType3, String principalSysUser, int needCallback, int sourceType, int isUrgent) {
        this.orderCode = orderCode;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.status = status;
        this.description = description;
        this.buildingCode = buildingCode;
        this.buildingFullName = buildingFullName;
        this.projectId = projectId;
        this.projectName = projectName;
        this.customerMobile = customerMobile;
        this.orderType1 = orderType1;
        this.orderType2 = orderType2;
        this.orderType3 = orderType3;
        this.principalSysUser = principalSysUser;
        this.needCallback = needCallback;
        this.sourceType = sourceType;
        this.isUrgent = isUrgent;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getBuildingFullName() {
        return buildingFullName;
    }

    public void setBuildingFullName(String buildingFullName) {
        this.buildingFullName = buildingFullName;
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

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getOrderType1() {
        return orderType1;
    }

    public void setOrderType1(String orderType1) {
        this.orderType1 = orderType1;
    }

    public String getOrderType2() {
        return orderType2;
    }

    public void setOrderType2(String orderType2) {
        this.orderType2 = orderType2;
    }

    public String getOrderType3() {
        return orderType3;
    }

    public void setOrderType3(String orderType3) {
        this.orderType3 = orderType3;
    }

    public String getPrincipalSysUser() {
        return principalSysUser;
    }

    public void setPrincipalSysUser(String principalSysUser) {
        this.principalSysUser = principalSysUser;
    }

    public int getNeelCallback() {
        return needCallback;
    }

    public void setNeelCallback(int needCallback) {
        this.needCallback = needCallback;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(int isUrgent) {
        this.isUrgent = isUrgent;
    }

    public int getNeedCallback() {
        return needCallback;
    }

    public void setNeedCallback(int needCallback) {
        this.needCallback = needCallback;
    }

    public String getProcesInstructions() {
        return procesInstructions;
    }

    public void setProcesInstructions(String procesInstructions) {
        this.procesInstructions = procesInstructions;
    }

    @Override
    public String toString() {
        return "MyWorkOrderDTO{" +
                "orderCode='" + orderCode + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", buildingCode='" + buildingCode + '\'' +
                ", buildingFullName='" + buildingFullName + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", customerMobile='" + customerMobile + '\'' +
                ", orderType1='" + orderType1 + '\'' +
                ", orderType2='" + orderType2 + '\'' +
                ", orderType3='" + orderType3 + '\'' +
                ", principalSysUser='" + principalSysUser + '\'' +
                ", needCallback=" + needCallback +
                ", sourceType=" + sourceType +
                ", isUrgent=" + isUrgent +
                '}';
    }
}
