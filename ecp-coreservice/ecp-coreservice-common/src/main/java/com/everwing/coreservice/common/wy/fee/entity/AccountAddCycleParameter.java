package com.everwing.coreservice.common.wy.fee.entity;

/**
 * 财务管理模块下周期性计费收入实体类
 */
public class AccountAddCycleParameter extends ProjectCycleDetail {

    private static final long serialVersionUID = -7942291591844912043L;
    private  Integer payType;//付款方式
    private String buildingFullName;//房屋地址
    private int countId;//总数
    private double accountMoney;//金额总数
    private double afterTaxAmountTotal;//税后总额
    private  double taxAmountTotal;//税金总额

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

    public double getAfterTaxAmountTotal() {
        return afterTaxAmountTotal;
    }

    public void setAfterTaxAmountTotal(double afterTaxAmountTotal) {
        this.afterTaxAmountTotal = afterTaxAmountTotal;
    }

    public double getTaxAmountTotal() {
        return taxAmountTotal;
    }

    public void setTaxAmountTotal(double taxAmountTotal) {
        this.taxAmountTotal = taxAmountTotal;
    }




}
