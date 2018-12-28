package com.everwing.server.wy.api.vo;

import java.io.Serializable;

/**
 * Created by zhugeruifei on 17/8/12.
 */
public class BaseApiVo<T extends Object> implements Serializable {


    private static final long serialVersionUID = 698836803111799063L;
    private String code;

    private String message;

    private  T data;

    public BaseApiVo() {
    }

    public BaseApiVo(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseApiVo{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
