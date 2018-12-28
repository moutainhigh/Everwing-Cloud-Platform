package com.everwing.server.wy.api.vo;

import java.io.Serializable;

/**
 * 水表抄表数据
 * Created by zhugeruifei on 17/8/12.
 */
public class WmeterDataVo extends MeterDataVo implements Serializable {


    private static final long serialVersionUID = 1336028141890831576L;
    /*上月读数*/
    private String lastMonthData;

    private String totalReading;

    public WmeterDataVo() {
    }

    public WmeterDataVo(String lastMonthData, String totalReading) {
        this.lastMonthData = lastMonthData;
        this.totalReading = totalReading;
    }

    public String getLastMonthData() {
        return lastMonthData;
    }

    public void setLastMonthData(String lastMonthData) {
        this.lastMonthData = lastMonthData;
    }

    public String getTotalReading() {
        return totalReading;
    }

    public void setTotalReading(String totalReading) {
        this.totalReading = totalReading;
    }

    @Override
    public String toString() {
        return "WmeterDataVo{" +
                "lastMonthData='" + lastMonthData + '\'' +
                ", totalReading='" + totalReading + '\'' +
                '}';
    }
}
