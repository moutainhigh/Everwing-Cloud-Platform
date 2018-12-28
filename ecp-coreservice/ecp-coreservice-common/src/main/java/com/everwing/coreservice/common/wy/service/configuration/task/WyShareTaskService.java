package com.everwing.coreservice.common.wy.service.configuration.task;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.configuration.support.BillingSupEntity;

/**
 * @TODO 物业管理费分摊计费
 *
 */
public interface WyShareTaskService {
	
	
	/**
	 * 异步发送数据到消息队列
	 * @param companyId
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BaseDto manualBilling(String companyId , TBsProject entity);

	public void update(String companyId, BillingSupEntity se);

	BaseDto doWyShareByCompnay(String companyId);
}
