package com.everwing.coreservice.common.wy.entity.system.user;/**
 * Created by wust on 2017/4/10.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017-4-10 11:23:24
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TSysUserList extends TSysUser{
    private static final long serialVersionUID = -4688831906099675994L;
    private String sexName;
    private String statusName;
    private String staffStateName;
    private String documentTypeName;
    private String companyCode;
    private String companyName;


    public String getSexName() {
        return this.sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStaffStateName() {
        return this.staffStateName;
    }

    public void setStaffStateName(String staffStateName) {
        this.staffStateName = staffStateName;
    }

    public String getDocumentTypeName() {
        return this.documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    public String getCompanyCode() {
        return this.companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
