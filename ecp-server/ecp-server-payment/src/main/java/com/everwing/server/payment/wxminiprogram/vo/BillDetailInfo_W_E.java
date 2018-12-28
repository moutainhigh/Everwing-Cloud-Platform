package com.everwing.server.payment.wxminiprogram.vo;

import java.io.Serializable;

/**
 * @Author: zgrf
 * @Description: 水费和电费账单明细
 * @Date: Create in 2018-10-19 17:30
 **/

public class BillDetailInfo_W_E extends  BillDetailInfo_WY_BT implements Serializable {

    private String lastReading;

    private String totalReading;

    private String rate;

    private String useCount;

    public BillDetailInfo_W_E() {
    }

    public BillDetailInfo_W_E(String lastReading, String totalReading, String rate, String useCount) {
        this.lastReading = lastReading;
        this.totalReading = totalReading;
        this.rate = rate;
        this.useCount = useCount;
    }

    public BillDetailInfo_W_E(String currFee, String lastUnPay, String lateFee, String shareFee, String lastReading, String totalReading, String rate, String useCount) {
        super(currFee, lastUnPay, lateFee, shareFee);
        this.lastReading = lastReading;
        this.totalReading = totalReading;
        this.rate = rate;
        this.useCount = useCount;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUseCount() {
        return useCount;
    }

    public void setUseCount(String useCount) {
        this.useCount = useCount;
    }

    @Override
    public String toString() {
        return "BillDetailInfo_W_E{" +
                "lastReading='" + lastReading + '\'' +
                ", totalReading='" + totalReading + '\'' +
                ", rate='" + rate + '\'' +
                ", useCount='" + useCount + '\'' +
                '}';
    }
}
