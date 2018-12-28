package com.everwing.coreservice.common.wy.fee.dto;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.wy.fee.entity.AcBillDetail;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shiny
 **/
public class BillDetailPageDto implements Serializable {

    private String id;

    private Date createTime;

    private String billMonth;

    private Integer billState;

    private BillDetailDto billDetail;

    private String houseCodeNew;

    private String accountId;

    private BigDecimal billAmount;

    private String billPayer;

    private String billAddress;

    private Date billInvalid;

    private String projectId;

    private String signature;

    private Integer payState;
    //页面用月份
    private String mon;

    public BillDetailPageDto() {
    }

    public BillDetailPageDto(AcBillDetail acBillDetail){
        this.id=acBillDetail.getId();
        this.createTime=acBillDetail.getCreateTime();
        this.billMonth=acBillDetail.getBillMonth();
        this.billState=acBillDetail.getBillState();
        this.houseCodeNew=acBillDetail.getHouseCodeNew();
        this.accountId=acBillDetail.getAccountId();
        this.billAmount=acBillDetail.getBillAmount();
        this.billPayer=acBillDetail.getBillPayer();
        this.billAddress=acBillDetail.getBillAddress();
        this.billInvalid=acBillDetail.getBillInvalid();
        this.projectId=acBillDetail.getProjectId();
        this.signature=acBillDetail.getSignature();
        this.payState=acBillDetail.getPayState();
        this.mon=acBillDetail.getMon();
        String billDetailStr=acBillDetail.getBillDetail();
        if(StringUtils.isNotEmpty(billDetailStr)) {
            this.billDetail = JSON.parseObject(billDetailStr, BillDetailDto.class);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getBillState() {
        return billState;
    }

    public void setBillState(Integer billState) {
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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
        return "BillDetailPageDto{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", billMonth='" + billMonth + '\'' +
                ", billState=" + billState +
                ", billDetail=" + billDetail +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", accountId='" + accountId + '\'' +
                ", billAmount=" + billAmount +
                ", billPayer='" + billPayer + '\'' +
                ", billAddress='" + billAddress + '\'' +
                ", billInvalid=" + billInvalid +
                ", projectId='" + projectId + '\'' +
                ", signature='" + signature + '\'' +
                ", payState=" + payState +
                '}';
    }

    public static void main(String[] args) {
        AcBillDetail billDetail=new AcBillDetail();
        billDetail.setBillAddress("2018-09");
        billDetail.setBillDetail("{\"postCode\":\"518000\"}");
        BillDetailPageDto billDetailPageDto=new BillDetailPageDto(billDetail);
        System.out.println(billDetailPageDto);
    }
}
