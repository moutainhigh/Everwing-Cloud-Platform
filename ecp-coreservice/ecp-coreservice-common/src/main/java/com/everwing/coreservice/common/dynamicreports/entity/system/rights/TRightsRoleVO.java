package com.everwing.coreservice.common.dynamicreports.entity.system.rights;/**
 * Created by wust on 2018/1/29.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/29
 * @author wusongti@lii.com.cn
 */
public class TRightsRoleVO extends TRightsRole {
    private static final long serialVersionUID = 5015326925726219520L;

    private String statusName;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
