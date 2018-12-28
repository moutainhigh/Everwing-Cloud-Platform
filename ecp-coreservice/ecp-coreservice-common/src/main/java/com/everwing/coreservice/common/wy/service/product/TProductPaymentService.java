package com.everwing.coreservice.common.wy.service.product;/**
 * Created by wust on 2017/9/27.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.product.*;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/27
 * @author wusongti@lii.com.cn
 */
public interface TProductPaymentService {
    List<TProductPaymentDetailList> findByCondition(WyBusinessContext ctx, TProductPaymentDetailSearch tProductPaymentDetailSearch);

    /**
     * 支付，判断支付是否完成，完成了则修改支付状态、订单状态和记录历史订单信息
     * @param ctx
     * @param isRenewalTerm
     * @param tProductOrder
     * @param productPaymentDetails
     * @return
     */
    MessageMap payment(final WyBusinessContext ctx,boolean isRenewalTerm, final TProductOrder tProductOrder,final  List<TProductPaymentDetail> productPaymentDetails);
}
