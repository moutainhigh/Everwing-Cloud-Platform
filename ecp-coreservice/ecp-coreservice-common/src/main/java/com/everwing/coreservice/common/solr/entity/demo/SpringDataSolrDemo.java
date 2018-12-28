package com.everwing.coreservice.common.solr.entity.demo;/**
 * Created by wust on 2018/6/4.
 */

import org.apache.solr.client.solrj.beans.Field;

import java.util.Date;

/**
 *
 * Function:
 * Reason:
 * Date:2018/6/4
 * @author wusongti@lii.com.cn
 */
public class SpringDataSolrDemo implements java.io.Serializable{
    private static final long serialVersionUID = 2816969759252558607L;
    @Field
    private String id;
    /** 项目编码 **/

    @Field
    private String projectId;

    /** 批次号 **/
    @Field
    private String batchNo;

    /** 产品类型 **/
    @Field
    private String productType;

    /** 产品名称 **/
    @Field
    private String productName;

    /** 买家信息集合 **/
    @Field
    private String buyerCommon;

    /** 总金额 **/
    @Field
    private Double totalPrice;

    /** 折扣金额 **/
    @Field
    private Double discountPrice;

    /** 订单状态 **/
    @Field
    private String status;

    /** 创建用户id **/
    @Field
    private String createrId;

    /** 创建用户名称 **/
    @Field
    private String createrName;

    /** 最后更新用户id **/
    @Field
    private String modifyId;

    /** 最后更新用户名称 **/
    @Field
    private String modifyName;

    /** 创建时间 **/
    @Field
    private Date createTime;

    /**
     * 最后更新时间，注意，该字段尽量要有，
     * 新增数据时该字段的值初始化为创建时间，以后每次修改数据都重新设置该时间，
     * 建议删除业务数据使用软删除并且更新该字段的时间。如果做不到软删除，那么在删除数据的时候
     * 你应该手工调用solr的接口去删除solr服务下的数据。
     * **/
    @Field
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

    public String getBuyerCommon() {
        return buyerCommon;
    }

    public void setBuyerCommon(String buyerCommon) {
        this.buyerCommon = buyerCommon;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
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

    @Override
    public String toString() {
        return "SpringDataSolrDemo{" +
                "id='" + id + '\'' +
                ", projectId='" + projectId + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", productType='" + productType + '\'' +
                ", productName='" + productName + '\'' +
                ", buyerCommon='" + buyerCommon + '\'' +
                ", totalPrice=" + totalPrice +
                ", discountPrice=" + discountPrice +
                ", status='" + status + '\'' +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", modifyId='" + modifyId + '\'' +
                ", modifyName='" + modifyName + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }


}
