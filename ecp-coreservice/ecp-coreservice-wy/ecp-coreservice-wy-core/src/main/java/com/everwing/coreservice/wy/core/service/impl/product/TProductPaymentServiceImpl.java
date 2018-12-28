package com.everwing.coreservice.wy.core.service.impl.product;/**
 * Created by wust on 2017/9/27.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.entity.product.*;
import com.everwing.coreservice.common.wy.service.delivery.TJgAccountReceivableService;
import com.everwing.coreservice.common.wy.service.product.TProductPaymentService;
import com.everwing.coreservice.wy.dao.mapper.product.*;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysProjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/27
 * @author wusongti@lii.com.cn
 */
@Service("tProductPaymentServiceImpl")
public class TProductPaymentServiceImpl implements TProductPaymentService {

    @Autowired
    private TProductPaymentDetailMapper tProductPaymentDetailMapper;

    @Autowired
    private TProductOrderMapper tProductOrderMapper;

    @Autowired
    private TProductOrderDetailMapper tProductOrderDetailMapper;

    @Autowired
    private TProductMapper tProductMapper;

    @Autowired
    private TDepositMapper tDepositMapper;

    @Autowired
    private TJgAccountReceivableService tJgAccountReceivableService;

    @Autowired
    private TSysProjectMapper tSysProjectMapper;


    @Override
    public List<TProductPaymentDetailList> findByCondition(WyBusinessContext ctx, TProductPaymentDetailSearch tProductPaymentDetailSearch) {
        return tProductPaymentDetailMapper.findByCondition(tProductPaymentDetailSearch);
    }


    /**
     * 支付
     * @param ctx
     * @param isRenewalTerm
     * @param productOrderPara
     * @param productPaymentDetails
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap payment(WyBusinessContext ctx,boolean isRenewalTerm,final TProductOrder productOrderPara, List<TProductPaymentDetail> productPaymentDetails) {
        MessageMap mm = new MessageMap();

        if(CollectionUtils.isNotEmpty(productPaymentDetails)){
            // 查询订单状态
            TProductOrderSearch tProductOrderSearch = new TProductOrderSearch();
            tProductOrderSearch.setBatchNo(productOrderPara.getBatchNo());
            tProductOrderSearch.setVersion(productOrderPara.getVersion());
            List<TProductOrderList> tProductOrderLists = tProductOrderMapper.findByCondition(tProductOrderSearch);
            if(CollectionUtils.isEmpty(tProductOrderLists)){
                throw new ECPBusinessException("支付失败，该订单刚刚被更新了，请到销售记录查询该订单状态，订单号["+productOrderPara.getBatchNo()+"]。");
            }

            TProductOrder productOrder = tProductOrderLists.get(0);
            String productType = productOrder.getProductType();


            /**
             * 1.更新产品状态为已售
             * 2.提取押金记录
             */
            TProductOrderDetailSearch tProductOrderDetailSearch = new TProductOrderDetailSearch();
            tProductOrderDetailSearch.setOrderBatchNo(productOrder.getBatchNo());
            List<TProductOrderDetailList> tProductOrderDetailLists = tProductOrderDetailMapper.findByCondition(tProductOrderDetailSearch);
            if(CollectionUtils.isNotEmpty(tProductOrderDetailLists)){
                for (TProductOrderDetailList tProductOrderDetailList : tProductOrderDetailLists) {
                    String projectId = productOrder.getProjectId();
                    String productCode = tProductOrderDetailList.getProductCode();

                    if(ProductConstant.PRODUCTTYPE_FIXEDCARPARK.equalsIgnoreCase(productType)) { // 固定车位
                        if(!isRenewalTerm){
                            TProductDetailSearch tProductDetailSearch = new TProductDetailSearch();
                            tProductDetailSearch.setProductDetailTableId("productdetail_fixedCarPark");
                            tProductDetailSearch.setCode(productCode);
                            List<TProductDetailList> productDetailLists = tProductMapper.findProductDetailByCondition(tProductDetailSearch);
                            if(CollectionUtils.isNotEmpty(productDetailLists)){
                                TProductDetailModify tProductDetailModify = new TProductDetailModify();
                                Map<String,String> fieldMap = new HashMap<>();
                                for (TProductDetailList productDetailList : productDetailLists) {
                                    if(ProductConstant.PRODUCTDETAIL_COLUMN_MARKET_STATE.equalsIgnoreCase(productDetailList.getFieldName())){
                                        String fieldValule = productDetailList.getFieldValue();
                                        if(ProductConstant.MARKET_STATE_002.equals(fieldValule)){
                                            throw new ECPBusinessException("支付失败，很遗憾，您购买的产品已出售，订单号["+productOrderPara.getBatchNo()+"]，产品编码["+productDetailList.getCode()+"]");
                                        }
                                        fieldMap.put(productDetailList.getFieldId(),ProductConstant.MARKET_STATE_002); // 已售
                                    }else{
                                        fieldMap.put(productDetailList.getFieldId(),productDetailList.getFieldValue());
                                    }

                                    if(StringUtils.isBlank(CommonUtils.null2String(tProductDetailModify.getBatchNo()))){
                                        tProductDetailModify.setBatchNo(productDetailList.getBatchNo());
                                    }
                                }
                                fieldMap.put("productdetail_fixedCarPark_modify_time",new DateTime().toString());
                                tProductDetailModify.setFieldMap(fieldMap);
                                tProductDetailModify.setProjectId(projectId);
                                tProductDetailModify.setCode(productCode);
                                tProductMapper.modifyProductDetail(tProductDetailModify);
                            }
                        }
                    }else if(ProductConstant.PRODUCTTYPE_COMMONSERVICE.equalsIgnoreCase(productType)) { // 普通产品

                    }else if(ProductConstant.PRODUCTTYPE_COMMONSERVICE.equalsIgnoreCase(productType)) { // 建筑租赁

                    }else if(ProductConstant.PRODUCTTYPE_COMMONSERVICE.equalsIgnoreCase(productType)) { // 停车优惠卡

                    }else if(ProductConstant.PRODUCTTYPE_COMMONSERVICE.equalsIgnoreCase(productType)) { // 装修服务

                    }else if(ProductConstant.PRODUCTTYPE_COMMONSERVICE.equalsIgnoreCase(productType)) { // 门禁卡

                    }
                }
            }



            // 创建支付记录
            tProductPaymentDetailMapper.batchInsert(productPaymentDetails);


            // 修改订单状态
            productOrder.setDiscountPrice(productOrderPara.getDiscountPrice());
            productOrder.setStatus(LookupItemEnum.productOrderState_success.getStringValue());
            productOrder.setModifyId(ctx.getUserId());
            productOrder.setModifyName(ctx.getStaffName());
            int result = tProductOrderMapper.modify(productOrder);
            if(result < 1){
                throw new ECPBusinessException("支付失败，请重新查询该订单状态。");
            }
        }
        return mm;
    }
}
