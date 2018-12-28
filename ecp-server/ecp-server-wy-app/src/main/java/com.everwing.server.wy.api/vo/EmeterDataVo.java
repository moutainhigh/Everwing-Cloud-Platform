package com.everwing.server.wy.api.vo;

import java.io.Serializable;

/**
 * 电表抄表数据
 * Created by zhugeruifei on 17/8/12.
 */
public class EmeterDataVo extends MeterDataVo implements Serializable {

    private static final long serialVersionUID = 3252665066506838706L;
    /*上月总读数*/
    private String lastMonthData;

    /*上月峰值读数*/
    private String lastPeakReading;

    /*上月谷值读数*/
    private String lastVallyReading;

    /*上月平均读数*/
    private String lastCommonReading;

    public EmeterDataVo() {
    }

    public EmeterDataVo(String lastMonthData, String lastPeakReading, String lastVallyReading, String lastCommonReading) {
        this.lastMonthData = lastMonthData;
        this.lastPeakReading = lastPeakReading;
        this.lastVallyReading = lastVallyReading;
        this.lastCommonReading = lastCommonReading;
    }

    public String getLastMonthData() {
        return lastMonthData;
    }

    public void setLastMonthData(String lastMonthData) {
        this.lastMonthData = lastMonthData;
    }

    public String getLastPeakReading() {
        return lastPeakReading;
    }

    public void setLastPeakReading(String lastPeakReading) {
        this.lastPeakReading = lastPeakReading;
    }

    public String getLastVallyReading() {
        return lastVallyReading;
    }

    public void setLastVallyReading(String lastVallyReading) {
        this.lastVallyReading = lastVallyReading;
    }

    public String getLastCommonReading() {
        return lastCommonReading;
    }

    public void setLastCommonReading(String lastCommonReading) {
        this.lastCommonReading = lastCommonReading;
    }

    @Override
    public String toString() {
        return "EmeterDataVo{" +
                "lastMonthData='" + lastMonthData + '\'' +
                ", lastPeakReading='" + lastPeakReading + '\'' +
                ", lastVallyReading='" + lastVallyReading + '\'' +
                ", lastCommonReading='" + lastCommonReading + '\'' +
                '}';
    }
}
