package com.everwing.server.wy.web.controller.sys;/**
 * Created by wust on 2017/6/13.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.UserContextModel;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.common.organization.*;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompany;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartment;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganization;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganizationList;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganizationSearch;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRole;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUser;
import com.everwing.coreservice.wy.api.sys.TSysOrganizationApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/system/TSysOrganizationController")
public class TSysOrganizationController {
    @Autowired
    private TSysOrganizationApi tSysOrganizationApi;

    @Autowired
    private SpringRedisTools springRedisTools;



    /**
     * 获取组织结构树
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Organization,businessName="查询组织结构树",operationType= OperationEnum.Search)
    @RequestMapping(value = "/findOrganizationTree",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto findOrganizationTree(){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        TSysOrganizationSearch condition = new TSysOrganizationSearch();

        RemoteModelResult<List<TSysOrganizationList>> result = tSysOrganizationApi.findOrganizationTree(ctx.getCompanyId(),condition);
        if(result.isSuccess()){
            mm.setFlag(MessageMap.INFOR_SUCCESS);
            baseDto.setLstDto(result.getModel());
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    /**
     * 当点击组织架构节点时，加载其所有子列表
     * @param tSysOrganizationSearch
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Organization,businessName="查询组织结构列表",operationType= OperationEnum.Search)
    @RequestMapping(value = "/findOrganizationByPid",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap findOrganizationByPid(@RequestBody TSysOrganizationSearch tSysOrganizationSearch){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult<MessageMap> result = tSysOrganizationApi.findOrganizationByPid(ctx.getCompanyId(), tSysOrganizationSearch);
        if(result.isSuccess()){
           mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Organization,businessName="保存组织",operationType= OperationEnum.Save)
    @RequestMapping(value="/save",method =RequestMethod.POST)
    public @ResponseBody  MessageMap save(@RequestBody Map map){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        if(CollectionUtils.isEmpty(map)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("保存失败，数据库空");
            return mm;
        }

        String type = CommonUtils.null2String(map.get("type"));
        String organizationId = UUID.randomUUID().toString();
        String pid = CommonUtils.null2String(map.get("pid"));
        String fk = UUID.randomUUID().toString();
        if("staff".equalsIgnoreCase(type)){
            fk = CommonUtils.null2String(map.get("fk"));
        }

        // 组织关系
        TSysOrganization tSysOrganization = new TSysOrganization();
        tSysOrganization.setType(type);
        tSysOrganization.setOrganizationId(organizationId);
        tSysOrganization.setPid(pid);
        tSysOrganization.setCode(fk);
        tSysOrganization.setCreaterId(ctx.getUserId());
        tSysOrganization.setCreaterName(ctx.getStaffName());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tSysOrganization",tSysOrganization);

        if(WyEnum.organizationType_company.getStringValue().equalsIgnoreCase(type)){    // 公司
            String name = CommonUtils.null2String(map.get("name"));
            String description = CommonUtils.null2String(map.get("description"));
            String leader = CommonUtils.null2String(map.get("leader"));

            TSysCompany tSysCompany = new TSysCompany();
            tSysCompany.setCompanyId(fk);
            tSysCompany.setName(name);
            tSysCompany.setDescription(description);
            tSysCompany.setLeader(leader);
            tSysCompany.setCreaterId(ctx.getUserId());
            tSysCompany.setCreaterName(ctx.getStaffName());

            jsonObject.put("tSysCompany",tSysCompany);
        }else if(WyEnum.organizationType_department.getStringValue().equalsIgnoreCase(type)){   // 部门
            String name = CommonUtils.null2String(map.get("name"));
            String description = CommonUtils.null2String(map.get("description"));
            String leader = CommonUtils.null2String(map.get("leader"));

            TSysDepartment tSysDepartment = new TSysDepartment();
            tSysDepartment.setDepartmentId(fk);
            tSysDepartment.setName(name);
            tSysDepartment.setDescription(description);
            tSysDepartment.setLeader(leader);
            tSysDepartment.setCreaterId(ctx.getUserId());
            tSysDepartment.setCreaterName(ctx.getStaffName());

            jsonObject.put("tSysDepartment",tSysDepartment);
        }else if(WyEnum.organizationType_project.getStringValue().equalsIgnoreCase(type)){  // 项目
            String name = CommonUtils.null2String(map.get("name"));
            String description = CommonUtils.null2String(map.get("description"));
            String leader = CommonUtils.null2String(map.get("leader"));
            String address = CommonUtils.null2String(map.get("address"));
            String zipCode = CommonUtils.null2String(map.get("zipCode"));
            String province = CommonUtils.null2String(map.get("province"));
            String city = CommonUtils.null2String(map.get("city"));
            String area = CommonUtils.null2String(map.get("area"));
            String streets = CommonUtils.null2String(map.get("streets"));

            TSysProject tSysProject = new TSysProject();
            tSysProject.setProjectId(fk);
            tSysProject.setName(name);
            tSysProject.setDescription(description);
            tSysProject.setLeader(leader);
            tSysProject.setAddress(address);
            tSysProject.setStatus(LookupItemEnum.enableDisable_enable.getStringValue());
            tSysProject.setZipCode(zipCode);
            tSysProject.setProvince(province);
            tSysProject.setCity(city);
            tSysProject.setArea(area);
            tSysProject.setStreets(streets);
            tSysProject.setCreaterId(ctx.getUserId());
            tSysProject.setCreaterName(ctx.getStaffName());

            jsonObject.put("tSysProject",tSysProject);
        }else if(WyEnum.organizationType_role.getStringValue().equalsIgnoreCase(type)){ // 岗位
            String name = CommonUtils.null2String(map.get("roleName"));
            String description = CommonUtils.null2String(map.get("roleDesc"));

            TSysRole tSysRole = new TSysRole();
            tSysRole.setRoleId(fk);
            tSysRole.setRoleName(name);
            tSysRole.setRoleDesc(description);
            tSysRole.setStatus(LookupItemEnum.enableDisable_enable.getStringValue());
            tSysRole.setCreaterId(ctx.getUserId());
            tSysRole.setCreaterName(ctx.getStaffName());

            jsonObject.put("tSysRole",tSysRole);
        }else if(WyEnum.organizationType_staff.getStringValue().equalsIgnoreCase(type)){    // 员工
            // 员工不是在此处新增，而是在员工页面邀请入职
        }


        RemoteModelResult<MessageMap> result = tSysOrganizationApi.save(ctx.getCompanyId(),jsonObject);
        if(result.isSuccess()){
            mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Organization,businessName="删除组织",operationType= OperationEnum.Delete)
    @RequestMapping(value="/delete/{organizationId}",method =RequestMethod.DELETE)
    public @ResponseBody  MessageMap delete(@PathVariable String organizationId){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        TSysOrganization entity = new TSysOrganization();
        entity.setOrganizationId(organizationId);

        RemoteModelResult<MessageMap> result = tSysOrganizationApi.delete(ctx.getCompanyId(),entity);
        if(result.isSuccess()){
            mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }



    /**
     * 将公司抽象构件转换为JsonObject
     * @param organizationComponent 抽象构件
     * @param jsonObject
     */
    private void lookupOrganization4company(OrganizationComponent organizationComponent,JSONObject jsonObject){
        if(organizationComponent instanceof CompanyComposite){
            TSysCompany tSysCompany = ((CompanyComposite)organizationComponent).gettSysCompany();
            if(tSysCompany != null){
                jsonObject.put("id",tSysCompany.getCompanyId());
                jsonObject.put("name",tSysCompany.getName());
                jsonObject.put("data",null);
            }
        }else if(organizationComponent instanceof DepartmentComposite){
            TSysDepartment tSysDepartment = ((DepartmentComposite)organizationComponent).gettSysDepartment();
            if(tSysDepartment != null){
                jsonObject.put("id",tSysDepartment.getDepartmentId());
                jsonObject.put("name",tSysDepartment.getName());
                jsonObject.put("data",null);
            }
        }else if(organizationComponent instanceof ProjectComposite){
            TSysProject tSysProject = ((ProjectComposite)organizationComponent).gettSysProject();
            if(tSysProject != null){
                jsonObject.put("id",tSysProject.getProjectId());
                jsonObject.put("name",tSysProject.getName());
                jsonObject.put("data",null);
            }
        }else if(organizationComponent instanceof RoleComposite){
            TSysRole tSysRole = ((RoleComposite)organizationComponent).gettSysRole();
            if(tSysRole != null){
                jsonObject.put("id",tSysRole.getRoleId());
                jsonObject.put("name",tSysRole.getRoleName());
                jsonObject.put("data",null);
            }
        }else if(organizationComponent instanceof  EmployeeLeaf){
            TSysUser tSysUser = ((EmployeeLeaf)organizationComponent).gettSysUser();
            if(tSysUser != null){
                jsonObject.put("id",tSysUser.getUserId());
                jsonObject.put("name",tSysUser.getStaffName());
                jsonObject.put("data",null);
            }
        }
        if(organizationComponent.hasChildren()){
            JSONArray childrens = new JSONArray();
            List<OrganizationComponent> organizationComponentChildrens = organizationComponent.getChildren();
            for (OrganizationComponent organizationComponentChildren : organizationComponentChildrens) {
                JSONObject children = new JSONObject();
                lookupOrganization4company(organizationComponentChildren,children);
                childrens.add(children);
            }
            jsonObject.put("children",childrens);
        }
    }


    /**
     * 以公司为维度获取当前登录用户的组织架构
     */
    @RequestMapping(value = "/findOrganizationalChartsByCompanyId",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap findOrganizationalChartsByCompanyId(){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        JSONObject jsonObject = new JSONObject();
        lookupOrganization4company(ctx.getOrganizationComponent(),jsonObject);

        mm.setFlag(MessageMap.INFOR_SUCCESS);
        mm.setObj(jsonObject);
        return mm;
    }


    /**
     * 将项目抽象构件转换为JSONObject
     * @param organizationComponent 抽象构件
     * @param jsonObject
     */
    private void lookupOrganization4project(OrganizationComponent organizationComponent,JSONObject jsonObject){
        if(organizationComponent instanceof CompanyComposite){

        }else if(organizationComponent instanceof DepartmentComposite){
            TSysDepartment tSysDepartment = ((DepartmentComposite)organizationComponent).gettSysDepartment();
            if(tSysDepartment != null){
                jsonObject.put("id",tSysDepartment.getDepartmentId());
                jsonObject.put("name",tSysDepartment.getName());
                jsonObject.put("data",null);
            }
        }else if(organizationComponent instanceof ProjectComposite){
            TSysProject tSysProject = ((ProjectComposite)organizationComponent).gettSysProject();
            if(tSysProject != null){
                jsonObject.put("id",tSysProject.getProjectId());
                jsonObject.put("name",tSysProject.getName());
                jsonObject.put("data",null);
            }
        }else if(organizationComponent instanceof RoleComposite){
            TSysRole tSysRole = ((RoleComposite)organizationComponent).gettSysRole();
            if(tSysRole != null){
                jsonObject.put("id",tSysRole.getRoleId());
                jsonObject.put("name",tSysRole.getRoleName());
                jsonObject.put("data",null);
            }
        }else if(organizationComponent instanceof  EmployeeLeaf){
            TSysUser tSysUser = ((EmployeeLeaf)organizationComponent).gettSysUser();
            if(tSysUser != null){
                jsonObject.put("id",tSysUser.getUserId());
                jsonObject.put("name",tSysUser.getStaffName());
                jsonObject.put("data",null);
            }
        }
        if(organizationComponent.hasChildren()){
            JSONArray childrens = new JSONArray();
            List<OrganizationComponent> organizationComponentChildrens = organizationComponent.getChildren();
            for (OrganizationComponent organizationComponentChildren : organizationComponentChildrens) {
                JSONObject children = new JSONObject();
                lookupOrganization4project(organizationComponentChildren,children);
                if(children != null && children.size() > 0){
                    childrens.add(children);
                }
            }
            if(childrens.size() > 0){
                jsonObject.put("children",childrens);
            }
        }
    }

    /**
     * 以选中项目为维度获取当前登录用户的组织架构
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/findOrganizationalChartsByProjectId/{projectId}",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap findOrganizationalChartsByProjectId(@PathVariable String projectId){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult<OrganizationComponent> componentRemoteModelResult = tSysOrganizationApi.getOrganizationComponent(ctx.getCompanyId(),WyEnum.organizationType_project.getStringValue(),projectId,WyEnum.organizationType_staff.getStringValue(),ctx.getLoginName());
        if(componentRemoteModelResult.isSuccess()){
            OrganizationComponent organizationComponent = componentRemoteModelResult.getModel();
            JSONObject jsonObject = new JSONObject();
            lookupOrganization4project(organizationComponent,jsonObject);
            mm.setFlag(MessageMap.INFOR_SUCCESS);
            mm.setObj(jsonObject);
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(componentRemoteModelResult.getMsg());
        }
        return mm;
    }



    /**
     * 登录首页缩略架构图
     * @return
     */
    @RequestMapping(value = "/findOrganizationalChartsByStaffNumber",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap findOrganizationalChartsByStaffNumber(){
        MessageMap mm = new MessageMap();


        WyBusinessContext ctx = WyBusinessContext.getContext();

        String key = String.format(ApplicationConstant.WY_WEB_LOGIN_KEY.getStringValue(),ctx.getLoginName());

        UserContextModel redisUserContext = springRedisTools.getByKey(key) == null ? null : (UserContextModel)springRedisTools.getByKey(key);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",redisUserContext.getRootCompany().getCompanyId());
        jsonObject.put("name",redisUserContext.getRootCompany().getName());
        jsonObject.put("data",null);

        JSONObject children = new JSONObject();
        children.put("id",redisUserContext.getLoginUser().getUserId());
        children.put("name",redisUserContext.getLoginUser().getStaffName());
        children.put("data",null);



        JSONArray childrens = new JSONArray();
        childrens.add(children);

        jsonObject.put("children",childrens);

        mm.setFlag(MessageMap.INFOR_SUCCESS);
        mm.setObj(jsonObject);
        return mm;
    }
}
