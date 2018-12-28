package com.everwing.coreservice.common.wy.entity.common.synchrodata;/**
 * Created by wust on 2018/12/19.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:
 * Reason:
 * Date:2018/12/19
 * @author wusongti@lii.com.cn
 */
public class TSynchrodataSearch extends TSynchrodata{
    private static final long serialVersionUID = -8743955708398060276L;

    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
