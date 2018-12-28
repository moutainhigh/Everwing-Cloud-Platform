package com.everwing.coreservice.common.wy.entity.personbuilding;

import java.io.Serializable;

public class PersonBuildingDto implements Serializable {
    private static final long serialVersionUID = -6757526379194307398L;
    private  String phone;
    private  String name;
    private  String buildingCode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }
}
