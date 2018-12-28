package com.everwing.coreservice.common.wy.charging.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TJfAssetsChargingDetailInfo {
    private String id;

    /**
     *计费项详情id
     */
    private String chargingTotalId;

    /**
     *计费类型  同 计费项类型
     */
    private Integer chargingType;

    /**
     *房屋房号
     */
    private String houseCode;

    /**
     *房屋名称
     */
    private String buildingName;

    /**
     *本期计费金额
     */
    private BigDecimal currentChargingFee;

    /**
     *上期计费id，关联上次的计费详情id
     */
    private String lastChargingId;

    /**
     * 计费状态  1  未计费  2 计费中  3 已计费  4 计费失败
     */
    private Integer chargingStatus;

    /**
     *计费详情（json串格式存储计费的详细明细，单价，规则，面积，各阶梯的金额等等）
     */
    private String chargingDetail;

    /**
     *审核状态   1 待审核   2审核中  3 审核完成   4 审核失败
     */
    private Integer auditStatus;

    /**
     *抵扣状态 1 未抵扣  2 抵扣中  3 抵扣成功  4 抵扣失败
     */
    private Integer deductibleStatus;

    /**
     *计费时间
     */
    private Date chargingTime;

    /**
     *是否生成账单
     */
    private Integer isCreateBill;

    /**
     *本期分摊金额
     */
    private BigDecimal currentShareFee;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *创建人
     */
    private String createOd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChargingTotalId() {
        return chargingTotalId;
    }

    public void setChargingTotalId(String chargingTotalId) {
        this.chargingTotalId = chargingTotalId;
    }

    public Integer getChargingType() {
        return chargingType;
    }

    public void setChargingType(Integer chargingType) {
        this.chargingType = chargingType;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public BigDecimal getCurrentChargingFee() {
        return currentChargingFee;
    }

    public void setCurrentChargingFee(BigDecimal currentChargingFee) {
        this.currentChargingFee = currentChargingFee;
    }

    public String getLastChargingId() {
        return lastChargingId;
    }

    public void setLastChargingId(String lastChargingId) {
        this.lastChargingId = lastChargingId;
    }

    public Integer getChargingStatus() {
        return chargingStatus;
    }

    public void setChargingStatus(Integer chargingStatus) {
        this.chargingStatus = chargingStatus;
    }

    public String getChargingDetail() {
        return chargingDetail;
    }

    public void setChargingDetail(String chargingDetail) {
        this.chargingDetail = chargingDetail;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getDeductibleStatus() {
        return deductibleStatus;
    }

    public void setDeductibleStatus(Integer deductibleStatus) {
        this.deductibleStatus = deductibleStatus;
    }

    public Date getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(Date chargingTime) {
        this.chargingTime = chargingTime;
    }

    public Integer getIsCreateBill() {
        return isCreateBill;
    }

    public void setIsCreateBill(Integer isCreateBill) {
        this.isCreateBill = isCreateBill;
    }

    public BigDecimal getCurrentShareFee() {
        return currentShareFee;
    }

    public void setCurrentShareFee(BigDecimal currentShareFee) {
        this.currentShareFee = currentShareFee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateOd() {
        return createOd;
    }

    public void setCreateOd(String createOd) {
        this.createOd = createOd;
    }
}