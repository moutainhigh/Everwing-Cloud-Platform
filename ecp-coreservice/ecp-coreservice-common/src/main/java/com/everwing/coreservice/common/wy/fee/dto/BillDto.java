package com.everwing.coreservice.common.wy.fee.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 子项
 *
 * @author DELL shiny
 * @create 2018/6/27
 */
public class BillDto implements Serializable {

    private String currFee;

    private String lastUnPay;

    private String lateFee;

    private String shareFee;

    private BigDecimal price;

    private String total;

    private String lastReading;

    private String totalReading;

    private String rate;

    private String useCount;

    private String waterPrice;

    private String waterPrice1;

    private String waterPrice2;

    private String rubbishPrice;

    private String rubbishPrice1;

    private String rubbishPrice2;

    private String pollutedPrice;

    private String pollutedPrice1;

    private String pollutedPrice2;
    
    private String electPrice;

    public String getCurrFee() {
        return currFee;
    }

    /**
     * 本期费用
     * @param currFee
     */
    public void setCurrFee(String currFee) {
        this.currFee = currFee;
    }

    public String getLastUnPay() {
        return lastUnPay;
    }

    /**
     * 历史未付
     * @param lastUnPay
     */
    public void setLastUnPay(String lastUnPay) {
        this.lastUnPay = lastUnPay;
    }

    public String getLateFee() {
        return lateFee;
    }

    /**
     * 违约金
     * @param lateFee
     */
    public void setLateFee(String lateFee) {
        this.lateFee = lateFee;
    }

    public String getShareFee() {
        return shareFee;
    }

    /**
     * 分摊金额
     * @param shareFee
     */
    public void setShareFee(String shareFee) {
        this.shareFee = shareFee;
    }

    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 单价
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getLastReading() {
        return lastReading;
    }

    /**
     * 上期读数
     * @param lastReading
     */
    public void setLastReading(String lastReading) {
        this.lastReading = lastReading;
    }

    public String getTotalReading() {
        return totalReading;
    }

    /**
     * 本期读数
     * @param totalReading
     */
    public void setTotalReading(String totalReading) {
        this.totalReading = totalReading;
    }

    public String getRate() {
        return rate;
    }

    /**
     * 倍率
     * @param rate
     */
    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUseCount() {
        return useCount;
    }

    /**
     * 用量
     * @param useCount
     */
    public void setUseCount(String useCount) {
        this.useCount = useCount;
    }

    public String getWaterPrice() {
        return waterPrice;
    }

    /**
     * 标准水费
     * @param waterPrice
     */
    public void setWaterPrice(String waterPrice) {
        this.waterPrice = waterPrice;
    }

    public String getWaterPrice1() {
        return waterPrice1;
    }

    /**
     * 超额水费1
     * @param waterPrice1
     */
    public void setWaterPrice1(String waterPrice1) {
        this.waterPrice1 = waterPrice1;
    }

    public String getWaterPrice2() {
        return waterPrice2;
    }

    /**
     * 超额水费2
     * @param waterPrice2
     */
    public void setWaterPrice2(String waterPrice2) {
        this.waterPrice2 = waterPrice2;
    }

    public String getRubbishPrice() {
        return rubbishPrice;
    }

    /**
     * 垃圾处理费
     * @param rubbishPrice
     */
    public void setRubbishPrice(String rubbishPrice) {
        this.rubbishPrice = rubbishPrice;
    }

    public String getRubbishPrice1() {
        return rubbishPrice1;
    }

    /**
     * 垃圾处理费1
     * @param rubbishPrice1
     */
    public void setRubbishPrice1(String rubbishPrice1) {
        this.rubbishPrice1 = rubbishPrice1;
    }

    public String getRubbishPrice2() {
        return rubbishPrice2;
    }

    /**
     * 垃圾处理费2
     * @param rubbishPrice2
     */
    public void setRubbishPrice2(String rubbishPrice2) {
        this.rubbishPrice2 = rubbishPrice2;
    }

    public String getPollutedPrice() {
        return pollutedPrice;
    }

    /**
     * 污水处理费
     * @param pollutedPrice
     */
    public void setPollutedPrice(String pollutedPrice) {
        this.pollutedPrice = pollutedPrice;
    }

    public String getPollutedPrice1() {
        return pollutedPrice1;
    }

    /**
     * 污水处理费1
     * @param pollutedPrice1
     */
    public void setPollutedPrice1(String pollutedPrice1) {
        this.pollutedPrice1 = pollutedPrice1;
    }

    public String getPollutedPrice2() {
        return pollutedPrice2;
    }

    /**
     * 污水处理费2
     * @param pollutedPrice2
     */
    public void setPollutedPrice2(String pollutedPrice2) {
        this.pollutedPrice2 = pollutedPrice2;
    }

    public String getTotal() {
        return total;
    }

    /**
     * 小计
     * @param total
     */
    public void setTotal(String total) {
        this.total = total;
    }

	public String getElectPrice() {
		return electPrice;
	}

	public void setElectPrice(String electPrice) {
		this.electPrice = electPrice;
	}

}
