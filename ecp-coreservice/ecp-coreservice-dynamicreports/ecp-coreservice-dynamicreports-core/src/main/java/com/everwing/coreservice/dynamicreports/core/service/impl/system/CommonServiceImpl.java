package com.everwing.coreservice.dynamicreports.core.service.impl.system;/**
 * Created by wust on 2018/2/5.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dynamicreports.DynamicreportsEnum;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.*;
import com.everwing.coreservice.common.dynamicreports.service.system.CommonService;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import com.everwing.coreservice.common.utils.RC4;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsMenu;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsResource;
import com.everwing.coreservice.common.xml.XMLAbstractResolver;
import com.everwing.coreservice.common.xml.XMLDefinitionFactory;
import com.everwing.coreservice.common.xml.factory.XMLDynamicreportsPermissionFactory;
import com.everwing.coreservice.dynamicreports.dao.mapper.system.rights.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Function:
 * Reason:
 * Date:2018/2/5
 *
 * @author wusongti@lii.com.cn
 */
@Service("commonServiceImpl")
public class CommonServiceImpl implements CommonService {
    @Autowired
    private TRightsMenuMapper tRightsMenuMapper;

    @Autowired
    private TRightsResourceMapper tRightsResourceMapper;

    @Autowired
    private TRightsRoleResourceMapper tRightsRoleResourceMapper;

    @Autowired
    private TRightsUserMapper tRightsUserMapper;

    @Autowired
    private TRightsUserRoleMapper tRightsUserRoleMapper;

    @Autowired
    private SpringRedisTools springRedisTools;


