package com.everwing.coreservice.common.wy.entity.system.role;/**
 * Created by wust on 2017/4/10.
 */

import com.everwing.coreservice.common.Page;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017-4-10 10:22:11
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TSysRoleSearch extends TSysRole{


    private static final long serialVersionUID = -5921190473735939021L;
    private Page page;

    private List<String> roleIdList;


    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<String> getRoleIdList() {
        return this.roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
