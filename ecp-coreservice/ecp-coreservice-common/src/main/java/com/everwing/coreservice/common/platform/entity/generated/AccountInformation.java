package com.everwing.coreservice.common.platform.entity.generated;

import java.io.Serializable;
import java.util.Date;

public class AccountInformation implements Serializable {
    /** 主键 */
    private String id;

    private String accountid;

    private String accountCode;

    private String cardnum;

    private String name;

    private String mobile;

    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AccountInformation{" +
                "id='" + id + '\'' +
                ", accountid='" + accountid + '\'' +
                ", accountCode='" + accountCode + '\'' +
                ", cardnum='" + cardnum + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
