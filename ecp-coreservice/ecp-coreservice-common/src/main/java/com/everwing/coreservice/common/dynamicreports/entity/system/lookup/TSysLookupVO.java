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
public class TSysLookupVO extends TSysLookup {
    private static final long serialVersionUID = 4683407244975810047L;
    private String statusName;
    private String projectName;

    public String getStatusName() {
        return this.statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
