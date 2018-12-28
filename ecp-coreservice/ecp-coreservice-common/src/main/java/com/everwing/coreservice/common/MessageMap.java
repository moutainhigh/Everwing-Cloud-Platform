/**
 * Project Name:webTYJ
 * File Name:MessageMap.java
 * Package Name:com.everwing.common
 * Date:2016年12月29日上午8:26:07
 * Copyright (c) 2016, wusongti@lii.com.cn All Rights Reserved.
 */

package com.everwing.coreservice.common;

import com.everwing.coreservice.common.utils.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function: 返回结果消息对象
 * Reason:
 * Date:     2016年12月29日 上午8:26:07
 * @author wust
 */
public class MessageMap implements Serializable {
    private static final long serialVersionUID = 1L;

    public MessageMap(String flag, String message) {
        this.setFlag(flag);
        this.setMessage(message);
    }

    public MessageMap() {
        this.setFlag("");
        this.setMessage("");
    }

    /**
     * Success
     */
    public static final String INFOR_SUCCESS = "SUCCESS";
    /**
     * Error
     */
    public static final String INFOR_ERROR = "ERROR";
    /**
     * Warning
     */
    public static final String INFOR_WARNING = "WARNING";

    /**
     * 返回结果为空
     */
    public static final String EMPTY_RESULT = "Return result is empty";

    /**
     * 结果标记，默认是成功
     */
    private String flag = INFOR_SUCCESS;

    /**
     * 结果消息
     */
    private String message = "";


    /**
     * 可封装的结果对象
     */
    private Object obj;

    /**
     * 需要返回的集合，可封装在这里
     */
    private Map<String, Object> mapMessage = new HashMap<>();
    private List<?> lstSuccessMessage = new ArrayList<>();
    private List<?> lstErrorMessage = new ArrayList<>();
    private List<?> lstWarningMessage = new ArrayList<>();


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        if(org.apache.commons.lang3.StringUtils.isBlank(CommonUtils.null2String(flag))){
            this.flag = INFOR_SUCCESS;
        }else{
            this.flag = flag;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if(org.apache.commons.lang3.StringUtils.isBlank(CommonUtils.null2String(message))){
            this.message = "成功";
        }else{
            this.message = message;
        }
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Map<String, Object> getMapMessage() {
        return mapMessage;
    }

    public void setMapMessage(Map<String, Object> mapMessage) {
        this.mapMessage = mapMessage;
    }

    public List<?> getLstSuccessMessage() {
        return lstSuccessMessage;
    }

    public void setLstSuccessMessage(List<?> lstSuccessMessage) {
        this.lstSuccessMessage = lstSuccessMessage;
    }

    public List<?> getLstErrorMessage() {
        return lstErrorMessage;
    }

    public void setLstErrorMessage(List<?> lstErrorMessage) {
        this.lstErrorMessage = lstErrorMessage;
    }

    public List<?> getLstWarningMessage() {
        return lstWarningMessage;
    }

    public void setLstWarningMessage(List<String> lstWarningMessage) {
        this.lstWarningMessage = lstWarningMessage;
    }

    @Override
    public String toString() {
        return "MessageMap [flag=" + flag + ", message=" + message + ", obj="
                + obj + ", mapMessage=" + mapMessage + ", lstSuccessMessage="
                + lstSuccessMessage + ", lstErrorMessage=" + lstErrorMessage
                + ", lstWarningMessage=" + lstWarningMessage + "]";
    }


}

