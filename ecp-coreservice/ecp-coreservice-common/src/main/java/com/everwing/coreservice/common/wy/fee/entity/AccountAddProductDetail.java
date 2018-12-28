package com.everwing.coreservice.common.wy.fee.entity;

public class AccountAddProductDetail extends ProjectProductDetail {

    private static final long serialVersionUID = 3768523892515253739L;
    private  String payType;//付款方式 1.cash 现金支付 2.charge 刷卡 3.weixinpay 微信支付
    private String buildingFullName;//房屋地址
    private int countId;//总数
    private double accountMoney;//金额总数
    private  String productName;//产品名

    private double afterTaxAmountTotal;//税后总额
    private  double taxAmountTotal;//税金总额
    private String houseCode;

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
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
