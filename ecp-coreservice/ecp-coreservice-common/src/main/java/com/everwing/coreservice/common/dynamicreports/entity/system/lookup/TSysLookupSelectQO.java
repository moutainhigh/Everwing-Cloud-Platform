package com.everwing.coreservice.common.dynamicreports.entity.system.lookup;/**
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
public class TSysLookupSelectQO extends BaseEntity{
    private static final long serialVersionUID = 4893204615655119268L;
    private String id;
    private String code;
    private String parentCode;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return this.parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
}
