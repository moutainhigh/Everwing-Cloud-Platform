package com.everwing.server.wy.web.controller.business.readingtask;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.wy.api.business.readingtask.TcReadingTaskApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/readingTask")
public class TcReadingTaskController {

	@Autowired
	private TcReadingTaskApi tcReadingTaskApi;
	
	/**
	 * @TODO 页面点击抄水表任务抄表,加载出该任务下的所有表数据
	 * @param req
	 * @return
	 *//*
	@RequestMapping(value="/getWaterTaskDetailById/{id}",method=RequestMethod.GET)
	public @ResponseBody BaseDto getWaterTaskDetailById(HttpServletRequest req , @PathVariable String id){
		return BaseDtoUtils.getDto(this.tcReadingTaskApi.getWaterTaskDetailById(CommonUtils.getCompanyId(req),id));
	}*/
	
	
	@RequestMapping(value="/listPageTasks",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPageTasks(HttpServletRequest req , @RequestBody TcReadingTask entity){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcReadingTaskApi.listPage(ctx.getCompanyId(),entity));
	}
	
	@RequestMapping(value="/listPageTasksToAudit",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPageTasksToAudit(HttpServletRequest req, @RequestBody TcReadingTask task){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcReadingTaskApi.listPageTasksToAudit(ctx.getCompanyId(),task));
	}
	
	@RequestMapping(value="/batchAudit/{auditStatus}",method=RequestMethod.POST)
	public @ResponseBody BaseDto batchAudit(HttpServletRequest req , @RequestBody List<TcReadingTask> tasks,@PathVariable Integer auditStatus){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcReadingTaskApi.batchAudit(ctx.getCompanyId(),tasks,auditStatus));
	}
	
	@RequestMapping(value="/batchCompelete",method=RequestMethod.POST)
	public @ResponseBody BaseDto batchCompelete(HttpServletRequest req , @RequestBody List<TcReadingTask> tasks){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcReadingTaskApi.batchCompelete(ctx.getCompanyId(),tasks));
	}
	
	@RequestMapping(value="/findByScheduleId/{scheduleId}",method=RequestMethod.GET)
	public @ResponseBody BaseDto findByScheduleId(HttpServletRequest req,@PathVariable String scheduleId){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcReadingTaskApi.findByScheduleId(ctx.getCompanyId(),scheduleId));
		
	}
	
	
}
