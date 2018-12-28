package com.everwing.coreservice.common.wy.entity.property.vehicle;

import java.util.Date;

public class TVehicle implements java.io.Serializable{

    private static final long serialVersionUID = 5582175900257151992L;
    private String id;

    private String projectId;

    private String houseCodeNew;

    private String vehicleNumber;

    private String vehicleLicense;

    private Date attainedVehicleLicenseDate;

    private String customerId;

    private String holderType;

    private String brand;

    private String vehicleType;

    private String engineNo;

    private Date vehicleRegisterDate;

    private String vehicleRegisterPerson;

    private String vehicleColor;

    private Integer canCarryPassengersNumber;

    private String modelNumber;

    private String createrId;

    private String createrName;

    private String modifyId;

    private String modifyName;

    private Date createTime;

    private Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleLicense() {
        return vehicleLicense;
    }

    public void setVehicleLicense(String vehicleLicense) {
        this.vehicleLicense = vehicleLicense;
    }

    public Date getAttainedVehicleLicenseDate() {
        return attainedVehicleLicenseDate;
    }

    public void setAttainedVehicleLicenseDate(Date attainedVehicleLicenseDate) {
        this.attainedVehicleLicenseDate = attainedVehicleLicenseDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getHolderType() {
        return holderType;
    }

    public void setHolderType(String holderType) {
        this.holderType = holderType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public Date getVehicleRegisterDate() {
        return vehicleRegisterDate;
    }

    public void setVehicleRegisterDate(Date vehicleRegisterDate) {
        this.vehicleRegisterDate = vehicleRegisterDate;
    }

    public String getVehicleRegisterPerson() {
        return vehicleRegisterPerson;
    }

    public void setVehicleRegisterPerson(String vehicleRegisterPerson) {
        this.vehicleRegisterPerson = vehicleRegisterPerson;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public Integer getCanCarryPassengersNumber() {
        return canCarryPassengersNumber;
    }

    public void setCanCarryPassengersNumber(Integer canCarryPassengersNumber) {
        this.canCarryPassengersNumber = canCarryPassengersNumber;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public String getModifyName() {
        return modifyName;
    }

    public void setModifyName(String modifyName) {
        this.modifyName = modifyName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}