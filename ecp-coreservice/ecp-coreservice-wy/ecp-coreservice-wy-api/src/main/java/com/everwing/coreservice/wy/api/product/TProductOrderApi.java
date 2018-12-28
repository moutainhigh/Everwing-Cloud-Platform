package com.everwing.coreservice.wy.api.product;/**
 * Created by wust on 2017/9/28.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.product.*;
import com.everwing.coreservice.common.wy.service.product.TProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/28
 * @author wusongti@lii.com.cn
 */
@Component
public class TProductOrderApi {
    @Autowired
    private TProductOrderService tProductOrderService;

    public RemoteModelResult<BaseDto> listPage(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch){
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(tProductOrderService.listPage(ctx,tProductOrderSearch));
        return result;
    }

    public RemoteModelResult<List<TProductOrderList>> findByCondition(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch){
        RemoteModelResult<List<TProductOrderList>> result = new RemoteModelResult<>();
        result.setModel(tProductOrderService.findByCondition(ctx,tProductOrderSearch));
        return result;
    }
    
    public RemoteModelResult<List<TProductOrderDetailList>> findTProductOrderDetailByCondition(WyBusinessContext ctx, TProductOrderDetailSearch tProductOrderDetailSearch){
        RemoteModelResult<List<TProductOrderDetailList>> result = new RemoteModelResult<>();
        result.setModel(tProductOrderService.findTProductOrderDetailByCondition(ctx,tProductOrderDetailSearch));
        return result;
    }

    public RemoteModelResult<List<TProductOrderDetailList>> findSoldProductByProductCode(WyBusinessContext ctx, String productCode){
        RemoteModelResult<List<TProductOrderDetailList>> result = new RemoteModelResult<>();
        result.setModel(tProductOrderService.findSoldProductByProductCode(ctx,productCode));
        return result;
    }

    public RemoteModelResult<List<TProductOrderDetailList>> findRecentProductOrderByProductCode(WyBusinessContext ctx, String productCode){
        RemoteModelResult<List<TProductOrderDetailList>> result = new RemoteModelResult<>();
        result.setModel(tProductOrderService.findRecentProductOrderByProductCode(ctx,productCode));
        return result;
    }

    public RemoteModelResult<MessageMap> createOrderInfo(WyBusinessContext ctx, TProductOrder tProductOrder, List<TProductOrderDetail> tProductOrderDetails) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tProductOrderService.createOrderInfo(ctx,tProductOrder,tProductOrderDetails));
        return result;
    }

    public RemoteModelResult<MessageMap> disableProductOrder(WyBusinessContext ctx, TProductOrder tProductOrder) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tProductOrderService.disableProductOrder(ctx,tProductOrder));
        return result;
    }



    public RemoteModelResult<List<Map>> productCollectingReports(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch){
        RemoteModelResult<List<Map>> result = new RemoteModelResult<>();
        result.setModel(tProductOrderService.productCollectingReports(ctx,tProductOrderSearch));
        return result;
    }


    public RemoteModelResult<List<Map>> productCollectingSerialNumberReports(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch){
        RemoteModelResult<List<Map>> result = new RemoteModelResult<>();
        result.setModel(tProductOrderService.productCollectingSerialNumberReports(ctx,tProductOrderSearch));
        return result;
    }

    public RemoteModelResult<List<Map>> productPayTypeReports(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch){
        RemoteModelResult<List<Map>> result = new RemoteModelResult<>();
        result.setModel(tProductOrderService.productPayTypeReports(ctx,tProductOrderSearch));
        return result;
    }

    public RemoteModelResult<MessageMap> rollbackProductOrder(WyBusinessContext ctx, String orderNo) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tProductOrderService.rollbackProductOrder(ctx,orderNo));
        return result;
    }
}
