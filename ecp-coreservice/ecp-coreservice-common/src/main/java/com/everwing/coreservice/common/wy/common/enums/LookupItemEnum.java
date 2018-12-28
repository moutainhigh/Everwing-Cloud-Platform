package com.everwing.coreservice.common.wy.common.enums;/**
 * Created by wust on 2017/5/25.
 */

/**
 *
 * Function:对应lookupItem表的code
 * Reason:枚举值格式为：父类编码_子编码
 * Date:2017/5/25
 * @author wusongti@lii.com.cn
 */
public enum LookupItemEnum {
    buildingType_qi("qi"),                      // 建筑类型_期
    buildingType_qu("qu"),                      // 建筑类型_区
    buildingType_dongzuo("dongzuo"),            // 建筑类型_栋座
    buildingType_danyuanrukou("danyuanrukou"),  // 建筑类型_单元入口
    buildingType_ceng("ceng"),                  // 建筑类型_层
    buildingType_house("house"),                // 建筑类型_住宅
    buildingType_store("store"),                // 建筑类型_商铺
    buildingType_parkspace("parkspace"),        // 建筑类型_车位
    buildingType_parentOther("parentOther"),    // 建筑类型_父节点其他
    buildingType_leafOther("leafOther"),        // 建筑类型_叶子节点其他
    buildingType_publicbuilding("publicbuilding"),// 建筑类型_公建

    enableDisable_enable("enable"),    // 启用
    enableDisable_disable("disable"),  // 禁用

    sex_male("male"),                  // 男性
    sex_female("female"),              // 女性

    staffState_draft("draft"),         // 未审核
    staffState_failed("failed"),       // 审核不通过
    staffState_joined("joined"),       // 已入职
    staffState_left("left"),           // 离职

    documentType_idcard("0"),       // 证件类型_身份证
    documentType_passport("1"),     // 证件类型_护照
    documentType_other("2"),        // 证件类型_其他


    staffType_systemAdmin("systemAdmin"),     // 员工类型_平台管理员
    staffType_staff("staff"),                 // 员工类型_普通员工
    staffType_project("project"),             // 员工类型_项目员工
    
    yesNo_yes("Yes"),			//是
    yesNo_no("No"),				//否


    valuationType_unitPrice("unitPrice"),        // 计价方式，单价
    valuationType_totalPrice("totalPrice"),      // 计价方式，总价

    timeUnit_days("days"),                      // 时间单位，天
    timeUnit_months("months"),                  // 时间单位，月

    productOrderState_draft("draft"),           // 订单状态，制单
    productOrderState_doing("doing"),           // 订单状态，交易中
    productOrderState_rollback("rollback"),     // 订单状态，回退
    productOrderState_success("success"),       // 订单状态，交易成功
    productOrderState_invalid("invalid"),       // 订单状态，作废

    depositState_draft("draft"),                // 押金状态，待退
    depositState_rollback("rollback"),          // 押金状态，回退
    depositState_done("done"),                  // 押金状态，已退
    depositState_invalid("invalid"),            // 押金状态，作废

    productOrderPayType_cash("cash"),                   // 订单支付类型，现金支付
    productOrderPayType_charge("charge"),               // 订单支付类型，刷卡
    productOrderPayType_bank("bank"),                   // 订单支付类型，银行收款
    productOrderPayType_alipay("alipay"),               // 订单支付类型，支付宝
    productOrderPayType_weixinpay("weixinpay"),         // 订单支付类型，微信
    productOrderPayType_returnmoney("returnmoney"),     // 订单支付类型，找零


    preferentialType_months("months"),                  // 停车优惠卡，月卡
    preferentialType_quarter("quarter"),                // 停车优惠卡，季卡
    preferentialType_halfaYear("halfaYear"),            // 停车优惠卡，半年卡
    preferentialType_year("year");                      // 停车优惠卡，年卡



    LookupItemEnum(){}
    private String stringValue;
    public String getStringValue() {
        return stringValue;
    }
    LookupItemEnum(String stringValue){
        this.stringValue = stringValue;
    }

    private int intValue;
    public int getIntValue() {
        return intValue;
    }
    LookupItemEnum(int intValue){
        this.intValue = intValue;
    }
}
