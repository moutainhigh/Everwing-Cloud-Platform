package com.everwing.coreservice.common.dynamicreports.entity.system.lookup;/**
 * Created by wust on 2017/4/1.
 */

/**
 *
 * Function:下拉框视图
 * Reason:
 * Date:2017年4月1日 17:26:52
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TSysLookupSelectVO implements java.io.Serializable{
    private static final long serialVersionUID = -4083430990800933927L;
    private String id;
    private String code;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
