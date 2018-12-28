
package com.everwing.server.wy.web.controller.configuration.task;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.wy.api.configuration.task.WyBillingTaskApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/wyBillingTask")
public class WyBillingTaskController {

	
	@Autowired
	private WyBillingTaskApi wyBillingTaskApi;
	
	@RequestMapping(value="/manaulBilling/{type}",method=RequestMethod.POST)
	public @ResponseBody BaseDto manualBilling(HttpServletRequest req, @RequestBody TBsProject entity, @PathVariable Integer type){
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.wyBillingTaskApi.manaulBilling(ctx.getCompanyId(),entity,type));
	}
	
	@RequestMapping(value="/autoBilling/{type}",method=RequestMethod.POST)
	public @ResponseBody BaseDto autoBilling(HttpServletRequest req,@PathVariable Integer type){
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.wyBillingTaskApi.autoBilling(ctx.getCompanyId(),type));
	}
	
	@RequestMapping(value="/reBilling/{userId}/{type}",method=RequestMethod.POST)
	public @ResponseBody BaseDto reBillingOpr(HttpServletRequest req,@RequestBody List<String> ids,@PathVariable String userId,@PathVariable Integer type){
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.wyBillingTaskApi.reBillingOpr(ctx.getCompanyId(),ids,userId,type));
	}
	
	
}
