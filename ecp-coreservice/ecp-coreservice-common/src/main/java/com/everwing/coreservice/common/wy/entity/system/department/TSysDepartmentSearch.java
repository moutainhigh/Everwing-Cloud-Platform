package com.everwing.coreservice.common.wy.entity.system.department;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.Page;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
public class TSysDepartmentSearch extends TSysDepartment{
    private Page page;
    private List<String> departmentIdList;

    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<String> getDepartmentIdList() {
        return this.departmentIdList;
    }

    public void setDepartmentIdList(List<String> departmentIdList) {
        this.departmentIdList = departmentIdList;
    }
}
