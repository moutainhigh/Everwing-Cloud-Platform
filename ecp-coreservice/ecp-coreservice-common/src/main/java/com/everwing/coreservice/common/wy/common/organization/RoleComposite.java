package com.everwing.coreservice.common.wy.common.organization;/**
 * Created by wust on 2017/8/17.
 */

import com.everwing.coreservice.common.wy.entity.system.role.TSysRole;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Function:岗位容器
 * Reason:
 * Date:2017/8/17
 * @author wusongti@lii.com.cn
 */
public class RoleComposite implements OrganizationComponent,Serializable {

    private static final long serialVersionUID = 5997221930667908066L;

    List<OrganizationComponent> organizationComponents = new ArrayList<>(20);

    private int level = 0;

    private TSysRole tSysRole;

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TSysRole gettSysRole() {
        return this.tSysRole;
    }

    public void settSysRole(TSysRole tSysRole) {
        this.tSysRole = tSysRole;
    }

    @Override
    public void add(OrganizationComponent organizationComponent) {
        organizationComponents.add(organizationComponent);
    }

    @Override
    public boolean hasChildren() {
        return CollectionUtils.isNotEmpty(organizationComponents);
    }

    @Override
    public List<OrganizationComponent> getChildren() {
        return organizationComponents;
    }
}
