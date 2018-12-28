package com.everwing.coreservice.common.wy.fee.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author DELL shiny
 * @create 2018/6/8
 */
public class AcBillDetailDto implements Serializable {

	private static final long serialVersionUID = 540026676384349879L;

	private String companyId;

    private Date createTime;

    private String billMonth;

    private int billState;

    private BillDetailDto billDetail;

    private String houseCodeNew;

    private BigDecimal billAmount;

    private String billPayer;

    private String billAddress;

    private Date billInvalid;

    private String projectId;

    private String projectName;

    private int payState;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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
        this.billMonth = billMonth;
    }

    public int getBillState() {
        return billState;
    }

    public void setBillState(int billState) {
        this.billState = billState;
    }

    public BillDetailDto getBillDetail() {
        return billDetail;
    }

    public void setBillDetail(BillDetailDto billDetail) {
        this.billDetail = billDetail;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
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
        this.billPayer = billPayer;
    }

    public String getBillAddress() {
        return billAddress;
    }

    public void setBillAddress(String billAddress) {
        this.billAddress = billAddress;
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
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    @Override
    public String toString() {
        return "AcBillDetailDto{" +
                "companyId='" + companyId + '\'' +
                ", createTime=" + createTime +
                ", billMonth='" + billMonth + '\'' +
                ", billState=" + billState +
                ", billDetail='" + billDetail + '\'' +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", billAmount=" + billAmount +
                ", billPayer='" + billPayer + '\'' +
                ", billAddress='" + billAddress + '\'' +
                ", billInvalid=" + billInvalid +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", payState=" + payState +
                '}';
    }
}
