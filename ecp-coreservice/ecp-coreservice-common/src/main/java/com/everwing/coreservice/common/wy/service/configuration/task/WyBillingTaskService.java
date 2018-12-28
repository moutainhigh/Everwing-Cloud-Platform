package com.everwing.coreservice.common.wy.service.configuration.task;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;
import com.everwing.coreservice.common.wy.entity.configuration.support.BillingSupEntity;

import java.util.List;

/**
 * @TODO 计费服务
 * @author DELL
 *
 */
public interface WyBillingTaskService {
	
	
	/**
	 * 异步发送数据到消息队列
	 * @param companyId
	 * @param entity
	 * @return
	 */
	public BaseDto manualBilling(String companyId , TBsProject entity , Integer type);

	
	public void insert(String companyId, BillingSupEntity se);


	/*
	 * @TODO 自动计费
	 */
	public BaseDto autoBilling(String companyId,Integer type);
	
	
	/**
	 * @TODO 自动计费: 按项目级
	 */
	public BaseDto autoBillingByProject(String companyId,TBsProject entity,Integer type);


	public BaseDto reBillingOpr(String companyId, List<String> ids,String userId,Integer type);


	public void rebilling(String companyId, List<String> ids,String userId,Integer type);


	public void singleRebilling(String companyId, TBsRebillingInfo entity,Integer type);
}
