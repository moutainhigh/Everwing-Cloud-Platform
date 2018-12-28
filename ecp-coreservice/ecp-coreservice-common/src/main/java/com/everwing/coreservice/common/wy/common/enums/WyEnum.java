package com.everwing.coreservice.common.wy.common.enums;/**
 * Created by wust on 2017/5/26.
 */

/**
 *
 * Function:物业平台公共部分枚举
 * Reason:
 * Date:2017/5/26
 * @author wusongti@lii.com.cn
 */
public enum  WyEnum {
    su_admin,       // 超级管理员
    m,              // 资源类型，菜单
    r,              // 资源类型，按钮
    anon,           // 白名单标识
    jzjg,           // 建筑结构根节点编码
    organizationType_staff("staff"),            // 组织类型，员工
    organizationType_role("role"),              // 组织类型，岗位
    organizationType_department("department"),  // 组织类型，部门
    organizationType_project("project"),        // 组织类型，项目
    organizationType_company("company"),        // 组织类型，公司
    download_redis_file_by_loginname_key("download_redis_file_by_loginname_key_%s"),// 当前登录用户导出的放在缓存中的文件key

    CODE_TYPE_1("1"),
    CODE_TYPE_2("2"),
    CODE_TYPE_3("3"),
    CODE_TYPE_04("04"),
    CODE_TYPE_05("05"),
    CODE_TYPE_06("06"),
    CODE_TYPE_07("07"),
    CODE_TYPE_08("08"),
    CODE_TYPE_09("09"),
    CODE_TYPE_10("10"),
    CODE_TYPE_11("11"),
    CODE_TYPE_12("12"),
    CODE_TYPE_13("13");

    WyEnum(){}
    WyEnum(String stringValue){
        this.stringValue = stringValue;
    }

    private String stringValue;
    public String getStringValue() {
        return stringValue;
    }
}
