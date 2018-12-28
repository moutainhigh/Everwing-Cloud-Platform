package com.everwing.server.wy.web.controller.product.charging;/**
 * Created by wust on 2018/10/11.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.entity.product.MyShoppingCart;
import com.everwing.coreservice.common.wy.entity.product.MyShoppingCartBuildingLease;
import com.everwing.coreservice.common.wy.entity.product.ProductConstant;

import java.math.BigDecimal;

/**
 *
 * Function:建筑租赁
 * Reason:
 * Date:2018/10/11
 * @author wusongti@lii.com.cn
 */
public class BuildingLeaseStrategy implements ProductChargingStrategy {

    private MyShoppingCart myShoppingCart;

    public BuildingLeaseStrategy(final MyShoppingCart myShoppingCart) {
        this.myShoppingCart = myShoppingCart;
    }

    /**
     * 一、单位
     * 1.计价方式是单价
     *  1)计价单位是天，则最终单位是“元/平米/天”
     *  2)计价单位是月，则最终单位是“元/平米/月”
     * 2.计价方式是总价
     *  1)计价单位是天，则最终单位是“元/天”
     *  2)计价单位是月，则最终单位是“元/月”
     *
     *  二、公式
     * 1.计价方式是总价
     *  总价 = (工本费 + 押金 + 平均价格 ) * 数量
     * 2.计价方式是单价
     *  总价 = (工本费 + 押金 + 平均价格 * 面积) * 数量
     */
    @Override
    public MyShoppingCart calculatePrice() {
        MyShoppingCartBuildingLease myShoppingCartBuildingLease = this.myShoppingCart.getMyShoppingCartBuildingLease();
        JSONObject productJSONObject = myShoppingCartBuildingLease.getProductJSONObject();
        Integer quantity = myShoppingCartBuildingLease.getQuantity();
        String depositDouble = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT);
        String averagePriceDouble = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE);
        String buildingAreaDouble = myShoppingCartBuildingLease.getBuildingArea();
        String valuationTypeString = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_VALUATION_TYPE);
        String valuationUnitString = productJSONObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_VALUATION_UNIT);

        if(LookupItemEnum.valuationType_unitPrice.getStringValue().equalsIgnoreCase(valuationTypeString)){
            if(LookupItemEnum.timeUnit_days.getStringValue().equalsIgnoreCase(valuationUnitString)){
                myShoppingCartBuildingLease.setUnit("元/平米/天");
            }else if(LookupItemEnum.timeUnit_months.getStringValue().equalsIgnoreCase(valuationUnitString)){
                myShoppingCartBuildingLease.setUnit("元/平米/月");
            }
            String totalPriceStr = new BigDecimal(depositDouble).add(new BigDecimal(averagePriceDouble)).multiply(new BigDecimal(buildingAreaDouble)).multiply(new BigDecimal(quantity)).toString();
            myShoppingCartBuildingLease.setTotalPrice(totalPriceStr);
        }else if(LookupItemEnum.valuationType_totalPrice.getStringValue().equalsIgnoreCase(valuationTypeString)){
            if(LookupItemEnum.timeUnit_days.getStringValue().equalsIgnoreCase(valuationUnitString)){
                myShoppingCartBuildingLease.setUnit("元/天");
            }else if(LookupItemEnum.timeUnit_months.getStringValue().equalsIgnoreCase(valuationUnitString)){
                myShoppingCartBuildingLease.setUnit("元/月");
            }
            String totalPriceStr = new BigDecimal(depositDouble).add(new BigDecimal(averagePriceDouble)).multiply(new BigDecimal(quantity)).toString();
            myShoppingCartBuildingLease.setTotalPrice(totalPriceStr);
        }
        return myShoppingCart;
    }
}
