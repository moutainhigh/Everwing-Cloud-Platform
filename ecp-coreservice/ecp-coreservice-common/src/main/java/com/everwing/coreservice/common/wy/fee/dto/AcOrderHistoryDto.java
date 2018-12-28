package com.everwing.coreservice.common.wy.fee.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-08-23 10:29
 **/

public class AcOrderHistoryDto implements Serializable{

    private String buildingFullName;

    private String orderNumber;

    private BigDecimal payMoney;

    private Date payTime;

    public AcOrderHistoryDto() {
    }

    public String getBuildingFullName() {
        return buildingFullName;
    }

    public void setBuildingFullName(String buildingFullName) {
        this.buildingFullName = buildingFullName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    @Override
    public String toString() {
        return "AcOrderHistoryDto{" +
                "buildingFullName='" + buildingFullName + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", payMoney=" + payMoney +
                '}';
    }
}
