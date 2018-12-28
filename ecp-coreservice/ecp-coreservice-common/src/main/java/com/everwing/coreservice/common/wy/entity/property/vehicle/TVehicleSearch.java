package com.everwing.coreservice.common.wy.entity.property.vehicle;/**
 * Created by wust on 2017/8/1.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/1
 * @author wusongti@lii.com.cn
 */
public class TVehicleSearch extends TVehicle {
    private static final long serialVersionUID = -4518553617424207125L;
    private Page page;

    private String customerId;


    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
