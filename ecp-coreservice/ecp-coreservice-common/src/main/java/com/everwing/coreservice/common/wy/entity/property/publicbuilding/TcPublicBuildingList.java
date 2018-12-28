package com.everwing.coreservice.common.wy.entity.property.publicbuilding;/**
 * Created by wust on 2017/7/25.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/7/25
 * @author wusongti@lii.com.cn
 */
public class TcPublicBuildingList extends TcPublicBuilding{
    private static final long serialVersionUID = 5248466860297345129L;

    private String fullName;
    private String marketStateName;

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMarketStateName() {
        return this.marketStateName;
    }

    public void setMarketStateName(String marketStateName) {
        this.marketStateName = marketStateName;
    }
}
