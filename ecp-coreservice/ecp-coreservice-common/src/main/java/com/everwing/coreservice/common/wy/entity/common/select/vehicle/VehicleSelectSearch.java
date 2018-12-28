package com.everwing.coreservice.common.wy.entity.common.select.vehicle;/**
 * Created by wust on 2018/12/4.
 */

/**
 *
 * Function:车辆下拉框选择器查询对象
 * Reason:
 * Date:2018/12/4
 * @author wusongti@lii.com.cn
 */
public class VehicleSelectSearch implements java.io.Serializable{

    private static final long serialVersionUID = 6374370936641939818L;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    private String customerId;      // 客户id
}
