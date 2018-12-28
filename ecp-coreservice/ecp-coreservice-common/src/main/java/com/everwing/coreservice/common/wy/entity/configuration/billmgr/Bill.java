package com.everwing.coreservice.common.wy.entity.configuration.billmgr;

import java.io.Serializable;

public class Bill implements Serializable{

	private static final long serialVersionUID = 3615451863714456345L;
	
	private Double currFee;			//本期费用
	private Double lastBillFee;		//上期费用
	private Double lateFee;			//违约金
	private Double lastUnPay;       //上期未付
	private Double lastPayed;		//上期支付
	private Double accountBalance;	//账户余额
	private Double currBillFee;		//本期应付
	private Double shareFee;		//分摊金额
	private Double total;			//总计
	private Double currPayed; 		//本期已付
	
	private String lastReading;		//上次读数
	private String totalReading;	//本次读数
	private Double useCount;		//用量
	private String rate;			//倍率
	
//	private List<BillDetail> details;		//项目详情
	private Double taxRate;			//税率

	private Double wyPrice;//物业费

	private Double btPrice;//本体费

	private Double waterPrice;

	private Double waterPrice1;

	private Double waterPrice2;

	private Double pollutedPrice;

	private Double pollutedPrice1;

	private Double pollutedPrice2;

	private Double rubbishPrice;

	private Double rubbishPrice1;

	private Double rubbishPrice2;

	private Double electPrice;

	public Bill(){}

	public Bill(Double currFee, Double lastBillFee, Double lateFee, Double lastUnPay, Double lastPayed, Double accountBalance, Double currBillFee, Double shareFee, Double total, Double currPayed, String lastReading, String totalReading, Double useCount, String rate, Double taxRate, Double wyPrice, Double btPrice, Double waterPrice, Double waterPrice1, Double waterPrice2, Double pollutedPrice, Double pollutedPrice1, Double pollutedPrice2, Double rubbishPrice, Double rubbishPrice1, Double rubbishPrice2, Double electPrice) {
		this.currFee = currFee;
		this.lastBillFee = lastBillFee;
		this.lateFee = lateFee;
		this.lastUnPay = lastUnPay;
		this.lastPayed = lastPayed;
		this.accountBalance = accountBalance;
		this.currBillFee = currBillFee;
		this.shareFee = shareFee;
		this.total = total;
		this.currPayed = currPayed;
		this.lastReading = lastReading;
		this.totalReading = totalReading;
		this.useCount = useCount;
		this.rate = rate;
		this.taxRate = taxRate;
		this.wyPrice = wyPrice;
		this.btPrice = btPrice;
		this.waterPrice = waterPrice;
		this.waterPrice1 = waterPrice1;
		this.waterPrice2 = waterPrice2;
		this.pollutedPrice = pollutedPrice;
		this.pollutedPrice1 = pollutedPrice1;
		this.pollutedPrice2 = pollutedPrice2;
		this.rubbishPrice = rubbishPrice;
		this.rubbishPrice1 = rubbishPrice1;
		this.rubbishPrice2 = rubbishPrice2;
		this.electPrice = electPrice;
	}

	public Double getCurrFee() {
		return currFee;
	}

	public void setCurrFee(Double currFee) {
		this.currFee = currFee;
	}

	public Double getLastBillFee() {
		return lastBillFee;
	}

	public void setLastBillFee(Double lastBillFee) {
		this.lastBillFee = lastBillFee;
	}

	public Double getLateFee() {
		return lateFee;
	}

	public void setLateFee(Double lateFee) {
		this.lateFee = lateFee;
	}

	public Double getLastUnPay() {
		return lastUnPay;
	}

	public void setLastUnPay(Double lastUnPay) {
		this.lastUnPay = lastUnPay;
	}

	public Double getLastPayed() {
		return lastPayed;
	}

	public void setLastPayed(Double lastPayed) {
		this.lastPayed = lastPayed;
	}

	public Double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public Double getCurrBillFee() {
		return currBillFee;
	}

	public void setCurrBillFee(Double currBillFee) {
		this.currBillFee = currBillFee;
	}

	public Double getShareFee() {
		return shareFee;
	}

