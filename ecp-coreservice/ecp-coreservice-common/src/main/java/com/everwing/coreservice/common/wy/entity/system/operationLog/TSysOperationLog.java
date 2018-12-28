package com.everwing.coreservice.common.wy.entity.system.operationLog;/**
 * Created by wust on 2017/6/2.
 */

import java.util.Date;

/**
 *
 * Function:操作日志持久化对象
 * Reason:
 * Date:2017/6/2
 * @author wusongti@lii.com.cn
 */
public class TSysOperationLog implements java.io.Serializable{
    private static final long serialVersionUID = 8527572266612249938L;
    //field
    /** 必填 **/
    private String operationLogId;
    /** 公司ID，非必填 **/
    private String companyId;
    /** 非必填 **/
    private String projectId;
    /** 必填 **/
    private String moduleName;
    /** 必填 **/
    private String businessName;
    /** 必填 **/
    private Date createTime;
    /** 操作角色，必填 **/
    private String operationRole;
    /** 必填 **/
    private String operationUser;
    /** 操作数据ID，非必填 **/
    private String operationDataId;
    /** 非必填 **/
    private Date operationDate;
    /** 必填 **/
    private String operationType;
    /** 非必填 **/
    private String operationIp;
    /** 日志来源，必填 **/
    private String logSourceType;

    public String getOperationLogId() {
        return operationLogId;
    }

    public void setOperationLogId(String operationLogId) {
        this.operationLogId = operationLogId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperationRole() {
        return operationRole;
    }

    public void setOperationRole(String operationRole) {
        this.operationRole = operationRole;
    }

    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

    public String getOperationDataId() {
        return operationDataId;
    }

    public void setOperationDataId(String operationDataId) {
        this.operationDataId = operationDataId;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationIp() {
        return operationIp;
    }

    public void setOperationIp(String operationIp) {
        this.operationIp = operationIp;
    }

    public String getLogSourceType() {
        return logSourceType;
    }

    public void setLogSourceType(String logSourceType) {
        this.logSourceType = logSourceType;
    }
}
