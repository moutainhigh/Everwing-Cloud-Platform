package com.everwing.coreservice.common.wy.entity.property.Engineering;

import com.everwing.coreservice.common.Page;

import java.util.Date;

/**
 *修改工程施工记录
 */
public class Engineeringlog implements java.io.Serializable {

    private static final long serialVersionUID = 3465527223392107218L;

    /** 主键 **/
    private String id;

    /** 建筑ID **/
    private  String buildingid;

    /** 操作人 **/
    private  String operator;

    /** 操作时间 **/
    private  Date modifyTime;

    /** 操作类型 **/
    private  String modifyType;

    /** 修改字段 **/
    private String modifyMatter;

    /** 施工编码 **/
    private String houseCodeNew;

    /** 工程名称 **/
    private String engineeringName;

    /** 施工地址 **/
    private String constructionAddr;

    /** 工程单位 **/
    private String engineeringUnit;

    /** 工程负责人 **/
    private String engineeringDirector;

    private Page page;

    private TcConstruction  tcConstruction;

    public String getEngineeringName() {
        return engineeringName;
    }

    public void setEngineeringName(String engineeringName) {
        this.engineeringName = engineeringName;
    }

    public String getConstructionAddr() {
        return constructionAddr;
    }

    public void setConstructionAddr(String constructionAddr) {
        this.constructionAddr = constructionAddr;
    }

    public String getEngineeringUnit() {
        return engineeringUnit;
    }

    public void setEngineeringUnit(String engineeringUnit) {
        this.engineeringUnit = engineeringUnit;
    }

    public String getEngineeringDirector() {
        return engineeringDirector;
    }

    public void setEngineeringDirector(String engineeringDirector) {
        this.engineeringDirector = engineeringDirector;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
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

    public String getBuildingid() {
        return buildingid;
    }

    public void setBuildingid(String buildingid) {
        this.buildingid = buildingid;
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

    public TcConstruction getTcConstruction() {
        return tcConstruction;
    }

    public void setTcConstruction(TcConstruction tcConstruction) {
        this.tcConstruction = tcConstruction;
    }

    @Override
    public String toString() {
        return "Engineeringlog{" +
                "id='" + id + '\'' +
                ", buildingid='" + buildingid + '\'' +
                ", operator='" + operator + '\'' +
                ", modifyTime=" + modifyTime +
                ", modifyType='" + modifyType + '\'' +
                ", modifyMatter='" + modifyMatter + '\'' +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", engineeringName='" + engineeringName + '\'' +
                ", constructionAddr='" + constructionAddr + '\'' +
                ", engineeringUnit='" + engineeringUnit + '\'' +
                ", engineeringDirector='" + engineeringDirector + '\'' +
                ", page=" + page +
                ", tcConstruction=" + tcConstruction +
                '}';
    }
}
