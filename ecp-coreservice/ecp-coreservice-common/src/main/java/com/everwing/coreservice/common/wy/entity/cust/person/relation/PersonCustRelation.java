package com.everwing.coreservice.common.wy.entity.cust.person.relation;

import java.io.Serializable;
import java.util.Date;

public class PersonCustRelation implements Serializable {

    private static final long serialVersionUID = -9075353632383524082L;

    /** UUID **/
    private String personRelationid;

    /** 本人ID **/
    private String custId;

    /** 本人编号 **/
    private String custCode;

    /** 本人姓名 **/
    private String name;

    /** 本人电话 **/
    private String registerPhone;

    /** 关系 **/
    private String relation;

    /** 关系人ID **/
    private String relatetionId;

    /** 创建人 **/
    private  String  createId;

    /** 创建人姓名 **/
    private  String  createName;

    /** 修改人ID **/
    private  String  modifyId;

    /** 修改人姓名 **/
    private  String  modifyName;

    /** 创建时间 **/
    private Date createTime;

    /** 修改时间 **/
    private  Date modifyTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPersonRelationid() {
        return personRelationid;
    }

    public void setPersonRelationid(String personRelationid) {
        this.personRelationid = personRelationid;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegisterPhone() {
        return registerPhone;
    }

    public void setRegisterPhone(String registerPhone) {
        this.registerPhone = registerPhone;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelatetionId() {
        return relatetionId;
    }

    public void setRelatetionId(String relatetionId) {
        this.relatetionId = relatetionId;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public String getModifyName() {
        return modifyName;
    }

    public void setModifyName(String modifyName) {
        this.modifyName = modifyName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        return "PersonCustRelation{" +
                "personRelationid='" + personRelationid + '\'' +
                ", custId='" + custId + '\'' +
                ", custCode='" + custCode + '\'' +
                ", name='" + name + '\'' +
                ", registerPhone='" + registerPhone + '\'' +
                ", relatetionId='" + relatetionId + '\'' +
                ", createId='" + createId + '\'' +
                ", createName='" + createName + '\'' +
                ", modifyId='" + modifyId + '\'' +
                ", modifyName='" + modifyName + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
