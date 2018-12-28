package com.everwing.coreservice.common.wy.entity.system.organization;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.wy.entity.system.company.TSysCompanySearch;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartmentSearch;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRoleSearch;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
public class TSysOrganizationSearch extends TSysOrganization{
    private TSysCompanySearch tSysCompanySearch;
    private TSysDepartmentSearch tSysDepartmentSearch;
    private TSysProjectSearch tSysProjectSearch;
    private TSysRoleSearch tSysRoleSearch;
    private TSysUserSearch tSysUserSearch;


    public TSysCompanySearch gettSysCompanySearch() {
        return this.tSysCompanySearch;
    }

    public void settSysCompanySearch(TSysCompanySearch tSysCompanySearch) {
        this.tSysCompanySearch = tSysCompanySearch;
    }

    public TSysDepartmentSearch gettSysDepartmentSearch() {
        return this.tSysDepartmentSearch;
    }

    public void settSysDepartmentSearch(TSysDepartmentSearch tSysDepartmentSearch) {
        this.tSysDepartmentSearch = tSysDepartmentSearch;
    }

    public TSysProjectSearch gettSysProjectSearch() {
        return this.tSysProjectSearch;
    }

    public void settSysProjectSearch(TSysProjectSearch tSysProjectSearch) {
        this.tSysProjectSearch = tSysProjectSearch;
    }

    public TSysRoleSearch gettSysRoleSearch() {
        return this.tSysRoleSearch;
    }

    public void settSysRoleSearch(TSysRoleSearch tSysRoleSearch) {
        this.tSysRoleSearch = tSysRoleSearch;
    }

    public TSysUserSearch gettSysUserSearch() {
        return this.tSysUserSearch;
    }

    public void settSysUserSearch(TSysUserSearch tSysUserSearch) {
        this.tSysUserSearch = tSysUserSearch;
    }
}
