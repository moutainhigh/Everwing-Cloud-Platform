package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/9/28.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/28
 * @author wusongti@lii.com.cn
 */
public class TProductPaymentDetailList extends TProductPaymentDetail{
    private static final long serialVersionUID = -6054720909095712298L;

    private String payTypeName;

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }
}
