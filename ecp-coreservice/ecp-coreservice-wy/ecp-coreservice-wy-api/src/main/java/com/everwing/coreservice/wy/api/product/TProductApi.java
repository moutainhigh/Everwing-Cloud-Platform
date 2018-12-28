package com.everwing.coreservice.wy.api.product;/**
 * Created by wust on 2017/8/30.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.product.*;
import com.everwing.coreservice.common.wy.service.product.TProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/30
 * @author wusongti@lii.com.cn
 */
@Component
public class TProductApi {
    @Autowired
    private TProductService tProductService;

    public RemoteModelResult<BaseDto> listPageProductDetail(WyBusinessContext ctx, TProductDetailSearch tProductDetailSearch) {
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(tProductService.listPageProductDetail(ctx,tProductDetailSearch));
        return result;
    }

    public RemoteModelResult<List<TProductDetailList>> findProductDetailByCondition(WyBusinessContext ctx, TProductDetailSearch tProductDetailSearch) {
        RemoteModelResult<List<TProductDetailList>> result = new RemoteModelResult<>();
        result.setModel(tProductService.findProductDetailByCondition(ctx,tProductDetailSearch));
        return result;
    }

    public RemoteModelResult<BaseDto> listPageProductSaleHistory(WyBusinessContext ctx, ProductSaleHistorySearch productSaleHistorySearch) {
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(tProductService.listPageProductSaleHistory(ctx,productSaleHistorySearch));
        return result;
    }

    public RemoteModelResult<MessageMap> batchInsertProduct(WyBusinessContext ctx, TProductDetailInsert tProductInsert) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tProductService.batchInsertProduct(ctx,tProductInsert));
        return result;
    }



    public RemoteModelResult<MessageMap> modifyProductDetail(WyBusinessContext ctx, TProductDetailModify tProductModify) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tProductService.modifyProductDetail(ctx,tProductModify));
        return result;
    }

    public RemoteModelResult<MessageMap> deleteProductDetailByProductCode(WyBusinessContext ctx, String projectId, String batchNo, String productCode) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tProductService.deleteProductDetailByProductCode(ctx,projectId,batchNo,productCode));
        return result;
    }

    public RemoteModelResult<MessageMap> shelve(WyBusinessContext ctx, TProductDetailModify tProductDetailModify) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tProductService.shelve(ctx,tProductDetailModify));
        return result;
    }

    public RemoteModelResult<MessageMap> normalOffShelve(WyBusinessContext ctx, TProductDetailModify tProductDetailModify) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tProductService.normalOffShelve(ctx,tProductDetailModify));
        return result;
    }

}
