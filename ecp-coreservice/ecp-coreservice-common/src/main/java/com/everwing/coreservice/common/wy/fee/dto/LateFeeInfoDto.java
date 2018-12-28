package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.Page;

import java.io.Serializable;

/**
 * @author DELL shiny
 * @create 2018/6/26
 */
public class LateFeeInfoDto implements Serializable{

    private String buildingCode;

    private String yearMonth;

    private String chargeType;

    private String payType;

    private String accountType;

    private String creater;

    private Page page;

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }


    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
