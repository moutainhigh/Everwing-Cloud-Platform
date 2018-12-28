package com.everwing.coreservice.common.wy.fee.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AcAccount implements Serializable {
    private String id;

    private String houseCodeNew;

    private BigDecimal totalAmount;

    private BigDecimal commonDepositAmount;

    private BigDecimal specialDepositAmount;

    private BigDecimal lateFeeAmount;

    private BigDecimal currentChargingAmount;

    private BigDecimal lastArrearsAmount;

    private BigDecimal currentBillAmount;

    private String projectId;

    private String projectName;

    private Date createTime;

    private String createId;

    private Date modifyTime;

    private String modifyId;

    private String signature;

    private String chargingMonth;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew == null ? null : houseCodeNew.trim();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getCommonDepositAmount() {
        return commonDepositAmount;
    }

    public void setCommonDepositAmount(BigDecimal commonDepositAmount) {
        this.commonDepositAmount = commonDepositAmount;
    }

    public BigDecimal getSpecialDepositAmount() {
        return specialDepositAmount;
    }

    public void setSpecialDepositAmount(BigDecimal specialDepositAmount) {
        this.specialDepositAmount = specialDepositAmount;
    }

    public BigDecimal getLateFeeAmount() {
        return lateFeeAmount;
    }

    public void setLateFeeAmount(BigDecimal lateFeeAmount) {
        this.lateFeeAmount = lateFeeAmount;
    }

    public BigDecimal getCurrentChargingAmount() {
        return currentChargingAmount;
    }

    public void setCurrentChargingAmount(BigDecimal currentChargingAmount) {
        this.currentChargingAmount = currentChargingAmount;
    }

    public BigDecimal getLastArrearsAmount() {
        return lastArrearsAmount;
    }

    public void setLastArrearsAmount(BigDecimal lastArrearsAmount) {
        this.lastArrearsAmount = lastArrearsAmount;
    }

    public BigDecimal getCurrentBillAmount() {
        return currentBillAmount;
    }

    public void setCurrentBillAmount(BigDecimal currentBillAmount) {
        this.currentBillAmount = currentBillAmount;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
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
        this.createId = createId == null ? null : createId.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId == null ? null : modifyId.trim();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public String getChargingMonth() {
        return chargingMonth;
    }

    public void setChargingMonth(String chargingMonth) {
        this.chargingMonth = chargingMonth == null ? null : chargingMonth.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", houseCodeNew=").append(houseCodeNew);
        sb.append(", totalAmount=").append(totalAmount);
        sb.append(", commonDepositAmount=").append(commonDepositAmount);
        sb.append(", specialDepositAmount=").append(specialDepositAmount);
        sb.append(", lateFeeAmount=").append(lateFeeAmount);
        sb.append(", currentChargingAmount=").append(currentChargingAmount);
        sb.append(", lastArrearsAmount=").append(lastArrearsAmount);
        sb.append(", currentBillAmount=").append(currentBillAmount);
        sb.append(", projectId=").append(projectId);
        sb.append(", projectName=").append(projectName);
        sb.append(", createTime=").append(createTime);
        sb.append(", createId=").append(createId);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", modifyId=").append(modifyId);
        sb.append(", signature=").append(signature);
        sb.append(", chargingMonth=").append(chargingMonth);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}