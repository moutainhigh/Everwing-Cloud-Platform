package com.everwing.coreservice.common.wy.fee.entity;

import com.everwing.coreservice.common.wy.fee.constant.AcChargeDetailBusinessTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AcCurrentChargeDetail implements Serializable {
    private String id;

    private String houseCodeNew;

    private BigDecimal chargeAmount;

    private String accountId;

    private int accountType;

    private Date chargeTime;

    private Date auditTime;

    private String lastChargeId;

    private String chargeDetail;

    private BigDecimal commonDikou;

    private BigDecimal specialDikou;

    private String projectId;

    private String projectName;

    private String createId;

    private Date createTime;

    private BigDecimal payedAmount;

    private BigDecimal assignAmount;

    private BigDecimal currenctArreas;  //本期欠费
    
    private BigDecimal currentPayment;  //后面增加了字段   记录本次计费的金额  2018-09-01 qhc

    private Date updateTime;

    private BigDecimal payableAmount;
    
    private AcChargeDetailBusinessTypeEnum businessTypeEnum ;
    
    private int businessType;

    private String operaId;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew == null ? null : houseCodeNew.trim();
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public Date getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Date chargeTime) {
        this.chargeTime = chargeTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getLastChargeId() {
        return lastChargeId;
    }

    public void setLastChargeId(String lastChargeId) {
        this.lastChargeId = lastChargeId == null ? null : lastChargeId.trim();
    }

    public String getChargeDetail() {
        return chargeDetail;
    }

    public void setChargeDetail(String chargeDetail) {
        this.chargeDetail = chargeDetail == null ? null : chargeDetail.trim();
    }

    public BigDecimal getCommonDikou() {
        return commonDikou;
    }

    public void setCommonDikou(BigDecimal commonDikou) {
        this.commonDikou = commonDikou;
    }

    public BigDecimal getSpecialDikou() {
        return specialDikou;
    }

    public void setSpecialDikou(BigDecimal specialDikou) {
        this.specialDikou = specialDikou;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId == null ? null : createId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getPayedAmount() {
        return payedAmount;
    }

    public void setPayedAmount(BigDecimal payedAmount) {
        this.payedAmount = payedAmount;
    }

    public BigDecimal getAssignAmount() {
        return assignAmount;
    }

    public void setAssignAmount(BigDecimal assignAmount) {
        this.assignAmount = assignAmount;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(BigDecimal payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getOperaId() {
        return operaId;
    }

    public void setOperaId(String operaId) {
        this.operaId = operaId == null ? null : operaId.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", houseCodeNew=").append(houseCodeNew);
        sb.append(", chargeAmount=").append(chargeAmount);
        sb.append(", accountId=").append(accountId);
        sb.append(", accountType=").append(accountType);
        sb.append(", chargeTime=").append(chargeTime);
        sb.append(", auditTime=").append(auditTime);
        sb.append(", lastChargeId=").append(lastChargeId);
        sb.append(", chargeDetail=").append(chargeDetail);
        sb.append(", commonDikou=").append(commonDikou);
        sb.append(", specialDikou=").append(specialDikou);
        sb.append(", projectId=").append(projectId);
        sb.append(", projectName=").append(projectName);
        sb.append(", createId=").append(createId);
        sb.append(", createTime=").append(createTime);
        sb.append(", payedAmount=").append(payedAmount);
        sb.append(", assignAmount=").append(assignAmount);
        sb.append(", currenctArreas=").append(currenctArreas);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", payableAmount=").append(payableAmount);
        sb.append(", operaId=").append(operaId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

	public BigDecimal getCurrenctArreas() {
		return currenctArreas;
	}

	public void setCurrenctArreas(BigDecimal currenctArreas) {
		this.currenctArreas = currenctArreas;
	}

	public AcChargeDetailBusinessTypeEnum getBusinessTypeEnum() {
		return businessTypeEnum;
	}

	public void setBusinessTypeEnum(AcChargeDetailBusinessTypeEnum businessTypeEnum) {
		this.businessTypeEnum = businessTypeEnum;
	}

	public int getBusinessType() {
		return businessType;
	}

	public void setBusinessType(int businessType) {
		this.businessType = businessType;
	}

	public BigDecimal getCurrentPayment() {
		return currentPayment;
	}

	public void setCurrentPayment(BigDecimal currentPayment) {
		this.currentPayment = currentPayment;
	}

}