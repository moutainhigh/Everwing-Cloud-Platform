package com.everwing.coreservice.wy.api.configuration.bsconstant;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.bsconstant.TBsConstant;
import com.everwing.coreservice.common.wy.service.configuration.bsconstant.TBsConstantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tBsConstantApi")
public class TBsConstantApi {

	@Autowired
	private TBsConstantService tBsConstantService;
	
	public RemoteModelResult<BaseDto> singleAdd(WyBusinessContext ctx,TBsConstant tBsConstant){
		
		return new RemoteModelResult<BaseDto>(tBsConstantService.singleAdd(ctx, tBsConstant));
	}
	
	public RemoteModelResult<BaseDto> listPageConstants(WyBusinessContext ctx, TBsConstant tBsConstant){
		return new RemoteModelResult<BaseDto>(tBsConstantService.listPageConstants(ctx, tBsConstant));
	}
	
   public RemoteModelResult<BaseDto> singleDel(WyBusinessContext ctx,String id){
		return new RemoteModelResult<BaseDto>(tBsConstantService.singleDel(ctx, id));
	}
   
   public RemoteModelResult<BaseDto> getConstantsByProIdAndType(WyBusinessContext ctx,TBsConstant tBsConstant){
	   return new RemoteModelResult<BaseDto>(tBsConstantService.getConstantsByProIdAndType(ctx, tBsConstant));
   }
}
