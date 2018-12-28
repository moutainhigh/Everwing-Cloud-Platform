package com.everwing.server.wy.web.controller.product.charging;/**
 * Created by wust on 2018/10/11.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.wy.entity.product.MyShoppingCart;
import com.everwing.coreservice.common.wy.entity.product.MyShoppingCartFixedcarPark;
import com.everwing.coreservice.common.wy.entity.product.ProductConstant;

import java.math.BigDecimal;

/**
 *
 * Function:固定车位普通计费
 * Reason:
 * Date:2018/10/11
 * @author wusongti@lii.com.cn
 */
public class FixedCarParkStrategy implements ProductChargingStrategy {
    private MyShoppingCart myShoppingCart;

    public FixedCarParkStrategy(final MyShoppingCart myShoppingCart) {
        this.myShoppingCart = myShoppingCart;
    }

    /**
     * 总价 =（工本费 + 押金 + 单价） * 数量
     * @return
     */
    @Override
    public MyShoppingCart calculatePrice() {
        MyShoppingCartFixedcarPark myShoppingCartFixedcarPark =this.myShoppingCart.getMyShoppingCartFixedcarPark();
        JSONObject productJSONObject = myShoppingCartFixedcarPark.getProductJSONObject();
        Integer quantity = myShoppingCartFixedcarPark.getQuantity();
        String cardFeeDouble = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_CARD_FEE);
        String depositDouble = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT);
        String averagePriceDouble = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE);
        String totalPriceStr = "";

        if(this.myShoppingCart.isRenewalTerm()){
            totalPriceStr = new BigDecimal(averagePriceDouble).multiply(new BigDecimal(quantity)).toString();
        }else{
            totalPriceStr = new BigDecimal(cardFeeDouble).add(new BigDecimal(depositDouble)).add(new BigDecimal(averagePriceDouble)).multiply(new BigDecimal(quantity)).toString();
        }

        myShoppingCartFixedcarPark.setTotalPrice(totalPriceStr);
        myShoppingCartFixedcarPark.setUnit("元/月");
        myShoppingCart.setMyShoppingCartFixedcarPark(myShoppingCartFixedcarPark);
        return myShoppingCart;
    }
}
