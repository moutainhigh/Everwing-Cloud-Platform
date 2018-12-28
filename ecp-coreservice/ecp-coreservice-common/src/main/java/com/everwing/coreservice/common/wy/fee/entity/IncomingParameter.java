package com.everwing.coreservice.common.wy.fee.entity;

import com.everwing.coreservice.common.Page;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 账户管理选择条件实体方法
 */
public class IncomingParameter implements  Serializable {


    private static final long serialVersionUID = -7149763073974927678L;
    private  String projectId;// 项目id
    private  Integer accountType; //计费项 6.为押金
    private  Integer chargeType;// 收费类型
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startTime;//开始时间
    private  Integer payType;//付款方式
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endTime;//结束时间
    private Integer businessType; //业务类型
    private String projectProductName; //产品名称
    private String name; //押金名
    private String  payTypes;//产品类收费类型
    private  String accountId ;//项目id
    private String companyId;//公司名
    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getProjectProductName() {
        return projectProductName;
    }

    public void setProjectProductName(String projectProductName) {
        this.projectProductName = projectProductName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(String payTypes) {
        this.payTypes = payTypes;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }



    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
