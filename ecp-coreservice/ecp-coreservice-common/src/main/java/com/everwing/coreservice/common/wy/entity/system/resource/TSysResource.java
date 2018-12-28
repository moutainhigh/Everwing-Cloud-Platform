package com.everwing.coreservice.common.wy.entity.system.resource;


import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:资源实体类
 * Reason:该实体类和数据库字段一一对应
 * Date:2017-4-10 09:44:23
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TSysResource extends BaseEntity {


    private static final long serialVersionUID = -6646248827577402242L;
    private String srcId;			//资源id
	private String menuId;			//菜单id
	private String srcName;			//资源名
	private String srcDesc;         //资源描述
    private String srcPermission;	//资源需要权限,如user:add
	private String srcUrl;          //资源url

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getSrcDesc() {
        return srcDesc;
    }

    public void setSrcDesc(String srcDesc) {
        this.srcDesc = srcDesc;
    }

    public String getSrcPermission() {
        return srcPermission;
    }

    public void setSrcPermission(String srcPermission) {
        this.srcPermission = srcPermission;
    }

    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    @Override
    public String toString() {
        return "TSysResource{" +
                "srcId='" + srcId + '\'' +
                ", menuId='" + menuId + '\'' +
                ", srcName='" + srcName + '\'' +
                ", srcDesc='" + srcDesc + '\'' +
                ", srcPermission='" + srcPermission + '\'' +
                ", srcUrl='" + srcUrl + '\'' +
                '}';
    }
}
