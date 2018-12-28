package com.everwing.coreservice.common.enums;

/**
 * wyApp推送类型
 *
 * @author DELL shiny
 * @create 2018/3/8
 */
public enum WyAppPushTypeEnum {
    /**
     * 工单我的任务
     */
    ORDER_MY_ORDER("3","1"),
    /**
     * 工单工单池
     */
    ORDER_ORDER_POOL("3","2"),
    /**
     * 工单审批列表
     */
    ORDER_AUDIT_TABLE("3","3"),
    /**
     * 水表任务
     */
    WATER_METER_ORDER("1","1"),
    /**
     * 水表投诉工单
     */
    WATER_METER_COMPLAIN("1","2"),
    /**
     * 电表任务
     */
    ELECTRIC_METER_ORDER("2","1"),
    /**
     * 电表投诉工单
     */
    ELECTRIC_METER_COMPLAIN("2","2");

    private String paterType;
    private String childType;

    WyAppPushTypeEnum(String paterType,String childType){
        this.paterType=paterType;
        this.childType=childType;
    }

    public String getPaterType(){
        return paterType;
    }

    public String getChildType(){
        return childType;
    }
}
