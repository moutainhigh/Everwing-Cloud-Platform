package com.everwing.coreservice.common.wy.entity.configuration.owed;

import java.io.Serializable;
import java.util.Date;

/**
 * 催缴报表违约金实体类
 */
public class TBsOwedHistoryDto implements Serializable {
    private static final long serialVersionUID = 6152961488898691838L;
    //房屋编号
    private  String buildingCode;
    //物业违约金
    private  Double wyoweamount;
    //本体违约金
    private  Double btoweamount;
    //水费违约金
    private  Double wateroweamount;
    //电费违约金
    private  Double eleoweamount;




    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public Double getWyoweamount() {
        return wyoweamount;
    }

    public void setWyoweamount(Double wyoweamount) {
        this.wyoweamount = wyoweamount;
    }

    public Double getBtoweamount() {
        return btoweamount;
    }

    public void setBtoweamount(Double btoweamount) {
        this.btoweamount = btoweamount;
    }

    public Double getWateroweamount() {
        return wateroweamount;
    }

    public void setWateroweamount(Double wateroweamount) {
        this.wateroweamount = wateroweamount;
    }

    public Double getEleoweamount() {
        return eleoweamount;
    }

    public void setEleoweamount(Double eleoweamount) {
        this.eleoweamount = eleoweamount;
    }
}
