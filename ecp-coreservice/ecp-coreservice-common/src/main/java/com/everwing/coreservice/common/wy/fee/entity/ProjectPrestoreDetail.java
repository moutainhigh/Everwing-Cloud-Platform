package com.everwing.coreservice.common.wy.fee.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProjectPrestoreDetail implements Serializable {
    private String id;

    private String prestoreAccount;

    private BigDecimal amount;

    private Date createTime;

    private String createBy;

    private int type;

    private int businessType;

    private String orderId;

    private String houseCodeNew;

    private String businessOperaDetailId;

    private Integer payWay;

    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPrestoreAccount() {
        return prestoreAccount;
    }

    public void setPrestoreAccount(String prestoreAccount) {
        this.prestoreAccount = prestoreAccount == null ? null : prestoreAccount.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew == null ? null : houseCodeNew.trim();
    }

    public String getBusinessOperaDetailId() {
        return businessOperaDetailId;
    }

    public void setBusinessOperaDetailId(String businessOperaDetailId) {
        this.businessOperaDetailId = businessOperaDetailId == null ? null : businessOperaDetailId.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", prestoreAccount=").append(prestoreAccount);
        sb.append(", amount=").append(amount);
        sb.append(", createTime=").append(createTime);
        sb.append(", createBy=").append(createBy);
        sb.append(", type=").append(type);
        sb.append(", businessType=").append(businessType);
        sb.append(", orderId=").append(orderId);
        sb.append(", houseCodeNew=").append(houseCodeNew);
        sb.append(", businessOperaDetailId=").append(businessOperaDetailId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}