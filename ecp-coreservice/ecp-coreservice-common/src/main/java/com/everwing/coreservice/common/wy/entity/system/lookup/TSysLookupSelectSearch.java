package com.everwing.coreservice.common.wy.entity.system.lookup;/**
 * Created by wust on 2017/4/1.
 */

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:下拉框视图
 * Reason:
 * Date:2017年4月1日 17:26:52
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TSysLookupSelectSearch extends BaseEntity{
    private static final long serialVersionUID = 4893204615655119268L;
    private String id;
    private String lookupCode;
    private String lookupItemCode;
    private String parentCode;
    private String projectCode;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLookupCode() {
        return lookupCode;
    }

    public void setLookupCode(String lookupCode) {
        this.lookupCode = lookupCode;
    }

    public String getLookupItemCode() {
        return lookupItemCode;
    }

    public void setLookupItemCode(String lookupItemCode) {
        this.lookupItemCode = lookupItemCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
}
