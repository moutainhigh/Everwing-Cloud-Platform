package com.everwing.coreservice.common.wy.entity.common.select.vehicle;/**
 * Created by wust on 2018/12/4.
 */

/**
 *
 * Function:车辆下拉框选择器值对象
 * Reason:
 * Date:2018/12/4
 * @author wusongti@lii.com.cn
 */
public class VehicleSelectList {

    private String vehicleNumber;

    /** 下拉框值 */
    private String value;

    /** 下拉框label */
    private String label;

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getValue() {
        this.setValue(this.getVehicleNumber());
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        this.setLabel(this.getVehicleNumber());
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
