package com.everwing.coreservice.wy.api.product;/**
 * Created by wust on 2017/9/27.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.product.*;
import com.everwing.coreservice.common.wy.service.product.TProductPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/27
 * @author wusongti@lii.com.cn
 */
@Component
public class TProductPaymentApi {
    @Autowired
    private TProductPaymentService tProductPaymentService;

    public RemoteModelResult<List<TProductPaymentDetailList>> findByCondition(WyBusinessContext ctx, TProductPaymentDetailSearch tProductPaymentDetailSearch){
        RemoteModelResult<List<TProductPaymentDetailList>> result = new RemoteModelResult<>();
        result.setModel(tProductPaymentService.findByCondition(ctx,tProductPaymentDetailSearch));
        return result;
    }

    public RemoteModelResult<MessageMap> payment(WyBusinessContext ctx,boolean isRenewalTerm,TProductOrder tProductOrder, List<TProductPaymentDetail> productPaymentDetails) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tProductPaymentService.payment(ctx,isRenewalTerm,tProductOrder,productPaymentDetails));
        return result;
    }
}
