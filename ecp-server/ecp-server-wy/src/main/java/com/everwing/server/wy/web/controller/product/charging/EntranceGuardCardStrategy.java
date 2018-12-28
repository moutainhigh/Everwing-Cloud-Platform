package com.everwing.server.wy.web.controller.product.charging;/**
 * Created by wust on 2018/10/11.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.wy.entity.product.MyShoppingCart;
import com.everwing.coreservice.common.wy.entity.product.MyShoppingCartEntranceGuardCard;
import com.everwing.coreservice.common.wy.entity.product.ProductConstant;

import java.math.BigDecimal;

/**
 *
 * Function:门禁卡
 * Reason:
 * Date:2018/10/11
 * @author wusongti@lii.com.cn
 */
public class EntranceGuardCardStrategy implements ProductChargingStrategy {
    private MyShoppingCart myShoppingCart;


    public EntranceGuardCardStrategy(final MyShoppingCart myShoppingCart) {
        this.myShoppingCart = myShoppingCart;
    }

    /**
     * 总价 =（工本费 + 押金 + 单价） * 数量
     * @return
     */
    @Override
    public MyShoppingCart calculatePrice() {
        MyShoppingCartEntranceGuardCard myShoppingCartEntranceGuardCard = myShoppingCart.getMyShoppingCartEntranceGuardCard();

        JSONObject productJSONObject = myShoppingCartEntranceGuardCard.getProductJSONObject();
        Integer quantity = myShoppingCartEntranceGuardCard.getQuantity();

        String cardFeeDouble = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_CARD_FEE);
        String depositDouble = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT);
        String averagePriceDouble = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE);

        String totalPriceStr = new BigDecimal(cardFeeDouble).add(new BigDecimal(depositDouble)).add(new BigDecimal(averagePriceDouble)).multiply(new BigDecimal(quantity)).toString();
        myShoppingCartEntranceGuardCard.setTotalPrice(totalPriceStr);
        myShoppingCart.setMyShoppingCartEntranceGuardCard(myShoppingCartEntranceGuardCard);
        return myShoppingCart;
    }
}
