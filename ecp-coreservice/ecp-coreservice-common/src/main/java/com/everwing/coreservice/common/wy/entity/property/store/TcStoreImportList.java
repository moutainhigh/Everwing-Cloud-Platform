package com.everwing.coreservice.common.wy.entity.property.store;/**
 * Created by wust on 2017/5/23.
 */

import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingImportList;

/**
 *
 * Function:
 * Reason:
 * Date:2017/5/23
 * @author wusongti@lii.com.cn
 */
public class TcStoreImportList extends TcBuildingImportList{
    private static final long serialVersionUID = 5059542835876354542L;
    /** 商铺名称 **/
    private String storeName;
    /** 商铺类型 **/
    private String storeType;
//    /** 产权类型 **/
//    private Object propertyType;


    // 是否成功，必须要加该字段
    private Boolean successFlag;

    // 错误原因，必须要加该字段
    private String errorMessage;


    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

//    public Object getPropertyType() {
//        return propertyType;
//    }
//
//    public void setPropertyType(Object propertyType) {
//        this.propertyType = propertyType;
//    }

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
        return super.toString() + "\nTcStoreImportList{" +
                "storeName='" + storeName + '\'' +
                ", storeType='" + storeType + '\'' +
//                ", propertyType=" + propertyType +
                ", successFlag=" + successFlag +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
