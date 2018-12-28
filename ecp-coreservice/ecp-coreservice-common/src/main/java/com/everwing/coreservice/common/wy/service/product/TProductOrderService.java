package com.everwing.coreservice.common.wy.service.product;/**
 * Created by wust on 2017/9/28.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.product.*;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:产品订单接口
 * Reason:
 * Date:2017/9/28
 * @author wusongti@lii.com.cn
 */
public interface TProductOrderService {
    /**
     * 分页查询订单
     * @param ctx
     * @param tProductOrderSearch
     * @return
     */
    BaseDto listPage(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch);

    /**
     * 获取指定条件的订单
     * @param ctx
     * @param tProductOrderSearch
     * @return
     */
    List<TProductOrderList> findByCondition(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch);


    /**
     * 获取指定条件的订单明细
     * @param ctx
     * @param tProductOrderDetailSearch
     * @return
     */
    List<TProductOrderDetailList> findTProductOrderDetailByCondition(WyBusinessContext ctx, TProductOrderDetailSearch tProductOrderDetailSearch);

    /**
     * 根据产品编码获取已售的订单明细
     * @param ctx
     * @param productCode
     * @return
     */
    List<TProductOrderDetailList> findSoldProductByProductCode(WyBusinessContext ctx, String productCode);


    /**
     * 根据产品编码获取最新的订单明细
     * @param ctx
     * @param productCode
     * @return
     */
    List<TProductOrderDetailList> findRecentProductOrderByProductCode(WyBusinessContext ctx,String productCode);

    /**
     * 创建订单信息
     * @param ctx
     * @param tProductOrder
     * @param tProductOrderDetails
     * @return
     */
    MessageMap createOrderInfo(WyBusinessContext ctx, TProductOrder tProductOrder, List<TProductOrderDetail> tProductOrderDetails);

    /**
     * 作废订单
     * @param ctx
     * @param tProductOrder
     * @return
     */
    MessageMap disableProductOrder(WyBusinessContext ctx, TProductOrder tProductOrder);

    /**
     * 产品收款报表
     * @param tProductOrderSearch
     * @return
     */
    List<Map> productCollectingReports(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch);

    /**
     * 产品收款报表（流水）
     * @param tProductOrderSearch
     * @return
     */
    List<Map> productCollectingSerialNumberReports(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch);

    /**
     * 产品收款方式汇总报表
     * @param tProductOrderSearch
     * @return
     */
    List<Map> productPayTypeReports(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch);


    /**
     * 回滚订单
     * @param ctx
     * @param orderNo
     * @return
     */
    MessageMap rollbackProductOrder(WyBusinessContext ctx, String orderNo);
}
