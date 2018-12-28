package com.everwing.coreservice.common.wy.entity.account.pay;

import java.io.Serializable;

public class TBsPayInfoImport implements Serializable {
    private static final long serialVersionUID = -3029971613245797974L;
    //房号
    private String houseCode;
    //地址
    private String address;
    //物业账户
    private Double wy;
    //本体账户
    private Double bt;
    //水费账户
    private Double water;
    //电费账户
    private Double elect;
    //账户金额金额
    private Double total;
    //物业支付金额
    private Double payWy;
    //本体支付金额
    private Double payBt;
    //水费支付金额
    private Double payWater;
    //电费支付金额
    private Double payele;
    //通用账户充值
    private Double payAmont;

    private String payName;

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

    public Double getWy() {
        return wy;
    }

    public void setWy(Double wy) {
        this.wy = wy;
    }

    public Double getBt() {
        return bt;
    }

    public void setBt(Double bt) {
        this.bt = bt;
    }

    public Double getWater() {
        return water;
    }

    public void setWater(Double water) {
        this.water = water;
    }

    public Double getElect() {
        return elect;
    }

    public void setElect(Double elect) {
        this.elect = elect;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getPayWy() {
        return payWy;
    }

    public void setPayWy(Double payWy) {
        this.payWy = payWy;
    }

    public Double getPayBt() {
        return payBt;
    }

    public void setPayBt(Double payBt) {
        this.payBt = payBt;
    }

    public Double getPayWater() {
        return payWater;
    }

    public void setPayWater(Double payWater) {
        this.payWater = payWater;
    }

    public Double getPayele() {
        return payele;
    }

    public void setPayele(Double payele) {
        this.payele = payele;
    }
}
