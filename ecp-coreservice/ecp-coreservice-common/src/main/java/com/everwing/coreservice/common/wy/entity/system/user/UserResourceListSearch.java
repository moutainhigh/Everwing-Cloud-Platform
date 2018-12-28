package com.everwing.coreservice.common.wy.entity.system.user;/**
 * Created by wust on 2017/6/27.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/27
 * @author wusongti@lii.com.cn
 */
public class UserResourceListSearch extends UserResourceList{
    private Page page;
    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }


}
