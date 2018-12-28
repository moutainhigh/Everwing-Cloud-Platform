package com.everwing.coreservice.common.wy.entity.common.select.customer;/**
 * Created by wust on 2018/12/4.
 */

/**
 *
 * Function:客户下拉框选择器查询对象
 * Reason:
 * Date:2018/12/4
 * @author wusongti@lii.com.cn
 */
public class CustomerSelectSearch implements java.io.Serializable{
    private static final long serialVersionUID = 3799012678397313606L;

    private String name;            // 客户名称
    private String idCard;          // 个人客户身份证号码
    private String registerPhone;   // 个人客户注册电话号码
    private String officePhone;     // 企业客户办公电话号码
    private String customerType;    // 客户类型：个人客户和企业客户

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRegisterPhone() {
        return registerPhone;
    }

    public void setRegisterPhone(String registerPhone) {
        this.registerPhone = registerPhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
}
