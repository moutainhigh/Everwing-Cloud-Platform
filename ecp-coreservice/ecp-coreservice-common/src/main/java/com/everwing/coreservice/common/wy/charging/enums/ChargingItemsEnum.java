package com.everwing.coreservice.common.wy.charging.enums;


/**
 * @auther: qhc
 * @date: 2018/11/14 10:58
 * @Description: 目前存在的计费项类型枚举
 **/
public enum ChargingItemsEnum{
    /**
     * 这里只是包含了目前存在的，后面如果有新增，按照规则递增就好了
     */
    CHARGING_WY(1, "物业管理费"),
    CHARGING_BT(2, "本体基金"),
    CHARGING_WATER(3, "基础水费"),
    CHARGING_ELECT(4, "基础电费"),
    CHARGING_SEWAGE(5, "污水处理费"),
    CHARGING_RUBBISH(6, "垃圾处理费");

    private int code;
    private String description;

    ChargingItemsEnum(int code,String description) {
        this.code = code;
        this.description = description;
    }

    public ChargingItemsEnum getChargingItemEnum(int code) {
        for (ChargingItemsEnum chargingItemsEnum : ChargingItemsEnum.values()) {
            if (chargingItemsEnum.getCode() == code) {
                return chargingItemsEnum;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
