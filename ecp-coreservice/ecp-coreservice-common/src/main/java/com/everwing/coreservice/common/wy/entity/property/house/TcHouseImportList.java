package com.everwing.coreservice.common.wy.entity.property.house;

import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingImportList;

/**
 * 导入excel封装数据的实体
 */
public class TcHouseImportList extends TcBuildingImportList {

    private static final long serialVersionUID = -6689763820117725518L;
    // 房屋属性
    private String houseProperty;

    // 户型
    private String houseType;


    // 是否成功，必须要加该字段
    private Boolean successFlag;

    // 错误原因，必须要加该字段
    private String errorMessage;


    public String getHouseProperty() {
        return houseProperty;
    }

    public void setHouseProperty(String houseProperty) {
        this.houseProperty = houseProperty;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public Boolean getSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(Boolean successFlag) {
        this.successFlag = successFlag;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return super.toString() + "\nTcHouseImportList{" +
                "houseProperty='" + houseProperty + '\'' +
                ", houseType='" + houseType + '\'' +
                ", successFlag=" + successFlag +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