    /**
     * 初始化菜单资源
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MessageMap initResources() {
        MessageMap mm = new MessageMap();

        XMLDefinitionFactory xmlDefinitionFactory = new XMLDynamicreportsPermissionFactory();
        XMLAbstractResolver xmlAbstractResolver = xmlDefinitionFactory.createXMLResolver();

        Map<String, List> map = xmlAbstractResolver.getResult();
        List<TRightsMenu> parseMenuList = map.get("parseMenuList");
        List<TRightsResource> parseResourceList = map.get("parseResourceList");


        TRightsRoleResource tSysRoleResourceSearch = new TRightsRoleResource();

        // 剔除已经分配的脏数据（菜单）
        tSysRoleResourceSearch.setSrcType(WyEnum.m.name());
        List<TRightsRoleResource> tSysRoleResourceMenus = tRightsRoleResourceMapper.findByCondition(tSysRoleResourceSearch);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(tSysRoleResourceMenus)) {
            Set<String> invalidSrcIdSet = new HashSet<>(10); // 一般不会有10个以上的菜单突然失效，除非重构系统

            for (TRightsRoleResource tSysRoleResourceMenu : tSysRoleResourceMenus) {
                boolean flag = false;
                for (TRightsMenu tSysMenu : parseMenuList) {
                    if (tSysMenu.getMenuId().equals(tSysRoleResourceMenu.getSrcId())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    invalidSrcIdSet.add(tSysRoleResourceMenu.getSrcId());
                }
            }


            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(invalidSrcIdSet)) {
                for (String s : invalidSrcIdSet) {
                    tRightsRoleResourceMapper.deleteByTypeAndSrcId(WyEnum.m.name(), s);
                }
            }
        }

        // 剔除已经分配的脏数据（资源）
        tSysRoleResourceSearch.setSrcType(WyEnum.r.name());
        List<TRightsRoleResource> tSysRoleResources = tRightsRoleResourceMapper.findByCondition(tSysRoleResourceSearch);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(tSysRoleResources)) {
            Set<String> invalidSrcIdSet = new HashSet<>(10); // 一般不会有10个以上的菜单突然失效，除非重构系统

            for (TRightsRoleResource tSysRoleResource : tSysRoleResources) {
                boolean flag = false;
                for (TRightsResource tSysResource : parseResourceList) {
                    if (tSysResource.getSrcId().equals(tSysRoleResource.getSrcId())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    invalidSrcIdSet.add(tSysRoleResource.getSrcId());
                }
            }
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(invalidSrcIdSet)) {
                for (String s : invalidSrcIdSet) {
                    tRightsRoleResourceMapper.deleteByTypeAndSrcId(WyEnum.r.name(), s);
                }
            }
        }

        // 生成菜单
        if (!CollectionUtils.isEmpty(parseMenuList)) {
            tRightsMenuMapper.deleteAll();
            tRightsMenuMapper.batchInsert(parseMenuList);
        }

        // 生成资源
        if (!CollectionUtils.isEmpty(parseResourceList)) {
            tRightsResourceMapper.deleteAll();
            tRightsResourceMapper.batchInsert(parseResourceList);
        }


        List<TRightsRoleResource> tSysRoleResourceListNew = new ArrayList<>();

        // 查询已经分配了权限的所有角色，并为这些角色重新设置白名单权限（有可能升级的时候增加了白名单权限，因此需要重新设置）
        List<TRightsRoleResource> menuIdsGroupByRoleId = tRightsRoleResourceMapper.groupMenuIdByRoleId();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(menuIdsGroupByRoleId)) {
            for (TRightsRoleResource tSysRoleResource : menuIdsGroupByRoleId) {
                String menuId = tSysRoleResource.getSrcId();
                String roleId = tSysRoleResource.getRoleId();

                // 该菜单下面的白名单资源
                List<TRightsResourceVO> annonResourcesByMenuId = tRightsResourceMapper.findWhiteListByMenuId(menuId);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(annonResourcesByMenuId)) {
                    for (TRightsResource tSysResource : annonResourcesByMenuId) {
                        tRightsRoleResourceMapper.deleteByRoleIdAndSrcId(roleId, tSysResource.getSrcId());
                        TRightsRoleResource tSysRoleResourceNew = new TRightsRoleResource();
                        tSysRoleResourceNew.setRoleId(roleId);
                        tSysRoleResourceNew.setSrcType(WyEnum.r.name());
                        tSysRoleResourceNew.setSrcId(tSysResource.getSrcId());
                        tSysRoleResourceNew.setId(UUID.randomUUID().toString());
                        tSysRoleResourceListNew.add(tSysRoleResourceNew);
                    }
                }
            }
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(tSysRoleResourceListNew)) {
                tRightsRoleResourceMapper.batchInsert(tSysRoleResourceListNew);
            }
        }
        return mm;
    }

    @Override
    public MessageMap login(String loginName, String pwd) {
        MessageMap mm = new MessageMap();
        TRightsUserQO tRightsUserQO = new TRightsUserQO();
        tRightsUserQO.setLoginName(loginName);
        tRightsUserQO.setPassword(Base64.encodeBase64String(RC4.encry_RC4_string(pwd, ApplicationConstant.systemProp.getStringValue()).getBytes()));
        List<TRightsUserVO> tRightsUserVOList = tRightsUserMapper.findByCondition(tRightsUserQO);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(tRightsUserVOList)) {
            JSONObject jsonObject = new JSONObject();
            TRightsUserVO tRightsUserVO = tRightsUserVOList.get(0);
            String userType = tRightsUserVO.getType();
            if (ApplicationConstant.userType_systemAdmin.getStringValue().equalsIgnoreCase(userType)) { // 系统管理员
                List<TRightsMenuVO> menusList = new ArrayList<>();
                List<TRightsResourceVO> resourceList = new ArrayList<>();

                /**
                 * 获取白名单菜单列表
                 */
                List<TRightsMenuVO> whiteList = tRightsMenuMapper.findWhiteListByAdmin();
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(whiteList)) {
                    menusList.addAll(whiteList);
                }

                /**
                 * 获取非白名单菜单列表
                 */
                List<TRightsMenuVO> isNotWhiteList = tRightsMenuMapper.findNonWhiteListByAdmin();
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(isNotWhiteList)) {
                    menusList.addAll(isNotWhiteList);
                }

                /**
                 * 按照级别分组菜单
                 */
                Map<Integer, List<TRightsMenuVO>> groupMenuByLevel = groupMenuByLevel(menusList);
                jsonObject.put("groupMenuByLevel", JSONObject.toJSONString(groupMenuByLevel));


                /**
                 * 按照pid分组菜单
                 */
                Map<String, List<TRightsMenuVO>> groupMenuByPid = groupMenuByPid(menusList);
                jsonObject.put("groupMenuByPid", JSONObject.toJSONString(groupMenuByPid));


                /**
                 * 获取白名单资源集合
                 */
                List<TRightsResourceVO> whiteResourceList = tRightsResourceMapper.findWhiteListByAdmin();
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(whiteResourceList)) {
                    resourceList.addAll(whiteResourceList);
                }


                /**
                 * 获取非白名单资源集合
                 */
                List<TRightsResourceVO> isNotWhiteResourceList = tRightsResourceMapper.findNonWhiteListByAdmin();
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(isNotWhiteResourceList)) {
                    resourceList.addAll(isNotWhiteResourceList);
                }


                /**
                 * 根据菜单id分组资源
                 */
                Map<String, List<TRightsResourceVO>> groupResourcesByMenuId = groupResourcesByMenuId(resourceList);
                jsonObject.put("groupResourcesByMenuId", JSONObject.toJSONString(groupResourcesByMenuId));
            } else { // 非系统管理员
                List<TRightsMenuVO> menusList = new ArrayList<>();
                List<TRightsResourceVO> resourceList = new ArrayList<>();

                /**
                 * 根据登录用户获取其拥有的角色列表
                 */
                List<String> roleIdList = new ArrayList<>(10);
                TRightsUserRole tRightsUserRole = new TRightsUserRole();
                tRightsUserRole.setUserId(tRightsUserVO.getUserId());
                List<TRightsUserRole> tRightsUserRoles = tRightsUserRoleMapper.findByCondition(tRightsUserRole);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(tRightsUserRoles)) {
                    for (TRightsUserRole rightsUserRole : tRightsUserRoles) {
                        roleIdList.add(rightsUserRole.getRoleId());
                    }
                } else {
                    mm.setFlag(MessageMap.INFOR_WARNING);
                    mm.setMessage("您无权登录系统");
                    return mm;
                }

                /**
                 * 获取白名单菜单列表
                 */
                TRightsMenuQO tRightsMenuQO = new TRightsMenuQO();
                tRightsMenuQO.setRoleIdList(roleIdList);
                List<TRightsMenuVO> whiteList = tRightsMenuMapper.findWhiteListByRole(tRightsMenuQO);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(whiteList)) {
                    menusList.addAll(whiteList);
                }

                /**
                 * 获取非白名单菜单列表
                 */
                List<TRightsMenuVO> isNotWhiteList = tRightsMenuMapper.findNonWhiteListByRole(tRightsMenuQO);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(isNotWhiteList)) {
                    menusList.addAll(isNotWhiteList);
                }


                /**
                 * 按照级别分组菜单
                 */
                Map<Integer, List<TRightsMenuVO>> groupMenuByLevel = groupMenuByLevel(menusList);
                jsonObject.put(DynamicreportsEnum.USERCONTEXT_groupMenuByLevel.getStringValue(), JSONObject.toJSONString(groupMenuByLevel));


                /**
                 * 按照pid分组菜单
                 */
                Map<String, List<TRightsMenuVO>> groupMenuByPid = groupMenuByPid(menusList);
                jsonObject.put(DynamicreportsEnum.USERCONTEXT_groupMenuByPid.getStringValue(), JSONObject.toJSONString(groupMenuByPid));


                /**
                 * 获取白名单资源集合
                 */
                TRightsResourceQO tRightsResourceQO = new TRightsResourceQO();
                tRightsResourceQO.setRoleIdList(null);
                List<TRightsResourceVO> whiteResourceList = tRightsResourceMapper.findWhiteListByRole(tRightsResourceQO);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(whiteResourceList)) {
                    resourceList.addAll(whiteResourceList);
                    jsonObject.put(DynamicreportsEnum.USERCONTEXT_whiteResource.getStringValue(), whiteResourceList);
                }


                /**
                 * 获取非白名单资源集合
                 */
                List<TRightsResourceVO> isNotWhiteResourceList = tRightsResourceMapper.findNonWhiteListByRole(tRightsResourceQO);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(isNotWhiteResourceList)) {
                    resourceList.addAll(isNotWhiteResourceList);
                    jsonObject.put(DynamicreportsEnum.USERCONTEXT_isNotWhiteResource.getStringValue(), isNotWhiteResourceList);
                }


                /**
                 * 根据菜单id分组资源
                 */
                Map<String, List<TRightsResourceVO>> groupResourcesByMenuId = groupResourcesByMenuId(resourceList);
                jsonObject.put(DynamicreportsEnum.USERCONTEXT_groupResourcesByMenuId.getStringValue(), JSONObject.toJSONString(groupResourcesByMenuId));
            }
            jsonObject.put(DynamicreportsEnum.USERCONTEXT_userInfo.getStringValue(), JSONObject.toJSONString(tRightsUserVOList.get(0)));
            springRedisTools.addData(String.format(ApplicationConstant.REPORT_WEB_LOGIN_KEY.getStringValue(), loginName), jsonObject);
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("账号或密码不正确");
            return mm;
        }
        return mm;
    }


    /**
     * 对菜单按级别分组
     *
     * @param menus
     * @return
     */
    private Map<Integer, List<TRightsMenuVO>> groupMenuByLevel(List<TRightsMenuVO> menus) {
        Map<Integer, List<TRightsMenuVO>> integerListMap = new HashMap<>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(menus)) {
            for (TRightsMenuVO menu : menus) {
                Integer key = menu.getMenuLevel();
                if (integerListMap.containsKey(key)) {
                    integerListMap.get(key).add(menu);
                } else {
                    List<TRightsMenuVO> menuList = new ArrayList<>(10);
                    menuList.add(menu);
                    integerListMap.put(key, menuList);
                }
            }
            return integerListMap;
        }
        return null;
    }

    /**
     * 对菜单按父菜单id分组
     *
     * @param menus
     * @return
     */
    private Map<String, List<TRightsMenuVO>> groupMenuByPid(List<TRightsMenuVO> menus) {
        Map<String, List<TRightsMenuVO>> stringListMap = new HashMap<>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(menus)) {
            for (TRightsMenuVO menu : menus) {
                String key = menu.getPid();
                if (stringListMap.containsKey(key)) {
                    stringListMap.get(key).add(menu);
                } else {
                    List<TRightsMenuVO> menuList = new ArrayList<>(10);
                    menuList.add(menu);
                    stringListMap.put(key, menuList);
                }
            }
            return stringListMap;
        }
        return null;
    }

    /**
     * 对资源按菜单分组
     *
     * @param resources
     * @return
     */
    private Map<String, List<TRightsResourceVO>> groupResourcesByMenuId(List<TRightsResourceVO> resources) {
        Map<String, List<TRightsResourceVO>> stringListHashMap = new HashMap<>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(resources)) {
            for (TRightsResourceVO resource : resources) {
                String key = resource.getMenuId();
                if (stringListHashMap.containsKey(key)) {
                    stringListHashMap.get(key).add(resource);
                } else {
                    List<TRightsResourceVO> resourceList = new ArrayList<>(10);
                    resourceList.add(resource);
                    stringListHashMap.put(key, resourceList);
                }
            }
            return stringListHashMap;
        }
        return null;
    }
}
