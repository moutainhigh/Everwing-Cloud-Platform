/**
 * Project Name:webTYJ
 * File Name:TUserServiceImpl.java
 * Package Name:com.flf.service.system.impl
 * Date:2016年12月12日下午2:22:19
 * Copyright (c) 2016, wusongti All Rights Reserved.
 *
 */

package com.everwing.coreservice.wy.core.service.impl.sys;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.wy.common.UserContextModel;
import com.everwing.coreservice.common.wy.common.enums.LookupEnum;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.common.organization.OrganizationComponent;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompany;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompanyList;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompanySearch;
import com.everwing.coreservice.common.wy.entity.system.menu.TSysMenu;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganizationList;
import com.everwing.coreservice.common.wy.entity.system.resource.TSysResource;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUser;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysOrganizationService;
import com.everwing.coreservice.common.wy.service.sys.TSysUserService;
import com.everwing.coreservice.platform.api.AccountApi;
import com.everwing.coreservice.platform.api.CompanyApi;
import com.everwing.coreservice.wy.dao.mapper.sys.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


@Service("tSysUserServiceImpl")
public class TSysUserServiceImpl implements TSysUserService
{
    static Logger logger = LogManager.getLogger(TSysUserServiceImpl.class);

    @Autowired
    private TSysUserMapper tSysUserMapper;

    @Autowired
    private TSysMenuMapper tSysMenuMapper;

    @Autowired
    private TSysResourceMapper tSysResourceMapper;

    @Autowired
    private TSysDataPrivilegeMapper tSysDataPrivilegeMapper;

    @Autowired
    private TSysOrganizationMapper tSysOrganizationMapper;


    @Autowired
    private AccountApi accountApi;

    @Autowired
    private CompanyApi companyApi;


    @Autowired
    private TSysRoleMapper tSysRoleMapper;

    @Autowired
    private TSysDepartmentMapper tSysDepartmentMapper;

    @Autowired
    private TSysProjectMapper tSysProjectMapper;

    @Autowired
    private TSysCompanyMapper tSysCompanyMapper;

    @Autowired
    private TSysOrganizationService tSysOrganizationService;



    @Override
    public BaseDto listPageUser(WyBusinessContext ctx, TSysUserSearch condition)
    {
        List<TSysUserList> list = tSysUserMapper.listPageUser(condition);
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(list)){
            for (TSysUserList tSysUserList : list) {
                tSysUserList.setDocumentTypeName( DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                        LookupEnum.documentType.name(),
                        tSysUserList.getDocumentType()));

                tSysUserList.setSexName( DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                        LookupEnum.sex.name(),
                        tSysUserList.getSex()));


