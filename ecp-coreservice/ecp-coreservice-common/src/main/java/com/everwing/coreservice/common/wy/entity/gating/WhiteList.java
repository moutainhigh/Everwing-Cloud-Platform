package com.everwing.coreservice.common.wy.entity.gating;

import java.io.Serializable;
import java.util.Date;

/**
 * 白名单
 *
 * @author DELL shiny
 * @create 2017/12/28
 */
public class WhiteList implements Serializable{

    private String id;

    private String userId;

    private String gatingCode;

    private String companyId;

    private String projectId;

    private String createBy;

    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGatingCode() {
        return gatingCode;
    }

    public void setGatingCode(String gatingCode) {
        this.gatingCode = gatingCode;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
