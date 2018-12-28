package com.everwing.coreservice.common.wy.fee.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProjectDelayDetail implements Serializable {
    private String id;

    private String delayAccountId;

    private BigDecimal amount;

    private Date createTime;

    private String createBy;

    private Integer accountType;

    private String houseCodeNew;

    private Integer chargeType;

    private String businessOperaDetailId;

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

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
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
        sb.append(", delayAccountId=").append(delayAccountId);
        sb.append(", amount=").append(amount);
        sb.append(", createTime=").append(createTime);
        sb.append(", createBy=").append(createBy);
        sb.append(", accountType=").append(accountType);
        sb.append(", houseCodeNew=").append(houseCodeNew);
        sb.append(", chargeType=").append(chargeType);
        sb.append(", businessOperaDetailId=").append(businessOperaDetailId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}