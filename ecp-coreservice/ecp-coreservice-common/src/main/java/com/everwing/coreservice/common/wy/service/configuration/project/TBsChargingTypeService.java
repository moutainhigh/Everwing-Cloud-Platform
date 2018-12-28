package com.everwing.coreservice.common.wy.service.configuration.project;

import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargeType;

public interface TBsChargingTypeService {

	/**
	 * 分页查询
	 * @param ctx
	 * @param tBsChargeType
	 * @return
	 */
	BaseDto  listPageChargingType(WyBusinessContext ctx,TBsChargeType tBsChargeType);
	
	/**
	 * 新增
	 * @param ctx
	 * @param tBsChargeType
	 * @return
	 */
	BaseDto addChargingType(WyBusinessContext ctx, TBsChargeType tBsChargeType);
	
	/**
	 * 删除
	 */
	BaseDto batchDel(WyBusinessContext ctx,TBsChargeType tBsChargeType);
	
	/**
	 * 公式试运算
	 * @param ctx
	 * @param formulaValue
	 * @param billingFeeItemTestValue
	 * @return
	 * @throws ECPBusinessException 
	 */
	BaseDto testOpeation(WyBusinessContext ctx,String formulaValue,String billingFeeItemTestValue) throws ECPBusinessException;
	
	
}
