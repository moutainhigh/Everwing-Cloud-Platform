package com.everwing.coreservice.common.wy.entity.account.pay;

import java.io.Serializable;
import java.util.Date;

/**
 * 催缴报表支付金额实体类
 */
public class TBsPayInfoDto implements Serializable {
    private static final long serialVersionUID = -6082916305014213653L;

    private  String buildingCode;
    //物业支付金额
    private  Double wypayN;
    //物业退款金额
    private  Double wypayBack;
    //物业减免金额
    private  Double wypayJM;
    //本体支付金额
    private  Double btpayN;
    //本体退款金额
    private  Double btpayBack;
    //本体减免金额
    private  Double btpayJM;
    //水费支付金额
    private  Double waterpayN;
    //水费退款金额
    private  Double waterpayBack;
    //水费退款金额
    private  Double waterpayJM;
    //电费支付金额
    private  Double electpayN;
    //电费退款金额
    private  Double electpayBack;
    //电费减免金额
    private  Double electpayJM;
    //通用支付金额
    private  Double commonpayN;
    //通用退款金额
    private  Double commonpayBack;

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public Double getWypayN() {
        return wypayN;
    }

    public void setWypayN(Double wypayN) {
        this.wypayN = wypayN;
    }

    public Double getWypayBack() {
        return wypayBack;
    }

    public void setWypayBack(Double wypayBack) {
        this.wypayBack = wypayBack;
    }

    public Double getWypayJM() {
        return wypayJM;
    }

    public void setWypayJM(Double wypayJM) {
        this.wypayJM = wypayJM;
    }

    public Double getBtpayN() {
        return btpayN;
    }

    public void setBtpayN(Double btpayN) {
        this.btpayN = btpayN;
    }

    public Double getBtpayBack() {
        return btpayBack;
    }

    public void setBtpayBack(Double btpayBack) {
        this.btpayBack = btpayBack;
    }

    public Double getBtpayJM() {
        return btpayJM;
    }

    public void setBtpayJM(Double btpayJM) {
        this.btpayJM = btpayJM;
    }

    public Double getWaterpayN() {
        return waterpayN;
    }

    public void setWaterpayN(Double waterpayN) {
        this.waterpayN = waterpayN;
    }

    public Double getWaterpayBack() {
        return waterpayBack;
    }

    public void setWaterpayBack(Double waterpayBack) {
        this.waterpayBack = waterpayBack;
    }

    public Double getWaterpayJM() {
        return waterpayJM;
    }

    public void setWaterpayJM(Double waterpayJM) {
        this.waterpayJM = waterpayJM;
    }

    public Double getElectpayN() {
        return electpayN;
    }

    public void setElectpayN(Double electpayN) {
        this.electpayN = electpayN;
    }

    public Double getElectpayBack() {
        return electpayBack;
    }

    public void setElectpayBack(Double electpayBack) {
        this.electpayBack = electpayBack;
    }

    public Double getElectpayJM() {
        return electpayJM;
    }

    public void setElectpayJM(Double electpayJM) {
        this.electpayJM = electpayJM;
    }

    public Double getCommonpayN() {
        return commonpayN;
    }

    public void setCommonpayN(Double commonpayN) {
        this.commonpayN = commonpayN;
    }

    public Double getCommonpayBack() {
        return commonpayBack;
    }

    public void setCommonpayBack(Double commonpayBack) {
        this.commonpayBack = commonpayBack;
    }
}
