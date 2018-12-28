package com.everwing.coreservice.common.wy.entity.property.store;/**
 * Created by wust on 2017/5/23.
 */

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:
 * Reason:
 * Date:2017/5/23
 * @author wusongti@lii.com.cn
 */
public class TcStore extends BaseEntity{
    private static final long serialVersionUID = 14305087585062726L;
    //field
    /** 商铺id **/
    private String id;
    /** 建筑code **/
    private String buildingCode;
    /** 商铺号 **/
    private String storeName;
    /** 商铺类型 **/
    private String storeType;

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

    @Override
    public String toString() {
        return "TcStore{" +
                "createrId='" + createrId + '\'' +
                ", id='" + id + '\'' +
                ", buildingCode='" + buildingCode + '\'' +
                ", createrName='" + createrName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", createTime=" + createTime +
                ", storeType='" + storeType + '\'' +
                ", modifyId='" + modifyId + '\'' +
                ", modifyName='" + modifyName + '\'' +
                ", modifyTime=" + modifyTime +
                ", lan='" + lan + '\'' +
                '}';
    }
}
