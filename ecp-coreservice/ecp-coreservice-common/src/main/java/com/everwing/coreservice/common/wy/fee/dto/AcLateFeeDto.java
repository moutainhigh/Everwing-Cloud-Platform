package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.wy.fee.constant.ChargingType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author DELL shiny
 * @create 2018/6/8
 */
public class AcLateFeeDto implements Serializable {
	
	private static final long serialVersionUID = 4526786847983665395L;

	private String companyId;

    private String projectId;

    private String projectName;

    private String houseCodeNew;

    private BigDecimal money;

    private ChargingType chargingType;

    private int businessType;

    private String desc;

    private String operateDetailId;

    private BigDecimal principal;

    private BigDecimal rate;

    private int isSingle;

    private int day;

    private String operator;

    /**
     * 1支付滞纳金0计算滞纳金
     */
    private int isPay;

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public ChargingType getChargingType() {
        return chargingType;
    }

    public void setChargingType(ChargingType chargingType) {
        this.chargingType = chargingType;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOperateDetailId() {
        return operateDetailId;
    }

    public void setOperateDetailId(String operateDetailId) {
        this.operateDetailId = operateDetailId;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public int getIsSingle() {
        return isSingle;
    }

    public void setIsSingle(int isSingle) {
        this.isSingle = isSingle;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    @Override
    public String toString() {
        return "AcLateFeeDto{" +
                "companyId='" + companyId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", money=" + money +
                ", chargingType=" + chargingType +
                ", businessType=" + businessType +
                ", desc='" + desc + '\'' +
                ", operateDetailId='" + operateDetailId + '\'' +
                ", principal=" + principal +
                ", rate=" + rate +
                ", isSingle=" + isSingle +
                ", day=" + day +
                ", operator='" + operator + '\'' +
                ", isPay=" + isPay +
                '}';
    }
}
