package com.everwing.coreservice.common.dynamicreports.entity.system.datasource;/**
 * Created by wust on 2018/1/29.
 */

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/29
 * @author wusongti@lii.com.cn
 */
public class TSysDataSource extends BaseEntity implements java.io.Serializable{
    private static final long serialVersionUID = -6660207705909366722L;
    private String id;
    private String userId;
    private String lookupId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLookupId() {
        return lookupId;
    }

    public void setLookupId(String lookupId) {
        this.lookupId = lookupId;
    }
}
