package com.everwing.coreservice.wy.api.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargeType;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsChargingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tBsChargingTypeApi")
public class TBsChargingTypeApi {

	@Autowired
	private TBsChargingTypeService tBsChargingTypeService;
	
	/**
	 * 分页查询
	 * @param ctx
	 * @param tBsChargeType
	 * @return
	 */
	public RemoteModelResult<BaseDto> listPageChargingType(WyBusinessContext ctx,TBsChargeType tBsChargeType){
		return new RemoteModelResult<BaseDto>(this.tBsChargingTypeService.listPageChargingType(ctx, tBsChargeType));
	}
	
	/**
	 * 新增
	 * @param ctx
	 * @param tBsChargeType
	 * @return
	 */
	public RemoteModelResult<BaseDto> addChargingType(WyBusinessContext ctx, TBsChargeType tBsChargeType){
		return new RemoteModelResult<BaseDto>(this.tBsChargingTypeService.addChargingType(ctx, tBsChargeType));
	}
	
	public RemoteModelResult<BaseDto> batchDel(WyBusinessContext ctx,TBsChargeType tBsChargeType){
		return new RemoteModelResult<BaseDto>(this.tBsChargingTypeService.batchDel(ctx, tBsChargeType));
	}
	
	/**
	 * 公式试运算
	 * @param ctx
	 * @param formulaValue
	 * @param billingFeeItemTestValue
	 * @return
	 * @throws ECPBusinessException
	 */
	public RemoteModelResult<BaseDto> testOpeation(WyBusinessContext ctx,String formulaValue,String billingFeeItemTestValue) throws ECPBusinessException{
		return new RemoteModelResult<BaseDto>(this.tBsChargingTypeService.testOpeation(ctx, formulaValue, billingFeeItemTestValue));
	}
	
	
}
