package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.wy.fee.constant.BusinessType;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.constant.PayChannel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author DELL shiny
 * @create 2018/6/8
 */
public class AcSpecialDetailDto implements Serializable{

	private static final long serialVersionUID = 3720598135642275374L;

	private String companyId;

    private String projectId;

    private String projectName;

    private String houseCodeNew;

    private BigDecimal moneyPrincipal; //这个字段用于存放抵扣本金的值
    
    private BigDecimal lateFeeDeductible;//抵扣违约金的部分

    private ChargingType chargingType;

    private BusinessType businessTypeEnum;

    private PayChannel payChannel;

    private String desc;

    /**
     * 抵扣明细id
     */
    private String deductionDetailId;

    /**
     * 前台操作id
     */
    private String operateDetailId;

    /**
     * 操作人
     */
    private String operator;

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

    public ChargingType getChargingType() {
        return chargingType;
    }

    public void setChargingType(ChargingType chargingType) {
        this.chargingType = chargingType;
    }

    public BusinessType getBusinessTypeEnum() {
        return businessTypeEnum;
    }

    public void setBusinessTypeEnum(BusinessType businessTypeEnum) {
        this.businessTypeEnum = businessTypeEnum;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDeductionDetailId() {
        return deductionDetailId;
    }

    public void setDeductionDetailId(String deductionDetailId) {
        this.deductionDetailId = deductionDetailId;
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

    public PayChannel getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(PayChannel payChannel) {
        this.payChannel = payChannel;
    }

    @Override
    public String toString() {
        return "AcSpecialDetailDto{" +
                "companyId='" + companyId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", moneyPrincipal=" + moneyPrincipal +
                ", lateFeeDeductible=" + lateFeeDeductible +
                ", chargingType=" + chargingType +
                ", businessTypeEnum=" + businessTypeEnum +
                ", payChannel=" + payChannel +
                ", desc='" + desc + '\'' +
                ", deductionDetailId='" + deductionDetailId + '\'' +
                ", operateDetailId='" + operateDetailId + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }

    public BigDecimal getMoneyPrincipal() {
		return moneyPrincipal;
	}

	public void setMoneyPrincipal(BigDecimal moneyPrincipal) {
		this.moneyPrincipal = moneyPrincipal;
	}

	public BigDecimal getLateFeeDeductible() {
		return lateFeeDeductible;
	}

	public void setLateFeeDeductible(BigDecimal lateFeeDeductible) {
		this.lateFeeDeductible = lateFeeDeductible;
	}
}
