package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/10/19.
 */

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/10/19
 * @author wusongti@lii.com.cn
 */
public class TProductOrderDetailSearch extends TProductOrderDetail{
    private static final long serialVersionUID = 5056788521174288522L;

    private List<String> orderIdList;

    public List<String> getOrderIdList() {
        return orderIdList;
    }

    public void setOrderIdList(List<String> orderIdList) {
        this.orderIdList = orderIdList;
    }
}
