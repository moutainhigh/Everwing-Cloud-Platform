package com.everwing.server.wy.web.controller.product.charging;/**
 * Created by wust on 2018/10/11.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.entity.product.MyShoppingCart;
import com.everwing.coreservice.common.wy.entity.product.MyShoppingCartDecorationService;

import java.math.BigDecimal;

/**
 *
 * Function:装修服务
 * Reason:
 * Date:2018/10/11
 * @author wusongti@lii.com.cn
 */
public class DecorationServiceStrategy implements ProductChargingStrategy {
    private MyShoppingCart myShoppingCart;


    public DecorationServiceStrategy(final MyShoppingCart myShoppingCart) {
        this.myShoppingCart = myShoppingCart;
    }

    @Override
    public MyShoppingCart calculatePrice() {
        MyShoppingCartDecorationService myShoppingCartDecorationService = this.myShoppingCart.getMyShoppingCartDecorationService();
        String buildingAreaDouble = myShoppingCartDecorationService.getBuildingArea();
        JSONArray subProductJSONArray = myShoppingCartDecorationService.getSubProduct();
        BigDecimal productTotalPrice = new BigDecimal(0);
        for (int i = 0;i<subProductJSONArray.size();i++) {
            JSONObject jsonObject = subProductJSONArray.getJSONObject(i);
            int quantity = jsonObject.getIntValue("quantity");
            BigDecimal subProductTotalPrice = new BigDecimal(0);
            BigDecimal priceDouble = jsonObject.getBigDecimal("price");
            String valuationTypeStr = jsonObject.getString("valuationType");
            if(LookupItemEnum.valuationType_unitPrice.getStringValue().equalsIgnoreCase(valuationTypeStr)){
                subProductTotalPrice = priceDouble.multiply(new BigDecimal(buildingAreaDouble)).multiply(new BigDecimal(quantity));
                jsonObject.put("unit","元/平米");
                jsonObject.put("totalPrice",subProductTotalPrice);
            }else if(LookupItemEnum.valuationType_totalPrice.getStringValue().equalsIgnoreCase(valuationTypeStr)){
                subProductTotalPrice = priceDouble.multiply(new BigDecimal(quantity));
                jsonObject.put("unit","元");
                jsonObject.put("totalPrice",subProductTotalPrice);
            }

            subProductJSONArray.remove(i);
            subProductJSONArray.add(i,jsonObject);

            productTotalPrice.add(subProductTotalPrice);
        }
        myShoppingCartDecorationService.setTotalPrice(productTotalPrice.toString());
        myShoppingCartDecorationService.setSubProduct(subProductJSONArray);
        return myShoppingCart;
    }
}
