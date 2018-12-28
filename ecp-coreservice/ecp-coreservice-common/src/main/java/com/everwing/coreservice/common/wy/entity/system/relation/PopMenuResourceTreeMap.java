/**
 * Project Name:webTYJ
 * File Name:RoleTree.java
 * Package Name:com.flf.entity.system
 * Date:2016年12月14日下午3:06:24
 * Copyright (c) 2016, wusongti@lii.com.cn All Rights Reserved.
 *
*/

package com.everwing.coreservice.common.wy.entity.system.relation;

/**
 *
 * Function:菜单资源树弹出框视图
 * Reason:与页面资源树一致
 * Date:2017-4-10 10:22:11
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class PopMenuResourceTreeMap implements java.io.Serializable
{
    private static final long serialVersionUID = 3033137362525019028L;
    private String id;
    private String pId;
    private String name;
    private String type;
    private String checked;
    
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public String getpId()
    {
        return pId;
    }
    public void setpId(String pId)
    {
        this.pId = pId;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }
    public String getChecked()
    {
        return checked;
    }
    public void setChecked(String checked)
    {
        this.checked = checked;
    }
}

