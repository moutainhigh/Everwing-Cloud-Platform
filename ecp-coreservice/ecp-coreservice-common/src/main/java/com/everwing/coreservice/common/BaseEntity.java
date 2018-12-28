
package com.everwing.coreservice.common;

import java.util.Date;

/**
 * Function: 基础对象
 * Reason:	 
 * Date:     2016年12月15日 下午3:09:07
 * @author   wust
 */
public class BaseEntity implements java.io.Serializable
{
    private static final long serialVersionUID = -7114772512426125749L;
    /** 创建人id **/
    protected String createrId;
    /** 创建人名称  **/
    protected String createrName;
    /** 创建时间 **/
    protected Date createTime;
    /** 修改人id **/
    protected String modifyId;
    /** 修改人名称 **/
    protected String modifyName;
    /** 修改时间 **/
    protected Date modifyTime;
    /** 语言环境**/
    protected String lan;

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public String getModifyName() {
        return modifyName;
    }

    public void setModifyName(String modifyName) {
        this.modifyName = modifyName;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }


    @Override
    public String toString() {
        return "BaseEntity{" +
                "createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", createTime=" + createTime +
                ", modifyId='" + modifyId + '\'' +
                ", modifyName='" + modifyName + '\'' +
                ", modifyTime=" + modifyTime +
                ", lan='" + lan + '\'' +
                '}';
    }
}

