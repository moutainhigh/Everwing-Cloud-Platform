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
public class TDepositDetail implements java.io.Serializable{
    private static final long serialVersionUID = 3932496901529654883L;

    //field
    /** 主键 **/
    private String id;
    /** 主表id **/
    private String depositId;
    /** 退押单号 **/
    private String orderNumber;
    /** 退押金额 **/
    private Double redeemDeposiAmount;
    /** 扣除金额 **/
    private Double deductAmount;
    /** 退押时间 **/
    private Date redeemDepositDatetime;
    /** 备注 **/
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepositId() {
        return depositId;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Double getRedeemDeposiAmount() {
        return redeemDeposiAmount;
    }

    public void setRedeemDeposiAmount(Double redeemDeposiAmount) {
        this.redeemDeposiAmount = redeemDeposiAmount;
    }

    public Double getDeductAmount() {
        return deductAmount;
    }

    public void setDeductAmount(Double deductAmount) {
        this.deductAmount = deductAmount;
    }

    public Date getRedeemDepositDatetime() {
        return redeemDepositDatetime;
    }

    public void setRedeemDepositDatetime(Date redeemDepositDatetime) {
        this.redeemDepositDatetime = redeemDepositDatetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
