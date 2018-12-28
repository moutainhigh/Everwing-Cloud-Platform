package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.wy.fee.constant.ChargingType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author DELL shiny
 * @create 2018/6/8
 */
public class AcLastBillFeeDto implements Serializable {


	private static final long serialVersionUID = 3949785305863757277L;

	private String companyId;

    private String projectId;

    private String projectName;

    private String houseCodeNew;

    private BigDecimal lastBillFee;

    private ChargingType chargingType;

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

    public BigDecimal getLastBillFee() {
        return lastBillFee;
    }

    public void setLastBillFee(BigDecimal lastBillFee) {
        this.lastBillFee = lastBillFee;
    }

    public ChargingType getChargingType() {
        return chargingType;
    }

    public void setChargingType(ChargingType chargingType) {
        this.chargingType = chargingType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "AcLastBillFeeDto{" +
                "companyId='" + companyId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", lastBillFee=" + lastBillFee +
                ", chargingType=" + chargingType +
                ", operator='" + operator + '\'' +
                '}';
    }
}
