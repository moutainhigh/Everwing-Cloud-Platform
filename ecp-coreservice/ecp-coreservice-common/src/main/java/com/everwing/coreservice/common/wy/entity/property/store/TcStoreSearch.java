package com.everwing.coreservice.common.wy.entity.property.store;/**
 * Created by wust on 2017/5/23.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:
 * Reason:
 * Date:2017/5/23
 * @author wusongti@lii.com.cn
 */
public class TcStoreSearch extends TcStore{
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
