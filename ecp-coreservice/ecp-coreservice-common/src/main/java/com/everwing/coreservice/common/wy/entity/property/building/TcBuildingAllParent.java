package com.everwing.coreservice.common.wy.entity.property.building;

import java.io.Serializable;

/**
 * 资产全部的全部的父id
 */
public class TcBuildingAllParent implements Serializable {


    private static final long serialVersionUID = -8805672480003392964L;
    private  String qu;
    private  String Type;
    private  String dong;
    private  String dangYuan;
    private  String ceng;
    private  String houseNumber;
    private  String address;
    private  String buildingCode;
    private  String houseCode;
    private  Double wyPrice;
    private  Double buildingArea;

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public Double getWyPrice() {
        return wyPrice;
    }

    public void setWyPrice(Double wyPrice) {
        this.wyPrice = wyPrice;
    }

    public Double getBuildingArea() {
        return buildingArea;
    }

    public void setBuildingArea(Double buildingArea) {
        this.buildingArea = buildingArea;
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
}
