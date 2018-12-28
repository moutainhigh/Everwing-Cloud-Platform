package com.everwing.coreservice.common.wy.fee.constant;

/**
 * @author shiny
 * Created by DELL on 2018/5/29.
 */
public enum AcOrderTypeEnum {

    CYCLE(1,"周期性收费订单"),

    PRODUCT(2,"产品类收费订单");

    private int type;

    private String desc;

    AcOrderTypeEnum(int type,String desc){
        this.type=type;
        this.desc=desc;
    }

    public int getType() {
        return type;
    }
}
