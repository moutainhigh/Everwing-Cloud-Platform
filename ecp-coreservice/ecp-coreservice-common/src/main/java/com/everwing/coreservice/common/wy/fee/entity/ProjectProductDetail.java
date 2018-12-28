package com.everwing.coreservice.common.wy.fee.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProjectProductDetail implements Serializable {
    private String id;

    private String projectAccountId;

    private BigDecimal money;

    private String createBy;

    private Date createTime;

    private String description;

    private String orderId;

    private Integer isAsset;

    private String houseCodeNew;

    private BigDecimal rateAfter;

    private BigDecimal rate;

    private BigDecimal rateFee;

    private String orderDetail;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProjectAccountId() {
        return projectAccountId;
    }

    public void setProjectAccountId(String projectAccountId) {
        this.projectAccountId = projectAccountId == null ? null : projectAccountId.trim();
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getIsAsset() {
        return isAsset;
    }

    public void setIsAsset(Integer isAsset) {
        this.isAsset = isAsset;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew == null ? null : houseCodeNew.trim();
    }

    public BigDecimal getRateAfter() {
        return rateAfter;
    }

    public void setRateAfter(BigDecimal rateAfter) {
        this.rateAfter = rateAfter;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRateFee() {
        return rateFee;
    }

    public void setRateFee(BigDecimal rateFee) {
        this.rateFee = rateFee;
    }

    public String getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        this.orderDetail = orderDetail == null ? null : orderDetail.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", projectAccountId=").append(projectAccountId);
        sb.append(", money=").append(money);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", description=").append(description);
        sb.append(", orderId=").append(orderId);
        sb.append(", isAsset=").append(isAsset);
        sb.append(", houseCodeNew=").append(houseCodeNew);
        sb.append(", rateAfter=").append(rateAfter);
        sb.append(", rate=").append(rate);
        sb.append(", rateFee=").append(rateFee);
        sb.append(", orderDetail=").append(orderDetail);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}