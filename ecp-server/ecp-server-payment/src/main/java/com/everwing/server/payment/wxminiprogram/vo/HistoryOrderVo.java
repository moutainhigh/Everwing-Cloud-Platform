package com.everwing.server.payment.wxminiprogram.vo;


import com.everwing.coreservice.common.wy.fee.dto.AcOrderHistoryDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: zgrf
 * @Description: 历史订单
 * @Date: Create in 2018-08-21 15:49
 **/

public class HistoryOrderVo implements Serializable{

    private BigDecimal payMoney;

    private String buildingFullName;

    private String projectName;

    private String orderNumber;

    private Date payTime;

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public HistoryOrderVo() {
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getBuildingFullName() {
        return buildingFullName;
    }

    public void setBuildingFullName(String buildingFullName) {
        this.buildingFullName = buildingFullName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setAcOrderHistoryDto(AcOrderHistoryDto acOrderHistoryDto) {
        this.buildingFullName = acOrderHistoryDto.getBuildingFullName();
        this.orderNumber = acOrderHistoryDto.getOrderNumber();
        this.payMoney = acOrderHistoryDto.getPayMoney();
        this.payTime = acOrderHistoryDto.getPayTime();
    }

    @Override
    public String toString() {
        return "HistoryOrderVo{" +
                "payMoney=" + payMoney +
                ", buildingFullName='" + buildingFullName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                '}';
    }
}
