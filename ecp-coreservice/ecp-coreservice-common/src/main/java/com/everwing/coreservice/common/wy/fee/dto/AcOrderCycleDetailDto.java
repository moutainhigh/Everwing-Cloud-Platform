package com.everwing.coreservice.common.wy.fee.dto;

import java.io.Serializable;

/**
 * @Author: zgrf
 * @Description: 周期性订单明细
 * @Date: Create in 2018-08-13 15:36
 **/

public class AcOrderCycleDetailDto implements Serializable{

    private String id;

    private String orderId;

    private int businessType;

    private int depositType;

    private int accountType;

    private double detailAmount;

    private String houseCodeNew;

    private double lateAmount;

    public AcOrderCycleDetailDto() {
    }

    public AcOrderCycleDetailDto(String id, String orderId, int businessType, int depositType, int accountType, double detailAmount, String houseCodeNew) {
        this.id = id;
        this.orderId = orderId;
        this.businessType = businessType;
        this.depositType = depositType;
        this.accountType = accountType;
        this.detailAmount = detailAmount;
        this.houseCodeNew = houseCodeNew;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public int getDepositType() {
        return depositType;
    }

    public void setDepositType(int depositType) {
        this.depositType = depositType;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public double getDetailAmount() {
        return detailAmount;
    }

    public void setDetailAmount(double detailAmount) {
        this.detailAmount = detailAmount;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public double getLateAmount() {
        return lateAmount;
    }

    public void setLateAmount(double lateAmount) {
        this.lateAmount = lateAmount;
    }

    @Override
    public String toString() {
        return "AcOrderCycleDetailDto{" +
                "id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", businessType=" + businessType +
                ", depositType=" + depositType +
                ", accountType=" + accountType +
                ", detailAmount=" + detailAmount +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                '}';
    }
}
