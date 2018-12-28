package com.everwing.coreservice.common.wy.service.product;/**
 * Created by wust on 2017/12/8.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.product.*;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/12/8
 * @author wusongti@lii.com.cn
 */
public interface TDepositService {
    BaseDto listPage(WyBusinessContext ctx, TDepositSearch tDepositSearch);

    List<TDepositList> findByCondition(WyBusinessContext ctx, TDepositSearch tDepositSearch);

    List<TDepositDetailList> findTDepositDetailByCondition(WyBusinessContext ctx, TDepositDetailSearch tDepositDetailSearch);

    List<TDepositDetailList> findDepositDetailByDepositId(WyBusinessContext ctx, String depositId);

    MessageMap rollBackDeposit(WyBusinessContext ctx,TDepositDetail tDepositDetail,List<TProductPaymentDetail> productPaymentDetails);
}
