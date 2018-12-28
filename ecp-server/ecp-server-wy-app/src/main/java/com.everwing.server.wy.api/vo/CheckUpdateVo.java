package com.everwing.server.wy.api.vo;

import java.io.Serializable;

/**
 * Created by zhugeruifei on 17/9/18.
 */
public class CheckUpdateVo implements Serializable{
    private static final long serialVersionUID = -6288518898162940424L;

    private String currVersion;

    private String packageUrl;

    private String md5;

    private String status;

    private String versionDescription;

    public CheckUpdateVo() {
    }

    public CheckUpdateVo(String currVersion, String packageUrl, String md5, String status, String versionDescription) {
        this.currVersion = currVersion;
        this.packageUrl = packageUrl;
        this.md5 = md5;
        this.status = status;
        this.versionDescription = versionDescription;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCurrVersion() {
        return currVersion;
    }

    public void setCurrVersion(String currVersion) {
        this.currVersion = currVersion;
    }

    public String getPackageUrl() {
        return packageUrl;
    }

    public void setPackageUrl(String packageUrl) {
        this.packageUrl = packageUrl;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersionDescription() {
        return versionDescription;
    }

    public void setVersionDescription(String versionDescription) {
        this.versionDescription = versionDescription;
    }

    @Override
    public String toString() {
        return "CheckUpdateVo{" +
                "currVersion='" + currVersion + '\'' +
                ", packageUrl='" + packageUrl + '\'' +
                ", md5='" + md5 + '\'' +
                ", status='" + status + '\'' +
                ", versionDescription='" + versionDescription + '\'' +
                '}';
    }
}
