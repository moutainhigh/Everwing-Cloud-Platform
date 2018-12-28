package com.everwing.coreservice.common.wy.entity.common.synchrodata;/**
 * Created by wust on 2018/12/19.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2018/12/19
 * @author wusongti@lii.com.cn
 */
public class TSynchrodataList extends TSynchrodata{
    private static final long serialVersionUID = -8217814465551918102L;

    private String priorityLevelName;

    private String operationName;

    private String stateName;

    public String getPriorityLevelName() {
        return priorityLevelName;
    }

    public void setPriorityLevelName(String priorityLevelName) {
        this.priorityLevelName = priorityLevelName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
