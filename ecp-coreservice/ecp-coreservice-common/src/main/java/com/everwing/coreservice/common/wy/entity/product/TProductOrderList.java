package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/9/27.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/27
 * @author wusongti@lii.com.cn
 */
public class TProductOrderList extends TProductOrder {
    private static final long serialVersionUID = -6054720909095712298L;

    private String statusName;

    private String buyerName;


    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
}
