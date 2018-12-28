package com.everwing.coreservice.common.wy.fee.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProjectCycleDetail  implements Serializable  {
    private String id;

    private String cycleId;

    private BigDecimal amount;

    private Date createTime;

    private String createBy;

    private String description;

    private int accountType;

    private int chargeType;

    private int payChannel;

    private int businessType;

    private String houseCodeNew;

    private String businessOperaDetail;

    private BigDecimal rateAfter;

    private BigDecimal rate;

    private BigDecimal rateFee;

    private Integer payWay;

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

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId == null ? null : cycleId.trim();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getChargeType() {
        return chargeType;
    }

    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public String getBusinessOperaDetail() {
        return businessOperaDetail;
    }

    public void setBusinessOperaDetail(String businessOperaDetail) {
        this.businessOperaDetail = businessOperaDetail == null ? null : businessOperaDetail.trim();
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

    public int getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(int payChannel) {
        this.payChannel = payChannel;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    @Override
    public String toString() {
        return "ProjectCycleDetail{" +
                "id='" + id + '\'' +
                ", cycleId='" + cycleId + '\'' +
                ", amount=" + amount +
                ", createTime=" + createTime +
                ", createBy='" + createBy + '\'' +
                ", description='" + description + '\'' +
                ", accountType=" + accountType +
                ", chargeType=" + chargeType +
                ", payChannel=" + payChannel +
                ", businessType=" + businessType +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", businessOperaDetail='" + businessOperaDetail + '\'' +
                ", rateAfter=" + rateAfter +
                ", rate=" + rate +
                ", rateFee=" + rateFee +
                ", payWay=" + payWay +
                '}';
    }
}