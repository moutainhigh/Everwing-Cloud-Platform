package com.everwing.coreservice.common.wy.common;
/**
 * Created by wust on 2017/6/22.
 */

import com.everwing.coreservice.common.wy.common.organization.OrganizationComponent;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompany;
import com.everwing.coreservice.common.wy.entity.system.menu.TSysMenu;
import com.everwing.coreservice.common.wy.entity.system.resource.TSysResource;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUser;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:登录用户上下文数据载体
 * Reason:
 * Date:2017/6/22
 * @author wusongti@lii.com.cn
 */
public class UserContextModel implements java.io.Serializable{
    private static final long serialVersionUID = 4244664578257839168L;
    private Map<Integer,List<TSysMenu>> groupMenusByLevel;                  // 当前登录用户所拥有的菜单集合，key=菜单级别，value=该级别的菜单集合
    private Map<String,List<TSysMenu>> groupMenusByPid;                     // 当前登录用户所拥有的菜单集合，key=父菜单id，value=子菜单集合
    private List<TSysResource> resources;                                   // 非白名单资源集合
    private List<TSysResource> anonResources;                               // 白名单资源集合
    private Map<String,List<TSysResource>> groupResourcesByPid;             // 当前登录用户所拥有的资源集合，key=菜单id，value=菜单下的资源集合
    private TSysUser loginUser;                                             // 当前登录用户
    private TSysCompany rootCompany;                                        // 总公司
    private OrganizationComponent organizationComponent;                    // 当前登录用户的组织架构，可以从该构建里面获取所有架构信息


    public Map<Integer, List<TSysMenu>> getGroupMenusByLevel() {
        return groupMenusByLevel;
    }

    public void setGroupMenusByLevel(Map<Integer, List<TSysMenu>> groupMenusByLevel) {
        this.groupMenusByLevel = groupMenusByLevel;
    }

    public Map<String, List<TSysMenu>> getGroupMenusByPid() {
        return groupMenusByPid;
    }

    public void setGroupMenusByPid(Map<String, List<TSysMenu>> groupMenusByPid) {
        this.groupMenusByPid = groupMenusByPid;
    }

    public List<TSysResource> getResources() {
        return resources;
    }

    public void setResources(List<TSysResource> resources) {
        this.resources = resources;
    }

    public List<TSysResource> getAnonResources() {
        return anonResources;
    }

    public void setAnonResources(List<TSysResource> anonResources) {
        this.anonResources = anonResources;
    }

    public Map<String, List<TSysResource>> getGroupResourcesByPid() {
        return groupResourcesByPid;
    }

    public void setGroupResourcesByPid(Map<String, List<TSysResource>> groupResourcesByPid) {
        this.groupResourcesByPid = groupResourcesByPid;
    }

    public TSysUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(TSysUser loginUser) {
        this.loginUser = loginUser;
    }

    public TSysCompany getRootCompany() {
        return rootCompany;
    }

    public void setRootCompany(TSysCompany rootCompany) {
        this.rootCompany = rootCompany;
    }

    public OrganizationComponent getOrganizationComponent() {
        return organizationComponent;
    }

    public void setOrganizationComponent(OrganizationComponent organizationComponent) {
        this.organizationComponent = organizationComponent;
    }

    @Override
    public String toString() {
        return "UserContext{" +
                "groupMenusByLevel=" + groupMenusByLevel +
                ", groupMenusByPid=" + groupMenusByPid +
                ", resources=" + resources +
                ", anonResources=" + anonResources +
                ", groupResourcesByPid=" + groupResourcesByPid +
                ", loginUser=" + loginUser +
                ", rootCompany=" + rootCompany +
                ", organizationComponent=" + organizationComponent +
                '}';
    }
}
