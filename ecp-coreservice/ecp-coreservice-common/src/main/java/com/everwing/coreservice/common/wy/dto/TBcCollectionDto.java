package com.everwing.coreservice.common.wy.dto;

import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 托收DTO
 *
 * @author DELL shiny
 * @create 2017/9/20
 */
public class TBcCollectionDto implements Serializable{
    private String id;

    private String custId;

    private String custName;

    private Integer createBank;

    private String createBankName;

    private String cardNum;

    private String province;

    private String city;

    private Date startTime;

    private String contractNo;

    private String relateBuildingCode;

    private String chargingItems;

    private String itemNames;

    private String attachment;

    private String relateBuildingFullName;

    private Integer status;

    private String projectId;

    private List<UploadFile> files;

    private Page page;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
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
        this.cardNum = cardNum;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

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
        this.contractNo = contractNo;
    }

    public String getRelateBuildingCode() {
        return relateBuildingCode;
    }

    public void setRelateBuildingCode(String relateBuildingCode) {
        this.relateBuildingCode = relateBuildingCode;
    }

    public String getChargingItems() {
        return chargingItems;
    }

    public void setChargingItems(String chargingItems) {
        this.chargingItems = chargingItems;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getRelateBuildingFullName() {
        return relateBuildingFullName;
    }

    public void setRelateBuildingFullName(String relateBuildingFullName) {
        this.relateBuildingFullName = relateBuildingFullName;
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
        this.projectId = projectId;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getCreateBankName() {
        return createBankName;
    }

    public void setCreateBankName(String createBankName) {
        this.createBankName = createBankName;
    }

    public String getItemNames() {
        return itemNames;
    }

    public void setItemNames(String itemNames) {
        this.itemNames = itemNames;
    }

    public List<UploadFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadFile> files) {
        this.files = files;
    }
}
