package com.everwing.coreservice.common.wy.entity.system.dataPrivilege;/**
 * Created by wust on 2017/7/3.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:
 * Reason:
 * Date:2017/7/3
 * @author wusongti@lii.com.cn
 */
public class TSysDataPrivilegeSearch extends TSysDataPrivilege{
    private static final long serialVersionUID = -2156368600029107929L;

    private Page page;

    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
