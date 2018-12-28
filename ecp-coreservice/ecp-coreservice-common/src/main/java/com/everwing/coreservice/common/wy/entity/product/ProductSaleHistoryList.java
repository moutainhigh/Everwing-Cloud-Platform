package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2018/11/21.
 */

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * Function:产品销售记录
 * Reason:
 * Date:2018/11/21
 * @author wusongti@lii.com.cn
 */
public class ProductSaleHistoryList implements java.io.Serializable{

    private static final long serialVersionUID = -8241944272371356204L;

    // 订单号
    private String orderNo;

    // 消费者
    private String buyerName;

    // 消费金额
    private BigDecimal price;

    // 消费数量
    private Integer quantity;

    // 消费时间
    private Date orderDateTime;

    /** 订单生效日期 **/
    private Date buyBeginDate;

    /** 订单失效日期 **/
    private Date buyEndDate;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
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

    public Date getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(Date orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public Date getBuyBeginDate() {
        return buyBeginDate;
    }

    public void setBuyBeginDate(Date buyBeginDate) {
        this.buyBeginDate = buyBeginDate;
    }

    public Date getBuyEndDate() {
        return buyEndDate;
    }

    public void setBuyEndDate(Date buyEndDate) {
        this.buyEndDate = buyEndDate;
    }
}
