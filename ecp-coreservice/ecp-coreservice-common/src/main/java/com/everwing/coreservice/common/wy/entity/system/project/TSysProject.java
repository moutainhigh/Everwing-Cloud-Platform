package com.everwing.coreservice.common.wy.entity.system.project;/**
 * Created by wust on 2017/6/20.
 */

import java.util.Date;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/20
 * @author wusongti@lii.com.cn
 */
public class TSysProject implements java.io.Serializable{

    private static final long serialVersionUID = -4179470884582246078L;

    //field
    /** 主键 **/
    private String projectId;
    /** 编码 **/
    private String code;
    /** 名称 **/
    private String name;
    /** 描述 **/
    private String description;
    /** 详细地址 **/
    private String address;
    /** 状态；参考t_sys_lookup表的enableDisable属性 **/
    private String status;
    /** 项目经理 **/
    private String leader;
    /** 邮政编码 **/
    private String zipCode;
    /** 省 **/
    private String province;
    /** 市 **/
    private String city;
    /** 县区 **/
    private String area;
    /** 街道 **/
    private String streets;
    /** 创建人id **/
    private String createrId;
    /** 创建人名称  **/
    private String createrName;
    /** 创建时间 **/
    private Date createTime;
    /** 修改人id **/
    private String modifyId;
    /** 修改人名称 **/
    private String modifyName;
    /** 修改时间 **/
    private Date modifyTime;
    
    /** 账单自动生成开关 0 : 开启     1: 关闭**/
    private Integer billStatus;


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreets() {
        return streets;
    }

    public void setStreets(String streets) {
        this.streets = streets;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public String getModifyName() {
        return modifyName;
    }

    public void setModifyName(String modifyName) {
        this.modifyName = modifyName;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Integer billStatus) {
        this.billStatus = billStatus;
    }

    @Override
    public String toString() {
        return "TSysProject{" +
                "projectId='" + projectId + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", leader='" + leader + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", streets='" + streets + '\'' +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", createTime=" + createTime +
                ", modifyId='" + modifyId + '\'' +
                ", modifyName='" + modifyName + '\'' +
                ", modifyTime=" + modifyTime +
                ", billStatus=" + billStatus +
                '}';
    }
}
