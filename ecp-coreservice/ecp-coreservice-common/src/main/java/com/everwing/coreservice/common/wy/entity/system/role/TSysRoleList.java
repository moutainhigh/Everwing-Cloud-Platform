package com.everwing.coreservice.common.wy.entity.system.role;


/**
 *
 * Function:角色列表视图
 * Reason:与页面列表视图一致
 * Date:2017-4-10 10:22:11
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TSysRoleList extends TSysRole{
    private static final long serialVersionUID = -4450365688364678929L;

    private String statusName;

    public String getStatusName() {
        return this.statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
