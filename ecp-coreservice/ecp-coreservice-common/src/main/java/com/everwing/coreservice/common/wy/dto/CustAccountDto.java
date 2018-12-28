package com.everwing.coreservice.common.wy.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author shiny
 **/
public class CustAccountDto implements Serializable {

    /**
     * 姓名
     */
    private String name;

    /**
     * 客户属性
     */
    private String custNature;

    /**
     * 注册电话
     */
    private String registerPhone;

    /**
     * 证件号码
     */
    private String cardNum;

    /**
     * 押金
     */
    private BigDecimal deposit;

    /**
     * 账户
     */
    private BigDecimal totalMoney;

    /**
     * 资产套数
     */
    private Integer buildingCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustNature() {
        return custNature;
    }

    public void setCustNature(String custNature) {
        this.custNature = custNature;
    }

    public String getRegisterPhone() {
        return registerPhone;
    }

    public void setRegisterPhone(String registerPhone) {
        this.registerPhone = registerPhone;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getBuildingCount() {
        return buildingCount;
    }

    public void setBuildingCount(Integer buildingCount) {
        this.buildingCount = buildingCount;
    }
}
