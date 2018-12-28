package com.everwing.coreservice.common.wy.fee.constant;

/**
 * @author shiny
 * Created by DELL on 2018/5/29.
 */
public enum AcPayTypeEnum {

    CYCLE(1,"周期性缴费"),

    DELAY(2,"滞纳金缴费");

    private int type;

    private String desc;

    AcPayTypeEnum(int type,String desc){
        this.type=type;
        this.desc=desc;
    }

    public int getType() {
        return type;
    }
}
