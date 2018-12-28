package com.everwing.coreservice.common.wy.entity.configuration.assetaccount;

import java.io.Serializable;

public class TBsAssetAccountInitialization implements Serializable {

    private static final long serialVersionUID = 2182226461916128483L;
    private  String buildingCode;
    private  double commonInit;
    // 初始化通用账户
    private double  wyInit;
    // -- 初始化物业账户预存
    private double  wyArreasInit;
    // -- 初始化的欠费
    private double  lateFeeInit;
    //-- 初始化违约金
    private double  btInit;
    //-- 初始化本体账户预存
    private double  btArreasInit;
    // -- 初始化的欠费
    private double  waterInit;
    // -- 初始化的欠费
    private double  waterArreasInit;
    // -- 初始化的欠费
    private double   electInit;
    // -- 初始化的欠费
    private double   electArreasInit;
    // -- 初始化的欠费

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public double getCommonInit() {
        return commonInit;
    }

    public void setCommonInit(double commonInit) {
        this.commonInit = commonInit;
    }

    public double getWyInit() {
        return wyInit;
    }

    public void setWyInit(double wyInit) {
        this.wyInit = wyInit;
    }

    public double getWyArreasInit() {
        return wyArreasInit;
    }

    public void setWyArreasInit(double wyArreasInit) {
        this.wyArreasInit = wyArreasInit;
    }

    public double getLateFeeInit() {
        return lateFeeInit;
    }

    public void setLateFeeInit(double lateFeeInit) {
        this.lateFeeInit = lateFeeInit;
    }

    public double getBtInit() {
        return btInit;
    }

    public void setBtInit(double btInit) {
        this.btInit = btInit;
    }

    public double getBtArreasInit() {
        return btArreasInit;
    }

    public void setBtArreasInit(double btArreasInit) {
        this.btArreasInit = btArreasInit;
    }

    public double getWaterInit() {
        return waterInit;
    }

    public void setWaterInit(double waterInit) {
        this.waterInit = waterInit;
    }

    public double getWaterArreasInit() {
        return waterArreasInit;
    }

    public void setWaterArreasInit(double waterArreasInit) {
        this.waterArreasInit = waterArreasInit;
    }

    public double getElectInit() {
        return electInit;
    }

    public void setElectInit(double electInit) {
        this.electInit = electInit;
    }

    public double getElectArreasInit() {
        return electArreasInit;
    }

    public void setElectArreasInit(double electArreasInit) {
        this.electArreasInit = electArreasInit;
    }
}
