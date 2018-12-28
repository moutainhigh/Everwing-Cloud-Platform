package com.everwing.coreservice.common.wy.entity.property.publicbuilding;/**
 * Created by wust on 2017/7/25.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:
 * Reason:
 * Date:2017/7/25
 * @author wusongti@lii.com.cn
 */
public class TcPublicBuildingSearch extends TcPublicBuilding{
    private static final long serialVersionUID = -5945183679947445016L;
    private Page page;

    private String projectCode;


    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getProjectCode() {
        return this.projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
}
