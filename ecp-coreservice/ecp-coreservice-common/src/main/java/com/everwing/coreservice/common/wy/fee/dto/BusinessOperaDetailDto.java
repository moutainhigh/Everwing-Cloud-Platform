package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.Page;

import java.io.Serializable;

/**
 * @author shiny
 **/
public class BusinessOperaDetailDto implements Serializable {

    private String buildingCode;

    private String yearMonth;

    private String businessType;

    private String clientType;

    private Page page;

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
