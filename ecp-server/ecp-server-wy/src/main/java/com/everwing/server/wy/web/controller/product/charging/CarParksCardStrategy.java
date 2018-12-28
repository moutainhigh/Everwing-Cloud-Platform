package com.everwing.server.wy.web.controller.product.charging;/**
 * Created by wust on 2018/10/11.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.wy.entity.product.MyShoppingCart;
import com.everwing.coreservice.common.wy.entity.product.MyShoppingCartCarParksCard;
import com.everwing.coreservice.common.wy.entity.product.ProductConstant;

import java.math.BigDecimal;

/**
 *
 * Function:停车优惠卡
 * Reason:
 * Date:2018/10/11
 * @author wusongti@lii.com.cn
 */
public class CarParksCardStrategy implements ProductChargingStrategy {
    private MyShoppingCart myShoppingCart;

    public CarParksCardStrategy(final MyShoppingCart myShoppingCart) {
        this.myShoppingCart = myShoppingCart;
    }

    /**
     * 总价 =（工本费 + 押金 + 单价） * 数量
     * @return
     */
    @Override
    public MyShoppingCart calculatePrice() {
        MyShoppingCartCarParksCard myShoppingCartCarParksCard = this.myShoppingCart.getMyShoppingCartCarParksCard();
        JSONObject productJSONObject = myShoppingCartCarParksCard.getProductJSONObject();
        Integer quantity = myShoppingCartCarParksCard.getQuantity();
        String cardFeeDouble = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_CARD_FEE);
        String depositDouble = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT);
        String averagePriceDouble = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE);
        String totalPriceStr = new BigDecimal(depositDouble).add(new BigDecimal(averagePriceDouble)).add(new BigDecimal(cardFeeDouble)).multiply(new BigDecimal(quantity)).toString();
        myShoppingCartCarParksCard.setTotalPrice(totalPriceStr);
        myShoppingCart.setMyShoppingCartCarParksCard(myShoppingCartCarParksCard);
        return myShoppingCart;
    }
}