                tSysUserList.setStaffStateName( DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),
                        LookupEnum.staffState.name(),
                        tSysUserList.getStaffState()));
            }
        }
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(condition.getPage());
        return baseDto ;
    }


    @Override
    public List<TSysUserList> findByCondition(WyBusinessContext ctx, TSysUserSearch tSysUserSearch) {
        return tSysUserMapper.findByCondition(tSysUserSearch);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap insert(WyBusinessContext ctx, TSysUser entity) {
        MessageMap mm = new MessageMap();

        TSysUserSearch condition = new TSysUserSearch();
        condition.setLoginName(entity.getLoginName());
        List<TSysUserList> list = tSysUserMapper.findByCondition(condition);
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(list)){
            TSysUser modify = new TSysUser();
            try {
                BeanUtils.copyProperties(modify,list.get(0));
                modify.setStaffName(entity.getStaffName());
                modify.setMobileTelephone(entity.getMobileTelephone());
                modify.setSex(entity.getSex());
                modify.setPassword(entity.getPassword());
                modify.setDocumentType(entity.getDocumentType());
                modify.setDocumentNumber(entity.getDocumentNumber());
                modify.setJoinDate(new Date());
                modify.setStaffState(entity.getStaffState());
                tSysUserMapper.modify(modify);
            } catch (IllegalAccessException e) {
                throw new ECPBusinessException("入职对象转换失败，"+e.getMessage());
            } catch (InvocationTargetException e) {
                throw new ECPBusinessException("入职对象转换失败，"+e.getMessage());
            }
        }else{
            String maxNumberStr = tSysUserMapper.getMaxStaffNumber();
            if(StringUtils.isEmpty(maxNumberStr)){
                maxNumberStr = "100000";
            }else{
                maxNumberStr = (Long.parseLong(maxNumberStr) + 1) + "";
            }

            entity.setStatus(LookupItemEnum.enableDisable_enable.getStringValue());
            entity.setStaffNumber(maxNumberStr);
            entity.setType(LookupItemEnum.staffType_staff.getStringValue());
            tSysUserMapper.insert(entity);
        }
        return mm;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public MessageMap modify(WyBusinessContext ctx, TSysUser entity)
    {
        MessageMap mm = new MessageMap();
        tSysUserMapper.modify(entity);
        return mm;
    }



    /**
     * 登录，检测用户是否合法，并获取资源权限
     * @param loginName
     * @param password
     * @return
     */
    @Override
    public MessageMap login(String loginName, String password) {
        MessageMap mm = new MessageMap();
        UserContextModel userContextModel = new UserContextModel();

        String companyId = null;

        logger.info("#########################登录开始，去平台查询账号是否合法，登录账号{}，登录密码{}#########################",loginName,"***");
        /**
         * 验证用户平台的数据合法性
         */
        RemoteModelResult<Account> accountRemoteModelResult = accountApi.queryByAccountNameAndPsw(loginName, Dict.ACCOUNT_TYPE_STAFF.getIntValue(), password);
        if(accountRemoteModelResult.isSuccess()) {
            Account account = accountRemoteModelResult.getModel();
            if(account == null){
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("您还没有注册，无法登录系统。");
                logger.info("#########################平台没有找到该用户，登录账号{}，登录密码{}#########################",loginName,"***");
                return mm;
            }else{
                logger.info("#########################平台匹成功配到该用户，开始去平台匹配用户对应的公司，登录账号{}，登录密码{}#########################",loginName,"***");

                RemoteModelResult<Company> companyInfoRemoteModelResult = companyApi.queryCompanyByUserLoginNameAndPsw(loginName,Dict.ACCOUNT_TYPE_STAFF.getIntValue(),password);
                if(companyInfoRemoteModelResult.isSuccess()) {
                    Company companyInfo = companyInfoRemoteModelResult.getModel();
                    if(companyInfo == null){
                        mm.setFlag(MessageMap.INFOR_WARNING);
                        mm.setMessage("平台关联不到您的公司，请联系管理员。");
                        logger.info("#########################平台匹成功配到该用户，但平台没有该用户对应的公司，考虑平台的公司数据问题，登录账号{}，登录密码{}#########################",loginName,"***");
                        return mm;
                    }else {
                        companyId = companyInfo.getCompanyId();
                        logger.info("#########################平台匹成功配到该用户和公司{}，登录账号{}，登录密码{}#########################",companyInfo.getCompanyId()+companyInfo.getCompanyName(),loginName,"***");
                    }
                }else{
                    mm.setFlag(MessageMap.INFOR_ERROR);
                    mm.setMessage(companyInfoRemoteModelResult.getMsg());
                    logger.info("#########################平台匹公司失败，登录账号{}，登录密码{}，平台返回的失败结果{}#########################",loginName,"***",companyInfoRemoteModelResult.getMsg());
                    return mm;
                }
            }
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(accountRemoteModelResult.getMsg());
            logger.info("#########################平台匹用户失败，登录账号{}，登录密码{}，平台返回的失败结果{}#########################",loginName,"***",accountRemoteModelResult.getMsg());
            return mm;
        }


        // 切换数据源
        WyBusinessContext.getContext().setCompanyId(companyId);


        /**
         * 设置父公司信息
         */
        TSysCompanySearch tSysCompanySearch = new TSysCompanySearch();
        tSysCompanySearch.setCompanyId(companyId);
        List<TSysCompanyList> tSysCompanyList = tSysCompanyMapper.findByCondition(tSysCompanySearch);
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tSysCompanyList)){
            TSysCompany parentCompany = tSysCompanyList.get(0);
            userContextModel.setRootCompany(parentCompany);
        }



        /**
         * 判断物业云平台员工状态
         */
        TSysUserSearch tSysUserSearch = new TSysUserSearch();
        tSysUserSearch.setLoginName(loginName);
        List<TSysUserList> tSysUserLists = tSysUserMapper.findByCondition(tSysUserSearch);
        if(org.apache.commons.collections.CollectionUtils.isEmpty(tSysUserLists)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("系统没有这个用户。");
            logger.info("#########################系统没有这个用户，登录账号{}，登录密码{}#########################",loginName,"***");
            return mm;
        }else{
            TSysUserList tSysUserList = tSysUserLists.get(0);
            if(!LookupItemEnum.enableDisable_enable.getStringValue().equalsIgnoreCase(tSysUserList.getStatus())){
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("您没有权限进入系统。");
                logger.info("#########################您没有权限进入系统，登录账号{}，登录密码{}#########################",loginName,"***");
                return mm;
            }else if(!LookupItemEnum.staffState_joined.getStringValue().equalsIgnoreCase(tSysUserList.getStaffState())){
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("您已离职，不能登录系统。");
                logger.info("#########################您已离职，不能登录系统，状态为{}，登录账号{}，登录密码{}#########################",tSysUserList.getStaffState(),loginName,"***");
                return mm;
            }else{
                tSysUserList.setPassword(password);
                userContextModel.setLoginUser(tSysUserList);
            }
        }




        /**
         * 判断员工类型，根据指定类型获取资源权限
         */
        List<TSysMenu> menus = null;                            // 非白名单菜单
        Map<Integer,List<TSysMenu>> groupMenusByLevel = null;
        Map<String,List<TSysMenu>> groupMenusByPid = null;

        List<TSysResource> resources = null;                    // 非白名单资源
        List<TSysResource> resources4anon = null;               // 白名单资源

        Map<String,List<TSysResource>> resourcesByPid = null;
        if(LookupItemEnum.staffType_systemAdmin.getStringValue().equals(userContextModel.getLoginUser().getType())){ // 系统管理员
            logger.info("#########################获取系统管理员的菜单资源权限，登录账号{}，登录密码{}#########################",loginName,"***");

            menus = tSysMenuMapper.findAllMenus4SystemAdmin();
            groupMenusByLevel = groupByLevelMenus(menus);
            groupMenusByPid = groupByPidMenus(menus);

            resources = tSysResourceMapper.findAllResources4SystemAdmin();
            resourcesByPid = groupByMenuIdResources(resources);
            resources4anon = tSysResourceMapper.findAllAnonResources4SystemAdmin();
        }else{ // 普通员工
            logger.info("#########################获取普通员工的菜单资源权限，登录账号{}，登录密码{}#########################",loginName,"***");

            List<String> roleIds = getRoleIdsByStaffNumber(tSysUserLists.get(0).getStaffNumber());
            if(org.apache.commons.collections.CollectionUtils.isNotEmpty(roleIds)){
                Map<String,Object> params = new HashMap<>();
                params.put("roleIds",roleIds);

                menus = tSysMenuMapper.findMenu(params);
                groupMenusByLevel = groupByLevelMenus(menus);
                groupMenusByPid = groupByPidMenus(menus);

                resources = tSysResourceMapper.findResources(params);
                resources4anon = tSysResourceMapper.findAnonResources(params);
                resourcesByPid = groupByMenuIdResources(resources);

                // 获取全局白名单资源集合
                List<TSysResource> commonAnonResources = tSysResourceMapper.findCommonAnonResources();
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(commonAnonResources)){
                    for (TSysResource commonAnonResource : commonAnonResources) {
                        resources4anon.add(commonAnonResource);
                    }
                }
            }
        }


        /**
         * 获取当前登录用户的组织架构
         */
        OrganizationComponent organizationComponent = tSysOrganizationService.getOrganizationComponent(companyId,
                WyEnum.organizationType_company.getStringValue(),
                companyId,WyEnum.organizationType_staff.getStringValue(),
                userContextModel.getLoginUser().getLoginName());


        // 放入用户上下文
        userContextModel.setGroupMenusByLevel(groupMenusByLevel);
        userContextModel.setGroupMenusByPid(groupMenusByPid);
        userContextModel.setResources(resources);
        userContextModel.setAnonResources(resources4anon);
        userContextModel.setGroupResourcesByPid(resourcesByPid);
        userContextModel.setOrganizationComponent(organizationComponent);
        mm.setObj(userContextModel);

        logger.info("#########################登录成功，登录账号{}，登录密码{}#########################",loginName,"***");
        return mm;
    }





    /**
     * 根据工号获取岗位集合
     * @param staffNumber
     * @return
     */
    private List<String> getRoleIdsByStaffNumber(String staffNumber){
        List<String> roleIdList = new ArrayList<>(10);
        List<TSysOrganizationList> tSysOrganizationLists = tSysOrganizationMapper.findRolesByStaffNumber(staffNumber);
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tSysOrganizationLists)){
            for (TSysOrganizationList tSysOrganizationList : tSysOrganizationLists) {
                roleIdList.add(tSysOrganizationList.getCode());
            }
        }
        return  roleIdList;
    }


    /**
     * 对菜单按级别分组
     * @param menus
     * @return
     */
    private Map<Integer,List<TSysMenu>> groupByLevelMenus(List<TSysMenu> menus){
        Map<Integer,List<TSysMenu>> integerListMap = new HashMap<>();
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(menus)){
            for (TSysMenu menu : menus) {
                Integer key = menu.getMenuLevel();
                if(integerListMap.containsKey(key)){
                    integerListMap.get(key).add(menu);
                }else{
                    List<TSysMenu> menuList = new ArrayList<>(10);
                    menuList.add(menu);
                    integerListMap.put(key,menuList);
                }
            }
            return integerListMap;
        }
        return null;
    }

    /**
     * 对菜单按父菜单id分组
     * @param menus
     * @return
     */
    private Map<String,List<TSysMenu>> groupByPidMenus(List<TSysMenu> menus){
        Map<String,List<TSysMenu>> stringListMap = new HashMap<>();
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(menus)){
            for (TSysMenu menu : menus) {
                String key = menu.getpId();
                if(stringListMap.containsKey(key)){
                    stringListMap.get(key).add(menu);
                }else{
                    List<TSysMenu> menuList = new ArrayList<>(10);
                    menuList.add(menu);
                    stringListMap.put(key,menuList);
                }
            }
            return stringListMap;
        }
        return null;
    }


    /**
     * 对资源按菜单分组
     * @param resources
     * @return
     */
    private Map<String,List<TSysResource>> groupByMenuIdResources(List<TSysResource> resources){
        Map<String,List<TSysResource>> stringListHashMap = new HashMap<>();
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(resources)){
            for (TSysResource resource : resources) {
                String key = resource.getMenuId();
                if(stringListHashMap.containsKey(key)){
                    stringListHashMap.get(key).add(resource);
                }else{
                    List<TSysResource> resourceList = new ArrayList<>(10);
                    resourceList.add(resource);
                    stringListHashMap.put(key,resourceList);
                }
            }
            return stringListHashMap;
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public BaseDto listPageUserOther(String companyId, TSysUserSearch condition) {
        return new BaseDto(this.tSysUserMapper.listPageOther(condition),condition.getPage());
    }

    @Override
    public BaseDto listPageUserInJg(String companyId, TSysUserSearch condition) {
        return new BaseDto(this.tSysUserMapper.listPageUserInJg(condition),condition.getPage());
    }

    @Override
    public List<Map<String, String>> findByCompanyId(String companyId) {
        return tSysUserMapper.selectByCompanyId(companyId);
    }

    @Override
    public TSysUser findByPrimaryKey(String companyId,String userId){
        return tSysUserMapper.selectByPrimaryKey(userId);
    }

}

