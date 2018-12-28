package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/11/14.
 */

import java.util.Date;

/**
 *
 * Function:
 * Reason:
 * Date:2017/11/14
 * @author wusongti@lii.com.cn
 */
public class TDeposit implements java.io.Serializable{
    private static final long serialVersionUID = -2121075202155637357L;
    //field
    /** 主键 **/
    private String id;
    /** 项目编码 **/
    private String projectId;
    /** 押金名 **/
    private String name;
    /** 押金收取时间 **/
    private Object payDepositDatetime;
    /** 押金总额 **/
    private Double totalAmount;
    /** 押金人 **/
    private String depositPerson;
    /** 订单批次号 **/
    private String orderBatchNo;
    /** 状态：待退，完成 **/
    private String status;
    /** 创建用户id **/
    private String createrId;
    /** 创建用户名称 **/
    private String createrName;
    /** 最后更新用户id **/
    private String modifyId;
    /** 最后更新用户名称 **/
    private String modifyName;
    /** 创建时间 **/
    private Date createTime;
    /** 最后更新时间 **/
    private Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getPayDepositDatetime() {
        return payDepositDatetime;
    }

    public void setPayDepositDatetime(Object payDepositDatetime) {
        this.payDepositDatetime = payDepositDatetime;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDepositPerson() {
        return depositPerson;
    }

    public void setDepositPerson(String depositPerson) {
        this.depositPerson = depositPerson;
    }

    public String getOrderBatchNo() {
        return orderBatchNo;
    }

    public void setOrderBatchNo(String orderBatchNo) {
        this.orderBatchNo = orderBatchNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
