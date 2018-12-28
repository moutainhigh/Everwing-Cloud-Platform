package com.everwing.coreservice.common.dynamicreports.entity.system.rights;/**
 * Created by wust on 2018/1/30.
 */

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/30
 * @author wusongti@lii.com.cn
 */
public class TRightsResourceQO extends TRightsResource {
    private static final long serialVersionUID = 4733808569193566448L;

    private List<String> roleIdList;

    public List<String> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
