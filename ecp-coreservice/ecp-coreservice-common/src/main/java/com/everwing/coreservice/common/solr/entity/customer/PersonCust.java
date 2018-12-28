package com.everwing.coreservice.common.solr.entity.customer;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * @author shiny
 **/
public class PersonCust implements Serializable {

    @Field("id")
    private String custId;

    @Field("cust_code")
    private String custCode;

    @Field("name")
    private String name;

    @Field("card_type")
    private String cardType;

    @Field("card_num")
    private String cardNum;

    @Field("register_phone")
    private String registerPhone;

    @Field("data_source")
    private String dataSource;

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getRegisterPhone() {
        return registerPhone;
    }

    public void setRegisterPhone(String registerPhone) {
        this.registerPhone = registerPhone;
    }
}
