package com.everwing.coreservice.common.context;/**
 * Created by wust on 2018/1/26.
 */


import com.alibaba.fastjson.JSONObject;

/**
 *
 * Function:动态报表系统专用上下文
 * Reason:
 * Date:2018/1/26
 * @author wusongti@lii.com.cn
 */
public class DynamicreportsBusinessContext extends BaseBusinessContext {
    private static final long serialVersionUID = -8928188428048136683L;
    private DynamicreportsBusinessContext(){
        super();
    }

    private static final ThreadLocal<DynamicreportsBusinessContext> LOCAL = new ThreadLocal<DynamicreportsBusinessContext>() {
        protected DynamicreportsBusinessContext initialValue() {
            return new DynamicreportsBusinessContext();
        }
    };

    public static DynamicreportsBusinessContext getContext() {
        return LOCAL.get();
    }

    // 用户类型，有平台用户，物业平台用户，报表平台用户
    private String userType;

    private String userId;

    private String loginName;

    private String staffNumber;

    private String staffName;

    // 数据源json串
    private JSONObject dataSourceJsonObject;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

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

    public JSONObject getDataSourceJsonObject() {
        return dataSourceJsonObject;
    }

    public void setDataSourceJsonObject(JSONObject dataSourceJsonObject) {
        this.dataSourceJsonObject = dataSourceJsonObject;
    }
}
