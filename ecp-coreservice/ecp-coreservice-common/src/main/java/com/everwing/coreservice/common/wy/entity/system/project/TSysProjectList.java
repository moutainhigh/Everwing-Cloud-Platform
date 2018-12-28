package com.everwing.coreservice.common.wy.entity.system.project;/**
 * Created by wust on 2017/6/20.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/20
 * @author wusongti@lii.com.cn
 */
public class TSysProjectList extends TSysProject{

    private static final long serialVersionUID = 6481403165635484849L;

    private String statusName;

    private String provinceName;

    private String cityName;

    private String areaName;

    private String streetsName;


    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getStreetsName() {
        return streetsName;
    }

    public void setStreetsName(String streetsName) {
        this.streetsName = streetsName;
    }


    @Override
    public String toString() {
        return "TSysProjectList{" +
                "statusName='" + statusName + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", areaName='" + areaName + '\'' +
                ", streetsName='" + streetsName + '\'' +
                '}';
    }
}
