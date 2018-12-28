package com.everwing.server.wy.web.controller.configuration.rebilling;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;
import com.everwing.coreservice.wy.api.configuration.rebilling.TBsRebillingInfoApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/rebilling")
public class TBsRebillingInfoController {

	
	@Autowired
	private TBsRebillingInfoApi tBsRebillingInfoApi;
	
	@RequestMapping(value="/listPageInfos",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPageInfos(HttpServletRequest req,@RequestBody TBsRebillingInfo entity){
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsRebillingInfoApi.listPageInfos(ctx.getCompanyId(),entity));
	}
	
	/**
	 * @TODO  提供接口  单个建筑的重新计费
	 */
	@RequestMapping(value="/rebilling/{type}",method=RequestMethod.POST)
	public @ResponseBody BaseDto rebilling(HttpServletRequest req,@RequestBody TBsRebillingInfo entity,@PathVariable Integer type){
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsRebillingInfoApi.rebilling(ctx.getCompanyId(),entity,type));
	}
	
}
