package com.everwing.coreservice.common.wy.fee.constant;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-07-31 17:06
 **/

public enum  ChargingType {

    WY(1,"物业"),

    BT(2,"本体"),

    WATER(3,"水费"),

    ELECT(4,"电费"),

    LATE_FEE(5,"违约金"),

    DEPOSIT(6,"押金");


    private int code;

    private String description;

    ChargingType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ChargingType getChargingTypeByCode(int code){
        for (ChargingType chargingType:ChargingType.values()){
            int defaultCode=chargingType.getCode();
            if(defaultCode==code){
                return chargingType;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
