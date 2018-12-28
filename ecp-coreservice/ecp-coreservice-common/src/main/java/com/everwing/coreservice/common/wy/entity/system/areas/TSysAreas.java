package com.everwing.coreservice.common.wy.entity.system.areas;/**
 * Created by wust on 2018/3/20.
 */

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:
 * Reason:
 * Date:2018/3/20
 * @author wusongti@lii.com.cn
 */
public class TSysAreas extends BaseEntity{
    private static final long serialVersionUID = -2204142647688189575L;


    //field
    /**  **/
    private String id;
    /** 区域名称 **/
    private String areaName;
    /** 父节点 **/
    private String pid;
    /** 简名 **/
    private String shortName;
    /** 经度 **/
    private String lng;
    /** 纬度 **/
    private String lat;
    /** 级别 **/
    private String level;
    /** 位置 **/
    private String position;
    /** 排序 **/
    private int sort;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
