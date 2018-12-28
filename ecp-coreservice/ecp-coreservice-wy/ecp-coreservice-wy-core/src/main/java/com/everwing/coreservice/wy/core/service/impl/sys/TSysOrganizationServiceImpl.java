package com.everwing.coreservice.wy.core.service.impl.sys;/**
 * Created by wust on 2017/6/13.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.generator.WyCodeGenerator;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.CollectionEnum;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.common.organization.*;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompany;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompanyList;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompanySearch;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartment;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartmentList;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartmentSearch;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganization;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganizationList;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganizationSearch;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRole;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRoleList;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRoleSearch;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUser;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import com.everwing.coreservice.common.wy.entity.system.user.UserResourceList;
import com.everwing.coreservice.common.wy.service.sys.TSysOrganizationService;
import com.everwing.coreservice.wy.dao.mapper.configuration.TBsProjectMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bc.project.TBcProjectMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
@Service("tSysOrganizationServiceImpl")
public class TSysOrganizationServiceImpl implements TSysOrganizationService {
    @Autowired
    private TSysOrganizationMapper tSysOrganizationMapper;

    @Autowired
    private TSysUserMapper tSysUserMapper;

    @Autowired
    private TSysCompanyMapper tSysCompanyMapper;

    @Autowired
    private TSysDepartmentMapper tSysDepartmentMapper;

    @Autowired
    private TSysProjectMapper tSysProjectMapper;

    @Autowired
    private TSysRoleMapper tSysRoleMapper;

    @Autowired
    private TBsProjectMapper tBsProjectMapper;
    
    @Autowired
    private TBcProjectMapper tBcProjectMapper;

    @Autowired
    private SpringRedisTools springRedisTools;

    @Override
    public List<TSysOrganizationList> findOrganizationTree(String companyId,TSysOrganizationSearch condition) {
        return tSysOrganizationMapper.findOrganizationTree(condition);
    }

    @Override
    public List<TSysOrganization> findByCondition(String companyId,TSysOrganizationSearch condition) {
        return tSysOrganizationMapper.findByCondition(condition);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap save(String companyId, JSONObject jsonObject) {
        MessageMap mm = new MessageMap();

        TSysOrganization tSysOrganization = jsonObject.get("tSysOrganization") == null ? null : (TSysOrganization)jsonObject.get("tSysOrganization");

        /**
         * 组织关系
         */
        TSysOrganizationSearch condition = new TSysOrganizationSearch();
        condition.setCode(tSysOrganization.getCode());
        condition.setType(tSysOrganization.getType());
        condition.setPid(tSysOrganization.getPid());
        List<TSysOrganization> tSysOrganizationList = tSysOrganizationMapper.findByCondition(condition);
        if(CollectionUtils.isNotEmpty(tSysOrganizationList)){
            throw new ECPBusinessException("该节点里面已经存在此条数据。");
        }else {
            if(tSysOrganization.getType().equalsIgnoreCase(WyEnum.organizationType_company.getStringValue())){
                TSysCompany tSysCompany = jsonObject.get("tSysCompany") == null ? null : (TSysCompany)jsonObject.get("tSysCompany");

                TSysCompanySearch tSysCompanySearch = new TSysCompanySearch();
                tSysCompanySearch.setName(tSysCompany.getName());
                List<TSysCompanyList> tSysCompanyLists = tSysCompanyMapper.findByCondition(tSysCompanySearch);
                if(CollectionUtils.isNotEmpty(tSysCompanyLists)){
                    throw new ECPBusinessException("公司["+tSysCompany.getName()+"]已经存在，请换一个公司名。");
                }
                tSysCompany.setCode(WyCodeGenerator.genCompanyCode());
                tSysCompanyMapper.insert(tSysCompany);
            }else if(tSysOrganization.getType().equalsIgnoreCase(WyEnum.organizationType_department.getStringValue())){
                TSysDepartment tSysDepartment = jsonObject.get("tSysDepartment") == null ? null : (TSysDepartment)jsonObject.get("tSysDepartment");
                tSysDepartment.setCode(WyCodeGenerator.genDepartmentCode());
                tSysDepartmentMapper.insert(tSysDepartment);
            }else if(tSysOrganization.getType().equalsIgnoreCase(WyEnum.organizationType_project.getStringValue())){
                TSysProject tSysProject = jsonObject.get("tSysProject") == null ? null : (TSysProject)jsonObject.get("tSysProject");
                tSysProject.setCode(WyCodeGenerator.genProjectCode());
                tSysProjectMapper.insert(tSysProject);

                 /* TODO 【【【不要这么做，需要优化掉】】】*/
                //insertTBsProject(tSysProject);	//同时插入本月与下月的数据
            }else if(tSysOrganization.getType().equalsIgnoreCase(WyEnum.organizationType_role.getStringValue())){
                TSysRole tSysRole = jsonObject.get("tSysRole") == null ? null : (TSysRole)jsonObject.get("tSysRole");
                tSysRole.setCode(WyCodeGenerator.genRoleCode());
                tSysRoleMapper.insert(tSysRole);
            }

            if(mm.getFlag().equals(MessageMap.INFOR_SUCCESS)){
                tSysOrganizationMapper.insert(tSysOrganization);
            }
        }
        return mm;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap delete(String companyId,TSysOrganization entity) {
        MessageMap mm = new MessageMap();
        tSysOrganizationMapper.delete(entity);
        return mm;
    }


    @Override
    public MessageMap findOrganizationByPid(String companyId, TSysOrganizationSearch tSysOrganizationSearch) {
        MessageMap mm = new MessageMap();

        TSysOrganizationSearch condition = new TSysOrganizationSearch();
        condition.setPid(tSysOrganizationSearch.getOrganizationId());
        List<TSysOrganization> tSysOrganizationList = tSysOrganizationMapper.findByCondition(condition);
        if(CollectionUtils.isNotEmpty(tSysOrganizationList)){
            Map<String,List<String>> groupOrganizationByType = groupOrganizationByType(tSysOrganizationList);
            List<String> companyIdList = groupOrganizationByType.get(WyEnum.organizationType_company.getStringValue());
            List<String> projectIdList = groupOrganizationByType.get(WyEnum.organizationType_project.getStringValue());
            List<String> departmentIdList = groupOrganizationByType.get(WyEnum.organizationType_department.getStringValue());
            List<String> roleIdList = groupOrganizationByType.get(WyEnum.organizationType_role.getStringValue());
            List<String> staffNumberList = groupOrganizationByType.get(WyEnum.organizationType_staff.getStringValue());

            if(CollectionUtils.isNotEmpty(companyIdList)){
                TSysCompanySearch tSysCompanySearch = tSysOrganizationSearch.gettSysCompanySearch();
                tSysCompanySearch.setCompanyIdList(companyIdList);
                List<TSysCompanyList> list = tSysCompanyMapper.listPage(tSysCompanySearch);
                BaseDto companyBaseDto = new BaseDto<>();
                companyBaseDto.setLstDto(list);
                companyBaseDto.setPage(tSysCompanySearch.getPage());
                mm.getMapMessage().put("companyBaseDto",companyBaseDto);
            }
            if(CollectionUtils.isNotEmpty(projectIdList)){
                TSysProjectSearch tSysProjectSearch = tSysOrganizationSearch.gettSysProjectSearch();
                tSysProjectSearch.setProjectIdList(projectIdList);
                List<TSysProjectList> list = tSysProjectMapper.listPage(tSysProjectSearch);
                BaseDto projectBaseDto = new BaseDto<>();
                projectBaseDto.setLstDto(list);
                projectBaseDto.setPage(tSysProjectSearch.getPage());
                mm.getMapMessage().put("projectBaseDto",projectBaseDto);
            }
            if(CollectionUtils.isNotEmpty(departmentIdList)){
                TSysDepartmentSearch tSysDepartmentSearch = tSysOrganizationSearch.gettSysDepartmentSearch();
                tSysDepartmentSearch.setDepartmentIdList(departmentIdList);
                List<TSysDepartmentList> list = tSysDepartmentMapper.listPage(tSysDepartmentSearch);
                BaseDto departmentBaseDto = new BaseDto<>();
                departmentBaseDto.setLstDto(list);
                departmentBaseDto.setPage(tSysDepartmentSearch.getPage());
                mm.getMapMessage().put("departmentBaseDto",departmentBaseDto);
            }
            if(CollectionUtils.isNotEmpty(roleIdList)){
                TSysRoleSearch tSysRoleSearch = tSysOrganizationSearch.gettSysRoleSearch();
                tSysRoleSearch.setRoleIdList(roleIdList);
                List<TSysRoleList> list = tSysRoleMapper.listPageRole(tSysRoleSearch);
                BaseDto roleBaseDto = new BaseDto<>();
                roleBaseDto.setLstDto(list);
                roleBaseDto.setPage(tSysRoleSearch.getPage());
                mm.getMapMessage().put("roleBaseDto",roleBaseDto);
            }
            if(CollectionUtils.isNotEmpty(staffNumberList)){
                TSysUserSearch tSysUserSearch = tSysOrganizationSearch.gettSysUserSearch();
                tSysUserSearch.setStaffNumberList(staffNumberList);
                List<TSysUserList> list = tSysUserMapper.listPageUser(tSysUserSearch);
                BaseDto userBaseDto = new BaseDto<>();
                userBaseDto.setLstDto(list);
                userBaseDto.setPage(tSysUserSearch.getPage());
                mm.getMapMessage().put("userBaseDto",userBaseDto);
            }
        }
        return mm;
    }


    private Map<String,TSysOrganization> groupOrganizationById = new HashMap<>(50);
    private Map<String,List<TSysOrganization>> groupOrganizationByPid = new HashMap<>(50);
    private Map<String,TSysOrganization> groupOrganizationByCode = new HashMap<>(50);
    private Map<String,TSysCompany> groupCompanyById = new HashMap<>(50);
    private Map<String,TSysDepartment>  groupDepartmentById = new HashMap<>(50);
    private Map<String,TSysProjectList> groupProjectById = new HashMap<>(50);
    private Map<String,TSysRoleList> groupRoleById = new HashMap<>(50);
    private Map<String,TSysUserList> groupUserByStaffNumber = new HashMap<>(50);


    /**
     * 根据bottomCompnentId获取其所有岗位、部门、项目以及公司信息集合
     * @param topComponentId    当前登录用户所属公司编码
     * @param bottomCompnentId  当前登录用户的登录账号或工号
     * @return
     */
    @Override
    public List<UserResourceList> getUserResourceListByKey(String topComponentId, String bottomCompnentId) {
        TSysUserList tSysUserList = tSysUserMapper.findUserByKey(bottomCompnentId);
        if(tSysUserList == null){
            throw new ECPBusinessException("工号或登录账号["+bottomCompnentId+"]在系统中不存在。");
        }

        List<UserResourceList> userResourceLists = new ArrayList<>(20);

        boolean isSystemAdmin = tSysUserList.getType().equalsIgnoreCase("systemAdmin");
        OrganizationComponent organizationComponent = getOrganizationComponent(isSystemAdmin,WyEnum.organizationType_company.getStringValue(),topComponentId,WyEnum.organizationType_staff.getStringValue(),bottomCompnentId);
        try {
            getUserResourceList(userResourceLists,new UserResourceList(),organizationComponent);
        } catch (CloneNotSupportedException e) {
            throw new ECPBusinessException(e);
        }
        return userResourceLists;
    }

    /**
     * 递归将组织树结构转换成列表结构
     * @param userResourceLists
     * @param userResourceList
     * @param organizationComponent
     * @throws CloneNotSupportedException
     */
    private void getUserResourceList(List<UserResourceList> userResourceLists,UserResourceList userResourceList,OrganizationComponent organizationComponent) throws CloneNotSupportedException {
        if(organizationComponent instanceof  CompanyComposite){
            TSysCompany tSysCompany = ((CompanyComposite)organizationComponent).gettSysCompany();
            if(tSysCompany != null){
                userResourceList.setCompanyId(tSysCompany.getCompanyId());
                userResourceList.setCompanyCode(tSysCompany.getCode());
                userResourceList.setCompanyName(tSysCompany.getName());
            }
        }else if(organizationComponent instanceof  DepartmentComposite){
            TSysDepartment tSysDepartment = ((DepartmentComposite)organizationComponent).gettSysDepartment();
            if(tSysDepartment != null){
                userResourceList.setDepartmentId(tSysDepartment.getDepartmentId());
                userResourceList.setDepartmentCode(tSysDepartment.getCode());
                userResourceList.setDepartmentName(tSysDepartment.getName());
            }
        }else if(organizationComponent instanceof  ProjectComposite){
            TSysProject tSysProject = ((ProjectComposite)organizationComponent).gettSysProject();
            if(tSysProject != null){
                userResourceList.setProjectId(tSysProject.getProjectId());
                userResourceList.setProjectCode(tSysProject.getCode());
                userResourceList.setProjectName(tSysProject.getName());
            }
        }else if(organizationComponent instanceof  RoleComposite){
            TSysRole tSysRole = ((RoleComposite)organizationComponent).gettSysRole();
            if(tSysRole != null){
                userResourceList.setRoleId(tSysRole.getRoleId());
                userResourceList.setRoleCode(tSysRole.getCode());
                userResourceList.setRoleName(tSysRole.getRoleName());
            }
        }else if(organizationComponent instanceof  EmployeeLeaf){
            TSysUser tSysUser = ((EmployeeLeaf)organizationComponent).gettSysUser();
            if(tSysUser != null){
                userResourceList.setStaffId(tSysUser.getUserId());
                userResourceList.setStaffNumber(tSysUser.getStaffNumber());
                userResourceList.setStaffName(tSysUser.getStaffName());
                userResourceList.setLoginName(tSysUser.getLoginName());
            }
        }
        if(organizationComponent.hasChildren()){
            List<OrganizationComponent> childrens = organizationComponent.getChildren();
            for (OrganizationComponent children : childrens) {
                UserResourceList childrenUserResourceList = (UserResourceList)userResourceList.clone();
                getUserResourceList(userResourceLists,childrenUserResourceList,children);
            }
        }else {
            userResourceLists.add(userResourceList);
        }
    }



    /**
     *
     * 先根据bottomCompnentType和bottomCompnentId向上递归，找出底部边界到顶部集合unionList，
     * 然后再根据条件topComponentType和topComponentId向下递归unionList，找出顶部边界的组织架构，
     * 最后的组织架构构件在区间bottomCompnentId和topComponentId之间，
     * 结果：OrganizationComponent >= bottomCompnentId &&  OrganizationComponent <= topComponentId
     * @param companyId
     * @param topComponentType  顶部边界类型：公司、部门、项目、岗位
     * @param topComponentId    边界类型对应的ID，如公司id，部门id...
     * @param bottomCompnentType    底部边界的类型：暂时只支持员工
     * @param bottomCompnentId      底部边界ID:如工号
     * @return 返回(OrganizationComponent >= bottomCompnentId &&  OrganizationComponent <= topComponentId)之间的集合
     */
    @Override
    public OrganizationComponent getOrganizationComponent(String companyId, String topComponentType,String topComponentId,String bottomCompnentType,String bottomCompnentId) {
        if(WyEnum.organizationType_staff.getStringValue().equalsIgnoreCase(bottomCompnentType)){
            TSysUserList tSysUserList = tSysUserMapper.findUserByKey(bottomCompnentId);
            if(tSysUserList == null){
                throw new ECPBusinessException("工号或登录账号["+bottomCompnentId+"]在系统中不存在。");
            }

            boolean isSystemAdmin = tSysUserList.getType().equalsIgnoreCase("systemAdmin");

            return getOrganizationComponent(isSystemAdmin,topComponentType,topComponentId,bottomCompnentType,tSysUserList.getStaffNumber());
        }else {
            throw new ECPBusinessException("底部边界暂时不支持员工以外的查询。");
        }
    }




    /**
     *
     * 先根据bottomCompnentType和bottomCompnentId向上递归，找出底部边界到顶部集合unionList，
     * 然后再根据条件topComponentType和topComponentId向下递归unionList，找出顶部边界的组织架构，
     * 最后的组织架构构件在区间bottomCompnentId和topComponentId之间，
     * 结果：OrganizationComponent >= bottomCompnentId &&  OrganizationComponent <= topComponentId
     * @param isSystemAdmin 是否是系统管理员
     * @param topComponentType
     * @param topComponentId
     * @param bottomComponentType
     * @param bottomComponentId
     * @return 返回(OrganizationComponent >= bottomCompnentId &&  OrganizationComponent <= topComponentId)之间的集合
     */
    private OrganizationComponent getOrganizationComponent(boolean isSystemAdmin,String topComponentType,String topComponentId,String bottomComponentType,String bottomComponentId){
        initData();

        OrganizationComponent organizationComponent = null;
        if(topComponentType.contains(WyEnum.organizationType_company.getStringValue())){
            CompanyComposite companyComposite = new CompanyComposite();
            TSysCompany tSysCompany = groupCompanyById.get(topComponentId);
            companyComposite.settSysCompany(tSysCompany);
            organizationComponent = companyComposite;
        }else if(topComponentType.contains(WyEnum.organizationType_department.getStringValue())){
            DepartmentComposite departmentComposite = new DepartmentComposite();
            TSysDepartment tSysDepartment = groupDepartmentById.get(topComponentId);
            departmentComposite.settSysDepartment(tSysDepartment);
            organizationComponent = departmentComposite;
        }else if(topComponentType.contains(WyEnum.organizationType_project.getStringValue())){
            ProjectComposite projectComposite = new ProjectComposite();
            TSysProject tSysProject = groupProjectById.get(topComponentId);
            projectComposite.settSysProject(tSysProject);
            organizationComponent = projectComposite;
        }else if(topComponentType.contains(WyEnum.organizationType_role.getStringValue())){
            RoleComposite roleComposite = new RoleComposite();
            TSysRole tSysRole = groupRoleById.get(topComponentId);
            roleComposite.settSysRole(tSysRole);
            organizationComponent = roleComposite;
        }else{
            throw new ECPBusinessException("您要获取的组件类型["+topComponentType+"]不合法");
        }

        if(!isSystemAdmin){
            // 非系统管理员只能看自己的组织关系，因此和系统管理员不同的是，普通员工需要先从下往上找出自己的组织关系

            TSysOrganizationSearch tSysOrganizationSearch = new TSysOrganizationSearch();
            tSysOrganizationSearch.setType(bottomComponentType);
            tSysOrganizationSearch.setCode(bottomComponentId);
            List<TSysOrganization> bottomValues = tSysOrganizationMapper.findByCondition(tSysOrganizationSearch);
            if(CollectionUtils.isNotEmpty(bottomValues)){
                /**
                 * 通过工号向上递归找出其组织架构
                 */
                Map<String,TSysOrganization> unionList = new HashMap<>(50);
                for (TSysOrganization staff : bottomValues) {
                    upwardRecursionOrganization(unionList,staff);
                }


                /**
                 * 根据pid分组该unionList集合
                 */
                Map<String,List<TSysOrganization>> groupByPid = new HashMap<>(50);
                Set<String> keys = unionList.keySet();
                for (String key : keys) {
                    TSysOrganization tSysOrganization = unionList.get(key);
                    if(groupByPid.containsKey(tSysOrganization.getPid())){
                        groupByPid.get(tSysOrganization.getPid()).add(tSysOrganization);
                    }else {
                        List<TSysOrganization> list = new ArrayList<>(10);
                        list.add(tSysOrganization);
                        groupByPid.put(tSysOrganization.getPid(),list);
                    }
                }
                doGetOrganizationComponent(organizationComponent,groupByPid);
            }
        }else{
            // 系统管理员可以看整个系统的组织关系
            doGetOrganizationComponent(organizationComponent,groupOrganizationByPid);
        }
        return organizationComponent;
    }




    /**
     * 向上递归，并将结果存储到map
     *
     * @param map
     * @param o
     */
    private void upwardRecursionOrganization(Map<String,TSysOrganization> map,TSysOrganization o){
        map.put(o.getOrganizationId(),o);
        if(groupOrganizationById.containsKey(o.getPid())){
            upwardRecursionOrganization(map,groupOrganizationById.get(o.getPid()));
        }
    }


    /**
     * 向下递归，构造组织架构组合构件
     * @param organizationComponent
     * @param groupByPid
     */
    private void doGetOrganizationComponent(OrganizationComponent organizationComponent,Map<String,List<TSysOrganization>> groupByPid){
        if(organizationComponent instanceof  CompanyComposite){ // 公司组件下面只能挂部门
            CompanyComposite companyComposite = (CompanyComposite)organizationComponent;
            TSysCompany tSysCompany = companyComposite.gettSysCompany();
            if(tSysCompany != null){
                TSysOrganization tSysOrganization = groupOrganizationByCode.get(tSysCompany.getCompanyId());
                List<TSysOrganization> childrens = groupByPid.get(tSysOrganization.getOrganizationId());
                if(CollectionUtils.isNotEmpty(childrens)){
                    for (TSysOrganization children : childrens) {
                        if(WyEnum.organizationType_department.getStringValue().equalsIgnoreCase(children.getType())){
                            TSysDepartment tSysDepartment = groupDepartmentById.get(children.getCode());
                            DepartmentComposite departmentComposite = new DepartmentComposite();
                            departmentComposite.settSysDepartment(tSysDepartment);
                            organizationComponent.add(departmentComposite);
                            doGetOrganizationComponent(departmentComposite,groupByPid);
                        }else {
                            doGetOrganizationComponent(organizationComponent,groupByPid);
                        }
                    }
                }
            }
        }else if(organizationComponent instanceof DepartmentComposite){ // 部门组件下面可以挂子公司、项目和岗位
            DepartmentComposite departmentComposite = (DepartmentComposite)organizationComponent;
            TSysDepartment tSysDepartment = departmentComposite.gettSysDepartment();
            if(tSysDepartment != null){
                TSysOrganization tSysOrganization = groupOrganizationByCode.get(tSysDepartment.getDepartmentId());
                List<TSysOrganization> childrens = groupByPid.get(tSysOrganization.getOrganizationId());
                if(CollectionUtils.isNotEmpty(childrens)){
                    for (TSysOrganization children : childrens) {
                        if(tSysDepartment.getName().contains("人力部")){
                            // 如果是人力部门，下面可以挂子公司和岗位

                            if(WyEnum.organizationType_company.getStringValue().equalsIgnoreCase(children.getType())){
                                TSysCompany tSysCompany = groupCompanyById.get(children.getCode());
                                CompanyComposite companyComposite = new CompanyComposite();
                                companyComposite.settSysCompany(tSysCompany);
                                organizationComponent.add(companyComposite);
                                doGetOrganizationComponent(companyComposite,groupByPid);
                            }else if(WyEnum.organizationType_role.getStringValue().equalsIgnoreCase(children.getType())){
                                TSysRole tSysRole = groupRoleById.get(children.getCode());
                                RoleComposite roleComposite = new RoleComposite();
                                roleComposite.settSysRole(tSysRole);
                                organizationComponent.add(roleComposite);
                                doGetOrganizationComponent(roleComposite,groupByPid);
                            }else {
                                doGetOrganizationComponent(organizationComponent,groupByPid);
                            }
                        }else if(tSysDepartment.getName().contains("项目部")){
                            // 如果是项目部门，下面可以挂项目

                            if(WyEnum.organizationType_project.getStringValue().equalsIgnoreCase(children.getType())){
                                TSysProject tSysProject = groupProjectById.get(children.getCode());
                                ProjectComposite projectComposite = new ProjectComposite();
                                projectComposite.settSysProject(tSysProject);
                                organizationComponent.add(projectComposite);
                                doGetOrganizationComponent(projectComposite,groupByPid);
                            }else {
                                doGetOrganizationComponent(organizationComponent,groupByPid);
                            }
                        }else {
                            // 其他部门只能挂岗位

                            TSysRole tSysRole = groupRoleById.get(children.getCode());
                            RoleComposite roleComposite = new RoleComposite();
                            roleComposite.settSysRole(tSysRole);
                            organizationComponent.add(roleComposite);
                            doGetOrganizationComponent(roleComposite,groupByPid);
                        }
                    }
                }
            }
        }else if(organizationComponent instanceof ProjectComposite){    // 项目组件下面可以挂部门
            ProjectComposite projectComposite = (ProjectComposite)organizationComponent;
            TSysProject tSysProject = projectComposite.gettSysProject();
            if(tSysProject != null){
                TSysOrganization tSysOrganization = groupOrganizationByCode.get(tSysProject.getProjectId());
                List<TSysOrganization> childrens = groupByPid.get(tSysOrganization.getOrganizationId());
                if(CollectionUtils.isNotEmpty(childrens)){
                    for (TSysOrganization children : childrens) {
                        if(WyEnum.organizationType_department.getStringValue().equalsIgnoreCase(children.getType())){
                            TSysDepartment tSysDepartment = groupDepartmentById.get(children.getCode());
                            DepartmentComposite departmentComposite = new DepartmentComposite();
                            departmentComposite.settSysDepartment(tSysDepartment);
                            organizationComponent.add(departmentComposite);
                            doGetOrganizationComponent(departmentComposite,groupByPid);
                        }else{
                            doGetOrganizationComponent(organizationComponent,groupByPid);
                        }
                    }
                }
            }
        }else if(organizationComponent instanceof RoleComposite){   // 岗位组件下面可以挂员工
            RoleComposite roleComposite = (RoleComposite)organizationComponent;
            TSysRole tSysRole = roleComposite.gettSysRole();
            if(tSysRole != null){
                TSysOrganization tSysOrganization = groupOrganizationByCode.get(tSysRole.getRoleId());
                List<TSysOrganization> childrens = groupByPid.get(tSysOrganization.getOrganizationId());
                if(CollectionUtils.isNotEmpty(childrens)){
                    for (TSysOrganization children : childrens) {
                        if(WyEnum.organizationType_staff.getStringValue().equalsIgnoreCase(children.getType())){
                            TSysUser tSysUser = groupUserByStaffNumber.get(children.getCode());
                            EmployeeLeaf employeeLeaf = new EmployeeLeaf();
                            employeeLeaf.settSysUser(tSysUser);
                            organizationComponent.add(employeeLeaf);
                        }else{
                            doGetOrganizationComponent(organizationComponent,groupByPid);
                        }
                    }
                }
            }
        }
    }


    private void initData(){
        groupOrganizationById = new HashMap<>(50);
        groupOrganizationByPid = new HashMap<>(50);
        groupOrganizationByCode = new HashMap<>(50);
        groupCompanyById = new HashMap<>(50);
        groupDepartmentById = new HashMap<>(50);
        groupProjectById = new HashMap<>(50);
        groupRoleById = new HashMap<>(50);
        groupUserByStaffNumber = new HashMap<>(50);

        TSysOrganizationSearch tSysOrganizationSearch = new TSysOrganizationSearch();
        List<TSysOrganization> tSysOrganizationList = tSysOrganizationMapper.findByCondition(tSysOrganizationSearch);

        groupOrganizationById(tSysOrganizationList);
        groupOrganizationByPid(tSysOrganizationList);
        groupOrganizationByCode(tSysOrganizationList);

        groupCompanyById();
        groupDepartmentById();
        groupProjectById();
        groupRoleById();
        groupUserByStaffNumber();
    }


    /**
     * 按照类型分组，每个类型得到一组code（对应其他表的主键）
     * @param tSysOrganizationList
     * @return key=对应的表，value=关联表的主键集合
     */
    private Map<String,List<String>> groupOrganizationByType(List<TSysOrganization> tSysOrganizationList){
        Map<String,List<String>> groupOrganizationByType = new HashMap<>(50);
        for (TSysOrganization tSysOrganization : tSysOrganizationList) {
            String key = tSysOrganization.getType();
            if(groupOrganizationByType.containsKey(key)){
                groupOrganizationByType.get(key).add(tSysOrganization.getCode());
            }else{
                List<String> codeList = new ArrayList<>(10);
                codeList.add(tSysOrganization.getCode());
                groupOrganizationByType.put(key,codeList);
            }
        }
        return groupOrganizationByType;
    }

    /**
     * 按照id分组组织
     * @return key=对应的表，value=关联表的主键集合
     */
    private void groupOrganizationById(List<TSysOrganization> tSysOrganizationList){
        for (TSysOrganization tSysOrganization : tSysOrganizationList) {
            groupOrganizationById.put(tSysOrganization.getOrganizationId(),tSysOrganization);
        }
    }

    /**
     * 按照pid分组组织
     * @return key=对应的表，value=关联表的主键集合
     */
    private void groupOrganizationByPid(List<TSysOrganization> tSysOrganizationList){
        for (TSysOrganization tSysOrganization : tSysOrganizationList) {
            String key = tSysOrganization.getPid();
            if(groupOrganizationByPid.containsKey(key)){
                groupOrganizationByPid.get(key).add(tSysOrganization);
            }else{
                List<TSysOrganization> codeList = new ArrayList<>(10);
                codeList.add(tSysOrganization);
                groupOrganizationByPid.put(key,codeList);
            }
        }
    }

    /**
     * 按照code分组组织
     * @return key=code，value=TSysOrganization
     */
    private void groupOrganizationByCode(List<TSysOrganization> tSysOrganizationList){
        for (TSysOrganization tSysOrganization : tSysOrganizationList) {
            String key = tSysOrganization.getCode();
            groupOrganizationByCode.put(key,tSysOrganization);
        }
    }



    /**
     * 按照id分组公司
     * @return key=对应的表，value=关联表的主键集合
     */
    private void groupCompanyById(){
        TSysCompanySearch tSysOrganizationSearch = new TSysCompanySearch();
        List<TSysCompanyList> allList = tSysCompanyMapper.findByCondition(tSysOrganizationSearch);
        for (TSysCompany tSysCompany : allList) {
            groupCompanyById.put(tSysCompany.getCompanyId(),tSysCompany);
        }
    }

    /**
     * 按照id分组部门
     * @return key=对应的表，value=关联表的主键集合
     */
    private void groupDepartmentById(){
        TSysDepartmentSearch tSysDepartmentSearch = new TSysDepartmentSearch();
        List<TSysDepartment> allList = tSysDepartmentMapper.findByCondition(tSysDepartmentSearch);
        for (TSysDepartment tSysDepartment : allList) {
            groupDepartmentById.put(tSysDepartment.getDepartmentId(),tSysDepartment);
        }
    }

    /**
     * 按照id分组项目
     * @return key=对应的表，value=关联表的主键集合
     */
    private void groupProjectById(){
        TSysProjectSearch tSysProjectSearch = new TSysProjectSearch();
        List<TSysProjectList> allList = tSysProjectMapper.findByCondition(tSysProjectSearch);
        for (TSysProjectList tSysProjectList : allList) {
            groupProjectById.put(tSysProjectList.getProjectId(),tSysProjectList);
        }
    }

    /**
     * 按照id分组岗位
     * @return key=对应的表，value=关联表的主键集合
     */
    private void groupRoleById(){
        TSysRoleSearch tSysRoleSearch = new TSysRoleSearch();
        List<TSysRoleList> allList = tSysRoleMapper.findByCondition(tSysRoleSearch);
        for (TSysRoleList tSysRoleList : allList) {
            groupRoleById.put(tSysRoleList.getRoleId(),tSysRoleList);
        }
    }

    /**
     * 按照工号分组员工
     * @return key=对应的表，value=关联表的主键集合
     */
    private void groupUserByStaffNumber(){
        TSysUserSearch tSysUserSearch = new TSysUserSearch();
        List<TSysUserList> allList = tSysUserMapper.findByCondition(tSysUserSearch);
        for (TSysUserList tSysUserList : allList) {
            groupUserByStaffNumber.put(tSysUserList.getStaffNumber(),tSysUserList);
        }
    }


    /**
     * TODO：备注，不要这么做，更不要为了省时间而直接在项目表加一个计费的字段，因为这里仅仅是基础数据，不应该在这里直接做业务操作，增加系统维护难度。
     * TODO：更好的做法：增加一个关系表，在计费模块增加一个按钮来手动初始化计费相关的数据，然后你在关系表随便你做什么都不会影响基础数据。
     * TODO：此处我把该方法标为作废，以后需要优化掉。
     * @param pro
     */
    @Deprecated
    private void insertTBsProject(TSysProject pro){
        /** 项目新增时, 同时创建项目计费状态数据 */
        TBsProject bp = new TBsProject();
        bp.setId(CommonUtils.getUUID());
        bp.setProjectId(pro.getCode());
        bp.setProjectName(pro.getName());
        bp.setWyOrder(1);
        bp.setWyStatus(1);
        bp.setBtOrder(2);
        bp.setBtStatus(1);
        bp.setWaterOrder(3);
        bp.setWaterStatus(1);
        bp.setElectOrder(4);
        bp.setElectStatus(1);
        bp.setCommonStatus(1);
        bp.setCurrentFee(0.0);
        bp.setLastOwedFee(0.0);
        bp.setTotalFee(0.0);
        bp.setBillingTime(new Date());
        bp.setCreateId(pro.getCreaterId());
        bp.setModifyId(pro.getModifyId());
        bp.setStatus(0);
        bp.setIsGenBill(BillingEnum.bill_is_gen_no.getIntV());
        this.tBsProjectMapper.insert(bp);	//本月的t_bs_project
        
        bp.setId(CommonUtils.getUUID());
        bp.setBillingTime(CommonUtils.addMonth(new Date(), 1));
        this.tBsProjectMapper.insert(bp);	//下月的t_bs_project
        
        //托收项目
        TBcProject bcp = new TBcProject();
        bcp.setId(CommonUtils.getUUID());
        bcp.setProjectId(pro.getCode());
        bcp.setProjectName(pro.getName());
        bcp.setWyType(CollectionEnum.type_off.getV());			//物业管理费状态: 未启动
        bcp.setBtType(CollectionEnum.type_off.getV());			//本体基金状态: 未启动
        bcp.setWaterType(CollectionEnum.type_off.getV());		//水费托收状态: 未启动
        bcp.setElectType(CollectionEnum.type_off.getV());		//电费托收状态 : 未启动
        bcp.setWyStatus(CollectionEnum.status_off.getV());		//物业管理费托收关闭
        bcp.setBtStatus(CollectionEnum.status_off.getV());		//本体基金托收关闭
        bcp.setWaterStatus(CollectionEnum.status_off.getV());	//水费托收关闭
        bcp.setElectStatus(CollectionEnum.status_off.getV());	//电费托收关闭
        bcp.setStatus(CollectionEnum.status_off.getV());	//项目托收关闭
        bcp.setUnionPrivateZone(Constants.STR_EMPTY);		//银联私有域
        bcp.setCreateTime(new Date());
        bcp.setModifyTime(new Date());
        bcp.setCreateId(pro.getCreaterId());
        bcp.setModifyId(bcp.getModifyId());
    	bcp.setUnionCount(0);
    	bcp.setJrlCount(0);
        this.tBcProjectMapper.insert(bcp);
    }
}
