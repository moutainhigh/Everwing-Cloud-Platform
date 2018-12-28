package com.everwing.coreservice.common.wy.fee.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-10-18 15:27
 **/

public class BillOfYearDto implements Serializable {

    private String billNumber;

    private String date;

    private BigDecimal billAmount;

    public BillOfYearDto() {
    }

    public BillOfYearDto(String billNumber, String date, BigDecimal billAmount) {
        this.billNumber = billNumber;
        this.date = date;
        this.billAmount = billAmount;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    @Override
    public String toString() {
        return "BillOfYearDto{" +
                "billNumber='" + billNumber + '\'' +
                ", date='" + date + '\'' +
                ", billAmount=" + billAmount +
                '}';
    }
}


