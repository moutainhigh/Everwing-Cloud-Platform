package com.everwing.server.wy.web.controller.product;/**
 * Created by wust on 2018/12/7.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.product.*;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.common.wy.fee.constant.*;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderDto;
import com.everwing.coreservice.common.wy.fee.entity.ProjectPrestoreDetail;
import com.everwing.coreservice.wy.api.product.TProductApi;
import com.everwing.coreservice.wy.api.product.TProductOrderApi;
import com.everwing.coreservice.wy.api.product.TProductPaymentApi;
import com.everwing.coreservice.wy.api.sys.TSysProjectApi;
import com.everwing.coreservice.wy.fee.order.api.AcFeeOrderApi;
import com.everwing.server.wy.web.controller.product.charging.ProductChargingContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * Function:产品订单前端控制
 * Reason:
 * Date:2018/12/7
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/ProductOrderController")
public class ProductOrderController {
    static Logger logger = LogManager.getLogger(ProductOrderController.class);

    private static final String MY_SHOPING_CAR_KEY = "shoppingcart_%s_%s"; // 购物车存储数据对应的key

    @Autowired
    private SpringRedisTools springRedisTools;

    @Autowired
    private TProductApi tProductApi;

    @Autowired
    private TProductOrderApi tProductOrderApi;

    @Autowired
    private TProductPaymentApi tProductPaymentApi;

    @Autowired
    private TSysProjectApi tSysProjectApi;

    @Autowired
    private AcFeeOrderApi acFeeOrderApi;



    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Product,businessName="分页查询订单信息",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPageProductOrder",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageProductOrder(@RequestBody TProductOrderSearch tProductOrderSearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = tProductOrderApi.listPage(ctx,tProductOrderSearch);
        if(result.isSuccess()){
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Product,businessName="分页查询产品销售记录",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPageProductSaleHistory",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageProductSaleHistory(@RequestBody ProductSaleHistorySearch productSaleHistorySearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = tProductApi.listPageProductSaleHistory(ctx,productSaleHistorySearch);
        if(result.isSuccess()){
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    /**
     * 展示订单明细信息，包括支付信息
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/showProductOrderDetail/{orderNo}",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto showProductOrderDetail(@PathVariable String orderNo) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        String productType = "";
        TProductOrderSearch tProductOrderSearch = new TProductOrderSearch();
        tProductOrderSearch.setBatchNo(orderNo);
        RemoteModelResult<List<TProductOrderList>> remoteModelResult = tProductOrderApi.findByCondition(ctx,tProductOrderSearch);
        if(remoteModelResult.isSuccess()) {
            TProductOrderList tProductOrderList = remoteModelResult.getModel().get(0);
            productType = tProductOrderList.getProductType();
        }

        /**
         * 获取订单明细
         */
        TProductOrderDetailSearch tProductOrderDetailSearch = new TProductOrderDetailSearch();
        tProductOrderDetailSearch.setOrderBatchNo(orderNo);
        RemoteModelResult<List<TProductOrderDetailList>> remoteModelResult1 = tProductOrderApi.findTProductOrderDetailByCondition(ctx,tProductOrderDetailSearch);
        if(remoteModelResult1.isSuccess()){
            List<TProductOrderDetailList> tProductOrderDetailLists = remoteModelResult1.getModel();
            if(CollectionUtils.isNotEmpty(tProductOrderDetailLists)){
                for (TProductOrderDetailList tProductOrderDetailList : tProductOrderDetailLists) {
                    if(ProductConstant.PRODUCTTYPE_BUILDINGLEASE.equals(productType)){

                    }else if(ProductConstant.PRODUCTTYPE_CARPARKSCARD.equals(productType)){

                    }else if(ProductConstant.PRODUCTTYPE_FIXEDCARPARK.equals(productType)){
                        JSONObject jsonObject = JSONObject.parseObject(tProductOrderDetailList.getProductCommon());
                        StringBuffer text = new StringBuffer();
                        text.append("产品名称：").append(jsonObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_NAME)).append("<br/>");
                        text.append("平均价格：").append(CommonUtils.null2String(jsonObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE))).append("元").append("<br/>");
                        text.append("押金金额：").append(CommonUtils.null2String(jsonObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT))).append("元").append("<br/>");
                        text.append("工本费：").append(CommonUtils.null2String(jsonObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_CARD_FEE))).append("元").append("<br/>");
                        text.append("客户：").append(CommonUtils.null2String(jsonObject.getString("customerName"))).append("<br/>");
                        text.append("资产：").append(CommonUtils.null2String(jsonObject.getString("buildingFullName"))).append("<br/>");
                        text.append("车辆：").append(CommonUtils.null2String(jsonObject.getString("vehicleNumber"))).append("<br/>");
                        tProductOrderDetailList.setProductCommon(text.toString());
                    }else if(ProductConstant.PRODUCTTYPE_ENTRANCEGUARDCARD.equals(productType)){
                    }else if(ProductConstant.PRODUCTTYPE_DECORATIONSERVICE.equals(productType)){
                        JSONObject jsonObject = JSONObject.parseObject(tProductOrderDetailList.getProductCommon());    // 主产品
                        JSONArray jsonArraySubProudct = jsonObject.getJSONArray("sub_product");                    // 子产品
                        StringBuffer text = new StringBuffer();
                        text.append("<b>主产品信息</b><br/>");
                        text.append("&nbsp;&nbsp;&nbsp;&nbsp;产品名称：").append(jsonObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_NAME)).append("<br/>");
                        text.append("&nbsp;&nbsp;&nbsp;&nbsp;建筑面积：").append(jsonObject.getString("buildingArea")).append("平米").append("<br/>");
                        text.append("<b>子产品信息</b><br/>");
                        for (int i = 0;i<jsonArraySubProudct.size();i++) {
                            JSONObject subProductJSONObject = jsonArraySubProudct.getJSONObject(i);
                            text.append("&nbsp;&nbsp;&nbsp;&nbsp;产品名称：").append(subProductJSONObject.getString("name")).append("<br/>");
                            //text.append("&nbsp;&nbsp;&nbsp;&nbsp;价格方式：").append(getLookupItemNameByCode(ctx,"valuationType",subProductJSONObject.getString("valuationType"))).append("<br/>");
                            text.append("&nbsp;&nbsp;&nbsp;&nbsp;价格：").append(subProductJSONObject.getString("price")).append("元").append("<br/>");
                            text.append("&nbsp;&nbsp;&nbsp;&nbsp;数量：").append(subProductJSONObject.getString("quantity")).append("个").append("<br/>");
                        }


                        text.append("客户：").append(CommonUtils.null2String(jsonObject.getString("customerName"))).append("<br/>");
                        text.append("资产：").append(CommonUtils.null2String(jsonObject.getString("buildingFullName"))).append("<br/>");
                        text.append("车辆：").append(CommonUtils.null2String(jsonObject.getString("vehicleNumber"))).append("<br/>");
                        tProductOrderDetailList.setProductCommon(text.toString());
                    }else if(ProductConstant.PRODUCTTYPE_COMMONSERVICE.equals(productType)){
                    }
                }
                baseDto.setLstDto(tProductOrderDetailLists);
            }
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(remoteModelResult1.getMsg());
        }

        /**
         * 获取支付明细
         */
        TProductPaymentDetailSearch tProductPaymentDetailSearch = new TProductPaymentDetailSearch();
        tProductPaymentDetailSearch.setOrderBatchNo(orderNo);
        RemoteModelResult<List<TProductPaymentDetailList>> remoteModelResult2 = tProductPaymentApi.findByCondition(ctx,tProductPaymentDetailSearch);
        if(remoteModelResult2.isSuccess()){
            baseDto.setLstDetailDto(remoteModelResult2.getModel());
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(remoteModelResult2.getMsg());
        }

        baseDto.setMessageMap(mm);
        return baseDto;
    }

    /**
     * 续费
     * 1.根据产品编号找出其有效订单，并复制；
     * 2.修复复制后的订单价格信息和数量信息，生成新订单。
     * @param myShoppingCart
     * @return MessageMap
     */
    @RequestMapping(value = "/deferBuyerEndDate",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap deferBuyerEndDate(@RequestBody MyShoppingCart myShoppingCart) {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        // 项目id
        String projectId = myShoppingCart.getProjectId();

        RemoteModelResult<String> orderNoRemoteModelResult = acFeeOrderApi.getOrderNo(ctx.getCompanyId(), projectId,BusinessType.PAYMENT.getCode(), PersonType.QT.getCode(), ClientType.PC.getCode(), AcOrderTypeEnum.PRODUCT.getType());
        if(!orderNoRemoteModelResult.isSuccess()){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("生产订单号失败：" + orderNoRemoteModelResult.getMsg());
            return mm;
        }
        String orderNo =  orderNoRemoteModelResult.getModel();

        TProductOrder productOrderNew = null;
        TProductOrderDetail productOrderDetailNew = null;

        String productCode = myShoppingCart.getProductCode();
        String productType = myShoppingCart.getProductType();
        if(ProductConstant.PRODUCTTYPE_BUILDINGLEASE.equals(productType)){              // 建筑租赁
        }else if(ProductConstant.PRODUCTTYPE_CARPARKSCARD.equals(productType)){         // 停车优惠卡
        }else if(ProductConstant.PRODUCTTYPE_FIXEDCARPARK.equals(productType)){         // 固定车位
            MyShoppingCartFixedcarPark myShoppingCartFixedcarPark = myShoppingCart.getMyShoppingCartFixedcarPark();
            JSONObject productJSONObject = myShoppingCartFixedcarPark.getProductJSONObject();
            Integer quantity = myShoppingCart.getMyShoppingCartFixedcarPark().getQuantity();
            String description = myShoppingCartFixedcarPark.getDescription();

            TProductOrderDetailSearch tProductOrderDetailSearch = new TProductOrderDetailSearch();
            tProductOrderDetailSearch.setProductCode(productCode);
            RemoteModelResult<List<TProductOrderDetailList>> remoteModelResult = tProductOrderApi.findRecentProductOrderByProductCode(ctx,productCode);
            if(remoteModelResult.isSuccess()){
                List<TProductOrderDetailList> tProductOrderDetailLists = remoteModelResult.getModel();
                if(CollectionUtils.isNotEmpty(tProductOrderDetailLists)){ // 从业务上来讲，是不会存在空情况
                    TProductOrderDetailList tProductOrderDetailList = tProductOrderDetailLists.get(0);
                    try {
                        productOrderDetailNew = (TProductOrderDetail)tProductOrderDetailList.clone();
                    } catch (CloneNotSupportedException e) {
                        mm.setFlag(MessageMap.INFOR_WARNING);
                        mm.setMessage("后台异常");
                        return mm;
                    }

                    TProductOrderSearch tProductOrderSearch = new TProductOrderSearch();
                    tProductOrderSearch.setBatchNo(tProductOrderDetailList.getOrderBatchNo());
                    RemoteModelResult<List<TProductOrderList>> remoteModelResult1 = tProductOrderApi.findByCondition(ctx,tProductOrderSearch);
                    if(remoteModelResult1.isSuccess()){
                        List<TProductOrderList> tProductOrderLists = remoteModelResult1.getModel();
                        if(CollectionUtils.isNotEmpty(tProductOrderLists)){
                            TProductOrderList tProductOrderList = tProductOrderLists.get(0);
                            try {
                                productOrderNew = (TProductOrder)tProductOrderList.clone();
                            } catch (CloneNotSupportedException e) {
                                mm.setFlag(MessageMap.INFOR_WARNING);
                                mm.setMessage("后台异常");
                                return mm;
                            }
                        }
                    }
                }
            }

            if(productOrderNew != null){
                // 计费
                myShoppingCart.setRenewalTerm(true);
                this.calculatePrice(ctx,myShoppingCart);

                String priceStr = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE);
                String buyEndDateStr = productOrderDetailNew.getBuyEndDate();
                DateTime buyEndDateDateTime = new DateTime(buyEndDateStr);


                productOrderNew.setId(UUID.randomUUID().toString());
                productOrderNew.setBatchNo(orderNo);
                productOrderNew.setStatus(LookupItemEnum.productOrderState_draft.getStringValue());
                productOrderNew.setTotalPrice(new BigDecimal(myShoppingCartFixedcarPark.getTotalPrice()));
                productOrderNew.setCreaterId(ctx.getUserId());
                productOrderNew.setCreaterName(ctx.getStaffName());

                productOrderDetailNew.setOrderId(productOrderNew.getId());
                productOrderDetailNew.setOrderBatchNo(orderNo);
                productOrderDetailNew.setPrice(new BigDecimal(priceStr));
                productOrderDetailNew.setOrderAmount(new BigDecimal(myShoppingCartFixedcarPark.getTotalPrice()));
                productOrderDetailNew.setBuyEndDate(buyEndDateDateTime.plusMonths(productOrderDetailNew.getQuantity()).toString("yyyy-MM-dd"));
                productOrderDetailNew.setQuantity(quantity);
                productOrderDetailNew.setDescription(description);
                productOrderDetailNew.setCreaterId(ctx.getUserId());
                productOrderDetailNew.setCreaterName(ctx.getStaffName());
            }
        }else if(ProductConstant.PRODUCTTYPE_ENTRANCEGUARDCARD.equals(productType)){    // 门禁卡
        }else if(ProductConstant.PRODUCTTYPE_DECORATIONSERVICE.equals(productType)){    // 装修服务
        }else if(ProductConstant.PRODUCTTYPE_COMMONSERVICE.equals(productType)) {       // 普通服务
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("该产产品类型尚未开放");
            return mm;
        }


        List<TProductOrderDetail> addProductOrderDetails = new ArrayList<>(1);
        addProductOrderDetails.add(productOrderDetailNew);
        RemoteModelResult<MessageMap> remoteModelResult = tProductOrderApi.createOrderInfo(ctx,productOrderNew,addProductOrderDetails);
        if(!remoteModelResult.isSuccess()){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(remoteModelResult.getMsg());
        }else {
            mm = remoteModelResult.getModel();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderNo",productOrderNew.getBatchNo());
        jsonObject.put("version",1);
        jsonObject.put("totalPrice",productOrderNew.getTotalPrice());
        mm.setObj(jsonObject.toJSONString());
        return mm;
    }


    /**
     * 立即购买
     * @param myShoppingCart
     * @return MessageMap
     */
    @RequestMapping(value = "/buyQuickly",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap buyQuickly(@RequestBody MyShoppingCart myShoppingCart) {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        // 项目id
        String projectId = myShoppingCart.getProjectId();

        RemoteModelResult<String> orderNoRemoteModelResult = acFeeOrderApi.getOrderNo(ctx.getCompanyId(), projectId,BusinessType.PAYMENT.getCode(), PersonType.QT.getCode(), ClientType.PC.getCode(), AcOrderTypeEnum.PRODUCT.getType());
        if(!orderNoRemoteModelResult.isSuccess()){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("生产订单号失败：" + orderNoRemoteModelResult.getMsg());
            return mm;
        }

        // 订单号
        String orderNo = orderNoRemoteModelResult.getModel();



        // 产品类型
        String productType = myShoppingCart.getProductType();

        // 买家
        String customerId = myShoppingCart.getCustomer().getCustomerId();

        // 订单头
        TProductOrder tProductOrder = new TProductOrder();

        // 订单明细
        List<TProductOrderDetail> tProductOrderDetails = new ArrayList<>();


        /**
         * 计费
         */
        this.calculatePrice(ctx,myShoppingCart);



        if(ProductConstant.PRODUCTTYPE_BUILDINGLEASE.equals(productType)){              // 建筑租赁
        }else if(ProductConstant.PRODUCTTYPE_CARPARKSCARD.equals(productType)){         // 停车优惠卡
        }else if(ProductConstant.PRODUCTTYPE_FIXEDCARPARK.equals(productType)){         // 固定车位
            MyShoppingCartFixedcarPark myShoppingCartFixedcarPark = myShoppingCart.getMyShoppingCartFixedcarPark();

            String totalPrice = myShoppingCartFixedcarPark.getTotalPrice() + "";

            String description = myShoppingCartFixedcarPark.getDescription();

            /**
             * 设置订单头
             */
            tProductOrder.setId(UUID.randomUUID().toString());
            tProductOrder.setProjectId(projectId);
            tProductOrder.setBatchNo(orderNo);
            tProductOrder.setProductType(productType);
            tProductOrder.setProductName(DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),"productType",productType));
            tProductOrder.setBuyerId(customerId);
            tProductOrder.setTotalPrice(new BigDecimal(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP));
            tProductOrder.setStatus(LookupItemEnum.productOrderState_draft.getStringValue());
            tProductOrder.setCreaterId(ctx.getUserId());
            tProductOrder.setCreaterName(ctx.getStaffName());


            /**
             * 设置订单明细
             */
            TProductOrderDetail tProductOrderDetail = new TProductOrderDetail();
            tProductOrderDetails.add(tProductOrderDetail);
            tProductOrderDetail.setId(UUID.randomUUID().toString());
            tProductOrderDetail.setOrderId(tProductOrder.getId());
            tProductOrderDetail.setOrderBatchNo(orderNo);



            /**
             * 抽取相关字段存放到productCommon，方便以后报表查询
             */
            JSONObject productJSONObject = myShoppingCartFixedcarPark.getProductJSONObject();
            JSONObject commonJSONObject = new JSONObject();
            commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_CODE,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_CODE));
            commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_NAME,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_NAME));
            commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE));
            commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_CARD_FEE,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_CARD_FEE));
            commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT));
            commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_TAXRATE,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_TAXRATE));
            commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_IS_CALENDAR_MONTH_SALE,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_IS_CALENDAR_MONTH_SALE));
            commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER));
            commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER_VEHICLE,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER_VEHICLE));
            commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_HOUSE_CODE,myShoppingCart.getAsset().getHouseCode());
            commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_HOUSE_CODE_NEW,myShoppingCart.getAsset().getHouseCodeNew());
            commonJSONObject.put("buildingFullName",myShoppingCart.getAsset().getBuildingFullName());
            commonJSONObject.put("assetAddress",myShoppingCart.getAsset().getAssetAddress());
            commonJSONObject.put("vehicleNumber",myShoppingCart.getVehicle().getVehicleNumber());
            commonJSONObject.put("customerId",myShoppingCart.getCustomer().getCustomerId());
            commonJSONObject.put("customerName",myShoppingCart.getCustomer().getCustomerName());


            tProductOrderDetail.setCustomerId(myShoppingCart.getCustomer().getValue());
            tProductOrderDetail.setHouseCodeNew(myShoppingCart.getAsset().getHouseCodeNew());
            tProductOrderDetail.setVehicleNumber(myShoppingCart.getVehicle().getVehicleNumber());
            tProductOrderDetail.setProductCode(productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_CODE));
            tProductOrderDetail.setOrderAmount(new BigDecimal(myShoppingCartFixedcarPark.getTotalPrice()));
            tProductOrderDetail.setProductCommon(commonJSONObject.toJSONString());
            tProductOrderDetail.setDescription(description);
            tProductOrderDetail.setCreaterId(ctx.getUserId());
            tProductOrderDetail.setCreaterName(ctx.getStaffName());
            if("Yes".equalsIgnoreCase(productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_IS_CALENDAR_MONTH_SALE))){
                DateTime buyerBeginDate = DateTime.parse(myShoppingCartFixedcarPark.getStartTime());
                int buyerBeginDateInt = buyerBeginDate.dayOfMonth().withMinimumValue().dayOfMonth().get();
                String buyerBeginDateStr = buyerBeginDate.toString("yyyy-MM") + "-" + StringUtils.leftPad(buyerBeginDateInt+"",2,"0") ;
                tProductOrderDetail.setBuyBeginDate(buyerBeginDateStr);

                DateTime buyerEndDate = DateTime.parse(myShoppingCartFixedcarPark.getEndTime());
                int buyerEndDateInt = buyerEndDate.dayOfMonth().withMaximumValue().dayOfMonth().get();
                String buyerEndDateStr = buyerEndDate.toString("yyyy-MM") + "-" + StringUtils.leftPad(buyerEndDateInt+"",2,"0") ;
                tProductOrderDetail.setBuyEndDate(buyerEndDateStr);
            }else{
                DateTime buyerBeginDate = DateTime.parse(myShoppingCartFixedcarPark.getStartTime());
                DateTime buyerEndDate = DateTime.parse(myShoppingCartFixedcarPark.getEndTime());
                tProductOrderDetail.setBuyBeginDate(buyerBeginDate.toString("yyyy-MM-dd"));
                tProductOrderDetail.setBuyEndDate(buyerEndDate.toString("yyyy-MM-dd"));
            }

            tProductOrderDetail.setPrice(myShoppingCartFixedcarPark.getProductJSONObject().getBigDecimal(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE));
            tProductOrderDetail.setQuantity(myShoppingCartFixedcarPark.getQuantity());
        }else if(ProductConstant.PRODUCTTYPE_ENTRANCEGUARDCARD.equals(productType)){    // 门禁卡
        }else if(ProductConstant.PRODUCTTYPE_DECORATIONSERVICE.equals(productType)){    // 装修服务
        }else if(ProductConstant.PRODUCTTYPE_COMMONSERVICE.equals(productType)) {        // 普通服务
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("该产产品类型尚未开放");
            return mm;
        }

        RemoteModelResult<MessageMap> remoteModelResult = tProductOrderApi.createOrderInfo(ctx,tProductOrder,tProductOrderDetails);
        if(!remoteModelResult.isSuccess()){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(remoteModelResult.getMsg());
        }else {
            mm = remoteModelResult.getModel();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderNo",orderNo);
        jsonObject.put("version",1);
        jsonObject.put("totalPrice",tProductOrder.getTotalPrice());
        mm.setObj(jsonObject.toJSONString());
        return mm;
    }


    /**
     * 创建订单信息
     * @param createProductOrderRequestBody
     * @return
     */
    @RequestMapping(value = "/createProductOrder",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap createProductOrder(@RequestBody CreateProductOrderRequestBody createProductOrderRequestBody) {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        // 项目id
        String projectId = createProductOrderRequestBody.getProjectId();

        RemoteModelResult<String> orderNoRemoteModelResult = acFeeOrderApi.getOrderNo(ctx.getCompanyId(),projectId, BusinessType.PAYMENT.getCode(), PersonType.QT.getCode(), ClientType.PC.getCode(), AcOrderTypeEnum.PRODUCT.getType());
        if(!orderNoRemoteModelResult.isSuccess()){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("生产订单号失败：" + orderNoRemoteModelResult.getMsg());
            return mm;
        }

        // 订单号
        String orderNo = orderNoRemoteModelResult.getModel();



        // 产品类型
        String productType = createProductOrderRequestBody.getProductType();

        // 买家
        String customerId = createProductOrderRequestBody.getCustomerId();

        // 订单总金额
        String totalPrice = createProductOrderRequestBody.getTotalPrice();

        // 选中的记录
        List<String> checkedProductCodes = createProductOrderRequestBody.getCheckedProductCodes();


        // 订单头
        TProductOrder tProductOrder = new TProductOrder();

        // 订单明细
        List<TProductOrderDetail> tProductOrderDetails = new ArrayList<>();


        String key = String.format(MY_SHOPING_CAR_KEY,productType,ctx.getLoginName());
        String JSONString = springRedisTools.getByKey(key) == null ? null : (String)springRedisTools.getByKey(key);
        List<MyShoppingCart> myShoppingCarts = JSONObject.parseArray(JSONString,MyShoppingCart.class);
        if(CollectionUtils.isNotEmpty(myShoppingCarts)){
            /**
             * 设置订单头
             */
            tProductOrder.setId(UUID.randomUUID().toString());
            tProductOrder.setProjectId(projectId);
            tProductOrder.setBatchNo(orderNo);
            tProductOrder.setProductType(productType);
            tProductOrder.setProductName(DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),"productType",productType));
            tProductOrder.setBuyerId(customerId);
            tProductOrder.setTotalPrice(new BigDecimal(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP));
            tProductOrder.setStatus(LookupItemEnum.productOrderState_draft.getStringValue());
            tProductOrder.setCreaterId(ctx.getUserId());
            tProductOrder.setCreaterName(ctx.getStaffName());


            /**
             * 设置订单明细
             */
            for (MyShoppingCart myShoppingCart : myShoppingCarts) {
                if(!checkedProductCodes.contains(myShoppingCart.getProductCode())){
                    continue;
                }
                TProductOrderDetail tProductOrderDetail = new TProductOrderDetail();
                tProductOrderDetail.setId(UUID.randomUUID().toString());
                tProductOrderDetail.setOrderId(tProductOrder.getId());
                tProductOrderDetail.setOrderBatchNo(orderNo);
                if(ProductConstant.PRODUCTTYPE_BUILDINGLEASE.equals(productType)){              // 建筑租赁
                }else if(ProductConstant.PRODUCTTYPE_CARPARKSCARD.equals(productType)){         // 停车优惠卡
                }else if(ProductConstant.PRODUCTTYPE_FIXEDCARPARK.equals(productType)){         // 固定车位
                    MyShoppingCartFixedcarPark myShoppingCartFixedcarPark = myShoppingCart.getMyShoppingCartFixedcarPark();

                    String description = myShoppingCartFixedcarPark.getDescription();

                    /**
                     * 抽取相关字段存放到productCommon，方便以后报表查询
                     */
                    JSONObject productJSONObject = myShoppingCartFixedcarPark.getProductJSONObject();
                    JSONObject commonJSONObject = new JSONObject();
                    commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_CODE,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_CODE));
                    commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_NAME,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_NAME));
                    commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE));
                    commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_CARD_FEE,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_CARD_FEE));
                    commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT));
                    commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_TAXRATE,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_TAXRATE));
                    commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_IS_CALENDAR_MONTH_SALE,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_IS_CALENDAR_MONTH_SALE));
                    commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER));
                    commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER_VEHICLE,productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER_VEHICLE));
                    commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_HOUSE_CODE,myShoppingCart.getAsset().getHouseCode());
                    commonJSONObject.put(ProductConstant.PRODUCTDETAIL_COLUMN_HOUSE_CODE_NEW,myShoppingCart.getAsset().getHouseCodeNew());
                    commonJSONObject.put("buildingFullName",myShoppingCart.getAsset().getBuildingFullName());
                    commonJSONObject.put("assetAddress",myShoppingCart.getAsset().getAssetAddress());
                    commonJSONObject.put("buildingArea",myShoppingCart.getAsset().getBuildingArea());
                    commonJSONObject.put("usableArea",myShoppingCart.getAsset().getUsableArea());
                    commonJSONObject.put("vehicleNumber",myShoppingCart.getVehicle().getVehicleNumber());
                    commonJSONObject.put("customerId",myShoppingCart.getCustomer().getCustomerId());
                    commonJSONObject.put("customerName",myShoppingCart.getCustomer().getCustomerName());


                    tProductOrderDetail.setCustomerId(myShoppingCart.getCustomer().getValue());
                    tProductOrderDetail.setHouseCodeNew(myShoppingCart.getAsset().getHouseCodeNew());
                    tProductOrderDetail.setVehicleNumber(myShoppingCart.getVehicle().getVehicleNumber());
                    tProductOrderDetail.setProductCode(myShoppingCart.getProductCode());
                    tProductOrderDetail.setOrderAmount(new BigDecimal(myShoppingCartFixedcarPark.getTotalPrice()));
                    tProductOrderDetail.setProductCommon(commonJSONObject.toJSONString());
                    tProductOrderDetail.setDescription(description);
                    tProductOrderDetail.setCreaterId(ctx.getUserId());
                    tProductOrderDetail.setCreaterName(ctx.getStaffName());
                    if("Yes".equalsIgnoreCase(productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_IS_CALENDAR_MONTH_SALE))){
                        DateTime buyerBeginDate = DateTime.parse(myShoppingCartFixedcarPark.getStartTime());
                        int buyerBeginDateInt = buyerBeginDate.dayOfMonth().withMinimumValue().dayOfMonth().get();
                        String buyerBeginDateStr = buyerBeginDate.toString("yyyy-MM") + "-" + StringUtils.leftPad(buyerBeginDateInt+"",2,"0") ;
                        tProductOrderDetail.setBuyBeginDate(buyerBeginDateStr);

                        DateTime buyerEndDate = DateTime.parse(myShoppingCartFixedcarPark.getEndTime());
                        int buyerEndDateInt = buyerEndDate.dayOfMonth().withMaximumValue().dayOfMonth().get();
                        String buyerEndDateStr = buyerEndDate.toString("yyyy-MM") + "-" + StringUtils.leftPad(buyerEndDateInt+"",2,"0") ;
                        tProductOrderDetail.setBuyEndDate(buyerEndDateStr);
                    }else{
                        DateTime buyerBeginDate = DateTime.parse(myShoppingCartFixedcarPark.getStartTime());
                        DateTime buyerEndDate = DateTime.parse(myShoppingCartFixedcarPark.getEndTime());
                        tProductOrderDetail.setBuyBeginDate(buyerBeginDate.toString("yyyy-MM-dd"));
                        tProductOrderDetail.setBuyEndDate(buyerEndDate.toString("yyyy-MM-dd"));
                    }

                    tProductOrderDetail.setPrice(myShoppingCartFixedcarPark.getProductJSONObject().getBigDecimal(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE));
                    tProductOrderDetail.setQuantity(myShoppingCartFixedcarPark.getQuantity());
                }else if(ProductConstant.PRODUCTTYPE_ENTRANCEGUARDCARD.equals(productType)){    // 门禁卡
                }else if(ProductConstant.PRODUCTTYPE_DECORATIONSERVICE.equals(productType)){    // 装修服务
                }else if(ProductConstant.PRODUCTTYPE_COMMONSERVICE.equals(productType)) {        // 普通服务
                }else{
                    continue;
                }
                tProductOrderDetails.add(tProductOrderDetail);
            }


            RemoteModelResult<MessageMap> remoteModelResult = tProductOrderApi.createOrderInfo(ctx,tProductOrder,tProductOrderDetails);
            if(!remoteModelResult.isSuccess()){
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage(remoteModelResult.getMsg());
                springRedisTools.deleteByKey(key);
            }else {
                mm = remoteModelResult.getModel();
            }
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("由于您长时间没有完成购物车订单，购物车数据已经失效，请重来");
        }
        mm.setObj(orderNo);
        return mm;
    }

    /**
     * 线下支付入口
     * @param orderNo   订单号
     * @param payType    支付类型
     * @param payAmount 刷卡金额
     * @param discountAmount 折扣金额
     * @param isRenewalTerm 是否续费
     * @return
     */
    @RequestMapping(value = "/payment/{version}/{orderNo}/{payType}/{payAmount:.+}/{discountAmount:.+}/{isRenewalTerm}",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap payment(@PathVariable Integer version,
                       @PathVariable String orderNo,
                       @PathVariable String payType,
                       @PathVariable String payAmount,
                       @PathVariable String discountAmount,
                       @PathVariable Boolean isRenewalTerm) {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        List<TProductPaymentDetail> productPaymentDetails = new ArrayList<>(1);


        TProductPaymentDetail tProductPaymentDetail = new TProductPaymentDetail();
        tProductPaymentDetail.setOrderBatchNo(orderNo);
        tProductPaymentDetail.setPayType(payType);
        tProductPaymentDetail.setPrice(new BigDecimal(payAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        tProductPaymentDetail.setCreaterId(ctx.getUserId());
        tProductPaymentDetail.setCreaterName(ctx.getStaffName());
        productPaymentDetails.add(tProductPaymentDetail);


        TProductOrder tProductOrder = new TProductOrder();
        tProductOrder.setVersion(version);
        tProductOrder.setBatchNo(orderNo);
        tProductOrder.setDiscountPrice(new BigDecimal(discountAmount));


        /**
         * 支付，修改产品订单状态和记录支付信息
         */
        RemoteModelResult<MessageMap> remoteModelResult1 =  tProductPaymentApi.payment(ctx,isRenewalTerm,tProductOrder,productPaymentDetails);
        if(!remoteModelResult1.isSuccess()){
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult1.getMsg());
        }else{
            boolean needRollback = false;
            try {
                TProductOrderSearch tProductOrderSearch = new TProductOrderSearch();
                tProductOrderSearch.setBatchNo(orderNo);
                tProductOrderSearch.setStatus(LookupItemEnum.productOrderState_success.getStringValue());
                RemoteModelResult<List<TProductOrderList>> remoteModelResult = this.tProductOrderApi.findByCondition(ctx, tProductOrderSearch);
                if (remoteModelResult.isSuccess()) {
                    List<TProductOrderList> tProductOrderLists = remoteModelResult.getModel();
                    if (CollectionUtils.isNotEmpty(tProductOrderLists)) {
                        TProductOrderList tProductOrderList = tProductOrderLists.get(0);

                        String projectId = tProductOrderList.getProjectId();
                        String orderId = tProductOrderList.getId();
                        String productType = tProductOrderList.getProductType();

                        /**
                         * 把订单数据放入公共池
                         */
                        int paymentChannel = payType.equals(LookupItemEnum.productOrderPayType_cash.getStringValue()) ? PayChannel.CASH.getCode()
                                : payType.equals(LookupItemEnum.productOrderPayType_weixinpay.getStringValue()) ? PayChannel.WE_CHAT.getCode()
                                : payType.equals(LookupItemEnum.productOrderPayType_charge.getStringValue()) ? PayChannel.UNION_PAY.getCode()
                                : payType.equals(LookupItemEnum.productOrderPayType_bank.getStringValue()) ? PayChannel.BANK.getCode()
                                : payType.equals(LookupItemEnum.productOrderPayType_alipay.getStringValue()) ? PayChannel.ALIPAY.getCode() : -1;

                        AcOrderDto acOrderDto = new AcOrderDto();
                        acOrderDto.setOrderNo(orderNo);
                        acOrderDto.setAmount(tProductOrderList.getTotalPrice().doubleValue());
                        acOrderDto.setId(tProductOrderList.getId());
                        acOrderDto.setPayer(tProductOrderList.getBuyerId());
                        acOrderDto.setOrderType(BusinessType.PAYMENT.getCode());
                        acOrderDto.setOrderState(AcOrderStateEnum.FINISHED.getState());
                        acOrderDto.setPayState(AcPayStateEnum.PAYED.getState());
                        acOrderDto.setPaymentCahnnel(paymentChannel);
                        acOrderDto.setOperaId(ctx.getUserId());


                        /**
                         * 如果存在押金数据，则把押金数据也推入公共池
                         */
                        ProjectPrestoreDetail projectPrestoreDetail = new ProjectPrestoreDetail();
                        projectPrestoreDetail.setProjectId(projectId);
                        projectPrestoreDetail.setOrderId(orderId);
                        projectPrestoreDetail.setType(ChargingType.DEPOSIT.getCode());
                        projectPrestoreDetail.setBusinessType(BusinessType.PAYMENT.getCode());
                        projectPrestoreDetail.setCreateBy(ctx.getUserId());

                        TProductOrderDetailSearch tProductOrderDetailSearch = new TProductOrderDetailSearch();
                        tProductOrderDetailSearch.setOrderBatchNo(orderNo);
                        RemoteModelResult<List<TProductOrderDetailList>> remoteModelResult3 = this.tProductOrderApi.findTProductOrderDetailByCondition(ctx, tProductOrderDetailSearch);
                        if (remoteModelResult3.isSuccess()) {
                            List<TProductOrderDetailList> tProductOrderDetailLists = remoteModelResult3.getModel();
                            if (CollectionUtils.isNotEmpty(tProductOrderDetailLists)) {
                                BigDecimal totalDeposit = new BigDecimal(0);
                                for (TProductOrderDetailList tProductOrderDetailList : tProductOrderDetailLists) {
                                    String productCommonStr = tProductOrderDetailList.getProductCommon();
                                    if (StringUtils.isBlank(CommonUtils.null2String(productCommonStr))) {
                                        continue;
                                    }

                                    JSONObject productJSONObject = JSONObject.parseObject(productCommonStr);
                                    String houseCodeNew = productJSONObject.containsKey(ProductConstant.PRODUCTDETAIL_COLUMN_HOUSE_CODE_NEW) ? productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_HOUSE_CODE_NEW) : "";
                                    projectPrestoreDetail.setHouseCodeNew(houseCodeNew);
                                    if (ProductConstant.PRODUCTTYPE_DECORATIONSERVICE.equalsIgnoreCase(productType)) {   // 装修服务产品
                                        JSONArray jsonArray = JSONArray.parseArray(productJSONObject.getString("sub_product"));
                                        for (int i = 0; i < jsonArray.size(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String depositFlag = jsonObject.getString("depositFlag");
                                            if (LookupItemEnum.yesNo_yes.getStringValue().equalsIgnoreCase(depositFlag)) {
                                                totalDeposit.add(jsonObject.getBigDecimal("price").multiply(new BigDecimal(tProductOrderDetailList.getQuantity())));
                                            }
                                        }
                                    } else {  // 其他产品
                                        if (productJSONObject.containsKey("deposit")) {
                                            BigDecimal deposit = productJSONObject.getBigDecimal("deposit");
                                            if (deposit.compareTo(BigDecimal.ZERO) != 0) {
                                                totalDeposit.add(deposit.multiply(new BigDecimal(tProductOrderDetailList.getQuantity())));
                                            }
                                        }
                                    }
                                }

                                if (totalDeposit.compareTo(BigDecimal.ZERO) != 0) {
                                    projectPrestoreDetail.setAmount(totalDeposit);
                                } else {
                                    projectPrestoreDetail = null;
                                }
                            }
                        }

                        RemoteModelResult<MessageMap> remoteModelResult2 = acFeeOrderApi.insertOrder(ctx.getCompanyId(), acOrderDto, projectPrestoreDetail);
                        if (!remoteModelResult2.isSuccess()) {
                            needRollback = true;
                            mm = remoteModelResult2.getModel();
                        }
                    }
                }
            }catch (Exception e){
                needRollback = true;
            }finally {
                if(needRollback){
                    logger.error("回滚本地订单数据");
                    this.tProductOrderApi.rollbackProductOrder(ctx,orderNo);
                }
            }
        }
        return mm;
    }



    /**
     * 作废订单
     * @param orderNo
     * @return MessageMap
     */
    @RequestMapping(value = "/disableProductOrder/{version}/{orderNo}",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap disableProductOrder(@PathVariable Integer version,@PathVariable String orderNo) {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        TProductOrder tProductOrder = new TProductOrder();
        tProductOrder.setBatchNo(orderNo);
        tProductOrder.setVersion(version);
        tProductOrder.setStatus(LookupItemEnum.productOrderState_invalid.getStringValue());
        tProductOrder.setModifyId(ctx.getUserId());
        tProductOrder.setModifyName(ctx.getStaffName());
        RemoteModelResult<MessageMap> remoteModelResult = tProductOrderApi.disableProductOrder(ctx,tProductOrder);
        if(remoteModelResult.isSuccess()){
            mm = remoteModelResult.getModel();
        }else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(remoteModelResult.getMsg());
        }
        return mm;
    }



    /**
     * 添加产品到购物车
     * @return
     */
    @RequestMapping(value = "/addToShoppingCart",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap addToShoppingCart(@RequestBody MyShoppingCart parameters) {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        String productType = parameters.getProductType();
        String projectId = parameters.getProjectId();
        String batchNo = parameters.getBatchNo();
        String productCode = parameters.getProductCode();

        // 购物车是否已经存在该产品
        boolean isNewProduct = true;


        // 购物车缓存数据
        String key = String.format(MY_SHOPING_CAR_KEY,productType,ctx.getLoginName());
        String JSONString = springRedisTools.getByKey(key) == null ? null : (String)springRedisTools.getByKey(key);
        List<MyShoppingCart> myShoppingCarts = JSONObject.parseArray(JSONString,MyShoppingCart.class);
        if(CollectionUtils.isNotEmpty(myShoppingCarts)){
            for (MyShoppingCart myShoppingCart : myShoppingCarts) {
                if (myShoppingCart.getProjectId().equals(projectId)
                        && myShoppingCart.getBatchNo().equals(batchNo)
                        && myShoppingCart.getProductCode().equals(productCode)) {   // 如果购物车已经存在该产品，则更新数量和总价
                    if(ProductConstant.PRODUCTTYPE_DECORATIONSERVICE.equalsIgnoreCase(productType)) {    // 装修服务产品
                        MyShoppingCartDecorationService myShoppingCartDecorationService = myShoppingCart.getMyShoppingCartDecorationService();
                        myShoppingCartDecorationService.setQuantity(myShoppingCartDecorationService.getQuantity() + parameters.getMyShoppingCartDecorationService().getQuantity());
                        myShoppingCart.setMyShoppingCartDecorationService(myShoppingCartDecorationService);
                    }else if(ProductConstant.PRODUCTTYPE_BUILDINGLEASE.equalsIgnoreCase(productType)){ // 建筑租赁
                        MyShoppingCartBuildingLease myShoppingCartBuildingLease = myShoppingCart.getMyShoppingCartBuildingLease();
                        myShoppingCartBuildingLease.setQuantity(myShoppingCartBuildingLease.getQuantity() + parameters.getMyShoppingCartBuildingLease().getQuantity());
                        myShoppingCart.setMyShoppingCartBuildingLease(myShoppingCartBuildingLease);
                    }else if(ProductConstant.PRODUCTTYPE_ENTRANCEGUARDCARD.equalsIgnoreCase(productType)){ // 门禁卡
                        MyShoppingCartEntranceGuardCard myShoppingCartEntranceGuardCard = myShoppingCart.getMyShoppingCartEntranceGuardCard();
                        myShoppingCartEntranceGuardCard.setQuantity(myShoppingCartEntranceGuardCard.getQuantity() + parameters.getMyShoppingCartEntranceGuardCard().getQuantity());
                        myShoppingCart.setMyShoppingCartEntranceGuardCard(myShoppingCartEntranceGuardCard);
                    }else if(ProductConstant.PRODUCTTYPE_FIXEDCARPARK.equalsIgnoreCase(productType)){ // 固定车位
                        MyShoppingCartFixedcarPark myShoppingCartFixedcarPark = myShoppingCart.getMyShoppingCartFixedcarPark();
                        myShoppingCartFixedcarPark.setQuantity(myShoppingCartFixedcarPark.getQuantity() + parameters.getMyShoppingCartFixedcarPark().getQuantity());
                        myShoppingCart.setMyShoppingCartFixedcarPark(myShoppingCartFixedcarPark);
                    }else if(ProductConstant.PRODUCTTYPE_CARPARKSCARD.equalsIgnoreCase(productType)){ // 停车优惠卡
                        MyShoppingCartCarParksCard myShoppingCartCarParksCard = myShoppingCart.getMyShoppingCartCarParksCard();
                        myShoppingCartCarParksCard.setQuantity(myShoppingCartCarParksCard.getQuantity() + parameters.getMyShoppingCartCarParksCard().getQuantity());
                        myShoppingCart.setMyShoppingCartCarParksCard(myShoppingCartCarParksCard);
                    }else if(ProductConstant.PRODUCTTYPE_COMMONSERVICE.equalsIgnoreCase(productType)) { // 普通产品
                        MyShoppingCartCommonService myShoppingCartCommonService = myShoppingCart.getMyShoppingCartCommonService();
                        myShoppingCartCommonService.setQuantity(myShoppingCartCommonService.getQuantity() + parameters.getMyShoppingCartCommonService().getQuantity());
                        myShoppingCart.setMyShoppingCartCommonService(myShoppingCartCommonService);
                    }else{
                        continue;
                    }

                    /**
                     * 计算结束日期
                     */
                    getBuyerEndDate(myShoppingCart);

                    calculatePrice(ctx, myShoppingCart);// 数量增加后重新计费
                    isNewProduct = false;// 非新加入的产品
                    break;
                }

            }
        }



        if(!isNewProduct){   // 购物车已经存在该产品，更新数量
            springRedisTools.deleteByKey(key);
            springRedisTools.addData(key,JSONObject.toJSONString(myShoppingCarts),10, TimeUnit.MINUTES);
        }else{  // 购物车没有存在该产品，构建购物车格式的数据加入购物车
            if(myShoppingCarts == null){
                myShoppingCarts = new ArrayList<>(10);
            }

            TProductDetailSearch tProductDetailSearch = new TProductDetailSearch();
            tProductDetailSearch.setProjectId(projectId);
            tProductDetailSearch.setBatchNo(batchNo);
            tProductDetailSearch.setCode(productCode);
            RemoteModelResult<List<TProductDetailList>> remoteModelResult = tProductApi.findProductDetailByCondition(ctx,tProductDetailSearch);
            if(remoteModelResult.isSuccess()){
                JSONObject productJSONObject = new JSONObject();
                JSONArray subProductJSONArray = new JSONArray();
                List<TProductDetailList> list = remoteModelResult.getModel();
                if(CollectionUtils.isNotEmpty(list)){
                    BigDecimal buildingArea = new BigDecimal(0);
                    BigDecimal usableArea = new BigDecimal(0);
                    for (TProductDetailList tProductDetailList : list) {
                        productJSONObject.put(tProductDetailList.getFieldName(), tProductDetailList.getFieldValue());

                        // 装修服务子产品
                        if(ProductConstant.PRODUCTDETAIL_COLUMN_SUB_PRODUCT.equalsIgnoreCase(tProductDetailList.getFieldName())){
                            subProductJSONArray = JSONArray.parseArray(tProductDetailList.getFieldValue());
                        }

                        if("house_code_new".equalsIgnoreCase(tProductDetailList.getFieldName())){
                            buildingArea = tProductDetailList.getBuildingArea();
                            usableArea = tProductDetailList.getUsableArea();
                        }
                    }

                    if(ProductConstant.PRODUCTTYPE_DECORATIONSERVICE.equalsIgnoreCase(productType)) {    // 装修服务产品，比较特殊
                        if(subProductJSONArray != null && subProductJSONArray.size() > 0){
                            for (int j=0;j<subProductJSONArray.size();j++) {
                                JSONObject subProductJson = (JSONObject)subProductJSONArray.get(j);
                                // 重新绑定子产品的数量信息
                                subProductJson.put("quantity", 1);
                                subProductJSONArray.remove(j);
                                subProductJSONArray.add(j,subProductJson);
                            }


                            /**
                             * 绑定数据到购物车：公共数据
                             */
                            MyShoppingCart myShoppingCart = new MyShoppingCart();
                            BeanUtils.copyProperties(parameters,myShoppingCart);


                            /**
                             * 绑定数据到购物车：装修服务特有数据
                             */
                            MyShoppingCartDecorationService myShoppingCartDecorationService = parameters.getMyShoppingCartDecorationService();
                            BeanUtils.copyProperties(parameters,myShoppingCartDecorationService);
                            myShoppingCartDecorationService.setProductJSONObject(productJSONObject);
                            myShoppingCartDecorationService.setBuildingArea(buildingArea.toString());
                            myShoppingCartDecorationService.setUsableArea(usableArea.toString());
                            myShoppingCartDecorationService.setSubProduct(subProductJSONArray);

                            myShoppingCart.setMyShoppingCartDecorationService(myShoppingCartDecorationService);
                            myShoppingCarts.add(myShoppingCart);


                            calculatePrice(ctx,myShoppingCart);// 计费
                        }
                    }else if(ProductConstant.PRODUCTTYPE_BUILDINGLEASE.equalsIgnoreCase(productType)){ // 建筑租赁
                        /**
                         * 绑定数据到购物车：公共数据
                         */
                        MyShoppingCart myShoppingCart = new MyShoppingCart();
                        BeanUtils.copyProperties(parameters,myShoppingCart);


                        /**
                         * 绑定数据到购物车：建筑租赁特有数据
                         */
                        MyShoppingCartBuildingLease myShoppingCartBuildingLease = parameters.getMyShoppingCartBuildingLease();
                        BeanUtils.copyProperties(parameters,myShoppingCartBuildingLease);
                        myShoppingCartBuildingLease.setProductJSONObject(productJSONObject);
                        myShoppingCartBuildingLease.setBuildingArea(buildingArea.toString());
                        myShoppingCartBuildingLease.setUsableArea(usableArea.toString());

                        myShoppingCart.setMyShoppingCartBuildingLease(myShoppingCartBuildingLease);
                        myShoppingCarts.add(myShoppingCart);

                        calculatePrice(ctx,myShoppingCart);// 计费
                    }else if(ProductConstant.PRODUCTTYPE_ENTRANCEGUARDCARD.equalsIgnoreCase(productType)){ // 门禁卡
                        /**
                         * 绑定数据到购物车：公共数据
                         */
                        MyShoppingCart myShoppingCart = new MyShoppingCart();
                        BeanUtils.copyProperties(parameters,myShoppingCart);



                        /**
                         * 绑定数据到购物车：建筑租赁特有数据
                         */
                        MyShoppingCartEntranceGuardCard myShoppingCartEntranceGuardCard = parameters.getMyShoppingCartEntranceGuardCard();
                        BeanUtils.copyProperties(parameters,myShoppingCartEntranceGuardCard);
                        myShoppingCartEntranceGuardCard.setProductJSONObject(productJSONObject);
                        myShoppingCart.setMyShoppingCartEntranceGuardCard(myShoppingCartEntranceGuardCard);
                        myShoppingCarts.add(myShoppingCart);

                        calculatePrice(ctx,myShoppingCart);// 计费
                    }else if(ProductConstant.PRODUCTTYPE_FIXEDCARPARK.equalsIgnoreCase(productType)){ // 固定车位
                        /**
                         * 绑定数据到购物车：公共数据
                         */
                        MyShoppingCart myShoppingCart = new MyShoppingCart();
                        BeanUtils.copyProperties(parameters,myShoppingCart);


                        /**
                         * 绑定数据到购物车：建筑租赁特有数据
                         */
                        MyShoppingCartFixedcarPark myShoppingCartFixedcarPark = parameters.getMyShoppingCartFixedcarPark();
                        BeanUtils.copyProperties(parameters,myShoppingCartFixedcarPark);
                        myShoppingCartFixedcarPark.setProductJSONObject(productJSONObject);
                        myShoppingCart.setMyShoppingCartFixedcarPark(myShoppingCartFixedcarPark);
                        myShoppingCarts.add(myShoppingCart);

                        calculatePrice(ctx,myShoppingCart);// 计费

                        /**
                         * 计算结束日期
                         */
                        getBuyerEndDate(myShoppingCart);
                    }else if(ProductConstant.PRODUCTTYPE_CARPARKSCARD.equalsIgnoreCase(productType)){ // 停车优惠卡
                        /**
                         * 绑定数据到购物车：公共数据
                         */
                        MyShoppingCart myShoppingCart = new MyShoppingCart();
                        BeanUtils.copyProperties(parameters,myShoppingCart);


                        /**
                         * 绑定数据到购物车：建筑租赁特有数据
                         */
                        MyShoppingCartCarParksCard myShoppingCartCarParksCard = parameters.getMyShoppingCartCarParksCard();
                        BeanUtils.copyProperties(parameters,myShoppingCartCarParksCard);
                        myShoppingCartCarParksCard.setProductJSONObject(productJSONObject);
                        myShoppingCart.setMyShoppingCartCarParksCard(myShoppingCartCarParksCard);
                        myShoppingCarts.add(myShoppingCart);

                        calculatePrice(ctx,myShoppingCart);// 计费
                    }else if(ProductConstant.PRODUCTTYPE_COMMONSERVICE.equalsIgnoreCase(productType)){ // 普通产品
                        /**
                         * 绑定数据到购物车：公共数据
                         */
                        MyShoppingCart myShoppingCart = new MyShoppingCart();
                        BeanUtils.copyProperties(parameters,myShoppingCart);


                        /**
                         * 绑定数据到购物车：建筑租赁特有数据
                         */
                        MyShoppingCartCommonService myShoppingCartCommonService = parameters.getMyShoppingCartCommonService();
                        BeanUtils.copyProperties(parameters,myShoppingCartCommonService);
                        myShoppingCartCommonService.setProductJSONObject(productJSONObject);
                        myShoppingCart.setMyShoppingCartCommonService(myShoppingCartCommonService);
                        myShoppingCarts.add(myShoppingCart);


                        calculatePrice(ctx,myShoppingCart);// 计费
                    }


                    springRedisTools.addData(key,JSONObject.toJSONString(myShoppingCarts),10, TimeUnit.MINUTES);
                }
            }else{
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage(remoteModelResult.getMsg());
            }
        }
        return mm;
    }

    /**
     * 我的购物车
     * @return
     */
    @RequestMapping(value = "/myShoppingCart/{productType}",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto myShoppingCart(@PathVariable String productType) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        String key = String.format(MY_SHOPING_CAR_KEY,productType,ctx.getLoginName());
        String JSONString = springRedisTools.getByKey(key) == null ? null : (String)springRedisTools.getByKey(key);
        List<MyShoppingCart> myShoppingCarts = JSONObject.parseArray(JSONString,MyShoppingCart.class);
        if(CollectionUtils.isNotEmpty(myShoppingCarts)){
            baseDto.setLstDto(myShoppingCarts);
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }








    /**
     * 将产品从购物车移除
     * @param parameters
     * @return
     */
    @RequestMapping(value = "/removeFromShoppingCart",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto removeFromShoppingCart(@RequestBody JSONObject parameters) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        String productType = parameters.getString("productType");


        String key = String.format(MY_SHOPING_CAR_KEY,productType,ctx.getLoginName());
        String JSONString = springRedisTools.getByKey(key) == null ? null : (String)springRedisTools.getByKey(key);
        List<MyShoppingCart> myShoppingCarts = JSONObject.parseArray(JSONString,MyShoppingCart.class);
        if(CollectionUtils.isNotEmpty(myShoppingCarts)){
            String projectId = parameters.getString("projectId");
            String batchNo = parameters.getString("batchNo");
            String productCode = parameters.getString("productCode");
            String[] productCodes = productCode.split(",");
            for (String code : productCodes) {
                for (MyShoppingCart myShoppingCart : myShoppingCarts) {
                    if(myShoppingCart.getProjectId().equals(projectId)
                            && myShoppingCart.getBatchNo().equals(batchNo)
                            && myShoppingCart.getProductCode().equals(code)){
                        myShoppingCarts.remove(myShoppingCart);
                        break;
                    }
                }
            }

            springRedisTools.deleteByKey(key);
            springRedisTools.addData(key,JSONObject.toJSONString(myShoppingCarts),10, TimeUnit.MINUTES);
            baseDto.setLstDto(myShoppingCarts);
        }

        baseDto.setMessageMap(mm);
        return baseDto;
    }



    /**
     * 刷新购物车
     * @param parameters
     * @return BaseDto
     */
    @RequestMapping(value = "/reloadMyShoppingCart",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto reloadMyShoppingCart(@RequestBody MyShoppingCart parameters) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        String productType = parameters.getProductType();
        String projectId = parameters.getProjectId();
        String batchNo = parameters.getBatchNo();
        String productCode = parameters.getProductCode();



        // 购物车缓存数据
        String key = String.format(MY_SHOPING_CAR_KEY,productType,ctx.getLoginName());
        String JSONString = springRedisTools.getByKey(key) == null ? null : (String)springRedisTools.getByKey(key);
        List<MyShoppingCart> myShoppingCarts = JSONObject.parseArray(JSONString,MyShoppingCart.class);
        if(CollectionUtils.isNotEmpty(myShoppingCarts)) {
            if(CollectionUtils.isNotEmpty(myShoppingCarts)){
                for (MyShoppingCart myShoppingCart : myShoppingCarts) {
                    if(myShoppingCart.getProjectId().equals(projectId)
                            && myShoppingCart.getBatchNo().equals(batchNo)
                            && myShoppingCart.getProductCode().equals(productCode)){
                        this.calculatePrice(ctx,parameters);
                        getBuyerEndDate(parameters);
                        myShoppingCarts.remove(myShoppingCart);
                        myShoppingCarts.add(parameters);
                        break;
                    }
                }
            }

            springRedisTools.deleteByKey(key);
            springRedisTools.addData(key,JSONObject.toJSONString(myShoppingCarts),10, TimeUnit.MINUTES);
            baseDto.setLstDto(myShoppingCarts);
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }




    /**
     * 打印订单小票
     * @param orderNo
     * @return BaseDto
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Product,businessName="打印订单小票",operationType= OperationEnum.Search)
    @RequestMapping(value = "/printOrderTicket/{orderNo}",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto printOrderTicket(@PathVariable String orderNo){
        BaseDto baseDto = new BaseDto();

        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        JSONObject result = new JSONObject();
        result.put("companyName",ctx.getCompanyName());

        /**
         * 订单明细
         */
        TProductOrderSearch tProductOrderSearch = new TProductOrderSearch();
        tProductOrderSearch.setBatchNo(orderNo);
        RemoteModelResult<List<TProductOrderList>> remoteModelResult = tProductOrderApi.findByCondition(ctx,tProductOrderSearch);
        if(remoteModelResult.isSuccess()){
            TProductOrderDetailSearch tProductOrderDetailSearch = new TProductOrderDetailSearch();
            tProductOrderDetailSearch.setOrderBatchNo(orderNo);
            RemoteModelResult<List<TProductOrderDetailList>> remoteModelResult1 = tProductOrderApi.findTProductOrderDetailByCondition(ctx,tProductOrderDetailSearch);
            if(remoteModelResult1.isSuccess()){
                List<TProductOrderDetailList> tProductOrderDetailLists = remoteModelResult1.getModel();
                JSONArray jsonArrayProductOrderDetail = new JSONArray(tProductOrderDetailLists.size());
                for (TProductOrderDetailList tProductOrderDetailList : tProductOrderDetailLists) {
                    JSONObject jsonObject = JSONObject.parseObject(tProductOrderDetailList.getProductCommon());
                    JSONObject jsonObjectDetail = new JSONObject();
                    jsonObjectDetail.put("productName",jsonObject.getString("name"));            // 产品名称
                    jsonObjectDetail.put("orderAmount",tProductOrderDetailList.getOrderAmount());     // 产品金额
                    if(StringUtils.isNotBlank(CommonUtils.null2String(jsonObject.getString("buildingFullName")))){
                        jsonObjectDetail.put("buildingFullName",CommonUtils.null2String(jsonObject.getString("buildingFullName")).replaceAll(";","<br/>"));
                    }

                    if(StringUtils.isNotBlank(CommonUtils.null2String(jsonObject.getString("vehicleNo")))){
                        jsonObjectDetail.put("vehicleNo",CommonUtils.null2String(jsonObject.getString("vehicleNo")).replaceAll(";","<br/>"));
                    }

                    jsonObjectDetail.put("description",tProductOrderDetailList.getDescription());

                    jsonArrayProductOrderDetail.add(jsonObjectDetail);
                }
                result.put("productOrderDetailInfo",jsonArrayProductOrderDetail);
            }



            /**
             * 订单头
             */
            JSONObject jsonObjectProductOrder = new JSONObject();
            TProductOrderList tProductOrderList = remoteModelResult.getModel().get(0);
            jsonObjectProductOrder.put("orderNo",tProductOrderList.getBatchNo());                                                      // 交易号
            jsonObjectProductOrder.put("orderDateTime",new DateTime(tProductOrderList.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));                                                      // 交易号
            jsonObjectProductOrder.put("buyer",tProductOrderList.getBuyerName());                                                      // 购买者
            jsonObjectProductOrder.put("payee",tProductOrderList.getCreaterName());                                                    // 收款人
            jsonObjectProductOrder.put("totalPrice",tProductOrderList.getTotalPrice());                                                // 应收总额
            jsonObjectProductOrder.put("discountPrice",tProductOrderList.getDiscountPrice());                                          // 折扣金额
            result.put("productOrderInfo",jsonObjectProductOrder);


            /**
             * 付款明细
             */
            TProductPaymentDetailSearch tProductPaymentDetailSearch = new TProductPaymentDetailSearch();
            tProductPaymentDetailSearch.setOrderBatchNo(orderNo);
            RemoteModelResult<List<TProductPaymentDetailList>> remoteModelResult2 = tProductPaymentApi.findByCondition(ctx,tProductPaymentDetailSearch);
            if(remoteModelResult2.isSuccess()){
                List<TProductPaymentDetailList> tProductPaymentDetailLists = remoteModelResult2.getModel();
                JSONArray jsonArrayTProductPaymentDetail = new JSONArray(tProductPaymentDetailLists.size());
                for (TProductPaymentDetailList tProductPaymentDetailList : tProductPaymentDetailLists) {
                    JSONObject jsonObjectDetail = new JSONObject();
                    String payTypeName = DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),"payType",tProductPaymentDetailList.getPayType());
                    jsonObjectDetail.put("name",payTypeName);    // 支付类型
                    jsonObjectDetail.put("payAmount",tProductPaymentDetailList.getPrice());     // 支付金额
                    jsonArrayTProductPaymentDetail.add(jsonObjectDetail);
                }


                JSONObject discountJSONObject = new JSONObject();
                discountJSONObject.put("name","折扣金额");    // 支付类型
                discountJSONObject.put("payAmount",tProductOrderList.getDiscountPrice());     // 支付金额
                jsonArrayTProductPaymentDetail.add(discountJSONObject);
                result.put("productPaymentDetailInfo",jsonArrayTProductPaymentDetail);
            }


            /**
             * 项目信息
             */
            TSysProjectSearch tSysProjectSearch = new TSysProjectSearch();
            tSysProjectSearch.setProjectId(tProductOrderList.getProjectId());
            RemoteModelResult<BaseDto> remoteModelResult3 = tSysProjectApi.findByCondition(ctx,tSysProjectSearch);
            if(remoteModelResult3.isSuccess()){
                List<TSysProjectList> tSysProjectLists = remoteModelResult3.getModel().getLstDto();
                TSysProjectList tSysProjectList = tSysProjectLists.get(0);
                result.put("projectName",tSysProjectList.getName());
            }
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(remoteModelResult.getMsg());
        }

        baseDto.setT(result);
        baseDto.setMessageMap(mm);
        return baseDto;
    }




    /**
     * 计算价格，委托给策略实现
     * @param ctx
     * @param myShoppingCart
     */
    private void calculatePrice(final WyBusinessContext ctx,final MyShoppingCart myShoppingCart){
        ProductChargingContext productChargingContext = ProductChargingContext.getInstance();
        productChargingContext.setChargingState(ctx,myShoppingCart);
        MyShoppingCart result = productChargingContext.getChargingState().calculatePrice();
        BeanUtils.copyProperties(result,myShoppingCart);
    }



    /**
     * 固定车位专用：根据开始时间和数量计算结束时间
     * @param myShoppingCart
     * @return
     */
    private void getBuyerEndDate(final MyShoppingCart myShoppingCart){
        if(ProductConstant.PRODUCTTYPE_FIXEDCARPARK.equalsIgnoreCase(myShoppingCart.getProductType())) { // 固定车位
            MyShoppingCartFixedcarPark myShoppingCartFixedcarPark = myShoppingCart.getMyShoppingCartFixedcarPark();
            DateTime buyEndDateForDateTime = new DateTime(myShoppingCartFixedcarPark.getStartTime()).plusMonths(myShoppingCartFixedcarPark.getQuantity());
            myShoppingCart.getMyShoppingCartFixedcarPark().setEndTime(buyEndDateForDateTime.toString("yyyy-MM-dd"));
        }
    }
}
