package com.everwing.coreservice.common.wy.fee.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProjectAccount implements Serializable {
    private String id;

    private String projectId;

    private String companyId;

    private String projectName;

    private String companyName;

    private Date createTime;

    private Date updateTime;

    private Integer version;

    private String signature;

    private BigDecimal totalAmount;

    private BigDecimal cycleAmount;

    private BigDecimal productAmount;

    private BigDecimal lateAmount;

    private BigDecimal fineAmount;

    private BigDecimal predepositAmount;

    private BigDecimal refundAmount;

    //单纯的接收一个参数
    private String staffCode;

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getCycleAmount() {
        return cycleAmount;
    }

    public void setCycleAmount(BigDecimal cycleAmount) {
        this.cycleAmount = cycleAmount;
    }

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    public BigDecimal getLateAmount() {
        return lateAmount;
    }

    public void setLateAmount(BigDecimal lateAmount) {
        this.lateAmount = lateAmount;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    public BigDecimal getPredepositAmount() {
        return predepositAmount;
    }

    public void setPredepositAmount(BigDecimal predepositAmount) {
        this.predepositAmount = predepositAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    @Override
    public String toString() {
        return "ProjectAccount{" +
                "id='" + id + '\'' +
                ", projectId='" + projectId + '\'' +
                ", companyId='" + companyId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                ", signature='" + signature + '\'' +
                ", totalAmount=" + totalAmount +
                ", cycleAmount=" + cycleAmount +
                ", productAmount=" + productAmount +
                ", lateAmount=" + lateAmount +
                ", fineAmount=" + fineAmount +
                ", predepositAmount=" + predepositAmount +
                ", refundAmount=" + refundAmount +
                ", staffCode='" + staffCode + '\'' +
                '}';
    }
}