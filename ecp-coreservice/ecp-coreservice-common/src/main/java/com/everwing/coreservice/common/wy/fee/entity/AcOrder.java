package com.everwing.coreservice.common.wy.fee.entity;

import com.everwing.coreservice.common.Page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AcOrder implements Serializable {
    private String id;

    private String orderNo;

    private BigDecimal amount;

    private String payer;

    private String payerMobile;

    private Integer orderState;

    private Integer payState;

    private Integer orderType;

    private Date updateTime;

    private String operaId;

    private String signature;

    private Date paymentTime;

    private String houseCodeNew;

    private Integer paymentChannel;

    private Integer isRcorded;

    private String payChannelTradeNo;

    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer == null ? null : payer.trim();
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile == null ? null : payerMobile.trim();
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOperaId() {
        return operaId;
    }

    public void setOperaId(String operaId) {
        this.operaId = operaId == null ? null : operaId.trim();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew == null ? null : houseCodeNew.trim();
    }

    public Integer getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(Integer paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public Integer getIsRcorded() {
        return isRcorded;
    }

    public void setIsRcorded(Integer isRcorded) {
        this.isRcorded = isRcorded;
    }

    public String getPayChannelTradeNo() {
        return payChannelTradeNo;
    }

    public void setPayChannelTradeNo(String payChannelTradeNo) {
        this.payChannelTradeNo = payChannelTradeNo == null ? null : payChannelTradeNo.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", amount=").append(amount);
        sb.append(", payer=").append(payer);
        sb.append(", payerMobile=").append(payerMobile);
        sb.append(", orderState=").append(orderState);
        sb.append(", payState=").append(payState);
        sb.append(", orderType=").append(orderType);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", operaId=").append(operaId);
        sb.append(", signature=").append(signature);
        sb.append(", paymentTime=").append(paymentTime);
        sb.append(", houseCodeNew=").append(houseCodeNew);
        sb.append(", paymentChannel=").append(paymentChannel);
        sb.append(", isRcorded=").append(isRcorded);
        sb.append(", payChannelTradeNo=").append(payChannelTradeNo);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}