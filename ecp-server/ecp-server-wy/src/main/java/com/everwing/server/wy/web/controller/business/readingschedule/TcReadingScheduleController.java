package com.everwing.server.wy.web.controller.business.readingschedule;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.business.readingschedule.TcReadingSchedule;
import com.everwing.coreservice.wy.api.business.readingschedule.TcReadingScheduleApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @TODO 抄表计划用controller
 *
 */
@Controller
@RequestMapping("/readingSchedule")
public class TcReadingScheduleController {

	
	@Autowired
	private TcReadingScheduleApi tcReadingScheduleApi;
	
	@RequestMapping(value="/listPage",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPage(HttpServletRequest req,@RequestBody TcReadingSchedule tcReadingSchedule){
		return BaseDtoUtils.getDto(this.tcReadingScheduleApi.listPage(CommonUtils.getCompanyId(req),tcReadingSchedule));
	}
	
	/**
	 * @TODO 新增计划以及任务,若flag为2 则为重抄用的任务,需要将之前的数据置为无效
	 * @param req
	 * @param tcReadingSchedule
	 * @param flag
	 * @return
	 */
	@RequestMapping(value="/insert/{flag}",method=RequestMethod.POST)
	public @ResponseBody BaseDto insert(HttpServletRequest req,@RequestBody TcReadingSchedule tcReadingSchedule,@PathVariable Integer flag){
		return BaseDtoUtils.getDto(this.tcReadingScheduleApi.insert(CommonUtils.getCompanyId(req),tcReadingSchedule,flag));
	}
	
	@RequestMapping(value="/modify",method=RequestMethod.POST)
	public @ResponseBody BaseDto modify(HttpServletRequest req,@RequestBody TcReadingSchedule tcReadingSchedule){
		return BaseDtoUtils.getDto(this.tcReadingScheduleApi.modify(CommonUtils.getCompanyId(req),tcReadingSchedule));
	}
	
	@RequestMapping(value="/del/{ids}",method=RequestMethod.GET)
	public @ResponseBody BaseDto del(HttpServletRequest req,@PathVariable String ids){
		return BaseDtoUtils.getDto(this.tcReadingScheduleApi.del(CommonUtils.getCompanyId(req),ids));
	}
	
	@RequestMapping(value="/getInfoById/{id}",method=RequestMethod.GET)
	public @ResponseBody BaseDto getInfoById(HttpServletRequest req, @PathVariable String id){
		return BaseDtoUtils.getDto(this.tcReadingScheduleApi.getInfoById(CommonUtils.getCompanyId(req),id));
	}
	
	@RequestMapping(value="/batchModify",method=RequestMethod.POST)
	public @ResponseBody BaseDto batchModify(HttpServletRequest req,@RequestBody TcReadingSchedule tcReadingSchedule){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcReadingScheduleApi.batchModify(ctx.getCompanyId(),tcReadingSchedule));
	}
	
	@RequestMapping(value="/findUsingSchedule",method=RequestMethod.POST)
	public @ResponseBody BaseDto findUsingSchedule(HttpServletRequest req,@RequestBody TcReadingSchedule entity){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcReadingScheduleApi.findUsingSchedule(ctx.getCompanyId(),entity));
	}
	
}
