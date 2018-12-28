package com.everwing.coreservice.common.context;/**
 * Created by wust on 2018/2/5.
 */

/**
 *
 * Function:上下文基类
 * Reason:
 * Date:2018/2/5
 * @author wusongti@lii.com.cn
 */
public class BaseBusinessContext implements java.io.Serializable{
    private static final long serialVersionUID = 5510386632143677793L;



    protected String lan;


    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }


    public enum BusinessContextEnum{
        COMPANY,
        DEPARTMENT,
        PROJECT,
        ROLE,
        USER
    }
}
