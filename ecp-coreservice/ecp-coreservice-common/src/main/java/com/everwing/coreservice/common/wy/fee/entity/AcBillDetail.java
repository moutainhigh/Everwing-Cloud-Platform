package com.everwing.coreservice.common.wy.fee.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AcBillDetail implements Serializable {
    private String id;

    private Date createTime;

    private String billMonth;

    private Integer billState;

    private String billDetail;

    private String houseCodeNew;

    private String accountId;

    private BigDecimal billAmount;

    private String billPayer;

    private String billAddress;

    private Date billInvalid;

    private String projectId;

    private String signature;

    private Integer payState;

    private String mon;
    
    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth == null ? null : billMonth.trim();
    }

    public Integer getBillState() {
        return billState;
    }

    public void setBillState(Integer billState) {
        this.billState = billState;
    }

    public String getBillDetail() {
        return billDetail;
    }

    public void setBillDetail(String billDetail) {
        this.billDetail=billDetail;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew == null ? null : houseCodeNew.trim();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillPayer() {
        return billPayer;
    }

    public void setBillPayer(String billPayer) {
        this.billPayer = billPayer == null ? null : billPayer.trim();
    }

    public String getBillAddress() {
        return billAddress;
    }

    public void setBillAddress(String billAddress) {
        this.billAddress = billAddress == null ? null : billAddress.trim();
    }

    public Date getBillInvalid() {
        return billInvalid;
    }

    public void setBillInvalid(Date billInvalid) {
        this.billInvalid = billInvalid;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", createTime=").append(createTime);
        sb.append(", billMonth=").append(billMonth);
        sb.append(", billState=").append(billState);
        sb.append(", billDetail=").append(billDetail);
        sb.append(", houseCodeNew=").append(houseCodeNew);
        sb.append(", accountId=").append(accountId);
        sb.append(", billAmount=").append(billAmount);
        sb.append(", billPayer=").append(billPayer);
        sb.append(", billAddress=").append(billAddress);
        sb.append(", billInvalid=").append(billInvalid);
        sb.append(", projectId=").append(projectId);
        sb.append(", signature=").append(signature);
        sb.append(", payState=").append(payState);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }


}