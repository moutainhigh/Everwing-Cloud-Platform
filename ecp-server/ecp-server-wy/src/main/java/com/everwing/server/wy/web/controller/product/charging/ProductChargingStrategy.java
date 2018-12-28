package com.everwing.server.wy.web.controller.product.charging;/**
 * Created by wust on 2018/10/11.
 */

import com.everwing.coreservice.common.wy.entity.product.MyShoppingCart;

/**
 *
 * Function:策略模式
 * Reason:
 * Date:2018/10/11
 * @author wusongti@lii.com.cn
 */
public interface ProductChargingStrategy {
    MyShoppingCart calculatePrice();
}
