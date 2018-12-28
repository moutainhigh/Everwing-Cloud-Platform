package com.everwing.coreservice.common.platform.entity.extra;

import com.alibaba.fastjson.JSONObject;

/**
 * 邻音统一请求格式
 */
public class LinPhoneRequest {

    private JSONObject data;

    private String token;

    private String sign;

    private String timestamp;

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
