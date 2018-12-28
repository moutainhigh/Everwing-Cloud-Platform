package com.everwing.coreservice.common.wy.entity.configuration.assetaccount;

import java.io.Serializable;

/**
 * 催缴报表使用的示例类
 */
public class TBsAssetAccountDto  implements Serializable {
    private static final long serialVersionUID = 8924192466189395596L;
    private String buildingCode;
    private Double wyAmount;
    private Double btamount;
    private Double wateramount;
    private Double eleamount;

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public Double getWyAmount() {
        return wyAmount;
    }

    public void setWyAmount(Double wyAmount) {
        this.wyAmount = wyAmount;
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

    public Double getEleamount() {
        return eleamount;
    }

    public void setEleamount(Double eleamount) {
        this.eleamount = eleamount;
    }
}
