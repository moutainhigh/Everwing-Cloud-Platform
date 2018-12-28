package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.wy.fee.constant.BusinessType;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.constant.PayChannel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author DELL shiny
 * @create 2018/6/6
 */
public class AcCommonAccountDetailDto implements Serializable {

	private static final long serialVersionUID = 4794124115975463808L;

	private String companyId;

    private String projectId;

    private String projectName;

    private String houseCodeNew;

    private java.math.BigDecimal money;
    
    //通用账户抵扣违约金金额
    private BigDecimal commonLateFeeDiKou;

    private BusinessType businessTypeEnum;

    private ChargingType chargingType;


    private PayChannel payChannel;

    private String deductionDetailId;

    private String desc;

    private String operateDetailId;

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

    public BusinessType getBusinessTypeEnum() {
        return businessTypeEnum;
    }

    public void setBusinessTypeEnum(BusinessType businessTypeEnum) {
        this.businessTypeEnum = businessTypeEnum;
    }

    public ChargingType getChargingType() {
        return chargingType;
    }

    public void setChargingType(ChargingType chargingType) {
        this.chargingType = chargingType;
    }

    public String getDeductionDetailId() {
        return deductionDetailId;
    }

    public void setDeductionDetailId(String deductionDetailId) {
        this.deductionDetailId = deductionDetailId;
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

    public PayChannel getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(PayChannel payChannel) {
        this.payChannel = payChannel;
    }

	public BigDecimal getCommonLateFeeDiKou() {
		return commonLateFeeDiKou;
	}

	public void setCommonLateFeeDiKou(BigDecimal commonLateFeeDiKou) {
		this.commonLateFeeDiKou = commonLateFeeDiKou;
	}

    @Override
    public String toString() {
        return "AcCommonAccountDetailDto{" +
                "companyId='" + companyId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", money=" + money +
                ", commonLateFeeDiKou=" + commonLateFeeDiKou +
                ", businessTypeEnum=" + businessTypeEnum +
                ", chargingType=" + chargingType +
                ", payChannel=" + payChannel +
                ", deductionDetailId='" + deductionDetailId + '\'' +
                ", desc='" + desc + '\'' +
                ", operateDetailId='" + operateDetailId + '\'' +
                ", operator='" + operator + '\'' +
                ", rate=" + rate +
                '}';
    }
}
