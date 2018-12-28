package com.everwing.server.payment.wxminiprogram.vo;

import java.io.Serializable;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-07-22 16:25
 **/

public class WxLoginVo implements Serializable{

    private String userId;

    private String openId;

    private String token;

    public WxLoginVo(String userId, String openId, String token) {
        this.userId = userId;
        this.openId = openId;
        this.token = token;
    }

    public WxLoginVo() { }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "WxLoginVo{" +
                "userId='" + userId + '\'' +
                ", openId='" + openId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
