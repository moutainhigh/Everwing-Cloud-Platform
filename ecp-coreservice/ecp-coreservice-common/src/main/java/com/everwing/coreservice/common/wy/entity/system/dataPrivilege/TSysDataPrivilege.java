package com.everwing.coreservice.common.wy.entity.system.dataPrivilege;/**
 * Created by wust on 2017/7/3.
 */

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:
 * Reason:
 * Date:2017/7/3
 * @author wusongti@lii.com.cn
 */
public class TSysDataPrivilege extends BaseEntity{
    private static final long serialVersionUID = -4717140803286503868L;
    /** 主键 **/
    private String dataPrivilegeId;
    /** 表名 **/
    private String tableName;
    /** 名称 **/
    private String rule;

    public String getDataPrivilegeId() {
        return this.dataPrivilegeId;
    }

    public void setDataPrivilegeId(String dataPrivilegeId) {
        this.dataPrivilegeId = dataPrivilegeId;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRule() {
        return this.rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return super.toString() + "\nTSysDataPrivilege{" +
                "dataPrivilegeId='" + dataPrivilegeId + '\'' +
                ", tableName='" + tableName + '\'' +
                ", rule='" + rule + '\'' +
                '}';
    }
}
