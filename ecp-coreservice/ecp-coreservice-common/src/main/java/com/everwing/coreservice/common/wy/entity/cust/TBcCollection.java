package com.everwing.coreservice.common.wy.entity.cust;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class TBcCollection implements Serializable {

    private String id;

    private String custId;

    private String custName;

    private Integer createBank;

    private String cardNum;

    private String province;

    private String city;

    private Date startTime;

    private String contractNo;

    private String relateBuildingCode;

    private String chargingItems;

    private String attachment;

    private String relateBuildingFullName;

    private Integer status;

    private String projectId;

    private Integer cardType;

    private String cardNo;

    private String createBy;

    private String createTime;

    private String updateBy;

    private String updateTime;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId == null ? null : custId.trim();
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public Integer getCreateBank() {
        return createBank;
    }

    public void setCreateBank(Integer createBank) {
        this.createBank = createBank;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum == null ? null : cardNum.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo == null ? null : contractNo.trim();
    }

    public String getRelateBuildingCode() {
        return relateBuildingCode;
    }

    public void setRelateBuildingCode(String relateBuildingCode) {
        this.relateBuildingCode = relateBuildingCode == null ? null : relateBuildingCode.trim();
    }

    public String getChargingItems() {
        return chargingItems;
    }

    public void setChargingItems(String chargingItems) {
        this.chargingItems = chargingItems == null ? null : chargingItems.trim();
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment == null ? null : attachment.trim();
    }

    public String getRelateBuildingFullName() {
        return relateBuildingFullName;
    }

    public void setRelateBuildingFullName(String relateBuildingFullName) {
        this.relateBuildingFullName = relateBuildingFullName == null ? null : relateBuildingFullName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "TBcCollection{" +
                "id='" + id + '\'' +
                ", custId='" + custId + '\'' +
                ", custName='" + custName + '\'' +
                ", createBank=" + createBank +
                ", cardNum='" + cardNum + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", startTime=" + startTime +
                ", contractNo='" + contractNo + '\'' +
                ", relateBuildingCode='" + relateBuildingCode + '\'' +
                ", chargingItems='" + chargingItems + '\'' +
                ", attachment='" + attachment + '\'' +
                ", relateBuildingFullName='" + relateBuildingFullName + '\'' +
                ", status=" + status +
                ", projectId='" + projectId + '\'' +
                ", cardType=" + cardType +
                ", cardNo='" + cardNo + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}