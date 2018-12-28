package com.everwing.coreservice.common.wy.entity.system.lookup;


import com.everwing.coreservice.common.Page;

/**
 * LookupItemVO
 *
 * @author Wusongti
 * @date Sep 16, 2015
 * @time 10:15:29 AM
 */
public class TSysLookupItemSearch extends TSysLookupItem {

    private Page page;
    private String projectCode;

    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getProjectCode() {
        return this.projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
}
