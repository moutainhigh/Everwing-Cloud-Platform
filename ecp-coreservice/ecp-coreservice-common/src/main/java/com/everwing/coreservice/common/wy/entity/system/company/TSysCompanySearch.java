package com.everwing.coreservice.common.wy.entity.system.company;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.Page;

import java.util.List;

/**
 *
 * Function:查询对象
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
public class TSysCompanySearch extends TSysCompany{
    private Page page;
    private List<String> companyIdList;

    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<String> getCompanyIdList() {
        return this.companyIdList;
    }

    public void setCompanyIdList(List<String> companyIdList) {
        this.companyIdList = companyIdList;
    }
}
