package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/9/28.
 */

import java.util.Date;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/28
 * @author wusongti@lii.com.cn
 */
public class TProductPaymentDetail implements java.io.Serializable,Cloneable{

    private static final long serialVersionUID = -3995636889435370824L;

    /**  **/
    private String id;
    /** 订单编号 **/
    private String orderId;
    /** 订单批次号 **/
    private String orderBatchNo;
    /** 银行交易流水号 **/
    private String businessSerialNumber;
    /** 支付类型：现金、刷卡、微信支付宝 **/
    private String payType;
    /** 当次支付金额金额 **/
    private Double price;
    /** 操作人、收款人id **/
    private String createrId;
    /** 操作人、收款人名称 **/
    private String createrName;
    /**  **/
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

    public String getBusinessSerialNumber() {
        return businessSerialNumber;
    }

    public void setBusinessSerialNumber(String businessSerialNumber) {
        this.businessSerialNumber = businessSerialNumber;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
