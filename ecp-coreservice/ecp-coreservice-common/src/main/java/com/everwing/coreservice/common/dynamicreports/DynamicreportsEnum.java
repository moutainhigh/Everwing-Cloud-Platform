package com.everwing.coreservice.common.dynamicreports;/**
 * Created by wust on 2018/2/6.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2018/2/6
 * @author wusongti@lii.com.cn
 */
public enum  DynamicreportsEnum {
    USERCONTEXT_whiteResource("whiteResource"),
    USERCONTEXT_isNotWhiteResource("isNotWhiteResource"),
    USERCONTEXT_groupMenuByLevel("groupMenuByLevel"),
    USERCONTEXT_groupMenuByPid("groupMenuByPid"),
    USERCONTEXT_groupResourcesByMenuId("groupResourcesByMenuId"),
    USERCONTEXT_userInfo("userInfo");
    DynamicreportsEnum(){}
    DynamicreportsEnum(String stringValue){
        this.stringValue = stringValue;
    }

    private String stringValue;
    public String getStringValue() {
        return stringValue;
    }
}
