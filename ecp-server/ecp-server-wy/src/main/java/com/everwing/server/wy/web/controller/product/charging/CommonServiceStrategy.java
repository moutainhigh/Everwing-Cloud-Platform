package com.everwing.server.wy.web.controller.product.charging;/**
 * Created by wust on 2018/10/11.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.wy.entity.product.MyShoppingCart;
import com.everwing.coreservice.common.wy.entity.product.MyShoppingCartCommonService;
import com.everwing.coreservice.common.wy.entity.product.ProductConstant;

import java.math.BigDecimal;

/**
 *
 * Function:普通服务
 * Reason:
 * Date:2018/10/11
 * @author wusongti@lii.com.cn
 */
public class CommonServiceStrategy implements ProductChargingStrategy {
    private WyBusinessContext ctx;
    private MyShoppingCart myShoppingCart;
    public CommonServiceStrategy(WyBusinessContext ctx, final MyShoppingCart myShoppingCart) {
        this.ctx = ctx;
        this.myShoppingCart = myShoppingCart;
    }

    @Override
    public MyShoppingCart calculatePrice() {
        MyShoppingCartCommonService myShoppingCartCommonService = this.myShoppingCart.getMyShoppingCartCommonService();
        JSONObject productJSONObject = myShoppingCartCommonService.getProductJSONObject();
        Integer quantity = myShoppingCartCommonService.getQuantity();
        String depositDouble = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT);
        String averagePriceDouble = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE);
        String productUnit = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_PRODUCT_UNIT);
        String totalPriceStr = new BigDecimal(depositDouble).add(new BigDecimal(averagePriceDouble)).multiply(new BigDecimal(quantity)).toString();
        String productUnitName = DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(this.ctx.getCompanyId(),"productUnit",CommonUtils.null2String(productUnit));

        myShoppingCartCommonService.setUnit("元/" + productUnitName);
        myShoppingCartCommonService.setTotalPrice(totalPriceStr);
        myShoppingCart.setMyShoppingCartCommonService(myShoppingCartCommonService);
        return myShoppingCart;
    }
}
