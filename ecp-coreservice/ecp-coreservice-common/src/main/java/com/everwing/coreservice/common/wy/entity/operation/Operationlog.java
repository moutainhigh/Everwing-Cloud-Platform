package com.everwing.coreservice.common.wy.entity.operation;

import com.everwing.coreservice.common.Page;

import java.util.Date;

/**
 * 修改建筑记录
 */
public class Operationlog implements java.io.Serializable {

    private static final long serialVersionUID = 3465527223392107217L;

    /** 主键 **/
    private String id;

    /** 新房屋编码 **/
    private  String  houseCodeNew;

    /** 建筑ID **/
    private  String buildingid;

    /** 节点全名称 **/
    private String buildingFullName;

    /** 地址 **/
    private String address;

    /** 操作人 **/
    private  String operator;

    /** 操作时间 **/
    private  Date modifyTime;

    /** 操作类型 **/
    private  String modifyType;

    /** 修改字段 **/
    private String modifyMatter;

    private Page page;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingid() {
        return buildingid;
    }

    public void setBuildingid(String buildingid) {
        this.buildingid = buildingid;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public String getBuildingFullName() {
        return buildingFullName;
    }

    public void setBuildingFullName(String buildingFullName) {
        this.buildingFullName = buildingFullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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


    @Override
    public String toString() {
        return "Operationlog{" +
                "id='" + id + '\'' +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", buildingid='" + buildingid + '\'' +
                ", buildingFullName='" + buildingFullName + '\'' +
                ", address='" + address + '\'' +
                ", operator='" + operator + '\'' +
                ", modifyTime=" + modifyTime +
                ", modifyType='" + modifyType + '\'' +
                ", modifyMatter='" + modifyMatter + '\'' +
                ", page=" + page +
                '}';
    }
}
