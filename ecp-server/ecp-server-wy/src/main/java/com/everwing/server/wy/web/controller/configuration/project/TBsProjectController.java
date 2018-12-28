package com.everwing.server.wy.web.controller.configuration.project;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.wy.api.configuration.project.TBsProjectApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @TODO 设置-收费计费-项目页面
 * @createTime 2017年7月3日16:06:36
 */
@Controller
@RequestMapping(value="/tBsProject")
public class TBsProjectController {

	/**
	 * TODO :  收费计费-项目页面提供功能:
	 * 			1. 分页显示查询    				ok
	 * 			2. 单击/双击获取项目详情			ok
	 * 			3. 柱状图统计(暂时不做)
	 * 			4. 启用/停用项目数量统计			ok
	 * 			5. 手动计费:
	 * 				5.1  采用循环对每个项目启单独线程计费 , 每个项目的计费线程中, 又启线程分别计算该项目中物业管理费/本体基金/水费/电费的计算,线程计算完毕后,将该项目的该种费用的状态修改为计费完成
	 * 						等待后期
	 * 		    6. 自动任务:
	 * 				//TODO ??
	 * 
	 */
	
	
	@Autowired
	private TBsProjectApi tBsProjectApi;
	
	
	@RequestMapping(value="/listPageBsProject",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPageBsProject(HttpServletRequest req, @RequestBody TBsProject project){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsProjectApi.listPageBsProject(ctx.getCompanyId(),project));
	}
	
	@RequestMapping(value="/countProject",method=RequestMethod.POST)
	public @ResponseBody BaseDto countProject(HttpServletRequest req){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsProjectApi.countProject(ctx.getCompanyId())); 
	}
	
	@RequestMapping(value="/findById/{id}",method=RequestMethod.GET)
	public @ResponseBody BaseDto findById(HttpServletRequest req, @PathVariable String id){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsProjectApi.findById(ctx.getCompanyId(),id));
	}
	
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public @ResponseBody BaseDto update(HttpServletRequest req,  @RequestBody TBsProject project){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsProjectApi.update(ctx.getCompanyId(),project)); 
	}
	
	/**
	 * @TODO 手动计费,暂时无法完成,主体思路见上方
	 * @param req
	 * @param projects
	 * @return
	 */
	@RequestMapping(value="/manaulBilling",method=RequestMethod.POST)
	public @ResponseBody BaseDto manualBilling(HttpServletRequest req,  @RequestBody List<TBsProject> projects){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsProjectApi.manualBilling(ctx.getCompanyId(),projects)); 
	} 
	
	
	@RequestMapping(value="/findCommonStatus/{userId}",method=RequestMethod.GET)
	public @ResponseBody BaseDto findCommonStatus(HttpServletRequest req,  @PathVariable("userId") String userId){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsProjectApi.findCommonStatus(ctx.getCompanyId(),userId)); 
	} 
	
	
	
	
}
