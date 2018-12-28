package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/10/19.
 */

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * Function:
 * Reason:
 * Date:2017/10/19
 * @author wusongti@lii.com.cn
 */
public class TProductOrderDetail implements java.io.Serializable,Cloneable{
    private static final long serialVersionUID = -1777743282661170973L;
    //field
    /**  **/
    private String id;
    /** 订单编号 **/
    private String orderId;
    /** 订单号 **/
    private String orderBatchNo;
    /** 产品单价 **/
    private BigDecimal price;
    /** 购买数量 **/
    private Integer quantity;
    /** 订单金额 **/
    private BigDecimal orderAmount;
    /** 订单开始时间 **/
    private String buyBeginDate;
    /** 订单到期私家 **/
    private String buyEndDate;
    /** 描述 **/
    private String description;
    /** 关联客户 **/
    private String customerId;
    /** 关联资产 **/
    private String houseCodeNew;
    /** 关联车辆 **/
    private String vehicleNumber;
    /** 产品编码 **/
    private String productCode;
    /** 产品公共字段部分 **/
    private String productCommon;
    /** 创建用户id **/
    private String createrId;
    /** 创建用户名称 **/
    private String createrName;
    /** 创建时间 **/
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderBatchNo() {
        return orderBatchNo;
    }

    public void setOrderBatchNo(String orderBatchNo) {
        this.orderBatchNo = orderBatchNo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getBuyBeginDate() {
        return buyBeginDate;
    }

    public void setBuyBeginDate(String buyBeginDate) {
        this.buyBeginDate = buyBeginDate;
    }

    public String getBuyEndDate() {
        return buyEndDate;
    }

    public void setBuyEndDate(String buyEndDate) {
        this.buyEndDate = buyEndDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductCommon() {
        return productCommon;
    }

    public void setProductCommon(String productCommon) {
        this.productCommon = productCommon;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
