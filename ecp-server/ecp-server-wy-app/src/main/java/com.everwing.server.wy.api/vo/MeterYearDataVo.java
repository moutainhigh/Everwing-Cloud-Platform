package com.everwing.server.wy.api.vo;

import java.io.Serializable;

/**
 * 年份查询读数返回VO，水电表通用
 * Created by zhugeruifei on 17/8/14.
 */
public class MeterYearDataVo implements Serializable{

    private static final long serialVersionUID = 5492388643389266443L;

    private String meterCode;

    private String year;

    private String month;

    private String totalReading;

    private String dosage;

    private String correction;

    private String peakReading;

    private String vallyReading;

    private String commonReading;

    public MeterYearDataVo() {
    }

    public MeterYearDataVo(String meterCode, String year, String month, String totalReading, String dosage, String correction, String peakReading, String vallyReading, String commonReading) {
        this.meterCode = meterCode;
        this.year = year;
        this.month = month;
        this.totalReading = totalReading;
        this.dosage = dosage;
        this.correction = correction;
        this.peakReading = peakReading;
        this.vallyReading = vallyReading;
        this.commonReading = commonReading;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMeterCode() {
        return meterCode;
    }

    public void setMeterCode(String meterCode) {
        this.meterCode = meterCode;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTotalReading() {
        return totalReading;
    }

    public void setTotalReading(String totalReading) {
        this.totalReading = totalReading;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getCorrection() {
        return correction;
    }

    public void setCorrection(String correction) {
        this.correction = correction;
    }

    public String getpeakReading() {
        return peakReading;
    }

    public void setpeakReading(String peakReading) {
        this.peakReading = peakReading;
    }

    public String getVallyReading() {
        return vallyReading;
    }

    public void setVallyReading(String vallyReading) {
        this.vallyReading = vallyReading;
    }

    public String getCommonReading() {
        return commonReading;
    }

    public void setCommonReading(String commonReading) {
        this.commonReading = commonReading;
    }

    @Override
    public String toString() {
        return "MeterYearDataVo{" +
                "meterCode='" + meterCode + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", totalReading='" + totalReading + '\'' +
                ", dosage='" + dosage + '\'' +
                ", correction='" + correction + '\'' +
                ", peakReading='" + peakReading + '\'' +
                ", vallyReading='" + vallyReading + '\'' +
                ", commonReading='" + commonReading + '\'' +
                '}';
    }
}
