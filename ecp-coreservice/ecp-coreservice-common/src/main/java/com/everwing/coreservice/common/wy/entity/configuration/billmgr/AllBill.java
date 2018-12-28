package com.everwing.coreservice.common.wy.entity.configuration.billmgr;

import java.io.Serializable;

public class AllBill implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -591660816438919192L;
	
	
	
	private String custName;
	private String custCode;
	private String billCode;
	private String billGenTime;
	private String fullName;
	private Double area;
	private String isCollection;
	private Double lastTotalBill;		//上期金额
	private Double currTotalBill;		//本期总费用  费用+分摊
	private Double totalLateFee;		//本期总违约金
	private Double totalDkFee;			//本期账户抵扣
	private Double totalSchareFee;		//总分摊
	private Double currBilling;		//本期应付
	private Double lastTotalPayed;	//上期支付
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCustCode() {
		return custCode;
	}
	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getBillGenTime() {
		return billGenTime;
	}
	public void setBillGenTime(String billGenTime) {
		this.billGenTime = billGenTime;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Double getArea() {
		return area;
	}
	public void setArea(Double area) {
		this.area = area;
	}
	public Double getLastTotalBill() {
		return lastTotalBill;
	}
	public void setLastTotalBill(Double lastTotalBill) {
		this.lastTotalBill = lastTotalBill;
	}
	public Double getCurrTotalBill() {
		return currTotalBill;
	}
	public void setCurrTotalBill(Double currTotalBill) {
		this.currTotalBill = currTotalBill;
	}
	public Double getTotalLateFee() {
		return totalLateFee;
	}
	public void setTotalLateFee(Double totalLateFee) {
		this.totalLateFee = totalLateFee;
	}
	public Double getTotalDkFee() {
		return totalDkFee;
	}
	public void setTotalDkFee(Double totalDkFee) {
		this.totalDkFee = totalDkFee;
	}
	public Double getCurrBilling() {
		return currBilling;
	}
	public void setCurrBilling(Double currBilling) {
		this.currBilling = currBilling;
	}
	public Double getLastTotalPayed() {
		return lastTotalPayed;
	}
	public void setLastTotalPayed(Double lastTotalPayed) {
		this.lastTotalPayed = lastTotalPayed;
	}
	public Double getTotalSchareFee() {
		return totalSchareFee;
	}
	public void setTotalSchareFee(Double totalSchareFee) {
		this.totalSchareFee = totalSchareFee;
	}

	public String getIsCollection() {
		return isCollection;
	}

	public void setIsCollection(String isCollection) {
		this.isCollection = isCollection;
	}
}
