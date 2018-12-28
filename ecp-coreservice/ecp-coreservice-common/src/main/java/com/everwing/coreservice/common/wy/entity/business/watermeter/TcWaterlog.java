package com.everwing.coreservice.common.wy.entity.business.watermeter;
import com.everwing.coreservice.common.Page;

import java.util.Date;

/**
 * @ClassName: 水表操作记录
 * @Author:Ck
 * @Date: 2018/11/19
 **/


public class TcWaterlog  implements java.io.Serializable {
    private static final long serialVersionUID = 3465527223392207228L;

    /** 主键 **/
    private String id;

    /** 水表ID **/
    private  String waterId;

    /** 水表编码 **/
    private  String code;

    /** 操作人 **/
    private  String operator;

    /** 操作时间 **/
    private Date modifyTime;

    /** 操作类型 **/
    private  String modifyType;

    /** 修改字段 **/
    private String modifyMatter;

    private Page page;

    private TcWaterMeter  tcWaterMeter;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWaterId() {
        return waterId;
    }

    public void setWaterId(String waterId) {
        this.waterId = waterId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyType() {
        return modifyType;
    }

    public void setModifyType(String modifyType) {
        this.modifyType = modifyType;
    }

    public String getModifyMatter() {
        return modifyMatter;
    }

    public void setModifyMatter(String modifyMatter) {
        this.modifyMatter = modifyMatter;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public TcWaterMeter getTcWaterMeter() {
        return tcWaterMeter;
    }

    public void setTcWaterMeter(TcWaterMeter tcWaterMeter) {
        this.tcWaterMeter = tcWaterMeter;
    }

    @Override
    public String toString() {
        return "TcWaterlog{" +
                "id='" + id + '\'' +
                ", waterId='" + waterId + '\'' +
                ", code='" + code + '\'' +
                ", operator='" + operator + '\'' +
                ", modifyTime=" + modifyTime +
                ", modifyType='" + modifyType + '\'' +
                ", modifyMatter='" + modifyMatter + '\'' +
                ", page=" + page +
                ", tcWaterMeter=" + tcWaterMeter +
                '}';
    }
}
