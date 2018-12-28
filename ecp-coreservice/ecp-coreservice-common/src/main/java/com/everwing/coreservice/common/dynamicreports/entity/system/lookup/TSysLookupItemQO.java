package com.everwing.coreservice.common.dynamicreports.entity.system.lookup;


import com.everwing.coreservice.common.Page;

/**
 * LookupItemVO
 *
 * @author Wusongti
 * @date Sep 16, 2015
 * @time 10:15:29 AM
 */
public class TSysLookupItemQO extends TSysLookupItem {

    private Page page;

    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
