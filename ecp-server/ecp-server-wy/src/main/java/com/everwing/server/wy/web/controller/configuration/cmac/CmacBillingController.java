package com.everwing.server.wy.web.controller.configuration.cmac;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.wy.api.configuration.cmac.CmacBillingApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/cmacBilling")
public class CmacBillingController {

	
	@Autowired
	private CmacBillingApi cmacBillingApi;
	
	@RequestMapping(value="/testDk",method=RequestMethod.POST)
	public @ResponseBody BaseDto testDk(HttpServletRequest req,@RequestBody TBsProject project){
		
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.cmacBillingApi.testDk(ctx.getCompanyId(),project));
	}
	
	@RequestMapping(value="/testLateFee",method=RequestMethod.POST)
	public @ResponseBody BaseDto testLateFee(HttpServletRequest req,@RequestBody TBsProject project){
		
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.cmacBillingApi.testLateFee(ctx.getCompanyId(),project));
	}
	
}
