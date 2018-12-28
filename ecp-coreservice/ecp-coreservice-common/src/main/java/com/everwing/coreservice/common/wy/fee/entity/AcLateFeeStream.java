package com.everwing.coreservice.common.wy.fee.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AcLateFeeStream implements Serializable {
    private String id;

    private String delayAccountId;

    private String houseCodeNew;

    private BigDecimal beforeAmount;

    private BigDecimal afterAmount;

    private BigDecimal changeAmount;

    private int businessType;

    private String projectId;

    private String projectName;

    private Date createTime;

    private String createId;

    private String description;

    private String operaId;

    private BigDecimal principalAccount;

    private BigDecimal rate;

    private Integer isSingleinterest;

    private Integer overdueDays;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDelayAccountId() {
        return delayAccountId;
    }

    public void setDelayAccountId(String delayAccountId) {
        this.delayAccountId = delayAccountId == null ? null : delayAccountId.trim();
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew == null ? null : houseCodeNew.trim();
    }

    public BigDecimal getBeforeAmount() {
        return beforeAmount;
    }

    public void setBeforeAmount(BigDecimal beforeAmount) {
        this.beforeAmount = beforeAmount;
    }

    public BigDecimal getAfterAmount() {
        return afterAmount;
    }

    public void setAfterAmount(BigDecimal afterAmount) {
        this.afterAmount = afterAmount;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getOperaId() {
        return operaId;
    }

    public void setOperaId(String operaId) {
        this.operaId = operaId == null ? null : operaId.trim();
    }

    public BigDecimal getPrincipalAccount() {
        return principalAccount;
    }

    public void setPrincipalAccount(BigDecimal principalAccount) {
        this.principalAccount = principalAccount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getIsSingleinterest() {
        return isSingleinterest;
    }

    public void setIsSingleinterest(Integer isSingleinterest) {
        this.isSingleinterest = isSingleinterest;
    }

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", delayAccountId=").append(delayAccountId);
        sb.append(", houseCodeNew=").append(houseCodeNew);
        sb.append(", beforeAmount=").append(beforeAmount);
        sb.append(", afterAmount=").append(afterAmount);
        sb.append(", changeAmount=").append(changeAmount);
        sb.append(", businessType=").append(businessType);
        sb.append(", projectId=").append(projectId);
        sb.append(", projectName=").append(projectName);
        sb.append(", createTime=").append(createTime);
        sb.append(", createId=").append(createId);
        sb.append(", description=").append(description);
        sb.append(", operaId=").append(operaId);
        sb.append(", principalAccount=").append(principalAccount);
        sb.append(", rate=").append(rate);
        sb.append(", isSingleinterest=").append(isSingleinterest);
        sb.append(", overdueDays=").append(overdueDays);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}