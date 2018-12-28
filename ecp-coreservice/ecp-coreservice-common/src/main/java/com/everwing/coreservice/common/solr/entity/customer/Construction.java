package com.everwing.coreservice.common.solr.entity.customer;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shiny
 **/
public class Construction implements Serializable {

    @Field("id")
    private String id;

    @Field("house_code_new")
    private String houseCodeNew;

    @Field("construction_addr")
    private String constructionAddr;

    @Field("start_date")
    private Date startDate;

    @Field("engineering_name")
    private String engineeringName;

    @Field("engineering_cycle")
    private String engineeringCycle;

    @Field("completion_date")
    private Date completionDate;

    @Field("electricity_use")
    private String electricityUse;

    @Field("water_use")
    private String waterUse;

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

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public String getConstructionAddr() {
        return constructionAddr;
    }

    public void setConstructionAddr(String constructionAddr) {
        this.constructionAddr = constructionAddr;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getEngineeringName() {
        return engineeringName;
    }

    public void setEngineeringName(String engineeringName) {
        this.engineeringName = engineeringName;
    }

    public String getEngineeringCycle() {
        return engineeringCycle;
    }

    public void setEngineeringCycle(String engineeringCycle) {
        this.engineeringCycle = engineeringCycle;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getElectricityUse() {
        return electricityUse;
    }

    public void setElectricityUse(String electricityUse) {
        this.electricityUse = electricityUse;
    }

    public String getWaterUse() {
        return waterUse;
    }

    public void setWaterUse(String waterUse) {
        this.waterUse = waterUse;
    }
}
