package com.everwing.coreservice.common.wy.fee.constant;

/**
 * 订单状态枚举
 *
 * @author DELL shiny
 * @create 2018/5/29
 */
public enum AcOrderStateEnum {

    GENERATED(1,"已生成"),

    FINISHED(2,"已完成"),

    INVALID(3,"已作废"),

    WAITING_RCORDED(4,"待入账");

    private int state;

    private String desc;

    AcOrderStateEnum(int state,String desc){
        this.state=state;
        this.desc=desc;
    }

    public static AcOrderStateEnum getAcOrderStateEnumByCode(int code){
        for (AcOrderStateEnum acOrderStateEnum:AcOrderStateEnum.values()){
            int defaultCode=acOrderStateEnum.getState();
            if(defaultCode==code){
                return acOrderStateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }
}
