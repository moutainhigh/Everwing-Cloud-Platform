package com.everwing.coreservice.common.dynamicreports.entity.system.lookup;


import com.everwing.coreservice.common.Page;

/**
 * @date Sep 16, 2015
 * @time 10:11:04 AM
 * @author Wusongti
 */
public class TSysLookupQO extends TSysLookup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Page page;

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
