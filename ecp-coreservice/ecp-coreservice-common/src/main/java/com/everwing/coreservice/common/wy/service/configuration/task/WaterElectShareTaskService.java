package com.everwing.coreservice.common.wy.service.configuration.task;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.configuration.support.BillingSupEntity;

/**
 * @TODO 物业管理费计费服务
 * @author DELL
 *
 */
public interface WaterElectShareTaskService {
	
	
	/**
	 * 异步发送数据到消息队列
	 * @param companyId
	 * @param entity
	 * @return
	 */
	BaseDto shareBilling(String companyId , TBsProject entity , int meterType);

	
	void update(String companyId, BillingSupEntity se);
	
	BaseDto doWaterElectShareBillingByCompnay(String companyId,int meterType);
	

}
