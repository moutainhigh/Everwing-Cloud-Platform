package com.everwing.coreservice.common.wy.service.product;/**
 * Created by wust on 2017/8/30.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.product.*;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/30
 * @author wusongti@lii.com.cn
 */
public interface TProductService {

    BaseDto listPageProductDetail(WyBusinessContext ctx, TProductDetailSearch tProductDetailSearch);

    List<TProductDetailList> findProductDetailByCondition(WyBusinessContext ctx, TProductDetailSearch tProductDetailSearch);

    BaseDto listPageProductSaleHistory(WyBusinessContext ctx,ProductSaleHistorySearch productSaleHistorySearch);


    MessageMap batchInsertProduct(WyBusinessContext ctx, TProductDetailInsert tProductInsert);

    /**
     * 上架
     * @param ctx
     * @param tProductDetailModify
     * @return
     */
    MessageMap shelve(WyBusinessContext ctx, TProductDetailModify tProductDetailModify);

    /**
     * 下架
     * @param ctx
     * @param tProductDetailModify
     * @return
     */
    MessageMap normalOffShelve(WyBusinessContext ctx, TProductDetailModify tProductDetailModify);

    MessageMap modifyProductDetail(WyBusinessContext ctx, TProductDetailModify tProductModify);

    MessageMap deleteProductDetailByProductCode(WyBusinessContext ctx, String projectId, String batchNo, String productCode);
}
