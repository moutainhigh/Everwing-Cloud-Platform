package com.everwing.coreservice.common.wy.entity.business.electmeter;
import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;

import java.util.Date;

/**
 * @ClassName: 电表操作记录
 * @Author:Ck
 * @Date: 2018/11/19
 **/


public class TcElectlog implements java.io.Serializable {
    private static final long serialVersionUID = 3465527223592227228L;

    /** 主键 **/
    private String id;

    /** 水表ID **/
    private  String electId;

    /** 操作人 **/
    private  String operator;

    /** 操作时间 **/
    private Date modifyTime;

    /** 操作类型 **/
    private  String modifyType;

    /** 修改字段 **/
    private String modifyMatter;

    /** 电表编码 **/
    private String code;

    private Page page;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private TcElectMeter  tcElectMeter;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public TcElectMeter getTcElectMeter() {
        return tcElectMeter;
    }

    public void setTcElectMeter(TcElectMeter tcElectMeter) {
        this.tcElectMeter = tcElectMeter;
    }

    public String getElectId() {
        return electId;
    }

    public void setElectId(String electId) {
        this.electId = electId;
    }

    @Override
    public String toString() {
        return "TcElectlog{" +
                "id='" + id + '\'' +
                ", electId='" + electId + '\'' +
                ", operator='" + operator + '\'' +
                ", modifyTime=" + modifyTime +
                ", modifyType='" + modifyType + '\'' +
                ", modifyMatter='" + modifyMatter + '\'' +
                ", code='" + code + '\'' +
                ", page=" + page +
                ", tcElectMeter=" + tcElectMeter +
                '}';
    }
}
