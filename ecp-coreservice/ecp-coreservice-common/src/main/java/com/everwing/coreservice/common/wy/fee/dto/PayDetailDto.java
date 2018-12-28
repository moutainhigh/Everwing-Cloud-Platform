package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.wy.fee.constant.ChargingType;

import java.io.Serializable;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-07-29 17:44
 **/

public class PayDetailDto implements Serializable {

    private String costName;

    private double money;

    private double lateFee;

    private int accountType ;

    public PayDetailDto() {
    }

    public PayDetailDto(String costName, double money, double lateFee, int accountType) {
        this.costName = costName;
        this.money = money;
        this.lateFee = lateFee;
        this.accountType = accountType;
    }

    public String getCostName() {
        return ChargingType.getChargingTypeByCode(accountType).getDescription();
    }

    public void setCostName(String costName) {
        this.costName = costName;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "PayDetailDto{" +
                "costName='" + costName + '\'' +
                ", money=" + money +
                ", lateFee=" + lateFee +
                ", accountType=" + accountType +
                '}';
    }
}
