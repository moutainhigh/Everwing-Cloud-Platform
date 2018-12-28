package com.everwing.coreservice.common.wy.common.organization;/**
 * Created by wust on 2017/8/17.
 */

import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartment;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Function:部门容器
 * Reason:
 * Date:2017/8/17
 * @author wusongti@lii.com.cn
 */
public class DepartmentComposite implements OrganizationComponent,Serializable {

    private static final long serialVersionUID = -3606402764241230172L;

    List<OrganizationComponent> organizationComponents = new ArrayList<>(20);

    private int level = 0;

    private TSysDepartment tSysDepartment;

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TSysDepartment gettSysDepartment() {
        return this.tSysDepartment;
    }

    public void settSysDepartment(TSysDepartment tSysDepartment) {
        this.tSysDepartment = tSysDepartment;
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
