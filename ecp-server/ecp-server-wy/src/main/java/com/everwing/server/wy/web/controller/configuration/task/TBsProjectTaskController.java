package com.everwing.server.wy.web.controller.configuration.task;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.wy.api.configuration.task.TBsProjectTaskApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/tBsProjectTask")
public class TBsProjectTaskController {

	
	@Autowired
	private TBsProjectTaskApi tBsProjectTaskApi;
	
	
	/**
	 * @TODO 用来手动测试计费项目的自动生成
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/testGenNextProject",method=RequestMethod.POST)
	public @ResponseBody BaseDto testGenNextProject(HttpServletRequest req){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsProjectTaskApi.testGenNextProject(ctx.getCompanyId()));
		
	}
	
	/**
	 * @TODO 用来手动测试通用账户扣取
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/testCmacBilling",method=RequestMethod.POST)
	public @ResponseBody BaseDto testCmacBilling(HttpServletRequest req){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsProjectTaskApi.testCmacBilling(ctx.getCompanyId()));
		
	}
	
	/**
	 * @TODO 用来手动测试通用账户扣取
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/testAutoFlushProject",method=RequestMethod.POST)
	public @ResponseBody BaseDto testAutoFlushProject(HttpServletRequest req){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsProjectTaskApi.testAutoFlushProject(ctx.getCompanyId()));
		
	}
	
	
	
}
