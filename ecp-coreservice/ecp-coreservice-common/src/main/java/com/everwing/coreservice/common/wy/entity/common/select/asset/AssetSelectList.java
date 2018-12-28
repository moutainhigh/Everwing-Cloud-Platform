package com.everwing.coreservice.common.wy.entity.common.select.asset;/**
 * Created by wust on 2018/12/4.
 */

import java.math.BigDecimal;

/**
 *
 * Function:资产下拉框选择器值对象
 * Reason:
 * Date:2018/12/4
 * @author wusongti@lii.com.cn
 */
public class AssetSelectList implements java.io.Serializable{
    private static final long serialVersionUID = -8620230402043837685L;

    /** 旧资产编码 */
    private String houseCode;

    /** 新资产编码 */
    private String houseCodeNew;

    /** 建筑全名 */
    private String buildingFullName;

    /** 资产地址 */
    private String assetAddress;

    /** 建筑面积 */
    private BigDecimal buildingArea;

    /** 实用面积 */
    private BigDecimal usableArea;

    /** 下拉框值 */
    private String value;

    /** 下拉框label */
    private String label;


    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public String getBuildingFullName() {
        return buildingFullName;
    }

    public void setBuildingFullName(String buildingFullName) {
        this.buildingFullName = buildingFullName;
    }

    public String getAssetAddress() {
        return assetAddress;
    }

    public void setAssetAddress(String assetAddress) {
        this.assetAddress = assetAddress;
    }

    public BigDecimal getBuildingArea() {
        return buildingArea;
    }

    public void setBuildingArea(BigDecimal buildingArea) {
        this.buildingArea = buildingArea;
    }

    public BigDecimal getUsableArea() {
        return usableArea;
    }

    public void setUsableArea(BigDecimal usableArea) {
        this.usableArea = usableArea;
    }

    public String getValue() {
        this.setValue(this.getHouseCodeNew());
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        this.setLabel(this.getBuildingFullName());
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
