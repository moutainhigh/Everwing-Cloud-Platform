package com.everwing.coreservice.common.wy.entity.common.select.customer;/**
 * Created by wust on 2018/12/4.
 */

/**
 *
 * Function:客户下拉框选择器值对象
 * Reason:
 * Date:2018/12/4
 * @author wusongti@lii.com.cn
 */
public class CustomerSelectList implements java.io.Serializable{

    private static final long serialVersionUID = 3594920533247777626L;

    private String customerId;

    private String customerName;

    /** 下拉框值 */
    private String value;

    /** 下拉框label */
    private String label;


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

    public String getValue() {
        this.setValue(this.getCustomerId());
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        this.setLabel(this.getCustomerName());
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
