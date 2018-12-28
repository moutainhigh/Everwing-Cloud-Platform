package com.everwing.coreservice.common.wy.common.organization;/**
 * Created by wust on 2017/8/17.
 */

import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Function:项目容器
 * Reason:
 * Date:2017/8/17
 * @author wusongti@lii.com.cn
 */
public class ProjectComposite implements OrganizationComponent,Serializable {

    private static final long serialVersionUID = 2622179975504548005L;

    List<OrganizationComponent> organizationComponents = new ArrayList<>(20);

    private int level = 0;

    private TSysProject tSysProject;

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TSysProject gettSysProject() {
        return this.tSysProject;
    }

    public void settSysProject(TSysProject tSysProject) {
        this.tSysProject = tSysProject;
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
