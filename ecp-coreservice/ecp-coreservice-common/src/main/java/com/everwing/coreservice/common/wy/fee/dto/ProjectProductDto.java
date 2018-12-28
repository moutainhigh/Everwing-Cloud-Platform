package com.everwing.coreservice.common.wy.fee.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 项目账户产品账户dto
 *
 * @author DELL shiny
 * @create 2018/6/11
 */
public class ProjectProductDto  implements Serializable{

	private static final long serialVersionUID = -8571558118646034323L;

	private String companyId;

    private String projectId;

    private BigDecimal money;

    private String orderId;

    private String orderJson;

    private int isAsset;

    private String houseCodeNew;

    private String logStream;

    private String operator;

    private BigDecimal rate;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderJson() {
        return orderJson;
    }

    public void setOrderJson(String orderJson) {
        this.orderJson = orderJson;
    }

    public int getIsAsset() {
        return isAsset;
    }

    public void setIsAsset(int isAsset) {
        this.isAsset = isAsset;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public String getLogStream() {
        return logStream;
    }

    public void setLogStream(String logStream) {
        this.logStream = logStream;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ProjectProductDto{" +
                "companyId='" + companyId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", money=" + money +
                ", orderId='" + orderId + '\'' +
                ", orderJson='" + orderJson + '\'' +
                ", isAsset=" + isAsset +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", logStream='" + logStream + '\'' +
                ", operator='" + operator + '\'' +
                ", rate=" + rate +
                '}';
    }
}
