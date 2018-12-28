package com.everwing.coreservice.common.wy.charging.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TJgChargingRuleDetailInfo {
    private String id;

    /**
     *方案id，关联计费方案表
     */
    private String schemeId;

    /**
     *规则名称
     */
    private String ruleName;

    /**
     *同方案类型
     */
    private Integer ruleType;

    /**
     *这个主要针对的是电存在峰谷值也有阶梯计费的情况
             1 标准   2 峰值  3  谷值   4 平 值  。标准的就给水费用，正常的各阶梯公式
            峰值就是峰值的各阶梯
     */
    private Integer isLadder;

    /**
     *标准单价，使用与所有计费项
     */
    private BigDecimal unitPrice;

    /**
     *标准临界值 （如水30吨以内不收费，此值为0）  水电相关才有值
     */
    private BigDecimal cricitcalValue;

    /**
     *标准公式  如  unit_price   *   用量
     */
    private String standardFormula;

    private String formulaDescription;

    /**
     *用于一些固定的系数值  如  0.27 * 0.9 * 1.08 * 用量  0.9这种固定的系数
     */
    private BigDecimal coefficient1;

    /**
     *用于一些固定的系数值  如  0.27 * 0.9 * 1.08 * 用量  0.9这种固定的系数
     */
    private BigDecimal coefficient2;

    private BigDecimal coefficient3;

    /**
     *创建人
     */
    private String createId;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *修改人
     */
    private String updateBy;

    /**
     *修改时间
     */
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getIsLadder() {
        return isLadder;
    }

    public void setIsLadder(Integer isLadder) {
        this.isLadder = isLadder;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getCricitcalValue() {
        return cricitcalValue;
    }

    public void setCricitcalValue(BigDecimal cricitcalValue) {
        this.cricitcalValue = cricitcalValue;
    }

    public String getStandardFormula() {
        return standardFormula;
    }

    public void setStandardFormula(String standardFormula) {
        this.standardFormula = standardFormula;
    }

    public String getFormulaDescription() {
        return formulaDescription;
    }

    public void setFormulaDescription(String formulaDescription) {
        this.formulaDescription = formulaDescription;
    }

    public BigDecimal getCoefficient1() {
        return coefficient1;
    }

    public void setCoefficient1(BigDecimal coefficient1) {
        this.coefficient1 = coefficient1;
    }

    public BigDecimal getCoefficient2() {
        return coefficient2;
    }

    public void setCoefficient2(BigDecimal coefficient2) {
        this.coefficient2 = coefficient2;
    }

    public BigDecimal getCoefficient3() {
        return coefficient3;
    }

    public void setCoefficient3(BigDecimal coefficient3) {
        this.coefficient3 = coefficient3;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}