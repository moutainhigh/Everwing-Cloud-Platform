package com.everwing.coreservice.common.wy.dto;

import java.io.Serializable;

/**
 * Created by zhugeruifei on 17/8/28.
 */
public class GatingDTO implements Serializable{

    private static final long serialVersionUID = 1535217483004956421L;


    private String accountName;

    private String gatingCode;

    private String password;

    private Integer online;

    private String address;

    private String gatingId;

    private String version;

    public GatingDTO() {
    }

    public GatingDTO(String accountName, String gatingCode, String password, Integer online, String address, String gatingId, String version) {
        this.accountName = accountName;
        this.gatingCode = gatingCode;
        this.password = password;
        this.online = online;
        this.address = address;
        this.gatingId = gatingId;
        this.version = version;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getGatingCode() {
        return gatingCode;
    }

    public void setGatingCode(String gatingCode) {
        this.gatingCode = gatingCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGatingId() {
        return gatingId;
    }

    public void setGatingId(String gatingId) {
        this.gatingId = gatingId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "GatingVo{" +
                "accountName='" + accountName + '\'' +
                ", gatingCode='" + gatingCode + '\'' +
                ", password='" + password + '\'' +
                ", online=" + online +
                ", address='" + address + '\'' +
                ", gatingId='" + gatingId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
