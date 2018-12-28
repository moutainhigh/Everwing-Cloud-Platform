package com.everwing.coreservice.common.dynamicreports.entity.system.rights;/**
 * Created by wust on 2018/1/30.
 */

import java.util.List;

/**
 *
 * Function:菜单查询对象
 * Reason:
 * Date:2018/1/30
 * @author wusongti@lii.com.cn
 */
public class TRightsMenuQO extends TRightsMenu {
    private static final long serialVersionUID = 1660995337746032486L;

    private List<String> roleIdList;

    public List<String> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
