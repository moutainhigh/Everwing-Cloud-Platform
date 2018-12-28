package com.everwing.coreservice.common.wy.fee.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AcSpecialDetail implements Serializable {
    private String id;

    private String specialId;

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

    private String billDetailId;

    private String operaId;

    //加一个账户类型，为推送到财务业务中要用到抵扣类型
    private  Integer accountType;

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSpecialId() {
        return specialId;
    }

    public void setSpecialId(String specialId) {
        this.specialId = specialId == null ? null : specialId.trim();
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

    public String getBillDetailId() {
        return billDetailId;
    }

    public void setBillDetailId(String billDetailId) {
        this.billDetailId = billDetailId == null ? null : billDetailId.trim();
    }

    public String getOperaId() {
        return operaId;
    }

    public void setOperaId(String operaId) {
        this.operaId = operaId == null ? null : operaId.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", specialId=").append(specialId);
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
        sb.append(", billDetailId=").append(billDetailId);
        sb.append(", operaId=").append(operaId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}