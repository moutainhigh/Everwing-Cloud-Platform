package com.everwing.coreservice.common.wy.entity.configuration.assetaccount;

import java.io.Serializable;

/**
 * 封装缴费类
 */
public class AccountPaymentEntiy implements Serializable {
    private static final long serialVersionUID = -3252334677120083138L;
    private  String qu;
    private  String Type;
    private  String dong;
    private  String dangYuan;
    private  String ceng;
    private  String houseNumber;
    private  String houseCode;
    private  String address;
    private  Double wyMonth;
    private  String name;
    private  String rephone;
    private  String phone;
    private  String blance;
    private String blanceAccount;
    private  Double wy;
    private  Double wylate;
    private  Double wyBillingFee;
    private  Double wyPayFee;
    private  Double wyRefundFee;
    private  Double  wyJMFee;
    private  Double wyChuHuaMoney;
    private Double wyLateMoney;
    private  Double btChuHuaMoney;
    private  Double waterChuHuaMoney;
    private  Double eleChuHuaMoney;
    private  Double commChuHuaMoney;
    private  Double water;
    private  Double waterlate;
    private  Double waterBillingFee;
    private  Double waterPayFee;
    private  Double waterRefundFee;
    private  Double  waterJMfFee;

    //已支付物业违约金
    private  Double wyPayOwnMoney;
    //已支付本体违约金
    private  Double btPayOwnMoney;
    //已支付水费违约金流水
    private  Double waterPayOwnMoney;
    //已支付电费违约金流水
    private  Double elePayOwnMoney;

    private  Double bt;
    private  Double btlate;
    private  Double btBillingFee;
    private  Double btPayFee;
    private  Double btRefundFee;
    private  Double  btJMFee;

    private  Double ele;
    private  Double elelate;
    private  Double eleBillingFee;
    private  Double elePayFee;
    private  Double eleRefundFee;
    private  Double  eleJMFee;
    private  Double common;
    private  Double commonRefundFee;
    private  Double totalAmount;

    private Double wyBenJin;
    private  Double btBenJin;
    private  Double waterBenJin;
    private  Double eleBenJin;


    public Double getWyPayOwnMoney() {
        return wyPayOwnMoney;
    }

    public void setWyPayOwnMoney(Double wyPayOwnMoney) {
        this.wyPayOwnMoney = wyPayOwnMoney;
    }

    public Double getBtPayOwnMoney() {
        return btPayOwnMoney;
    }

    public void setBtPayOwnMoney(Double btPayOwnMoney) {
        this.btPayOwnMoney = btPayOwnMoney;
    }

    public Double getWaterPayOwnMoney() {
        return waterPayOwnMoney;
    }

    public void setWaterPayOwnMoney(Double waterPayOwnMoney) {
        this.waterPayOwnMoney = waterPayOwnMoney;
    }

    public Double getElePayOwnMoney() {
        return elePayOwnMoney;
    }

    public void setElePayOwnMoney(Double elePayOwnMoney) {
        this.elePayOwnMoney = elePayOwnMoney;
    }

    public Double getWyChuHuaMoney() {
        return wyChuHuaMoney;
    }

    public void setWyChuHuaMoney(Double wyChuHuaMoney) {
        this.wyChuHuaMoney = wyChuHuaMoney;
    }

    public Double getWyLateMoney() {
        return wyLateMoney;
    }

    public void setWyLateMoney(Double wyLateMoney) {
        this.wyLateMoney = wyLateMoney;
    }



    public Double getBtChuHuaMoney() {
        return btChuHuaMoney;
    }

    public void setBtChuHuaMoney(Double btChuHuaMoney) {
        this.btChuHuaMoney = btChuHuaMoney;
    }

    public Double getWaterChuHuaMoney() {
        return waterChuHuaMoney;
    }

    public void setWaterChuHuaMoney(Double waterChuHuaMoney) {
        this.waterChuHuaMoney = waterChuHuaMoney;
    }

    public Double getEleChuHuaMoney() {
        return eleChuHuaMoney;
    }

    public void setEleChuHuaMoney(Double eleChuHuaMoney) {
        this.eleChuHuaMoney = eleChuHuaMoney;
    }

    public Double getCommChuHuaMoney() {
        return commChuHuaMoney;
    }

