package com.everwing.coreservice.common.wy.common.organization;/**
 * Created by wust on 2017/8/17.
 */

import java.util.List;

/**
 *
 * Function:组织架构抽象构件
 * Reason:
 * Date:2017/8/17
 * @author wusongti@lii.com.cn
 */
public interface OrganizationComponent {
    void add(OrganizationComponent organizationComponent);

    boolean hasChildren();

    List<OrganizationComponent> getChildren();
}
