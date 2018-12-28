package com.everwing.coreservice.common.wy.fee.entity;

import java.io.Serializable;

/**
 * 微信小程序绑定po
 */
public class AcWxBinding implements Serializable {

    private String userId;

    private String openId;

    private String mobile;

    private String realName;

    private String identity;

    public AcWxBinding() {
    }

    public AcWxBinding(String userId, String openId, String mobile, String realName, String idnetity) {
        this.userId = userId;
        this.openId = openId;
        this.mobile = mobile;
        this.realName = realName;
        this.identity = identity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "AcWxBinding{" +
                "userId='" + userId + '\'' +
                ", openId='" + openId + '\'' +
                ", mobile='" + mobile + '\'' +
                ", realName='" + realName + '\'' +
                ", identity='" + identity + '\'' +
                '}';
    }
}
