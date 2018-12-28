package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.wy.fee.constant.BusinessType;
import com.everwing.coreservice.common.wy.fee.constant.ClientType;
import com.everwing.coreservice.common.wy.fee.constant.OperatorType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author DELL shiny
 * @create 2018/8/13
 */
public class AcBusinessOperaDto implements Serializable {

    private String operationId;

    private Date operationTime;

    private BusinessType businessType;

    private BigDecimal amount;

    private String projectId;

    private String projectName;

    private String remark;

    private OperatorType operatorType;

    private ClientType clientType;

    private String companyId;

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
