package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/12/5.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/12/5
 * @author wusongti@lii.com.cn
 */
public class TDepositDetailList extends TDepositDetail{

    private static final long serialVersionUID = -3357283040339408315L;

    // 支付类型集合，以分隔符分割类型
    private String payTypeNames;

    public String getPayTypeNames() {
        return payTypeNames;
    }

    public void setPayTypeNames(String payTypeNames) {
        this.payTypeNames = payTypeNames;
    }
}
