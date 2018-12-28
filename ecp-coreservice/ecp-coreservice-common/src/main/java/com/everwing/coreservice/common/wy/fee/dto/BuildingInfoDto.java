package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.Page;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 新账户页面dto
 *
 * @author DELL shiny
 * @create 2018/6/20
 */
public class BuildingInfoDto implements Serializable{

    private String buildingCode;

    private String id;

    private String houseCode;

    private String address;

    private String wy;

    private String bt;

    private String water;

    private String elect;

    private BigDecimal common;

    private BigDecimal total;

    private  String custId;

    private Page page;

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getWy() {
        return wy;
    }

    public void setWy(String wy) {
        this.wy = wy;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getElect() {
        return elect;
    }

    public void setElect(String elect) {
        this.elect = elect;
    }

    public BigDecimal getCommon() {
        return common;
    }

    public void setCommon(BigDecimal common) {
        this.common = common;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getCustId() { return custId; }

    public void setCustId(String custId) { this.custId = custId; }
}
