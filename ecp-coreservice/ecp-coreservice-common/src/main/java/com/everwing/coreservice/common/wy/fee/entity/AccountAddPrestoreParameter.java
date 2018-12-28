package com.everwing.coreservice.common.wy.fee.entity;

public class AccountAddPrestoreParameter extends ProjectPrestoreDetail {
    private static final long serialVersionUID = -2500606701555417515L;
    private  Integer payType;//付款方式
    private String buildingFullName;//房屋地址
    private int countId;//总数
    private double accountMoney;//金额总数
    private String houseCode;

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getBuildingFullName() {
        return buildingFullName;
    }

    public void setBuildingFullName(String buildingFullName) {
        this.buildingFullName = buildingFullName;
    }

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
