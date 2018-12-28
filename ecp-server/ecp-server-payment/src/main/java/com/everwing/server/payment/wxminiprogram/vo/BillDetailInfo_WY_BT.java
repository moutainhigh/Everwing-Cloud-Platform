package com.everwing.server.payment.wxminiprogram.vo;

import java.io.Serializable;

/**
 * @Author: zgrf
 * @Description: 物业和本体账单明细
 * @Date: Create in 2018-10-19 17:28
 **/

public class BillDetailInfo_WY_BT implements Serializable {

    private String currFee;

    private String lastUnPay;

    private String lateFee;

    private String shareFee;

    private String billMonth;

    public BillDetailInfo_WY_BT() {
    }

    public BillDetailInfo_WY_BT(String currFee, String lastUnPay, String lateFee, String shareFee) {
        this.currFee = currFee;
        this.lastUnPay = lastUnPay;
        this.lateFee = lateFee;
        this.shareFee = shareFee;
    }

    public String getCurrFee() {
        return currFee;
    }

    public void setCurrFee(String currFee) {
        this.currFee = currFee;
    }

    public String getLastUnPay() {
        return lastUnPay;
    }

    public void setLastUnPay(String lastUnPay) {
        this.lastUnPay = lastUnPay;
    }

    public String getLateFee() {
        return lateFee;
    }

    public void setLateFee(String lateFee) {
        this.lateFee = lateFee;
    }

    public String getShareFee() {
        return shareFee;
    }

    public void setShareFee(String shareFee) {
        this.shareFee = shareFee;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    @Override
    public String toString() {
        return "BillDetailInfo_WY_BT{" +
                "currFee='" + currFee + '\'' +
                ", lastUnPay='" + lastUnPay + '\'' +
                ", lateFee='" + lateFee + '\'' +
                ", shareFee='" + shareFee + '\'' +
                '}';
    }
}