    public void setCommChuHuaMoney(Double commChuHuaMoney) {
        this.commChuHuaMoney = commChuHuaMoney;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getQu() {
        return qu;
    }

    public void setQu(String qu) {
        this.qu = qu;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDong() {
        return dong;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public String getDangYuan() {
        return dangYuan;
    }

    public void setDangYuan(String dangYuan) {
        this.dangYuan = dangYuan;
    }

    public String getCeng() {
        return ceng;
    }

    public void setCeng(String ceng) {
        this.ceng = ceng;
    }

    public Double getWyBenJin() {
        return wyBenJin;
    }

    public void setWyBenJin(Double wyBenJin) {
        this.wyBenJin = wyBenJin;
    }

    public Double getBtBenJin() {
        return btBenJin;
    }

    public void setBtBenJin(Double btBenJin) {
        this.btBenJin = btBenJin;
    }

    public Double getWaterBenJin() {
        return waterBenJin;
    }

    public void setWaterBenJin(Double waterBenJin) {
        this.waterBenJin = waterBenJin;
    }

    public Double getEleBenJin() {
        return eleBenJin;
    }

    public void setEleBenJin(Double eleBenJin) {
        this.eleBenJin = eleBenJin;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getCommonRefundFee() {
        return commonRefundFee;
    }

    public void setCommonRefundFee(Double commonRefundFee) {
        this.commonRefundFee = commonRefundFee;
    }

    public Double getCommon() {
        return common;
    }

    public void setCommon(Double common) {
        this.common = common;
    }

    public Double getWyJMFee() {
        return wyJMFee;
    }

    public void setWyJMFee(Double wyJMFee) {
        this.wyJMFee = wyJMFee;
    }

    public Double getWaterJMfFee() {
        return waterJMfFee;
    }

    public void setWaterJMfFee(Double waterJMfFee) {
        this.waterJMfFee = waterJMfFee;
    }

    public Double getBtJMFee() {
        return btJMFee;
    }

    public void setBtJMFee(Double btJMFee) {
        this.btJMFee = btJMFee;
    }

    public Double getEleJMFee() {
        return eleJMFee;
    }

    public void setEleJMFee(Double eleJMFee) {
        this.eleJMFee = eleJMFee;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getWyMonth() {
        return wyMonth;
    }

    public void setWyMonth(Double wyMonth) {
        this.wyMonth = wyMonth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRephone() {
        return rephone;
    }

    public void setRephone(String rephone) {
        this.rephone = rephone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBlance() {
        return blance;
    }

    public void setBlance(String blance) {
        this.blance = blance;
    }

    public String getBlanceAccount() {
        return blanceAccount;
    }

    public void setBlanceAccount(String blanceAccount) {
        this.blanceAccount = blanceAccount;
    }

    public Double getWy() {
        return wy;
    }

    public void setWy(Double wy) {
        this.wy = wy;
    }

    public Double getWylate() {
        return wylate;
    }

    public void setWylate(Double wylate) {
        this.wylate = wylate;
    }

    public Double getWyBillingFee() {
        return wyBillingFee;
    }

    public void setWyBillingFee(Double wyBillingFee) {
        this.wyBillingFee = wyBillingFee;
    }

    public Double getWyPayFee() {
        return wyPayFee;
    }

    public void setWyPayFee(Double wyPayFee) {
        this.wyPayFee = wyPayFee;
    }

    public Double getWyRefundFee() {
        return wyRefundFee;
    }

    public void setWyRefundFee(Double wyRefundFee) {
        this.wyRefundFee = wyRefundFee;
    }



    public Double getWater() {
        return water;
    }

    public void setWater(Double water) {
        this.water = water;
    }

    public Double getWaterlate() {
        return waterlate;
    }

    public void setWaterlate(Double waterlate) {
        this.waterlate = waterlate;
    }

    public Double getWaterBillingFee() {
        return waterBillingFee;
    }

    public void setWaterBillingFee(Double waterBillingFee) {
        this.waterBillingFee = waterBillingFee;
    }

    public Double getWaterPayFee() {
        return waterPayFee;
    }

    public void setWaterPayFee(Double waterPayFee) {
        this.waterPayFee = waterPayFee;
    }

    public Double getWaterRefundFee() {
        return waterRefundFee;
    }

    public void setWaterRefundFee(Double waterRefundFee) {
        this.waterRefundFee = waterRefundFee;
    }


    public Double getBt() {
        return bt;
    }

    public void setBt(Double bt) {
        this.bt = bt;
    }

    public Double getBtlate() {
        return btlate;
    }

    public void setBtlate(Double btlate) {
        this.btlate = btlate;
    }

    public Double getBtBillingFee() {
        return btBillingFee;
    }

    public void setBtBillingFee(Double btBillingFee) {
        this.btBillingFee = btBillingFee;
    }

    public Double getBtPayFee() {
        return btPayFee;
    }

    public void setBtPayFee(Double btPayFee) {
        this.btPayFee = btPayFee;
    }

    public Double getBtRefundFee() {
        return btRefundFee;
    }

    public void setBtRefundFee(Double btRefundFee) {
        this.btRefundFee = btRefundFee;
    }



    public Double getEle() {
        return ele;
    }

    public void setEle(Double ele) {
        this.ele = ele;
    }

    public Double getElelate() {
        return elelate;
    }

    public void setElelate(Double elelate) {
        this.elelate = elelate;
    }

    public Double getEleBillingFee() {
        return eleBillingFee;
    }

    public void setEleBillingFee(Double eleBillingFee) {
        this.eleBillingFee = eleBillingFee;
    }

    public Double getElePayFee() {
        return elePayFee;
    }

    public void setElePayFee(Double elePayFee) {
        this.elePayFee = elePayFee;
    }

    public Double getEleRefundFee() {
        return eleRefundFee;
    }

    public void setEleRefundFee(Double eleRefundFee) {
        this.eleRefundFee = eleRefundFee;
    }



}
