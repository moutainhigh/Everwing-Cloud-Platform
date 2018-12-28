package com.everwing.coreservice.common.solr.entity.customer;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * @author shiny
 **/
public class Asset implements Serializable {

    @Field("id")
    private String id;

    @Field("building_code")
    private String buildingCode;

    @Field("building_full_name")
    private String buildingFullName;

    @Field("house_code_new")
    private String houdeCodeNew;

    @Field("address")
    private String address;

    @Field("is_manage")
    private String isManage;

    @Field("is_water")
    private String isWater;

    @Field("is_electricity")
    private String isElectricity;

    @Field("data_source")
    private String dataSource;

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getBuildingFullName() {
        return buildingFullName;
    }

    public void setBuildingFullName(String buildingFullName) {
        this.buildingFullName = buildingFullName;
    }

    public String getHoudeCodeNew() {
        return houdeCodeNew;
    }

    public void setHoudeCodeNew(String houdeCodeNew) {
        this.houdeCodeNew = houdeCodeNew;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsManage() {
        return isManage;
    }

    public void setIsManage(String isManage) {
        this.isManage = isManage;
    }

    public String getIsWater() {
        return isWater;
    }

    public void setIsWater(String isWater) {
        this.isWater = isWater;
    }

    public String getIsElectricity() {
        return isElectricity;
    }

    public void setIsElectricity(String isElectricity) {
        this.isElectricity = isElectricity;
    }
}
