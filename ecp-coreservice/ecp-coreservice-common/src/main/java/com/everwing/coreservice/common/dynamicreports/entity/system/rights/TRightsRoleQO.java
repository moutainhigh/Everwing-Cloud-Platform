package com.everwing.coreservice.common.dynamicreports.entity.system.rights;/**
 * Created by wust on 2018/1/29.
 */

import com.everwing.coreservice.common.Page;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/29
 * @author wusongti@lii.com.cn
 */
public class TRightsRoleQO extends TRightsRole {
    private static final long serialVersionUID = 5464415978751486904L;
    private Page page;

    private List<String> roleIdList;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<String> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
