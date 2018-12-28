package com.everwing.coreservice.common.wy.charging.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TJfChargingItemTotalInfo {
    private String id;

    /**
     *计费项id  关联计费项信息表
     */
    private String chargingItemId;

    /**
     *计费项名称
     */
    private String chargingItemName;

    /**
     *计费类型  1 物业   2 本体 3 水费  4 电费  5 污水处理费   6 垃圾处理费等等，后面有新增继续递增就好
     */
    private Integer chargingType;

    /**
     *开始计费时间是对项目可开始计费时间的描述，实际计费时间不一定是这一天
     */
    private Date startChargingTime;

    /**
     *计费状态  1  未计费  2 计费中  3 已计费  4 计费失败
     */
    private Integer chargingStatus;

    /**
     *审核状态   1 待审核   2审核中  3 审核完成   4 审核失败
     */
    private Integer auditStatus;

    /**
     *抵扣状态 1 未抵扣  2 抵扣中  3 抵扣成功  4 抵扣失败
     */
    private Integer deductStatus;

    /**
     *计费时间
     */
    private Date chargingTime;

    /**
     *审核时间
     */
    private Date auditTime;

    /**
     *抵扣时间
     */
    private Date deductTime;

    /**
     *本期计费总额
     */
    private BigDecimal currentChargingTotal;

    /**
     *本期抵扣总额
     */
    private BigDecimal currentDeductTotal;

    /**
     *上期欠费总额
     */
    private BigDecimal lastArrearsTotal;

    /**
     *计费次数
     */
    private Integer chargingTimes;

    /**
     *上期计费id
     */
    private String lastChargingId;

    /**
     *本期总计费数
     */
    private Integer chargingNum;

    /**
     *项目id
     */
    private String projectId;

    /**
     *项目名称
     */
    private String projectName;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *创建人
     */
    private String createId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChargingItemId() {
        return chargingItemId;
    }

    public void setChargingItemId(String chargingItemId) {
        this.chargingItemId = chargingItemId;
    }

    public String getChargingItemName() {
        return chargingItemName;
    }

    public void setChargingItemName(String chargingItemName) {
        this.chargingItemName = chargingItemName;
    }

    public Integer getChargingType() {
        return chargingType;
    }

    public void setChargingType(Integer chargingType) {
        this.chargingType = chargingType;
    }

    public Date getStartChargingTime() {
        return startChargingTime;
    }

    public void setStartChargingTime(Date startChargingTime) {
        this.startChargingTime = startChargingTime;
    }

    public Integer getChargingStatus() {
        return chargingStatus;
    }

    public void setChargingStatus(Integer chargingStatus) {
        this.chargingStatus = chargingStatus;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getDeductStatus() {
        return deductStatus;
    }

    public void setDeductStatus(Integer deductStatus) {
        this.deductStatus = deductStatus;
    }

    public Date getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(Date chargingTime) {
        this.chargingTime = chargingTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Date getDeductTime() {
        return deductTime;
    }

    public void setDeductTime(Date deductTime) {
        this.deductTime = deductTime;
    }

    public BigDecimal getCurrentChargingTotal() {
        return currentChargingTotal;
    }

    public void setCurrentChargingTotal(BigDecimal currentChargingTotal) {
        this.currentChargingTotal = currentChargingTotal;
    }

    public BigDecimal getCurrentDeductTotal() {
        return currentDeductTotal;
    }

    public void setCurrentDeductTotal(BigDecimal currentDeductTotal) {
        this.currentDeductTotal = currentDeductTotal;
    }

    public BigDecimal getLastArrearsTotal() {
        return lastArrearsTotal;
    }

    public void setLastArrearsTotal(BigDecimal lastArrearsTotal) {
        this.lastArrearsTotal = lastArrearsTotal;
    }

    public Integer getChargingTimes() {
        return chargingTimes;
    }

    public void setChargingTimes(Integer chargingTimes) {
        this.chargingTimes = chargingTimes;
    }

    public String getLastChargingId() {
        return lastChargingId;
    }

    public void setLastChargingId(String lastChargingId) {
        this.lastChargingId = lastChargingId;
    }

    public Integer getChargingNum() {
        return chargingNum;
    }

    public void setChargingNum(Integer chargingNum) {
        this.chargingNum = chargingNum;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }
}