package com.everwing.coreservice.common.wy.entity.system.user;/**
 * Created by wust on 2017/4/10.
 */

import com.everwing.coreservice.common.Page;

import java.util.List;

/**
 *
 * Function:用户查询实体类
 * Reason:查询封装的条件
 * Date:2017-4-10 09:44:23
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TSysUserSearch extends TSysUser{

    private static final long serialVersionUID = -9085107334809288508L;
    private Page page;
    private String projectId;

    private List<String> staffNumberList;

    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<String> getStaffNumberList() {
        return this.staffNumberList;
    }

    public void setStaffNumberList(List<String> staffNumberList) {
        this.staffNumberList = staffNumberList;
    }

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
