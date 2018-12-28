package com.everwing.coreservice.common.wy.entity.system.organization;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:组织持久化对象
 * Reason:组织是子公司、部门、项目、岗位和员工的关系映射，因为他们的关系是一棵树
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
public class TSysOrganization extends BaseEntity{
    private static final long serialVersionUID = 3374505169802126338L;
    //field
    /** 主键 **/
    private String organizationId;
    /** 组织类型，有公司，部门，项目，岗位，员工 **/
    private String type;
    /** 公司，部门，项目，岗位，员工的编码 **/
    private String code;
    /** organizationId的父类id **/
    private String pid;
    /** 描述 **/
    private String description;


    public String getOrganizationId() {
        return this.organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPid() {
        return this.pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TSysOrganization{" +
                "organizationId='" + organizationId + '\'' +
                ", type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", pid='" + pid + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
