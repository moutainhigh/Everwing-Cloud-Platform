package com.everwing.coreservice.common.wy.common.enums;/**
 * Created by wust on 2017/6/2.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/2
 * @author wusongti@lii.com.cn
 */
public enum OperationEnum {
    Insert("新增"),
    Modify("修改"),
    Delete("删除"),
    Search("查询"),
    Save("保存"),
    Download("下载"),
    Upload("上传"),
    Import("导入"),
    Export("导出"),
    Login("登录"),
    Logout("注销"),
    Module_Common("通用模块"),
    Module_Role("角色模块"),
    Module_User("用户模块"),
    Module_Lookup("属性模块"),
    Module_ImportExport("导入导出模块"),
    Module_OperationLog("操作日志模块"),
    Module_Building("建筑模块"),
    Module_Company("公司模块"),
    Module_Department("部门模块"),
    Module_Organization("组织模块"),
    Module_Project("项目模块"),
    Module_Vehicle("车辆模块"),
    Module_Property("资产模块"),
    Module_Collection("托收模块"),
    Modele_BankDelivery("交割模块"),
    Module_Pay("支付模块"),
    Module_Billing("支付模块"),
    Module_Gating("门控机模块"),
    Module_Product("产品模块"),
    Module_Deposit("押金管理"),
    Module_Synchrodata("同步数据");


    private String value;
    public String getValue() {
        return value;
    }
    OperationEnum(String value){
        this.value = value;
    }
}
