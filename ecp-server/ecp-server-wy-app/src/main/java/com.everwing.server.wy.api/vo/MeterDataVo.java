package com.everwing.server.wy.api.vo;

import java.io.Serializable;

/**
 * 抄表数据
 * Created by zhugeruifei on 17/8/12.
 */
public class MeterDataVo implements Serializable {

    private static final long serialVersionUID = -4475228621553238236L;
    /*表编号*/
    private String meterCode;

    /*表描述*/
    private String meterName;

    private String imageUrl;

    public MeterDataVo() {
    }

    public MeterDataVo(String meterCode, String meterName, String imageUrl) {
        this.meterCode = meterCode;
        this.meterName = meterName;
        this.imageUrl = imageUrl;
    }

    public String getMeterCode() {
        return meterCode;
    }

    public void setMeterCode(String meterCode) {
        this.meterCode = meterCode;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "MeterDataVo{" +
                "meterCode='" + meterCode + '\'' +
                ", meterName='" + meterName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }



}
