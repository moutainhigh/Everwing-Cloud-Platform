package com.everwing.coreservice.common.wy.fee.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-08-14 15:24
 **/

public class AcBusinessOperaDetailDto implements Serializable{

    private String id;

    private String operationId;

    private Date operationTime;

    private int businessType;

    private Double amount;

    private String projectId;

    private String projectName;

    private String remark;

    private int personType;

    private int clientType;

    private String signature;

    private String companyId;

    public AcBusinessOperaDetailDto() {
    }

    public AcBusinessOperaDetailDto(String id, String operationId, Date operationTime, int businessType, Double amount, String projectId, String projectName, String remark, int personType, int clientType, String signature) {
        this.id = id;
        this.operationId = operationId;
        this.operationTime = operationTime;
        this.businessType = businessType;
        this.amount = amount;
        this.projectId = projectId;
        this.projectName = projectName;
        this.remark = remark;
        this.personType = personType;
        this.clientType = clientType;
        this.signature = signature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "AcBusinessOperaDetailDto{" +
                "id='" + id + '\'' +
                ", operationId='" + operationId + '\'' +
                ", operationTime=" + operationTime +
                ", businessType=" + businessType +
                ", amount=" + amount +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", remark='" + remark + '\'' +
                ", personType=" + personType +
                ", clientType=" + clientType +
                ", signature='" + signature + '\'' +
                '}';
    }
}
