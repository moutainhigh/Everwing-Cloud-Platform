package com.everwing.coreservice.common.wy.common.enums;/**
 * Created by wust on 2018/12/19.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2018/12/19
 * @author wusongti@lii.com.cn
 */
public enum SynchrodataEnum {
    priorityLevel_high(1),
    priorityLevel_middle(2),
    priorityLevel_lower(3),

    state_draft("draft"),
    state_done("done"),
    state_error("error"),

    table_tc_building("tc_building"),
    table_tc_person_cust("tc_person_cust"),
    table_tc_person_building("tc_person_building");

    SynchrodataEnum(){}
    private String stringValue;
    public String getStringValue() {
        return stringValue;
    }
    SynchrodataEnum(String stringValue){
        this.stringValue = stringValue;
    }

    private int intValue;
    public int getIntValue() {
        return intValue;
    }
    SynchrodataEnum(int intValue){
        this.intValue = intValue;
    }
}
