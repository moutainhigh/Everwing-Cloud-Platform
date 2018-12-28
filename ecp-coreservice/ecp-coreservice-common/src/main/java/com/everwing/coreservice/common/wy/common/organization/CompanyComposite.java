package com.everwing.coreservice.common.wy.common.organization;/**
 * Created by wust on 2017/8/17.
 */

import com.everwing.coreservice.common.wy.entity.system.company.TSysCompany;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Function:公司容器
 * Reason:
 * Date:2017/8/17
 * @author wusongti@lii.com.cn
 */
public class CompanyComposite implements OrganizationComponent,Serializable{

    private static final long serialVersionUID = 3871517041979361065L;

    private List<OrganizationComponent> organizationComponents = new ArrayList<>(20);

    private int level = 0;

    private TSysCompany tSysCompany;

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TSysCompany gettSysCompany() {
        return this.tSysCompany;
    }

    public void settSysCompany(TSysCompany tSysCompany) {
        this.tSysCompany = tSysCompany;
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
