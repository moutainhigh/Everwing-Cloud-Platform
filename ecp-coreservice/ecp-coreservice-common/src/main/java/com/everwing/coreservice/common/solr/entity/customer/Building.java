package com.everwing.coreservice.common.solr.entity.customer;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * @author shiny
 **/
public class Building implements Serializable {

    @Field("id")
    private String id;

    @Field("building_code")
    private String buildingCode;

    @Field("building_full_name")
    private String buildingFullName;

    @Field("house_code_new")
    private String houseCodeNew;

    @Field("building_area")
    private String buildingArea;

    @Field("building_type")
    private String buildingType;

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

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public String getBuildingArea() {
        return buildingArea;
    }

    public void setBuildingArea(String buildingArea) {
        this.buildingArea = buildingArea;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }
}
