package com.everwing.coreservice.common.wy.common.organization;/**
 * Created by wust on 2018/9/4.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartment;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRole;

import java.util.List;

/**
 *
 * Function:组织架构辅助类
 * Reason:
 * Date:2018/9/4
 * @author wusongti@lii.com.cn
 */
public class OrganizationHelper {
    private OrganizationHelper(){}


    /**
     * 从organizationComponent构件里面找出部门集合
     * @param organizationComponent
     * @param childrens
     */
    public static void lookupOrganizationForDepartment(OrganizationComponent organizationComponent,JSONArray childrens){
        if(organizationComponent instanceof DepartmentComposite){
            DepartmentComposite departmentComposite = (DepartmentComposite)organizationComponent;
            TSysDepartment tSysDepartment = departmentComposite.gettSysDepartment();
            if(tSysDepartment != null){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",tSysDepartment.getDepartmentId());
                jsonObject.put("name",tSysDepartment.getName());
                jsonObject.put("level",departmentComposite.getLevel());
                jsonObject.put("data",null);
                childrens.add(jsonObject);
            }
        }else{
            if(organizationComponent.hasChildren()){
                List<OrganizationComponent> organizationComponentChildrens = organizationComponent.getChildren();
                for (OrganizationComponent organizationComponentChildren : organizationComponentChildrens) {
                    lookupOrganizationForDepartment(organizationComponentChildren,childrens);
                }
            }
        }
    }

    /**
     * 从organizationComponent构件里面找出项目集合
     * @param organizationComponent
     * @param childrens
     */
    public static void lookupOrganizationForProject(OrganizationComponent organizationComponent,JSONArray childrens){
        if(organizationComponent instanceof ProjectComposite){
            ProjectComposite projectComposite = (ProjectComposite)organizationComponent;
            TSysProject tSysProject = projectComposite.gettSysProject();
            if(tSysProject != null){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",tSysProject.getProjectId());
                jsonObject.put("name",tSysProject.getName());
                jsonObject.put("level",projectComposite.getLevel());
                jsonObject.put("data",null);
                childrens.add(jsonObject);
            }
        }else{
            if(organizationComponent.hasChildren()){
                List<OrganizationComponent> organizationComponentChildrens = organizationComponent.getChildren();
                for (OrganizationComponent organizationComponentChildren : organizationComponentChildrens) {
                    lookupOrganizationForProject(organizationComponentChildren,childrens);
                }
            }
        }
    }

    /**
     * 从organizationComponent构件里面找出岗位集合
     * @param organizationComponent
     * @param childrens
     */
    public static void lookupOrganizationForRole(OrganizationComponent organizationComponent,JSONArray childrens){
        if(organizationComponent instanceof RoleComposite){
            RoleComposite roleComposite = (RoleComposite)organizationComponent;
            TSysRole tSysRole = roleComposite.gettSysRole();
            if(tSysRole != null){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",tSysRole.getRoleId());
                jsonObject.put("name",tSysRole.getRoleName());
                jsonObject.put("level",roleComposite.getLevel());
                jsonObject.put("data",null);
                childrens.add(jsonObject);
            }
        }else{
            if(organizationComponent.hasChildren()){
                List<OrganizationComponent> organizationComponentChildrens = organizationComponent.getChildren();
                for (OrganizationComponent organizationComponentChildren : organizationComponentChildrens) {
                    lookupOrganizationForRole(organizationComponentChildren,childrens);
                }
            }
        }
    }


}
