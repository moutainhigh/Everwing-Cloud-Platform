package com.everwing.autotask.core.service;/**
 * Created by wust on 2018/12/18.
 */

import com.everwing.coreservice.common.MessageMap;

/**
 *
 * Function:
 * Reason:
 * Date:2018/12/18
 * @author wusongti@lii.com.cn
 */
public interface ProductService {
    /**
     * 固定车位产品销售状态监控任务
     * @param companyId
     * @return
     */
    MessageMap fixedCarParkSalesStatusScheduleTask(String companyId);
}
