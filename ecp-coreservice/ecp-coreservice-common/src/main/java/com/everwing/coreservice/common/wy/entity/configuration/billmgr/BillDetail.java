package com.everwing.coreservice.common.wy.entity.configuration.billmgr;

import java.io.Serializable;


public class BillDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1531697282445625142L;

	
	private String name;			//计费项名
	private String billTime;			//计费时间
	private Double unitPrice;		//单价
	
	private Double lastReading;		//上次读数
	private Double totalReading;	//本次读数
	private Double useCount;		//用量
	private Double rate;			//倍率
		
	private Double total;			//本计费项合计

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getBillTime() {
		return billTime;
	}

	public void setBillTime(String billTime) {
		this.billTime = billTime;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getLastReading() {
		return lastReading;
	}

	public void setLastReading(Double lastReading) {
		this.lastReading = lastReading;
	}

	public Double getTotalReading() {
		return totalReading;
	}

	public void setTotalReading(Double totalReading) {
		this.totalReading = totalReading;
	}

	public Double getUseCount() {
		return useCount;
	}

	public void setUseCount(Double useCount) {
		this.useCount = useCount;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
	
	
}
