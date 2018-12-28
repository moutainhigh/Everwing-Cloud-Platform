package com.everwing.server.wy.web.controller.product.charging;/**
 * Created by wust on 2018/10/11.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.wy.entity.product.MyShoppingCart;
import com.everwing.coreservice.common.wy.entity.product.ProductConstant;

/**
 *
 * Function:根据不同产品类型选择对应的计费算法
 * Reason:
 * Date:2018/10/11
 * @author wusongti@lii.com.cn
 */
public class ProductChargingContext {
    private ProductChargingContext(){}

    private static final ProductChargingContext instance = new ProductChargingContext();

    public static ProductChargingContext getInstance(){
        return instance;
    }

    private ProductChargingStrategy chargingState;

    public ProductChargingStrategy getChargingState() {
        return chargingState;
    }

    public void setChargingState(final WyBusinessContext ctx, final MyShoppingCart myShoppingCart) {
        switch (myShoppingCart.getProductType()){
            case ProductConstant.PRODUCTTYPE_BUILDINGLEASE:
                this.chargingState = new BuildingLeaseStrategy(myShoppingCart);
                break;
            case ProductConstant.PRODUCTTYPE_CARPARKSCARD:
                this.chargingState = new CarParksCardStrategy(myShoppingCart);
                break;
            case ProductConstant.PRODUCTTYPE_FIXEDCARPARK:
                this.chargingState = new FixedCarParkStrategy(myShoppingCart);
                break;
            case ProductConstant.PRODUCTTYPE_ENTRANCEGUARDCARD:
                this.chargingState = new EntranceGuardCardStrategy(myShoppingCart);
                break;
            case ProductConstant.PRODUCTTYPE_DECORATIONSERVICE:
                this.chargingState = new DecorationServiceStrategy(myShoppingCart);
                break;
            case ProductConstant.PRODUCTTYPE_COMMONSERVICE:
                this.chargingState = new CommonServiceStrategy(ctx,myShoppingCart);
                break;
            default:
                throw new ECPBusinessException("无效的产品类型："+myShoppingCart.getProductType());
        }
    }
}
