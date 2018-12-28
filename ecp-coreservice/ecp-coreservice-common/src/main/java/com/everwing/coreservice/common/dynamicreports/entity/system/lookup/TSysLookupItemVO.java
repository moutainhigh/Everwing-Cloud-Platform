package com.everwing.coreservice.common.dynamicreports.entity.system.lookup;/**
 * Created by wust on 2017/6/16.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/16
 * @author wusongti@lii.com.cn
 */
public class TSysLookupItemVO extends TSysLookupItem {

    private static final long serialVersionUID = 1345924595253590772L;
    private String statusName;

    public String getStatusName() {
        return this.statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
