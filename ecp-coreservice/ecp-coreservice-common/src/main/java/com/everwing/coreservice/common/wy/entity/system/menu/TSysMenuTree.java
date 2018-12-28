/**
 * Project Name:webTYJ
 * File Name:RoleTree.java
 * Package Name:com.flf.entity.system
 * Date:2016年12月14日下午3:06:24
 * Copyright (c) 2016, wusongti@lii.com.cn All Rights Reserved.
 *
*/

package com.everwing.coreservice.common.wy.entity.system.menu;

/**
 *
 * Function:菜单树视图
 * Reason:与页面菜单树一致
 * Date:2017-4-10 10:22:11
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TSysMenuTree extends TSysMenu
{

    private String checked;
    private String nocheck;

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getNocheck() {
        return nocheck;
    }

    public void setNocheck(String nocheck) {
        this.nocheck = nocheck;
    }
}

