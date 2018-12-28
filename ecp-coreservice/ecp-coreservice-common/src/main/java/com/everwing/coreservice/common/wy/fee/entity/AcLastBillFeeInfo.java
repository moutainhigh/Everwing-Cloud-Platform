package com.everwing.coreservice.common.wy.fee.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AcLastBillFeeInfo implements Serializable {
    private String id;

    private String houseCodeNew;

    private BigDecimal lastBillFee;

    private String accountId;

    private String projectId;

    private String projectName;

    private String createId;

    private Date createTime;

    private String modifyId;

    private Date modifyTime;

    private int accountType;

    private String signature;

    private Integer chargingMonth;

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

    public BigDecimal getLastBillFee() {
        return lastBillFee;
    }

    public void setLastBillFee(BigDecimal lastBillFee) {
        this.lastBillFee = lastBillFee;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
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

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId == null ? null : createId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId == null ? null : modifyId.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public Integer getChargingMonth() {
        return chargingMonth;
    }

    public void setChargingMonth(Integer chargingMonth) {
        this.chargingMonth = chargingMonth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", houseCodeNew=").append(houseCodeNew);
        sb.append(", lastBillFee=").append(lastBillFee);
        sb.append(", accountId=").append(accountId);
        sb.append(", projectId=").append(projectId);
        sb.append(", projectName=").append(projectName);
        sb.append(", createId=").append(createId);
        sb.append(", createTime=").append(createTime);
        sb.append(", modifyId=").append(modifyId);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", accountType=").append(accountType);
        sb.append(", signature=").append(signature);
        sb.append(", chargingMonth=").append(chargingMonth);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}