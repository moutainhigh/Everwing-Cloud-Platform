package com.everwing.coreservice.common.wy.entity.system.code;/**
 * Created by wust on 2018/8/6.
 */

/**
 *
 * Function:系统编码表
 * Reason:
 * Date:2018/8/6
 * @author wusongti@lii.com.cn
 */
public class TSysCode implements java.io.Serializable{
    private static final long serialVersionUID = 8306189530720570194L;
    private String type;
    private String prefix;
    private String suffix;
    private String timestampStr;
    private String code;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTimestampStr() {
        return timestampStr;
    }

    public void setTimestampStr(String timestampStr) {
        this.timestampStr = timestampStr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
