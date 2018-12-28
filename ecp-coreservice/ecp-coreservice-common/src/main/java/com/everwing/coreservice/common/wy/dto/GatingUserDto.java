package com.everwing.coreservice.common.wy.dto;

import com.everwing.coreservice.common.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 门禁授权查询dto
 *
 * @author DELL shiny
 * @create 2017/12/27
 */
public class GatingUserDto implements Serializable{

    private String userId;

    private String companyId;

    private String loginName;

    private String accountName;

    private String equipmentName;

    private List<String> projectIds;

    private String projectId;

    private Integer start;

    private Page page;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Page getPage() {
        if(page==null){
            page=new Page();
        }
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<String> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<String> projectIds) {
        this.projectIds = projectIds;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
