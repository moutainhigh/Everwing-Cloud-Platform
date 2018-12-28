package com.everwing.coreservice.common.wy.dto;

import java.io.Serializable;

/**
 * Created by zhugeruifei on 17/8/29.
 */
public class GatingLogStatisticsDTO implements Serializable{
    private static final long serialVersionUID = -4869516526785598369L;

    private String logDate;

    private int invitationCount;

    private int passwordCount;

    private int scanCount;

    private int callCount;

    private int onekeyCount;

    private int totalCount;

    public GatingLogStatisticsDTO() {
    }

    public GatingLogStatisticsDTO(String logDate, int invitationCount, int passwordCount, int scanCount, int callCount, int onekeyCount, int totalCount) {
        this.logDate = logDate;
        this.invitationCount = invitationCount;
        this.passwordCount = passwordCount;
        this.scanCount = scanCount;
        this.callCount = callCount;
        this.onekeyCount = onekeyCount;
        this.totalCount = totalCount;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public int getInvitationCount() {
        return invitationCount;
    }

    public void setInvitationCount(int invitationCount) {
        this.invitationCount = invitationCount;
    }

    public int getPasswordCount() {
        return passwordCount;
    }

    public void setPasswordCount(int passwordCount) {
        this.passwordCount = passwordCount;
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public int getCallCount() {
        return callCount;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    public int getOnekeyCount() {
        return onekeyCount;
    }

    public void setOnekeyCount(int onekeyCount) {
        this.onekeyCount = onekeyCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "GatingLogStatisticsDTO{" +
                "logDate='" + logDate + '\'' +
                ", invitationCount=" + invitationCount +
                ", passwordCount=" + passwordCount +
                ", scanCount=" + scanCount +
                ", callCount=" + callCount +
                ", onekeyCount=" + onekeyCount +
                ", totalCount=" + totalCount +
                '}';
    }

}
