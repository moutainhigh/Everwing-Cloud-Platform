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
public class TRightsUserVO extends TRightsUser {
    private static final long serialVersionUID = 9076770317820386309L;
    private String sexName;
    private String statusName;

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
