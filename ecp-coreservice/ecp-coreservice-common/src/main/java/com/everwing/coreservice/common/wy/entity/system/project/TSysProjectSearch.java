package com.everwing.coreservice.common.wy.entity.system.project;/**
 * Created by wust on 2017/6/20.
 */

import com.everwing.coreservice.common.Page;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/20
 * @author wusongti@lii.com.cn
 */
public class TSysProjectSearch extends TSysProject{
    private static final long serialVersionUID = 8417425906194439744L;

    private Page page;

    private List<String> projectIdList;

    private String loginName;
    
    private String searchCondition;			//辅助查询条件
    
    private String companyId;				//辅助查询条件
    
    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<String> getProjectIdList() {
        return this.projectIdList;
    }

    public void setProjectIdList(List<String> projectIdList) {
        this.projectIdList = projectIdList;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
}
