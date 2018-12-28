package com.everwing.coreservice.common.wy.charging.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TJfChargingScheme {
    private String id;

    /**
     *方案名称
     */
    private String schemeName;

    /**
     *方案类型（1:物业管理费，2：本体基金 3：水费 4：电费  5 垃圾处理费
            6 污水处理费
     */
    private Integer schemeType;

    /**
     *启用状态（0：启用，1停用）
     */
    private Integer isUsed;

    /**
     *计费方式（0：自动，1：手动）
     */
    private Integer chargingType;

    /**
     *违约金计算方式（0：单利，1：复利）
     */
    private BigDecimal proportion;

    /**
     *启用日期 
     */
    private Date startUsingDate;

    /**
     *失效日期
     */
    private Date endUsingDate;

    /**
     *逾期天数
     */
    private Integer overdueStartDates;

    /**
     *违约金计算方式（0：单利，1：复利）
     */
    private Integer calculationType;

    /**
     *计算频率(以月为单位)
     */
    private Integer frequency;

    /**
     *自动计费的会用到的，手动的不影响
     */
    private Integer chargeData;

    private String projectId;

    private String projectName;

    private String createId;

    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public Integer getSchemeType() {
        return schemeType;
    }

    public void setSchemeType(Integer schemeType) {
        this.schemeType = schemeType;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public Integer getChargingType() {
        return chargingType;
    }

    public void setChargingType(Integer chargingType) {
        this.chargingType = chargingType;
    }

    public BigDecimal getProportion() {
        return proportion;
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }

    public Date getStartUsingDate() {
        return startUsingDate;
    }

    public void setStartUsingDate(Date startUsingDate) {
        this.startUsingDate = startUsingDate;
    }

    public Date getEndUsingDate() {
        return endUsingDate;
    }

    public void setEndUsingDate(Date endUsingDate) {
        this.endUsingDate = endUsingDate;
    }

    public Integer getOverdueStartDates() {
        return overdueStartDates;
    }

    public void setOverdueStartDates(Integer overdueStartDates) {
        this.overdueStartDates = overdueStartDates;
    }

    public Integer getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(Integer calculationType) {
        this.calculationType = calculationType;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getChargeData() {
        return chargeData;
    }

    public void setChargeData(Integer chargeData) {
        this.chargeData = chargeData;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
}