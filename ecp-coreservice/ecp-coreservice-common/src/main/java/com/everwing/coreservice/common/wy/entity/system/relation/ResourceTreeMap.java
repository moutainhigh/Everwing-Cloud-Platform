
package com.everwing.coreservice.common.wy.entity.system.relation;

/**
 *
 * Function:资源树视图
 * Reason:与页面资源树一致
 * Date:2017-4-10 10:22:11
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class ResourceTreeMap implements java.io.Serializable
{
    private static final long serialVersionUID = 998913347243442196L;
    private String srcId;
    private String srcName;
    private String srcDesc;
    private String menuId;
    private String checked;
    private String nocheck;
    
    public String getSrcId()
    {
        return srcId;
    }
    public void setSrcId(String srcId)
    {
        this.srcId = srcId;
    }
    public String getSrcName()
    {
        return srcName;
    }
    public void setSrcName(String srcName)
    {
        this.srcName = srcName;
    }
    public String getSrcDesc()
    {
        return srcDesc;
    }
    public void setSrcDesc(String srcDesc)
    {
        this.srcDesc = srcDesc;
    }
    public String getMenuId()
    {
        return menuId;
    }
    public void setMenuId(String menuId)
    {
        this.menuId = menuId;
    }
    public String getChecked()
    {
        return checked;
    }
    public void setChecked(String checked)
    {
        this.checked = checked;
    }
    public String getNocheck()
    {
        return nocheck;
    }
    public void setNocheck(String nocheck)
    {
        this.nocheck = nocheck;
    }
}

