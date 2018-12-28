package com.everwing.coreservice.common.wy.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 白名单接收前端dto
 *
 * @author DELL shiny
 * @create 2017/12/28
 */
public class WhiteListDto implements Serializable{

    private String userId;

    private String gatingCodes;

    private String companyId;

    private String projectIds;

    private String projectId;

    private Boolean all;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGatingCodes() {
        return gatingCodes;
    }

    public void setGatingCodes(String gatingCodes) {
        this.gatingCodes = gatingCodes;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(String projectIds) {
        this.projectIds = projectIds;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Boolean getAll() {
        return all;
    }

    public void setAll(Boolean all) {
        this.all = all;
    }

    public boolean isAnyNull(){
        if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(companyId)){
            return true;
        }
        return false;
    }
}
