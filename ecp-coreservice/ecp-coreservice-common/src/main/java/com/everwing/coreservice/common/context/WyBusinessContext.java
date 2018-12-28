package com.everwing.coreservice.common.context;/**
 * Created by wust on 2017/5/10.
 */

import com.alibaba.fastjson.JSONArray;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.wy.common.organization.OrganizationComponent;
import com.everwing.coreservice.common.wy.common.organization.OrganizationHelper;

/**
 *
 * Function:物业系统专用上下文
 * Reason:封装公司编码，项目编码，语言环境等公共信息
 * Date:2017/5/10
 * @author wusongti@lii.com.cn
 */
public class WyBusinessContext extends BaseBusinessContext {
    private static final long serialVersionUID = -4791548159866747192L;

    private WyBusinessContext(){}

    private static final ThreadLocal<WyBusinessContext> LOCAL = new ThreadLocal<WyBusinessContext>() {
        protected WyBusinessContext initialValue() {
            return new WyBusinessContext();
        }
    };

    public static WyBusinessContext getContext() {
        return LOCAL.get();
    }

    /**
     * 当前登录用户id
     */
    private String userId;

    /**
     * 当前登录用户账号
     */
    private String loginName;

    /**
     * 当前登录员工工号
     */
    private String staffNumber;

    /**
     * 当前登录员工姓名
     */
    private String staffName;

    /**
     * 当前登录用户所属公司id
     */
    private String companyId;

    /**
     * 当前登录用户所属公司code
     */
    private String companyCode;

    /**
     * 当前登录用户所属公司名称
     */
    private String companyName;

    /**
     * 当前登录用户操作的项目id
     */
    private String projectId;

    /**
     * 当前登录用户操作的项目code
     */
    private String projectCode;

    /**
     * 当前登录用户操作的项目名称
     */
    private String projectName;

    /**
     * 当前登录用户是否超级管理员
     */
    private boolean isSuAdmin;

    /**
     * 前后端操作签名
     */
    private String jsonWebToken;

    /**
     * 当前登录用户的组织架构，可以从该构建里面获取所有架构信息
     */
    private OrganizationComponent organizationComponent;



    private String contextPath;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public boolean isSuAdmin() {
        return isSuAdmin;
    }

    public void setSuAdmin(boolean suAdmin) {
        isSuAdmin = suAdmin;
    }

    public String getJsonWebToken() {
        return jsonWebToken;
    }

    public void setJsonWebToken(String jsonWebToken) {
        this.jsonWebToken = jsonWebToken;
    }

    public JSONArray getOrganizationByType(BusinessContextEnum e){
        JSONArray jsonArray = new JSONArray();
        if(BusinessContextEnum.DEPARTMENT.name().equals(e.name())){
            OrganizationHelper.lookupOrganizationForDepartment(organizationComponent,jsonArray);
        }else if(BusinessContextEnum.PROJECT.name().equals(e.name())){
            OrganizationHelper.lookupOrganizationForProject(organizationComponent,jsonArray);
        }else if(BusinessContextEnum.ROLE.name().equals(e.name())){
            OrganizationHelper.lookupOrganizationForRole(organizationComponent,jsonArray);
        }else{
            throw new ECPBusinessException("暂时不支持获取该类型["+e.name()+"]的组织架构！");
        }
        return jsonArray;
    }

    public OrganizationComponent getOrganizationComponent() {
        return this.organizationComponent;
    }

    public void setOrganizationComponent(OrganizationComponent organizationComponent) {
        this.organizationComponent = organizationComponent;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}
