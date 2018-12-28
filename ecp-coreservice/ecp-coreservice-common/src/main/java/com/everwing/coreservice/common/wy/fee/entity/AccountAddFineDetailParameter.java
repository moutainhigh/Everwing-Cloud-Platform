package com.everwing.coreservice.common.wy.fee.entity;

public class AccountAddFineDetailParameter extends ProjectFineDetail {

    private static final long serialVersionUID = 2484001378847754043L;
    private int countId;//总数
    private double accountMoney;//金额总数



    public int getCountId() {
        return countId;
    }

    public void setCountId(int countId) {
        this.countId = countId;
    }

    public double getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(double accountMoney) {
        this.accountMoney = accountMoney;
    }


}
