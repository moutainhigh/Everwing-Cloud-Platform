package com.everwing.coreservice.common.wy.entity.property.publicbuilding;

import com.everwing.coreservice.common.BaseEntity;

public class TcPublicBuilding extends BaseEntity{
    private static final long serialVersionUID = -7109303678898492669L;
    /** 主键(uuid) **/
    private String id;
    /** 建筑code **/
    private String buildingCode;
    /** 名称 **/
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TcPublicBuilding{" +
                "id='" + id + '\'' +
                ", buildingCode='" + buildingCode + '\'' +
                ", name='" + name + '\'' +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", createTime=" + createTime +
                ", modifyId='" + modifyId + '\'' +
                ", modifyName='" + modifyName + '\'' +
                ", modifyTime=" + modifyTime +
                ", lan='" + lan + '\'' +
                '}';
    }
}
