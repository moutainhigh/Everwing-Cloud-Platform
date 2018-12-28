package com.everwing.coreservice.common.wy.fee.dto;

import com.everwing.coreservice.common.wy.entity.configuration.billmgr.Bill;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账单数据存放
 *
 * @author DELL shiny
 * @create 2018/5/30
 */
public class UtilityBillDto implements Serializable{

	private static final long serialVersionUID = 5963071216186718990L;
	/**
     * 邮编
     */
    private String postCode;
    /**
     * 地址
     */
    private String address;
    /**
     * 新houseCode
     */
    private String houseCodeNew;
    /**
     * logo名称(各公司不同)
     */
    private String logoName;
    /**
     * 账单编号
     */
    private String billNo;
    /**
     * 计费日期
     */
    private String billTime;
    /**
     * 是否托收
     */
    private String isCollection;
    /**
     * 房屋面积
     */
    private Double area;
    /**
     * 历史未付
     */
    private BigDecimal unPayHistory;
    /**
     * 本月应付
     */
    private BigDecimal needPay;
    /**
     * 违约金
     */
    private BigDecimal lateFee;
    /**
     * 累计应付
     */
    private BigDecimal needPayAll;
    /**
     * 物业
     */
    private Bill wy;
    /**
     *本体
     */
    private Bill bt;
    /**
     *水费
     */
    private Bill water;
    /**
     *电费
     */
    private Bill elect;

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public String getLogoName() {
        return logoName;
    }

    public void setLogoName(String logoName) {
        this.logoName = logoName;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillTime() {
        return billTime;
    }

    public void setBillTime(String billTime) {
        this.billTime = billTime;
    }

    public String getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public BigDecimal getUnPayHistory() {
        return unPayHistory;
    }

    public void setUnPayHistory(BigDecimal unPayHistory) {
        this.unPayHistory = unPayHistory;
    }

    public BigDecimal getNeedPay() {
        return needPay;
    }

    public void setNeedPay(BigDecimal needPay) {
        this.needPay = needPay;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    public BigDecimal getNeedPayAll() {
        return needPayAll;
    }

    public void setNeedPayAll(BigDecimal needPayAll) {
        this.needPayAll = needPayAll;
    }

    public Bill getWy() {
        return wy;
    }

    public void setWy(Bill wy) {
        this.wy = wy;
    }

    public Bill getBt() {
        return bt;
    }

    public void setBt(Bill bt) {
        this.bt = bt;
    }

    public Bill getWater() {
        return water;
    }

    public void setWater(Bill water) {
        this.water = water;
    }

    public Bill getElect() {
        return elect;
    }

    public void setElect(Bill elect) {
        this.elect = elect;
    }
}
