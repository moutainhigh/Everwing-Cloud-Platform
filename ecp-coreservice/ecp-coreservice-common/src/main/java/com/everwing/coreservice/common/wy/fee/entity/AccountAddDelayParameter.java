package com.everwing.coreservice.common.wy.fee.entity;

public class AccountAddDelayParameter extends ProjectDelayDetail {
    private static final long serialVersionUID = -273833922332993313L;
    private  Integer payType;//付款方式
    private String buildingFullName;//房屋地址
    private Integer countId;//总数
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

    public Integer getCountId() {
        return countId;
    }

    public void setCountId(Integer countId) {
        this.countId = countId;
    }

    public double getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(double accountMoney) {
        this.accountMoney = accountMoney;
    }


}
