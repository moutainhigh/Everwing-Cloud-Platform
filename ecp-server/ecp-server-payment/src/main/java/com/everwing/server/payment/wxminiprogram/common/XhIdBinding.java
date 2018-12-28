package com.everwing.server.payment.wxminiprogram.common;

import java.io.Serializable;

/**
 * 信号ID绑定数据
 * 小程序调用接口传入该值
 * 绑定 公司ID，项目ID，商户号，appid，AppSecret
 * @author zhuge
 * @create 2018/6/5
 */
public class XhIdBinding implements Serializable{

    private String xhId;

    private String companyId;

    private String projectId;

    private String merchantNo;

    private String appId;

    //登录获取openId用
    private String appSecret;

    //下单交易用
    private String appKey;

    private String compayName;

    private String projectName;

    private String notifyUrl;

    private String projectCode;

    public XhIdBinding() {
    }

    public XhIdBinding(String xhId, String companyId, String projectId, String merchantNo, String appId, String appSecret) {
        this.xhId = xhId;
        this.companyId = companyId;
        this.projectId = projectId;
        this.merchantNo = merchantNo;
        this.appId = appId;
        this.appSecret = appSecret;
    }

    public String getXhId() {
        return xhId;
    }

    public void setXhId(String xhId) {
        this.xhId = xhId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getCompayName() {
        return compayName;
    }

    public void setCompayName(String compayName) {
        this.compayName = compayName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @Override
    public String toString() {
        return "XhIdBinding{" +
                "xhId='" + xhId + '\'' +
                ", companyId='" + companyId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", merchantNo='" + merchantNo + '\'' +
                ", appId='" + appId + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", appKey='" + appKey + '\'' +
                ", compayName='" + compayName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                '}';
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public XhIdBinding(String xhId, String companyId, String projectId, String merchantNo, String appId, String appSecret, String compayName, String projectName) {
        this.xhId = xhId;
        this.companyId = companyId;
        this.projectId = projectId;
        this.merchantNo = merchantNo;
        this.appId = appId;
        this.appSecret = appSecret;
        this.compayName = compayName;
        this.projectName = projectName;
    }

}
