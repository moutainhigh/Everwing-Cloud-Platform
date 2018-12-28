package com.everwing.coreservice.common.wy.entity.property.vehicle;/**
 * Created by wust on 2017/8/1.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/1
 * @author wusongti@lii.com.cn
 */
public class TVehicleList extends TVehicle {
    private static final long serialVersionUID = 4676127227600757366L;

    private String address;         // 资产地址
    private String projectName;     // 项目名称
    private String customerType;    // 客户类型
    private String customerId;      // 客户编码
    private String customerName;    // 客户名称
    private String holderTypeName;  // 资产客户关系名称

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getHolderTypeName() {
        return holderTypeName;
    }

    public void setHolderTypeName(String holderTypeName) {
        this.holderTypeName = holderTypeName;
    }
}
