package com.everwing.coreservice.common.solr.entity.customer;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * @author shiny
 **/
public class EnterpriseCust implements Serializable {

    @Field("id")
    private String enterpriseId;

    @Field("enterprise_name")
    private String enterpriseName;

    @Field("address")
    private String address;

    @Field("representative")
    private String representative;

    @Field("office_phone")
    private String officePhone;

    @Field("trading_number")
    private String tradingNumber;

    @Field("data_source")
    private String dataSource;

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getTradingNumber() {
        return tradingNumber;
    }

    public void setTradingNumber(String tradingNumber) {
        this.tradingNumber = tradingNumber;
    }
}
