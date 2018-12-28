package com.everwing.coreservice.common.wy.entity.system.department;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:部门持久化对象
 * Reason:子公司下面的部门
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
public class TSysDepartment extends BaseEntity{
    private static final long serialVersionUID = -2738462592517499566L;
    //field
    /** 主键 **/
    private String departmentId;
    /** 编码 **/
    private String code;
    /** 名称 **/
    private String name;
    /** 描述 **/
    private String description;
    /** 部门负责人 **/
    private String leader;

    public String getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeader() {
        return this.leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    @Override
    public String toString() {
        return super.toString() + "\nTSysDepartment{" +
                "departmentId='" + departmentId + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", leader='" + leader + '\'' +
                '}';
    }
}
