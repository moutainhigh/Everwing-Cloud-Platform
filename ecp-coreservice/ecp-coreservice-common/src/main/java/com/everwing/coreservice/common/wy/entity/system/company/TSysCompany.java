package com.everwing.coreservice.common.wy.entity.system.company;/**
 * Created by wust on 2017/6/13.
 */

import java.util.Date;

/**
 *
 * Function:公司持久化对象
 * Reason:与物业公司相关的子公司
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
public class TSysCompany implements java.io.Serializable{
    private static final long serialVersionUID = -435784729861846124L;

    /** 公司编码**/
    private String companyId;
    /** 编码 **/
    private String code;
    /** 名称 **/
    private String name;
    /** 描述 **/
    private String description;
    /** 公司负责人 **/
    private String leader;
    /** 创建人id **/
    private String createrId;
    /** 创建人名称  **/
    private String createrName;
    /** 创建时间 **/
    private Date createTime;
    /** 修改人id **/
    private String modifyId;
    /** 修改人名称 **/
    private String modifyName;
    /** 修改时间 **/
    private Date modifyTime;

    public String getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeader() {
        return this.leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getCreaterId() {
        return this.createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreaterName() {
        return this.createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyId() {
        return this.modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public String getModifyName() {
        return this.modifyName;
    }

    public void setModifyName(String modifyName) {
        this.modifyName = modifyName;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
