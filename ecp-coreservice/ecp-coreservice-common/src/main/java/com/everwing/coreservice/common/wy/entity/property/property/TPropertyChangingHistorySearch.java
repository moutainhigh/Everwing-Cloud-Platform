package com.everwing.coreservice.common.wy.entity.property.property;/**
 * Created by wust on 2017/8/9.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/9
 * @author wusongti@lii.com.cn
 */
public class TPropertyChangingHistorySearch extends TPropertyChangingHistory{
    private static final long serialVersionUID = 3501399539637474813L;
    private Page page;
    private String buildingFullName;
//    private String projectId;


    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getBuildingFullName() {
        return this.buildingFullName;
    }

    public void setBuildingFullName(String buildingFullName) {
        this.buildingFullName = buildingFullName;
    }

//    public String getProjectId() {
//        return this.projectId;
//    }
//
//    public void setProjectId(String projectId) {
//        this.projectId = projectId;
//    }
}
