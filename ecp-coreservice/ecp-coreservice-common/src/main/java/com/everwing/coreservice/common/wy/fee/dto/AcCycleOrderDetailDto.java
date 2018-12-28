package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.wy.fee.constant.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author DELL shiny
 * @create 2018/8/14
 */
public class AcCycleOrderDetailDto implements Serializable {

    private String companyId;

    private BusinessType businessType;

    private AccountType accountType;

    private OperatorType operatorType;

    private ClientType clientType;

    private BigDecimal amount;

    private String payer;

    private String payerMobile;

    private AcOrderStateEnum acOrderStateEnum;

    private AcPayStateEnum payStateEnum;

    private AcOrderTypeEnum orderTypeEnum;

    private String operationDetailId;

    private Date payTime;

    private AcPayTypeEnum payTypeEnum;

    private ChargingType chargingType;

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(OperatorType operatorType) {
        this.operatorType = operatorType;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public AcOrderStateEnum getAcOrderStateEnum() {
        return acOrderStateEnum;
    }

    public void setAcOrderStateEnum(AcOrderStateEnum acOrderStateEnum) {
        this.acOrderStateEnum = acOrderStateEnum;
    }

    public AcPayStateEnum getPayStateEnum() {
        return payStateEnum;
    }

    public void setPayStateEnum(AcPayStateEnum payStateEnum) {
        this.payStateEnum = payStateEnum;
    }

    public AcOrderTypeEnum getOrderTypeEnum() {
        return orderTypeEnum;
    }

    public void setOrderTypeEnum(AcOrderTypeEnum orderTypeEnum) {
        this.orderTypeEnum = orderTypeEnum;
    }

    public String getOperationDetailId() {
        return operationDetailId;
    }

    public void setOperationDetailId(String operationDetailId) {
        this.operationDetailId = operationDetailId;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public AcPayTypeEnum getPayTypeEnum() {
        return payTypeEnum;
    }

    public void setPayTypeEnum(AcPayTypeEnum payTypeEnum) {
        this.payTypeEnum = payTypeEnum;
    }

    public ChargingType getChargingType() {
        return chargingType;
    }

    public void setChargingType(ChargingType chargingType) {
        this.chargingType = chargingType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
