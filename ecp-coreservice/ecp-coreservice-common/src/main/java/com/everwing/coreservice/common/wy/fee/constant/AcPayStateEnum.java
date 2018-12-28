package com.everwing.coreservice.common.wy.fee.constant;

/**
 * 支付状态枚举
 *
 * @author DELL shiny
 * @create 2018/5/29
 */
public enum AcPayStateEnum {

    UN_PAY(1,"未支付"),

    PAY_ING(2,"支付中"),

    PART_PAYED(3,"部分支付"),

    PAYED(4,"已支付");

    private int state;

    private String desc;

    AcPayStateEnum(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }

    public static AcPayStateEnum getAcPayStateEnumByCode(int code){
        for (AcPayStateEnum acPayStateEnum:AcPayStateEnum.values()){
            int defaultCode=acPayStateEnum.getState();
            if(defaultCode==code){
                return acPayStateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
