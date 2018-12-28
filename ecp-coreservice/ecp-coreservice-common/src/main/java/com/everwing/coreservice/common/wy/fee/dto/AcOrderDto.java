package com.everwing.coreservice.common.wy.fee.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: zgrf
 * @Description: 订单，可能包含周期性订单和产品型订单
 * @Date: Create in 2018-08-13 15:36
 **/

public class AcOrderDto implements Serializable{

    private String id;

    private String orderNo;

    private double amount;

    private String payer;

    private String payerMobile;

    private int orderState;

    private int payState;

    private int orderType;

    private Date updateTime;

    private String operaId;

    private String signature;

    private Date paymentTime;

    private int paymentCahnnel;

    private String houseCodeNew;

    private int isRcorded;

    private String transactionId;

    private List<AcOrderCycleDetailDto> orderCycleDetailList;

    private List<AcOrderProductDetailDto> orderProductDetailList;

    public AcOrderDto() {
    }

    public AcOrderDto(String id, String orderNo, double amount, String payer, String payerMobile, int orderState, int payState, int orderType, Date updateTime, String operaId, String signature, Date paymentTime, int paymentCahnnel, List<AcOrderCycleDetailDto> orderCycleDetailList, List<AcOrderProductDetailDto> orderProductDetailList) {
        this.id = id;
        this.orderNo = orderNo;
        this.amount = amount;
        this.payer = payer;
        this.payerMobile = payerMobile;
        this.orderState = orderState;
        this.payState = payState;
        this.orderType = orderType;
        this.updateTime = updateTime;
        this.operaId = operaId;
        this.signature = signature;
        this.paymentTime = paymentTime;
        this.paymentCahnnel = paymentCahnnel;
        this.orderCycleDetailList = orderCycleDetailList;
        this.orderProductDetailList = orderProductDetailList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
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
        this.operaId = operaId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public int getPaymentCahnnel() {
        return paymentCahnnel;
    }

    public void setPaymentCahnnel(int paymentCahnnel) {
        this.paymentCahnnel = paymentCahnnel;
    }

    public List<AcOrderCycleDetailDto> getOrderCycleDetailList() {
        return orderCycleDetailList;
    }

    public void setOrderCycleDetailList(List<AcOrderCycleDetailDto> orderCycleDetailList) {
        this.orderCycleDetailList = orderCycleDetailList;
    }

    public List<AcOrderProductDetailDto> getOrderProductDetailList() {
        return orderProductDetailList;
    }

    public void setOrderProductDetailList(List<AcOrderProductDetailDto> orderProductDetailList) {
        this.orderProductDetailList = orderProductDetailList;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public int getIsRcorded() {
        return isRcorded;
    }

    public void setIsRcorded(int isRcorded) {
        this.isRcorded = isRcorded;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "AcOrderDto{" +
                "id='" + id + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", amount=" + amount +
                ", payer='" + payer + '\'' +
                ", payerMobile='" + payerMobile + '\'' +
                ", orderState=" + orderState +
                ", payState=" + payState +
                ", orderType=" + orderType +
                ", updateTime=" + updateTime +
                ", operaId='" + operaId + '\'' +
                ", signature='" + signature + '\'' +
                ", paymentTime=" + paymentTime +
                ", paymentCahnnel=" + paymentCahnnel +
                ", orderCycleDetailList=" + orderCycleDetailList +
                ", orderProductDetailList=" + orderProductDetailList +
                '}';
    }

}
