package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.wy.fee.constant.ChargingType;

import java.io.Serializable;

/**
 * 账单详情
 *
 * @author DELL shiny
 * @create 2018/6/27
 */
public class BillDetailDto implements Serializable {

    private String postCode;

    private String logoPath;

    private String fullName;

    private String houseCode;

    private String custName;

    private String billCode;

    private String billGenTime;

    private String isCollection;

    private String area;

    private String lastTotalBill;

    private String currentTotalBill;

    private String totalLateFee;

    private String currentBilling;

    private BillDto wy;

    private BillDto bt;

    private BillDto water;

    private BillDto elect;

    public String getPostCode() {
        return postCode;
    }

    /**
     * 邮编
     * @param postCode
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getLogoPath() {
        return logoPath;
    }

    /**
     * 二维码path
     * @param logoPath
     */
    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getFullName() {
        return fullName;
    }

    /**
     * 建筑全名
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHouseCode() {
        return houseCode;
    }

    /**
     * houseCode
     * @param houseCode
     */
    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getCustName() {
        return custName;
    }

    /**
     * 客户姓名
     * @param custName
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getBillCode() {
        return billCode;
    }

    /**
     * 账单编号
     * @param billCode
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getBillGenTime() {
        return billGenTime;
    }

    /**
     * 账单生成日期
     * @param billGenTime
     */
    public void setBillGenTime(String billGenTime) {
        this.billGenTime = billGenTime;
    }

    public String getIsCollection() {
        return isCollection;
    }

    /**
     * 是否托收
     * @param isCollection
     */
    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    public String getArea() {
        return area;
    }

    /**
     * 房屋面积
     * @param area
     */
    public void setArea(String area) {
        this.area = area;
    }

    public String getLastTotalBill() {
        return lastTotalBill;
    }

    /**
     * 历史未付金额
     * @param lastTotalBill
     */
    public void setLastTotalBill(String lastTotalBill) {
        this.lastTotalBill = lastTotalBill;
    }

    public String getCurrentTotalBill() {
        return currentTotalBill;
    }

    /**
     * 本月应付金额
     * @param currentTotalBill
     */
    public void setCurrentTotalBill(String currentTotalBill) {
        this.currentTotalBill = currentTotalBill;
    }

    public String getTotalLateFee() {
        return totalLateFee;
    }

    /**
     * 违约金
     * @param totalLateFee
     */
    public void setTotalLateFee(String totalLateFee) {
        this.totalLateFee = totalLateFee;
    }

    public String getCurrentBilling() {
        return currentBilling;
    }

    /**
     * 累计应付金额
     * @param currentBilling
     */
    public void setCurrentBilling(String currentBilling) {
        this.currentBilling = currentBilling;
    }

    public BillDto getWy() {
        return wy;
    }

    public void setWy(BillDto wy) {
        this.wy = wy;
    }

    public BillDto getBt() {
        return bt;
    }

    public void setBt(BillDto bt) {
        this.bt = bt;
    }

    public BillDto getWater() {
        return water;
    }

    public void setWater(BillDto water) {
        this.water = water;
    }

    public BillDto getElect() {
        return elect;
    }

    public void setElect(BillDto elect) {
        this.elect = elect;
    }

    public BillDto getBillDtoByChargeType(int chargeType) {
        BillDto ret = null;
        if(ChargingType.getChargingTypeByCode(chargeType).getCode() == ChargingType.WY.getCode()) {
            ret = this.wy;
        }
        if(ChargingType.getChargingTypeByCode(chargeType).getCode() == ChargingType.BT.getCode()) {
            ret = this.bt;
        }
        if(ChargingType.getChargingTypeByCode(chargeType).getCode() == ChargingType.WATER.getCode()) {
            ret = this.water;
        }
        if(ChargingType.getChargingTypeByCode(chargeType).getCode() == ChargingType.ELECT.getCode()) {
            ret = this.elect;
        }
        return ret;
    }
}
