package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/9/26.
 */

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * Function:支付详细持久化类
 * Reason:
 * Date:2017/9/26
 * @author wusongti@lii.com.cn
 */
public class TProductOrder implements java.io.Serializable,Cloneable{
    private static final long serialVersionUID = -3995636889435370824L;

    /**  **/
    private String id;
    /** 项目编码 **/
    private String projectId;
    /** 批次号 **/
    private String batchNo;
    /** 产品类型 **/
    private String productType;
    /** 产品名称 **/
    private String productName;
    /** 买家id，对应个人客户或者企业客户的id **/
    private String buyerId;
    /** 总金额 **/
    private BigDecimal totalPrice;
    /** 折扣金额 **/
    private BigDecimal discountPrice;
    /** 订单状态 **/
    private String status;
    /** 当前版本 **/
    private Integer version;
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
