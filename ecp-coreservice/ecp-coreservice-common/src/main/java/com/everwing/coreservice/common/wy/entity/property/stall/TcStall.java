package com.everwing.coreservice.common.wy.entity.property.stall;/**
 * Created by wust on 2017/5/19.
 */

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:
 * Reason:
 * Date:2017/5/19
 * @author wusongti@lii.com.cn
 */
public class TcStall extends BaseEntity{
    private static final long serialVersionUID = 2988032329741445577L;
    private String id;
    /** 建筑code **/
    private String buildingCode;
    /**  **/
    private String name;
    /** 是否机械车位 **/
    private String isMechanicalStall;
    /** 车位类型 **/
    private String stallType;
    /** 如果是私家车位，产生的管理费用，单位(元/月) **/
    private Double administrativeExpenese;

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

    public String getIsMechanicalStall() {
        return isMechanicalStall;
    }

    public void setIsMechanicalStall(String isMechanicalStall) {
        this.isMechanicalStall = isMechanicalStall;
    }

    public String getStallType() {
        return stallType;
    }

    public void setStallType(String stallType) {
        this.stallType = stallType;
    }

    public Double getAdministrativeExpenese() {
        return administrativeExpenese;
    }

    public void setAdministrativeExpenese(Double administrativeExpenese) {
        this.administrativeExpenese = administrativeExpenese;
    }

    @Override
    public String toString() {
        return "TcStall{" +
                "id='" + id + '\'' +
                ", createrId='" + createrId + '\'' +
                ", buildingCode='" + buildingCode + '\'' +
                ", createrName='" + createrName + '\'' +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", isMechanicalStall='" + isMechanicalStall + '\'' +
                ", modifyId='" + modifyId + '\'' +
                ", stallType='" + stallType + '\'' +
                ", modifyName='" + modifyName + '\'' +
                ", administrativeExpenese=" + administrativeExpenese +
                ", modifyTime=" + modifyTime +
                ", lan='" + lan + '\'' +
                '}';
    }
}
