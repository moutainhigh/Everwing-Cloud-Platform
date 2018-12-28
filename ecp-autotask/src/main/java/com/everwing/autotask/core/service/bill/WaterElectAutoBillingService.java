package com.everwing.autotask.core.service.bill;

/**
 * 水电自动计费逻辑
 *
 * @author DELL shiny
 * @create 2018/6/5
 */
public interface WaterElectAutoBillingService {

    void autoBilling(String companyId,Integer type);
}
