package com.everwing.coreservice.common.wy.fee.constant;

/**
 * @author DELL shiny
 * @create 2018/8/13
 */
public enum OperatorType {

    FRONT_DESK(1,"前台"),

    OWNER(2,"业主");

    private int code;

    private String desc;

    OperatorType(int code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
