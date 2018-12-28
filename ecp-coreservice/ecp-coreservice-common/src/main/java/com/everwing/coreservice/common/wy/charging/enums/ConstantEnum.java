package com.everwing.coreservice.common.wy.charging.enums;

/**
 * @auther: qhc
 * @Date: 2018/11/14 11:35
 * @Description: 存放心计费中的一些常量信息使用
 */
public enum ConstantEnum {

    /**
     * 计费项使用状态
     */
    charging_item_is_used(0,"计费项使用中"),
    charging_item_not_used(1,"计费项停用"),

    /**
     * 单个计费项当月总的计费状态
     */
    charging_total_item_not_charging(1,"未计费"),
    charging_total_item_in_charging(2,"计费中"),
    charging_total_item_charging_end(3,"已计费"),
    charging_total_item_charging_fail(4,"计费失败"),

    /**
     * 单个计费项当月总的审核状态
     */
    Audi_total_item_not_charging(1,"未审核"),
    Audi_total_item_in_charging(2,"审核中"),
    Audi_total_item_charging_end(3,"已审核"),
    Audi_total_item_charging_fail(4,"审核失败"),


    /**
     * 单个计费项当月总的抵扣状态
     */
    Deductible_total_item_not_charging(1,"未抵扣"),
    Deductible_total_item_in_charging(2,"计抵扣"),
    Deductible_total_item_charging_end(3,"已抵扣"),
    Deductible_total_item_charging_fail(4,"抵扣失败"),

    /**
     * 是否生成账单描述
     */
    is_create_bill_yes(0,"已生成"),
    is_create_bill_no(1,"未生成"),

    /**
     * 计费方案的启用状态
     */
    charging_scheme_is_used(0,"启用"),
    charging_scheme_is_not_used(1,"停用"),

    /**
     * 计费方案的计费方式
     */
    charging_mode_auto(0,"自动"),
    charging_mode_hand(1,"手动"),

    /**
     * 计费规则详情中用于描述如何区分峰谷平的字段
     * 标准就不区分峰谷平
     */
    charging_rule_type_standard(1,"标准"),
    charging_rule_type_Peak_value(2,"峰值"),
    charging_rule_type_Valleys(3,"谷值"),
    charging_rule_type_flat_value(4,"平值");


    ConstantEnum(int code,String descrip){
        this.code = code;
        this.descrip = descrip;
    }

    private int code;
    private String descrip;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }



}
