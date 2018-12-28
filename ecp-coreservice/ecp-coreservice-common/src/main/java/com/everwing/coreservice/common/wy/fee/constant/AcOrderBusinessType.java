package com.everwing.coreservice.common.wy.fee.constant;

/**
 * @author shiny
 * Created by DELL on 2018/5/29.
 */
public enum AcOrderBusinessType {

    PAYMENT(1,"缴费"),

    PRESTORE(2,"预存");

    private int type;

    private String desc;

    AcOrderBusinessType(int type,String desc){
        this.type=type;
        this.desc=desc;
    }

    public int getType() {
        return type;
    }
}
