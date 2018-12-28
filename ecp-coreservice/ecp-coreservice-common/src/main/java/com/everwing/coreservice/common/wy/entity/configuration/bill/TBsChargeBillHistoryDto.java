package com.everwing.coreservice.common.wy.entity.configuration.bill;

import java.io.Serializable;
import java.util.Date;

/**
 * 导出缴费报表计费实体类
 */
public class TBsChargeBillHistoryDto implements Serializable {
    private static final long serialVersionUID = 3268255898388601646L;
    //资产编号
    private  String buildingCode;
    // 物业金额
    private  Double wyamount;
    //本体金额
    private  Double btamount;
    //水费计费
    private  Double wateramount;
    private  Double electamount;
    private String readTime;

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public Double getWyamount() {
        return wyamount;
    }

    public void setWyamount(Double wyamount) {
        this.wyamount = wyamount;
    }

    public Double getBtamount() {
        return btamount;
    }

    public void setBtamount(Double btamount) {
        this.btamount = btamount;
    }

    public Double getWateramount() {
        return wateramount;
    }

    public void setWateramount(Double wateramount) {
        this.wateramount = wateramount;
    }

    public Double getElectamount() {
        return electamount;
    }

    public void setElectamount(Double electamount) {
        this.electamount = electamount;
    }
}
