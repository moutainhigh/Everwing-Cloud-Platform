package com.everwing.coreservice.wy.core.service.impl.sys;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.entity.system.menu.TSysMenu;
import com.everwing.coreservice.common.wy.entity.system.menu.TSysMenuTree;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganization;
import com.everwing.coreservice.common.wy.entity.system.relation.PopMenuResourceTreeMap;
import com.everwing.coreservice.common.wy.entity.system.relation.ResourceTreeMap;
import com.everwing.coreservice.common.wy.entity.system.relation.TSysRoleResource;
import com.everwing.coreservice.common.wy.entity.system.resource.TSysResource;
import com.everwing.coreservice.common.wy.entity.system.resource.TSysResourceSearch;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRole;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRoleList;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRoleSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysRoleService;
import com.everwing.coreservice.common.xml.XMLAbstractResolver;
import com.everwing.coreservice.common.xml.XMLDefinitionFactory;
import com.everwing.coreservice.common.xml.factory.XMLWyPermissionFactory;
import com.everwing.coreservice.wy.dao.mapper.sys.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service("tSysRoleServiceImpl")
public class TSysRoleServiceImpl implements TSysRoleService {
	@Autowired
	private TSysRoleMapper tSysRoleMapper;

	@Autowired
	private TSysRoleResourceMapper tSysRoleResourceMapper;

	@Autowired
    private TSysMenuMapper tSysMenuMapper;

    @Autowired
    private TSysResourceMapper tSysResourceMapper;

    @Autowired
    private TSysOrganizationMapper tSysOrganizationMapper;


