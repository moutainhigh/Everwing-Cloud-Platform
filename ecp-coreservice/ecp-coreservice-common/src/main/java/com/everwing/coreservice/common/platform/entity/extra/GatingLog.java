package com.everwing.coreservice.common.platform.entity.extra;

import java.io.Serializable;
import java.util.Date;

/**
 * 门控机日志
 *
 * @author DELL shiny
 * @create 2017/7/20
 */
public class GatingLog implements Serializable{

    private String id;

    private String companyId;

    private String projectId;

    private String toBuildingCode;

    private String logId;

    private String gatingCode;

    private String formCode;

    private String type;

    private String gatingAccount;

    private String fromBuildingCode;

    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getToBuildingCode() {
        return toBuildingCode;
    }

    public void setToBuildingCode(String toBuildingCode) {
        this.toBuildingCode = toBuildingCode;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getGatingCode() {
        return gatingCode;
    }

    public void setGatingCode(String gatingCode) {
        this.gatingCode = gatingCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGatingAccount() {
        return gatingAccount;
    }

    public void setGatingAccount(String gatingAccount) {
        this.gatingAccount = gatingAccount;
    }

    public String getFromBuildingCode() {
        return fromBuildingCode;
    }

    public void setFromBuildingCode(String fromBuildingCode) {
        this.fromBuildingCode = fromBuildingCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }
}
