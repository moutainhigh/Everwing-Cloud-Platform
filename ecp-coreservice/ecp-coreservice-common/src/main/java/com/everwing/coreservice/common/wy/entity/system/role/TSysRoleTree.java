

package com.everwing.coreservice.common.wy.entity.system.role;



/**
 *
 * Function:角色树视图
 * Reason:映射角色树
 * Date:2017-4-10 09:44:23
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TSysRoleTree extends TSysRole
{
    private String checked;
    private String open;

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getNocheck() {
        return nocheck;
    }

    public void setNocheck(String nocheck) {
        this.nocheck = nocheck;
    }

    private String nocheck;
    

}

