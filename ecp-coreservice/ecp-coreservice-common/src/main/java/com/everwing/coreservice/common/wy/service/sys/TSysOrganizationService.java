package com.everwing.coreservice.common.wy.service.sys;/**
 * Created by wust on 2017/6/13.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.organization.OrganizationComponent;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganization;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganizationList;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganizationSearch;
import com.everwing.coreservice.common.wy.entity.system.user.UserResourceList;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
public interface TSysOrganizationService {
    List<TSysOrganizationList> findOrganizationTree(String companyId, TSysOrganizationSearch condition);

    List<TSysOrganization> findByCondition(String companyId, TSysOrganizationSearch condition);

    MessageMap save(String companyId, JSONObject jsonObject);

    MessageMap delete(String companyId, TSysOrganization entity);

    MessageMap findOrganizationByPid(String companyId, TSysOrganizationSearch tSysOrganizationSearch);

    /**
     * 根据bottomCompnentId获取其所有岗位、部门、项目以及公司信息集合
     * @param topComponentId    当前登录用户所属公司编码
     * @param bottomCompnentId  当前登录用户的登录账号或工号
     * @return
     */
    List<UserResourceList> getUserResourceListByKey(String topComponentId, String bottomCompnentId);

    /**
     *
     * 先根据bottomCompnentType和bottomCompnentId向上递归，找出底部边界到顶部集合unionList，
     * 然后再根据条件topComponentType和topComponentId向下递归unionList，找出顶部边界的组织架构，
     * 最后的组织架构构件在区间bottomCompnentId和topComponentId之间，
     * 结果：OrganizationComponent >= bottomCompnentId &&  OrganizationComponent <= topComponentId
     * @param companyId
     * @param topComponentType  顶部边界类型：公司、部门、项目、岗位
     * @param topComponentId    边界类型对应的ID，如公司id，部门id...
     * @param bottomCompnentType    底部边界的类型：暂时只支持员工
     * @param bottomCompnentId      底部边界ID:如工号
     * @return 返回(OrganizationComponent >= bottomCompnentId &&  OrganizationComponent <= topComponentId)之间的集合
     */
    OrganizationComponent getOrganizationComponent(String companyId, String topComponentType, String topComponentId, String bottomCompnentType, String bottomCompnentId);
}
