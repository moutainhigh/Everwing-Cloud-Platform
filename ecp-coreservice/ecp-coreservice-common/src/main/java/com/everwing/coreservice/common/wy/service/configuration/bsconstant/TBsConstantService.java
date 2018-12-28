package com.everwing.coreservice.common.wy.service.configuration.bsconstant;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.configuration.bsconstant.TBsConstant;

public interface TBsConstantService {

	/**
	 * 新增
	 * @param ctx
	 * @param tBsConstant
	 * @return
	 */
	public BaseDto singleAdd(WyBusinessContext ctx,TBsConstant tBsConstant);
	
	/**
	 * 查询常量
	 * @param ctx
	 * @param tBsConstant
	 * @return
	 */
	public BaseDto listPageConstants(WyBusinessContext ctx,TBsConstant tBsConstant);
	
	/**
	 * 单个删除
	 * @param req
	 * @param id
	 * @return
	 */
	BaseDto singleDel(WyBusinessContext ctx, String id);
	
    BaseDto getConstantsByProIdAndType(WyBusinessContext ctx,TBsConstant tBsConstant);
}