    @Override
    public BaseDto listPageRole(WyBusinessContext ctx, TSysRoleSearch condition)
    {
        List<TSysRoleList> list = tSysRoleMapper.listPageRole(condition);

        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(condition.getPage());
        return baseDto ;
    }


    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap save(WyBusinessContext ctx, TSysRole entity)
    {
        MessageMap mm = new MessageMap();

        TSysRoleSearch condition = new TSysRoleSearch();
        condition.setCode(entity.getCode());
        List<TSysRoleList> list = tSysRoleMapper.findByCondition(condition);

        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(list) && StringUtils.isNotEmpty(entity.getRoleId())){
            if(WyEnum.su_admin.name().equalsIgnoreCase(list.get(0).getRoleName())){
                throw new ECPBusinessException("不能对超级管理员执行此操作");
            }else{
                tSysRoleMapper.modify(entity);
            }
        }else{
            if(!CollectionUtils.isEmpty(list)){
                throw new ECPBusinessException("该岗位编码["+entity.getCode()+"]已经存在，请换个试试");
            }

            tSysRoleMapper.insert(entity);
        }
        return  mm;
    }

    @Override
    public TSysRoleList findByGuid(String companyId,String guid)
    {
        TSysRoleSearch condition = new TSysRoleSearch();
        condition.setRoleId(guid);
        List<TSysRoleList> list = tSysRoleMapper.findByCondition(condition);
        if(!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }



    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap delete(String companyId,String guids)
    {
        MessageMap mm = new MessageMap();

        List<String> guidList = CommonUtils.str2List(guids,",");

        // 先删除角色资源表
        tSysRoleResourceMapper.deleteRoleResourceByRoleIds(guidList);

        // 后删除角色表
        tSysRoleMapper.deleteRoleByRoleIds(guidList);

        // 解除组织关系
        TSysOrganization tSysOrganization = new TSysOrganization();
        tSysOrganization.setCode(guidList.get(0));
        tSysOrganization.setType(WyEnum.organizationType_role.getStringValue());
        tSysOrganizationMapper.delete(tSysOrganization);

        return mm;
    }


    @Override
    @Transactional(rollbackFor=Exception.class)
    public MessageMap changeStatus(WyBusinessContext ctx, TSysRole entity)
    {
        MessageMap mm = new MessageMap();
        if(WyEnum.su_admin.name().equalsIgnoreCase(entity.getRoleName())){
            // 不能修改超级管理员
            throw new ECPBusinessException("不能对超级管理员执行此操作");
        }else{
            if(LookupItemEnum.enableDisable_enable.getStringValue().equals(entity.getStatus())){
                entity.setStatus(LookupItemEnum.enableDisable_disable.getStringValue());
                tSysRoleMapper.modify(entity);
            }else if(LookupItemEnum.enableDisable_disable.getStringValue().equals(entity.getStatus())){
                entity.setStatus(LookupItemEnum.enableDisable_enable.getStringValue());
                tSysRoleMapper.modify(entity);
            }else{
                throw new ECPBusinessException("数据不合法");
            }
        }
        return mm;
    }

    /**
     * 初始化菜单资源
     * @param companyId
     * @return
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public MessageMap initResources(String companyId)
    {
        MessageMap mm = new MessageMap();

        XMLDefinitionFactory xmlDefinitionFactory = new XMLWyPermissionFactory();
        XMLAbstractResolver xmlAbstractResolver = xmlDefinitionFactory.createXMLResolver();

        Map<String,List> map = xmlAbstractResolver.getResult();
        List<TSysMenu> parseMenuList = map.get("parseMenuList");
        List<TSysResource> parseResourceList = map.get("parseResourceList");

        TSysRoleResource tSysRoleResourceSearch = new TSysRoleResource();

        // 剔除已经分配的脏数据（菜单）
        tSysRoleResourceSearch.setSrcType(WyEnum.m.name());
        List<TSysRoleResource> tSysRoleResourceMenus = tSysRoleResourceMapper.findByCondition(tSysRoleResourceSearch);
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tSysRoleResourceMenus)){
            Set<String> invalidSrcIdSet = new HashSet<>(10); // 一般不会有10个以上的菜单突然失效，除非重构系统

            for (TSysRoleResource tSysRoleResourceMenu : tSysRoleResourceMenus) {
                boolean flag = false;
                for (TSysMenu tSysMenu : parseMenuList) {
                    if (tSysMenu.getMenuId().equals(tSysRoleResourceMenu.getSrcId())) {
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    invalidSrcIdSet.add(tSysRoleResourceMenu.getSrcId());
                }
            }


            if(org.apache.commons.collections.CollectionUtils.isNotEmpty(invalidSrcIdSet)){
                for (String s : invalidSrcIdSet) {
                    tSysRoleResourceMapper.deleteRoleResourceBySrcId(WyEnum.m.name(),s);
                }
            }
        }

        // 剔除已经分配的脏数据（资源）
        tSysRoleResourceSearch.setSrcType(WyEnum.r.name());
        List<TSysRoleResource> tSysRoleResources = tSysRoleResourceMapper.findByCondition(tSysRoleResourceSearch);
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tSysRoleResources)){
            Set<String> invalidSrcIdSet = new HashSet<>(10); // 一般不会有10个以上的菜单突然失效，除非重构系统

            for (TSysRoleResource tSysRoleResource : tSysRoleResources) {
                boolean flag = false;
                for (TSysResource tSysResource : parseResourceList) {
                    if (tSysResource.getSrcId().equals(tSysRoleResource.getSrcId())) {
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    invalidSrcIdSet.add(tSysRoleResource.getSrcId());
                }
            }
            if(org.apache.commons.collections.CollectionUtils.isNotEmpty(invalidSrcIdSet)){
                for (String s : invalidSrcIdSet) {
                    tSysRoleResourceMapper.deleteRoleResourceBySrcId(WyEnum.r.name(),s);
                }
            }
        }

        // 生成菜单
        if(!CollectionUtils.isEmpty(parseMenuList)){
            tSysMenuMapper.clearMenu();
            tSysMenuMapper.batchInsert(parseMenuList);
        }

        // 生成资源
        if(!CollectionUtils.isEmpty(parseResourceList)){
            tSysResourceMapper.clearResource();
            tSysResourceMapper.batchInsert(parseResourceList);
        }


        List<TSysRoleResource> tSysRoleResourceListNew = new ArrayList<>();

        // 查询已经分配了权限的所有角色，并为这些角色重新设置白名单权限（有可能升级的时候增加了白名单权限，因此需要重新设置）
        List<TSysRoleResource> menuIdsGroupByRoleId = tSysRoleResourceMapper.findMenuIdGroupByRoleId();
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(menuIdsGroupByRoleId)){
            for (TSysRoleResource tSysRoleResource : menuIdsGroupByRoleId) {
                String menuId = tSysRoleResource.getSrcId();
                String roleId = tSysRoleResource.getRoleId();

                // 该菜单下面的白名单资源
                List<TSysResource> annonResourcesByMenuId = tSysResourceMapper.findAnonResourcesByMenuId(menuId);
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(annonResourcesByMenuId)){
                    for (TSysResource tSysResource : annonResourcesByMenuId) {
                        tSysRoleResourceMapper.deleteRoleResourceByRoleIdAndSrcId(roleId,tSysResource.getSrcId());
                        TSysRoleResource tSysRoleResourceNew = new TSysRoleResource();
                        tSysRoleResourceNew.setRoleId(roleId);
                        tSysRoleResourceNew.setSrcType(WyEnum.r.name());
                        tSysRoleResourceNew.setSrcId(tSysResource.getSrcId());
                        tSysRoleResourceNew.setId(UUID.randomUUID().toString());
                        tSysRoleResourceListNew.add(tSysRoleResourceNew);
                    }
                }
            }
            if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tSysRoleResourceListNew)){
                tSysRoleResourceMapper.batchInsert(tSysRoleResourceListNew);
            }
        }
        return mm;
    }


    @Override
    public List<PopMenuResourceTreeMap> findResourceTreeByRoleId(String companyId,String toRoleId)
    {
        List<PopMenuResourceTreeMap> resultList = new ArrayList<PopMenuResourceTreeMap>();
        if(StringUtils.isBlank(CommonUtils.null2String(toRoleId))){
            return resultList;  
        }else{
            // 叶子菜单编码集合
            Set<String> leafNodeSet = new HashSet<>();
            
            // 根据授予角色对象获取菜单树
            List<TSysMenuTree> menuList =  tSysRoleMapper.findMenuTreeByRoleId(toRoleId);
            if(!CollectionUtils.isEmpty(menuList)){
                for(TSysMenuTree menu : menuList){
                    if(menu.getMenuLevel() != null && menu.getMenuLevel() == -1){   // 叶子节点
                        leafNodeSet.add(menu.getMenuId());
                    }
                    PopMenuResourceTreeMap popMenuResource = new PopMenuResourceTreeMap();
                    popMenuResource.setId(menu.getMenuId());
                    popMenuResource.setName(menu.getMenuDesc());
                    popMenuResource.setpId(menu.getpId());
                    popMenuResource.setType(WyEnum.m.name());
                    popMenuResource.setChecked(menu.getChecked());
                    resultList.add(popMenuResource);
                }
            }
            
            // 根据叶子菜单获取资源
            if(!CollectionUtils.isEmpty(leafNodeSet)){
                List<ResourceTreeMap> resourceListAll =  tSysRoleMapper.findResourceTreeByRoleId(toRoleId);
                if(!CollectionUtils.isEmpty(resourceListAll)){
                    Map<String,List<ResourceTreeMap>> resultMapAll = groupResourcesByMenuId(resourceListAll);
                    
                    for(String menuId : leafNodeSet){
                        List<ResourceTreeMap> resourceList = resultMapAll.get(menuId);
                        if(CollectionUtils.isEmpty(resourceList)){
                            continue;
                        }
                        for(ResourceTreeMap resource : resourceList){
                            PopMenuResourceTreeMap popMenuResource = new PopMenuResourceTreeMap();
                            popMenuResource.setId(resource.getSrcId());
                            popMenuResource.setName(resource.getSrcDesc());
                            popMenuResource.setpId(menuId);
                            popMenuResource.setType(WyEnum.r.name());
                            popMenuResource.setChecked(resource.getChecked());
                            resultList.add(popMenuResource);
                        }
                    }
                }
            }
        }

        // 打印菜单权限
        // pringt();
        return resultList;
    }

    private void pringt(){
        /**
         * 根据pid分组菜单
         */
        List<TSysMenu> tSysMenus = tSysMenuMapper.findAllMenus4SystemAdmin();
        Map<String,List<TSysMenu>> groupMenuByPidMap = new HashMap<>();
        for (TSysMenu tSysMenu : tSysMenus) {
            String key = tSysMenu.getpId();
            if(groupMenuByPidMap.containsKey(key)){
                groupMenuByPidMap.get(key).add(tSysMenu);
            }else{
                List<TSysMenu> menuList = new ArrayList<>(10);
                menuList.add(tSysMenu);
                groupMenuByPidMap.put(key,menuList);
            }
        }


        /**
         * 根据menuid分组资源
         */
        TSysResourceSearch tSysResourceSearch = new TSysResourceSearch();
        List<TSysResource> tSysResources = tSysResourceMapper.findByCondition(tSysResourceSearch);
        Map<String,List<TSysResource>> groupResouceByMenuIdMap = new HashMap<>();
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tSysResources)){
            for (TSysResource resource : tSysResources) {
                String key = resource.getMenuId();
                if(groupResouceByMenuIdMap.containsKey(key)){
                    groupResouceByMenuIdMap.get(key).add(resource);
                }else{
                    List<TSysResource> resourceList = new ArrayList<>(10);
                    resourceList.add(resource);
                    groupResouceByMenuIdMap.put(key,resourceList);
                }
            }
        }

        digui(groupMenuByPidMap,groupResouceByMenuIdMap,null);
    }

    private void digui(Map<String,List<TSysMenu>> groupMenuByPidMap,Map<String,List<TSysResource>> groupResouceByMenuIdMap,String menuId){
        List<TSysMenu> tSysMenus = groupMenuByPidMap.get(menuId);
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tSysMenus)){
            for (TSysMenu tSysMenu : tSysMenus) {
                System.out.println("菜单:"+tSysMenu.getMenuDesc());
                digui(groupMenuByPidMap,groupResouceByMenuIdMap,tSysMenu.getMenuId());
            }
        }else{
            List<TSysResource> tSysResources = groupResouceByMenuIdMap.get(menuId);
            if(org.apache.commons.collections.CollectionUtils.isNotEmpty(tSysResources)){
                for (TSysResource tSysResource : tSysResources) {
                    if(tSysResource.getSrcName().equalsIgnoreCase("anon")){
                        continue;
                    }
                    System.out.println("资源:"+tSysResource.getSrcDesc());
                }
            }
        }
    }

    /**
     * 根据菜单ID分组资源
     * @param resourceList
     * @return
     */
    private Map<String,List<ResourceTreeMap>> groupResourcesByMenuId(List<ResourceTreeMap> resourceList){
        Map<String,List<ResourceTreeMap>> resultMap = new HashMap<>();
        for(ResourceTreeMap rt : resourceList){
            if(resultMap.containsKey(rt.getMenuId())){
                List<ResourceTreeMap> list = resultMap.get(rt.getMenuId());
                list.add(rt);
                resultMap.put(rt.getMenuId(), list);
            }else{
                List<ResourceTreeMap> resultList = new ArrayList<>();
                resultList.add(rt);
                resultMap.put(rt.getMenuId(), resultList);
            }
        }
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public MessageMap authorize(WyBusinessContext ctx, TSysRoleResource rr)
    {
        MessageMap mm = new MessageMap();

        if(StringUtils.isBlank(CommonUtils.null2String(rr.getSrcId()))){
            tSysRoleResourceMapper.deleteRoleResourceByRoleId(rr.getRoleId());
        }else{
            String[] rsIdsString = rr.getSrcId().split(";");
            List<TSysRoleResource> list = new ArrayList<>();
            for(String rsIdString : rsIdsString){
                String[] srcIdAndType = rsIdString.split(",");
                TSysRoleResource addRR = new TSysRoleResource();
                addRR.setId(UUID.randomUUID().toString());
                addRR.setRoleId(rr.getRoleId());
                addRR.setSrcId(srcIdAndType[0]);
                addRR.setSrcType(srcIdAndType[1]);
                list.add(addRR);

                // 如果勾选的是菜单，则将该菜单下面的所有私有白名单权限也自动授予角色
                if(WyEnum.m.name().equalsIgnoreCase(srcIdAndType[1])){
                    List<TSysResource> anonList = tSysResourceMapper.findAnonResourcesByMenuId(srcIdAndType[0]);
                    if(!CollectionUtils.isEmpty(anonList)){
                        for(TSysResource anonR : anonList){
                            TSysRoleResource anon = new TSysRoleResource();
                            anon.setId(UUID.randomUUID().toString());
                            anon.setRoleId(rr.getRoleId());
                            anon.setSrcId(anonR.getSrcId());
                            anon.setSrcType(WyEnum.r.name());
                            list.add(anon);
                        }
                    }
                }
            }


            tSysRoleResourceMapper.deleteRoleResourceByRoleId(rr.getRoleId());
            tSysRoleResourceMapper.batchInsert(list);
        }
        return mm;
    }
}
