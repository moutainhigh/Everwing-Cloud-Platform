package com.everwing.coreservice.wy.api.product;/**
 * Created by wust on 2017/12/8.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.product.*;
import com.everwing.coreservice.common.wy.service.product.TDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/12/8
 * @author wusongti@lii.com.cn
 */
@Component
public class TDepositApi {
    @Autowired
    private TDepositService tDepositService;

    public RemoteModelResult<BaseDto> listPage(WyBusinessContext ctx, TDepositSearch tDepositSearch){
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(tDepositService.listPage(ctx,tDepositSearch));
        return result;
    }

    public RemoteModelResult<List<TDepositList>> findByCondition(WyBusinessContext ctx, TDepositSearch tDepositSearch){
        RemoteModelResult<List<TDepositList>> result = new RemoteModelResult<>();
        result.setModel(tDepositService.findByCondition(ctx,tDepositSearch));
        return result;
    }

    public RemoteModelResult<List<TDepositDetailList>> findTDepositDetailByCondition(WyBusinessContext ctx, TDepositDetailSearch tDepositDetailSearch){
        RemoteModelResult<List<TDepositDetailList>> result = new RemoteModelResult<>();
        result.setModel(tDepositService.findTDepositDetailByCondition(ctx,tDepositDetailSearch));
        return result;
    }

    public RemoteModelResult<List<TDepositDetailList>> findDepositDetailByDepositId(WyBusinessContext ctx, String depositId){
        RemoteModelResult<List<TDepositDetailList>> result = new RemoteModelResult<>();
        result.setModel(tDepositService.findDepositDetailByDepositId(ctx,depositId));
        return result;
    }

    public RemoteModelResult<MessageMap> rollBackDeposit(WyBusinessContext ctx,TDepositDetail tDepositDetail,List<TProductPaymentDetail> productPaymentDetails){
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tDepositService.rollBackDeposit(ctx,tDepositDetail,productPaymentDetails));
        return result;
    }
}
