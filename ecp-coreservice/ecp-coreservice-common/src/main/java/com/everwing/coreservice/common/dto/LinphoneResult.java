package com.everwing.coreservice.common.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.everwing.coreservice.common.constant.ResponseCode;
import com.everwing.coreservice.common.constant.ReturnCode;

import java.io.Serializable;

public class LinphoneResult implements Serializable{

    private static final long serialVersionUID = 32341L;

    private String code;

    private String message;

    private Object data;

    public LinphoneResult(RemoteModelResult remoteModelResult){
        this.code=remoteModelResult.getCode();
        this.message=remoteModelResult.getMsg();
        this.data=remoteModelResult.getModel();
    }

    public LinphoneResult(){
        ResponseCode responseCode=ResponseCode.RESOLVE_SUCCESS;
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public LinphoneResult(ReturnCode returnCode){
        this.code=returnCode.getCode();
        this.message=returnCode.getDescription();
    }

    public LinphoneResult(Object data) {
        ResponseCode responseCode=ResponseCode.RESOLVE_SUCCESS;
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    public LinphoneResult(ResponseCode responseCode, Object data){
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    public LinphoneResult(ResponseCode responseCode){
        this.code=responseCode.getCode();
        this.message=responseCode.getMessage();
    }
    @JSONField(serialize = false)
    public boolean isSuccess(){
        return this.code.equals(ReturnCode.API_RESOLVE_SUCCESS.getCode());
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
