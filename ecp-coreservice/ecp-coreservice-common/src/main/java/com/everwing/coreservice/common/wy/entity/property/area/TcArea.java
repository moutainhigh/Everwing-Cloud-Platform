package com.everwing.coreservice.common.wy.entity.property.area;/**
 * Created by wust on 2017/4/18.
 */

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:
 * Reason:
 * Date:2017-4-18 14:53:07
 * @author wusongti@lii.com.cn
 */
public class TcArea extends BaseEntity{
    private String id;
    private String buildingCode;//建筑code
    private String areaType;//面积类型code
    private String buildingArea;//建筑面积
    private String shareArea;//公摊面积
    private String insideArea;//套内面积
    private String utilityRatio;//实用率

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

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getBuildingArea() {
        return buildingArea;
    }

    public void setBuildingArea(String buildingArea) {
        this.buildingArea = buildingArea;
    }

    public String getShareArea() {
        return shareArea;
    }

    public void setShareArea(String shareArea) {
        this.shareArea = shareArea;
    }

    public String getInsideArea() {
        return insideArea;
    }

    public void setInsideArea(String insideArea) {
        this.insideArea = insideArea;
    }

    public String getUtilityRatio() {
        return utilityRatio;
    }

    public void setUtilityRatio(String utilityRatio) {
        this.utilityRatio = utilityRatio;
    }
}
