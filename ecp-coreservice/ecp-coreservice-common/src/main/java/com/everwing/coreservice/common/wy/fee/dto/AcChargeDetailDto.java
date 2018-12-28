package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.wy.fee.constant.AcChargeDetailBusinessTypeEnum;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.constant.PayChannel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author DELL shiny
 * @create 2018/6/8
 */
public class AcChargeDetailDto implements Serializable {

	private static final long serialVersionUID = 8682260075974145002L;

	private String companyId;

    private String projectId;

    private String projectName;

    private String houseCodeNew;

    private Date auditTime;

    private BigDecimal chargeAmount;

    private ChargingType acChargeTypeEnum;

    private PayChannel payChannel;

    private Date chargeTime;

    private String lastChargeId;

    private String chargeDetail;

    private BigDecimal commonDiKou;

    private BigDecimal specialDiKou;

    private BigDecimal payedAmount;

    private BigDecimal assignAmount;

    private BigDecimal arrearsAmount;

    private BigDecimal payableAmount;

    private BigDecimal currentArreas;

    private String operationDetailId;

    private String operator;
    
    //增加的收费结果明细表的业务类型字段  1 计费  2 专项账户扣减  3 通用账户扣减   
    private AcChargeDetailBusinessTypeEnum businessTypeEnum ;

    private String chargingMoth;
    
    /**
     * 这里增加两个业务实现过程中会用到的对象属性
     */
    private AcSpecialDetailDto specialDetailDto;
    
    private AcLateFeeDto acLateFeeDto;
    
    private AcCommonAccountDetailDto acCommonAccountDetailDto;

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

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public ChargingType getChargingType() {
        return acChargeTypeEnum;
    }

    public void setChargingType(ChargingType acChargeTypeEnum) {
        this.acChargeTypeEnum = acChargeTypeEnum;
    }

    public Date getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Date chargeTime) {
        this.chargeTime = chargeTime;
    }

    public String getLastChargeId() {
        return lastChargeId;
    }

    public void setLastChargeId(String lastChargeId) {
        this.lastChargeId = lastChargeId;
    }

    public String getChargeDetail() {
        return chargeDetail;
    }

    public void setChargeDetail(String chargeDetail) {
        this.chargeDetail = chargeDetail;
    }

    public BigDecimal getCommonDiKou() {
        return commonDiKou;
    }

    public void setCommonDiKou(BigDecimal commonDiKou) {
        this.commonDiKou = commonDiKou;
    }

    public BigDecimal getSpecialDiKou() {
        return specialDiKou;
    }

    public void setSpecialDiKou(BigDecimal specialDiKou) {
        this.specialDiKou = specialDiKou;
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

    public BigDecimal getArrearsAmount() {
        return arrearsAmount;
    }

    public void setArrearsAmount(BigDecimal arrearsAmount) {
        this.arrearsAmount = arrearsAmount;
    }

    public BigDecimal getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(BigDecimal payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getOperationDetailId() {
        return operationDetailId;
    }

    public void setOperationDetailId(String operationDetailId) {
        this.operationDetailId = operationDetailId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getChargingMoth() {
        return chargingMoth;
    }

    public void setChargingMoth(String chargingMoth) {
        this.chargingMoth = chargingMoth;
    }

    @Override
    public String toString() {
        return "AcChargeDetailDto{" +
                "companyId='" + companyId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", auditTime=" + auditTime +
                ", chargeAmount=" + chargeAmount +
                ", acChargeTypeEnum=" + acChargeTypeEnum +
                ", payChannel=" + payChannel +
                ", chargeTime=" + chargeTime +
                ", lastChargeId='" + lastChargeId + '\'' +
                ", chargeDetail='" + chargeDetail + '\'' +
                ", commonDiKou=" + commonDiKou +
                ", specialDiKou=" + specialDiKou +
                ", payedAmount=" + payedAmount +
                ", assignAmount=" + assignAmount +
                ", arrearsAmount=" + arrearsAmount +
                ", payableAmount=" + payableAmount +
                ", currentArreas=" + currentArreas +
                ", operationDetailId='" + operationDetailId + '\'' +
                ", operator='" + operator + '\'' +
                ", businessTypeEnum=" + businessTypeEnum +
                ", chargingMoth='" + chargingMoth + '\'' +
                ", specialDetailDto=" + specialDetailDto +
                ", acLateFeeDto=" + acLateFeeDto +
                ", acCommonAccountDetailDto=" + acCommonAccountDetailDto +
                '}';
    }

    public ChargingType getAcChargeTypeEnum() {
        return acChargeTypeEnum;
    }

    public void setAcChargeTypeEnum(ChargingType acChargeTypeEnum) {
        this.acChargeTypeEnum = acChargeTypeEnum;
    }

    public PayChannel getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(PayChannel payChannel) {
        this.payChannel = payChannel;
    }

    public AcSpecialDetailDto getSpecialDetailDto() {
		return specialDetailDto;
	}

	public void setSpecialDetailDto(AcSpecialDetailDto specialDetailDto) {
		this.specialDetailDto = specialDetailDto;
	}

	public AcLateFeeDto getAcLateFeeDto() {
		return acLateFeeDto;
	}

	public void setAcLateFeeDto(AcLateFeeDto acLateFeeDto) {
		this.acLateFeeDto = acLateFeeDto;
	}

	public AcCommonAccountDetailDto getAcCommonAccountDetailDto() {
		return acCommonAccountDetailDto;
	}

	public void setAcCommonAccountDetailDto(AcCommonAccountDetailDto acCommonAccountDetailDto) {
		this.acCommonAccountDetailDto = acCommonAccountDetailDto;
	}

	public BigDecimal getCurrentArreas() {
		return currentArreas;
	}

	public void setCurrentArreas(BigDecimal currentArreas) {
		this.currentArreas = currentArreas;
	}

	public AcChargeDetailBusinessTypeEnum getBusinessTypeEnum() {
		return businessTypeEnum;
	}

	public void setBusinessTypeEnum(AcChargeDetailBusinessTypeEnum businessTypeEnum) {
		this.businessTypeEnum = businessTypeEnum;
	}
}
