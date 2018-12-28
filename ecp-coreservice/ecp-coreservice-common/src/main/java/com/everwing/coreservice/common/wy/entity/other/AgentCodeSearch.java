package com.everwing.coreservice.common.wy.entity.other;

import com.everwing.coreservice.common.Page;

public class AgentCodeSearch implements java.io.Serializable{
    private static final long serialVersionUID = 2846771087598203468L;
    private Page page;
    private String loginName;
    private String agentCode;
    private String staffName;


	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