	public void setShareFee(Double shareFee) {
		this.shareFee = shareFee;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getCurrPayed() {
		return currPayed;
	}

	public void setCurrPayed(Double currPayed) {
		this.currPayed = currPayed;
	}

	public String getLastReading() {
		return lastReading;
	}

	public void setLastReading(String lastReading) {
		this.lastReading = lastReading;
	}

	public String getTotalReading() {
		return totalReading;
	}

	public void setTotalReading(String totalReading) {
		this.totalReading = totalReading;
	}

	public Double getUseCount() {
		return useCount;
	}

	public void setUseCount(Double useCount) {
		this.useCount = useCount;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Double getWyPrice() {
		return wyPrice;
	}

	public void setWyPrice(Double wyPrice) {
		this.wyPrice = wyPrice;
	}

	public Double getBtPrice() {
		return btPrice;
	}

	public void setBtPrice(Double btPrice) {
		this.btPrice = btPrice;
	}

	public Double getWaterPrice() {
		return waterPrice;
	}

	public void setWaterPrice(Double waterPrice) {
		this.waterPrice = waterPrice;
	}

	public Double getWaterPrice1() {
		return waterPrice1;
	}

	public void setWaterPrice1(Double waterPrice1) {
		this.waterPrice1 = waterPrice1;
	}

	public Double getWaterPrice2() {
		return waterPrice2;
	}

	public void setWaterPrice2(Double waterPrice2) {
		this.waterPrice2 = waterPrice2;
	}





	public Double getRubbishPrice() {
		return rubbishPrice;
	}

	public void setRubbishPrice(Double rubbishPrice) {
		this.rubbishPrice = rubbishPrice;
	}

	public Double getRubbishPrice1() {
		return rubbishPrice1;
	}

	public void setRubbishPrice1(Double rubbishPrice1) {
		this.rubbishPrice1 = rubbishPrice1;
	}

	public Double getRubbishPrice2() {
		return rubbishPrice2;
	}

	public void setRubbishPrice2(Double rubbishPrice2) {
		this.rubbishPrice2 = rubbishPrice2;
	}

	public Double getElectPrice() {
		return electPrice;
	}

	public void setElectPrice(Double electPrice) {
		this.electPrice = electPrice;
	}

	public Double getPollutedPrice() {
		return pollutedPrice;
	}

	public void setPollutedPrice(Double pollutedPrice) {
		this.pollutedPrice = pollutedPrice;
	}

	public Double getPollutedPrice1() {
		return pollutedPrice1;
	}

	public void setPollutedPrice1(Double pollutedPrice1) {
		this.pollutedPrice1 = pollutedPrice1;
	}

	public Double getPollutedPrice2() {
		return pollutedPrice2;
	}

	public void setPollutedPrice2(Double pollutedPrice2) {
		this.pollutedPrice2 = pollutedPrice2;
	}

	@Override
	public String toString() {
		return "Bill{" +
				"currFee=" + currFee +
				", lastBillFee=" + lastBillFee +
				", lateFee=" + lateFee +
				", lastUnPay=" + lastUnPay +
				", lastPayed=" + lastPayed +
				", accountBalance=" + accountBalance +
				", currBillFee=" + currBillFee +
				", shareFee=" + shareFee +
				", total=" + total +
				", currPayed=" + currPayed +
				", lastReading=" + lastReading +
				", totalReading=" + totalReading +
				", useCount=" + useCount +
				", rate=" + rate +
				", taxRate=" + taxRate +
				", wyPrice=" + wyPrice +
				", btPrice=" + btPrice +
				", waterPrice=" + waterPrice +
				", waterPrice1=" + waterPrice1 +
				", waterPrice2=" + waterPrice2 +
				", pollutedPrice=" + pollutedPrice +
				", pollutedPrice1=" + pollutedPrice1 +
				", pollutedPrice2=" + pollutedPrice2 +
				", rubbishPrice=" + rubbishPrice +
				", rubbishPrice1=" + rubbishPrice1 +
				", rubbishPrice2=" + rubbishPrice2 +
				", electPrice=" + electPrice +
				'}';
	}
}
