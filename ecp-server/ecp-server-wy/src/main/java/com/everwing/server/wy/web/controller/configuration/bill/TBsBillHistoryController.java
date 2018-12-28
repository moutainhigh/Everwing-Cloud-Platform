package com.everwing.server.wy.web.controller.configuration.bill;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.wy.api.configuration.bill.TBsBillHistoryApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/billHistory")
public class TBsBillHistoryController {

	/**
	 *  1. 根据传入的billTotalId / 房屋名  分页查询元数据
	 * 
	 * 
	 */
	
	@Autowired
	private TBsBillHistoryApi tBsBillHistoryApi;

	@RequestMapping(value="/listPageData",method=RequestMethod.POST)
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Billing,businessName="账单服务 : 账单查询",operationType= OperationEnum.Search)
	public @ResponseBody BaseDto listPageData(HttpServletRequest req, @RequestBody TBsChargeBillHistory entity){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsBillHistoryApi.listPageData(ctx.getCompanyId(),entity));
	}
	
	@RequestMapping(value="/listPageInCustomerService",method=RequestMethod.POST)
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Billing,businessName="账单服务 : 账单查询",operationType= OperationEnum.Search)
	public @ResponseBody BaseDto listPageInCustomerService(HttpServletRequest req, @RequestBody TBsChargeBillHistory entity){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsBillHistoryApi.listPageInCustomerService(ctx.getCompanyId(),entity));
	}

}
