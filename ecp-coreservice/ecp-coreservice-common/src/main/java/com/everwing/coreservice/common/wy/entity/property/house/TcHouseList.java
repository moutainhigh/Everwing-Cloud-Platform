package com.everwing.coreservice.common.wy.entity.property.house;/**
 * Created by wust on 2017/4/21.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/4/21
 * @author wusongti@lii.com.cn
 */
public class TcHouseList extends TcHouse{
    // 房屋全名称
    private String houseFullName;

    // 销售状态名称
    private String marketStateName;

    // 房屋属性名称
    private String housePropertyName;

    // 房屋类型名称
    private String houseTypeName;


    public String getHouseFullName() {
        return houseFullName;
    }

    public void setHouseFullName(String houseFullName) {
        this.houseFullName = houseFullName;
    }

    public String getMarketStateName() {
        return marketStateName;
    }

    public void setMarketStateName(String marketStateName) {
        this.marketStateName = marketStateName;
    }

    public String getHousePropertyName() {
        return housePropertyName;
    }

    public void setHousePropertyName(String housePropertyName) {
        this.housePropertyName = housePropertyName;
    }

    public String getHouseTypeName() {
        return houseTypeName;
    }

    public void setHouseTypeName(String houseTypeName) {
        this.houseTypeName = houseTypeName;
    }
}
