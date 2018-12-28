package com.everwing.coreservice.common.wy.entity.system.operationLog;/**
 * Created by wust on 2017/6/2.
 */

/**
 *
 * Function:操作日志列表视图对象
 * Reason:
 * Date:2017/6/2
 * @author wusongti@lii.com.cn
 */
public class TSysOperationLogList extends TSysOperationLog{
    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
