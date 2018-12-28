package com.everwing.coreservice.common.enums;/**
 * Created by wust on 2018/1/26.
 */

/**
 *
 * Function:跨平台的全局常量
 * Reason:
 * Date:2018/1/26
 * @author wusongti@lii.com.cn
 */
public enum ApplicationConstant {
    userType_platform("platform"),
    userType_wy("wy"),
    userType_reportPlatform("reportPlatform"),
    userType_systemAdmin("systemAdmin"),
    systemProp("jZ5$x!6yeAo1Qe^r"),
    COOKIE_TOKEN_REPORT_WEB_LOGIN("COOKIE_TOKEN_REPORT_WEB_LOGIN"),
    REPORT_WEB_LOGIN_KEY("REPORT_WEB_LOGIN_KEY_%s"),
    WY_WEB_LOGIN_KEY("WY_WEB_LOGIN_%s"),


    /**
     * redis缓存表key
     */
    REDIS_TABLE_KEY_LOOKUP("REDIS_TABLE_KEY_LOOKUP"),
    REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_CODE_BY_PARENT_CODE_AND_NAME_MAP("REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_CODE_BY_PARENT_CODE_AND_NAME_MAP"),
    REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_NAME_BY_PARENT_CODE_AND_CODE_MAP("REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_NAME_BY_PARENT_CODE_AND_CODE_MAP"),
    REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_BY_LOOKUP_ID_AND_PARENT_CODE_MAP("REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_BY_LOOKUP_ID_AND_PARENT_CODE_MAP"),
    REDIS_TABLE_KEY_AREAS("REDIS_TABLE_KEY_AREAS"),
    REDIS_TABLE_KEY_BUILDING_TREE("REDIS_TABLE_KEY_BUILDING_TREE"),
    REDIS_TABLE_KEY_DATA_SOURCE("REDIS_TABLE_KEY_DATA_SOURCE");

    ApplicationConstant(){}
    private String stringValue;
    public String getStringValue() {
        return stringValue;
    }
    ApplicationConstant(String stringValue){
        this.stringValue = stringValue;
    }

    private int intValue;
    public int getIntValue() {
        return intValue;
    }
    ApplicationConstant(int intValue){
        this.intValue = intValue;
    }
}
