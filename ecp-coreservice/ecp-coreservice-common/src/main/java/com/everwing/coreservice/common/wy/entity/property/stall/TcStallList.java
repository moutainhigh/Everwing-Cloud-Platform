package com.everwing.coreservice.common.wy.entity.property.stall;/**
 * Created by wust on 2017/5/19.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/5/19
 * @author wusongti@lii.com.cn
 */
public class TcStallList extends TcStall{
    private static final long serialVersionUID = -8217889708379387009L;
    private String projectName;
    private String stallFullName;
    private String marketStateName;
    private String stallPropertyName;
    private String stallTypeName;
    private String isMechanicalStallName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStallFullName() {
        return stallFullName;
    }

    public void setStallFullName(String stallFullName) {
        this.stallFullName = stallFullName;
    }

    public String getMarketStateName() {
        return marketStateName;
    }

    public void setMarketStateName(String marketStateName) {
        this.marketStateName = marketStateName;
    }

    public String getStallPropertyName() {
        return stallPropertyName;
    }

    public void setStallPropertyName(String stallPropertyName) {
        this.stallPropertyName = stallPropertyName;
    }

    public String getStallTypeName() {
        return stallTypeName;
    }

    public void setStallTypeName(String stallTypeName) {
        this.stallTypeName = stallTypeName;
    }

    public String getIsMechanicalStallName() {
        return isMechanicalStallName;
    }

    public void setIsMechanicalStallName(String isMechanicalStallName) {
        this.isMechanicalStallName = isMechanicalStallName;
    }
}
