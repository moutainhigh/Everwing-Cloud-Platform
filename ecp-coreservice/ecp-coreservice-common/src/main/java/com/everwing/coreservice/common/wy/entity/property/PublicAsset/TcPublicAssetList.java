package com.everwing.coreservice.common.wy.entity.property.PublicAsset;

public class TcPublicAssetList extends  TcPublicAsset{

    private static final long serialVersionUID = 1872341310346318126L;

    /** 资产总数 **/
    private Integer assetAmount;

    /** 自持总数 **/
    private Integer assetHold;

    /** 非自持总数 **/
    private Integer assetIsntHold;

    /** 经营总数 **/
    private Integer assetManage;

    /** 计费总数 **/
    private Integer assetBilling;

    /** 可供水资产 **/
    private Integer assetWater;

    /** 可供电资产 **/
    private Integer assetElectricity;

    public Integer getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(Integer assetAmount) {
        this.assetAmount = assetAmount;
    }

    public Integer getAssetHold() {
        return assetHold;
    }

    public void setAssetHold(Integer assetHold) {
        this.assetHold = assetHold;
    }

    public Integer getAssetIsntHold() {
        return assetIsntHold;
    }

    public void setAssetIsntHold(Integer assetIsntHold) {
        this.assetIsntHold = assetIsntHold;
    }

    public Integer getAssetManage() {
        return assetManage;
    }

    public void setAssetManage(Integer assetManage) {
        this.assetManage = assetManage;
    }

    public Integer getAssetBilling() {
        return assetBilling;
    }

    public void setAssetBilling(Integer assetBilling) {
        this.assetBilling = assetBilling;
    }

    public Integer getAssetWater() {
        return assetWater;
    }

    public void setAssetWater(Integer assetWater) {
        this.assetWater = assetWater;
    }

    public Integer getAssetElectricity() {
        return assetElectricity;
    }

    public void setAssetElectricity(Integer assetElectricity) {
        this.assetElectricity = assetElectricity;
    }

    @Override
    public String toString() {
        return "TcPublicAssetList{" +
                "assetAmount=" + assetAmount +
                ", assetHold=" + assetHold +
                ", assetIsntHold=" + assetIsntHold +
                ", assetManage=" + assetManage +
                ", assetBilling=" + assetBilling +
                ", assetWater=" + assetWater +
                ", assetElectricity=" + assetElectricity +
                '}';
    }
}
