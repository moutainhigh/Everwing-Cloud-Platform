package com.everwing.coreservice.common.wy.entity.property.store;/**
 * Created by wust on 2017/5/23.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/5/23
 * @author wusongti@lii.com.cn
 */
public class TcStoreList extends TcStore{
    private String storeFullName;
    private String marketStateName;
    private String storeTypeName;

    public String getStoreFullName() {
        return storeFullName;
    }

    public void setStoreFullName(String storeFullName) {
        this.storeFullName = storeFullName;
    }

    public String getMarketStateName() {
        return marketStateName;
    }

    public void setMarketStateName(String marketStateName) {
        this.marketStateName = marketStateName;
    }

    public String getStoreTypeName() {
        return storeTypeName;
    }

    public void setStoreTypeName(String storeTypeName) {
        this.storeTypeName = storeTypeName;
    }
}
