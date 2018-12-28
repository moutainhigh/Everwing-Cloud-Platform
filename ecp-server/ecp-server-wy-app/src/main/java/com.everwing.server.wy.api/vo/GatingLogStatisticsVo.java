package com.everwing.server.wy.api.vo;

import java.io.Serializable;

/**
 * 门控机日志统计
 * Created by zhugeruifei on 17/8/29.
 */
public class GatingLogStatisticsVo implements Serializable{

    private static final long serialVersionUID = 7156375515785179742L;

    private String logDate;

    private int invitationCount;

    private int passwordCount;

    private int scanCount;

    private int callCount;

    private int onekeyCount;

    private int totalCount;

    public GatingLogStatisticsVo() {
    }

    public GatingLogStatisticsVo(String logDate, int invitationCount, int passwordCount, int scanCount, int callCount, int onekeyCount, int totalCount) {
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
        return "GatingLogStatisticsVo{" +
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
