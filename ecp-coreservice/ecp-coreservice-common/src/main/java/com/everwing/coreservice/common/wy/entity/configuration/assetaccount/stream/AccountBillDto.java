package com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream;


/***
 * 前台导出账户具体的收费计费数据
 */


import java.io.Serializable;

public class AccountBillDto implements Serializable {

    private static final long serialVersionUID = 1693519392539070254L;
    // 月份
    private  String time;
    //物业管理费
    private  Double wy;
    // 物业交费
    private  Double wyPay;
    // 物业退款
    private  Double wyBack;
    //本体
    private  Double bt;
    //本体交费
    private  Double btPay;
    //本体退款
    private  Double btBack;
    //水费
    private  Double water;
    //水费交费
    private  Double waterPay;
    //水费用量
    private  Double waterUserCount;
    //水费退款
    private  Double waterBack;
    //电费
    private  Double ele;
    //电费交费
    private  Double elePay;
    //电费用量
    private  Double eleUserCount;
    //电费退款
    private  Double eleBack;
    // 违约金
    private  Double wyOwn;
    private  Double btOwn;
    private  Double waterOwn;
    private  Double eleOwn;

    private  Double comm;
    private  Double commBack;

    public Double getWyOwn() {
        return wyOwn;
    }

    public void setWyOwn(Double wyOwn) {
        this.wyOwn = wyOwn;
    }

    public Double getBtOwn() {
        return btOwn;
    }

    public void setBtOwn(Double btOwn) {
        this.btOwn = btOwn;
    }

    public Double getWaterOwn() {
        return waterOwn;
    }

    public void setWaterOwn(Double waterOwn) {
        this.waterOwn = waterOwn;
    }

    public Double getEleOwn() {
        return eleOwn;
    }

    public void setEleOwn(Double eleOwn) {
        this.eleOwn = eleOwn;
    }

    public Double getComm() {
        return comm;
    }

    public void setComm(Double comm) {
        this.comm = comm;
    }

    public Double getCommBack() {
        return commBack;
    }

    public void setCommBack(Double commBack) {
        this.commBack = commBack;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getWy() {
        return wy;
    }

    public void setWy(Double wy) {
        this.wy = wy;
    }

    public Double getWyPay() {
        return wyPay;
    }

    public void setWyPay(Double wyPay) {
        this.wyPay = wyPay;
    }

    public Double getWyBack() {
        return wyBack;
    }

    public void setWyBack(Double wyBack) {
        this.wyBack = wyBack;
    }

    public Double getBt() {
        return bt;
    }

    public void setBt(Double bt) {
        this.bt = bt;
    }

    public Double getBtPay() {
        return btPay;
    }

    public void setBtPay(Double btPay) {
        this.btPay = btPay;
    }

    public Double getBtBack() {
        return btBack;
    }

    public void setBtBack(Double btBack) {
        this.btBack = btBack;
    }

    public Double getWater() {
        return water;
    }

    public void setWater(Double water) {
        this.water = water;
    }

    public Double getWaterPay() {
        return waterPay;
    }

    public void setWaterPay(Double waterPay) {
        this.waterPay = waterPay;
    }

    public Double getWaterUserCount() {
        return waterUserCount;
    }

    public void setWaterUserCount(Double waterUserCount) {
        this.waterUserCount = waterUserCount;
    }

    public Double getWaterBack() {
        return waterBack;
    }

    public void setWaterBack(Double waterBack) {
        this.waterBack = waterBack;
    }

    public Double getEle() {
        return ele;
    }

    public void setEle(Double ele) {
        this.ele = ele;
    }

    public Double getElePay() {
        return elePay;
    }

    public void setElePay(Double elePay) {
        this.elePay = elePay;
    }

    public Double getEleUserCount() {
        return eleUserCount;
    }

    public void setEleUserCount(Double eleUserCount) {
        this.eleUserCount = eleUserCount;
    }

    public Double getEleBack() {
        return eleBack;
    }

    public void setEleBack(Double eleBack) {
        this.eleBack = eleBack;
    }
}
