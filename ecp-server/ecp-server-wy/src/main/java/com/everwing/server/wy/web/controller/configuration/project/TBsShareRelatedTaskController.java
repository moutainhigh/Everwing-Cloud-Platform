package com.everwing.server.wy.web.controller.configuration.project;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareRelatedTask;
import com.everwing.coreservice.wy.api.configuration.project.TBsShareRelatedTaskApi;

/**
 *@describe 分摊基础数据相关控制
 */
@Controller
@RequestMapping(value="/shareRelatedTask")
public class TBsShareRelatedTaskController {

	@Autowired
	private TBsShareRelatedTaskApi tBsShareRelatedTaskApi;
	
	private static Logger logger=Logger.getLogger(TBsShareRelatedTaskController.class);
	
	
	/**
	 * @describe 新增分摊信息
	 * @return MessageMap 执行结果
	 */
	@RequestMapping(value="/addShareTask",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap addShareTask(HttpServletRequest req,@RequestBody TBsShareRelatedTask  entity){
		logger.info("====================请求地址：/shareRelatedTask/addShareTask");
		//根据项目id，进行对此项目的手动计费
		return tBsShareRelatedTaskApi.addShareTask(CommonUtils.getCompanyId(req),entity).getModel(); 
	}

	/**
	 * @describe 新增方案信息
	 * @return MessageMap 执行结果
	 */
	@RequestMapping(value="/listPageShareTask",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageShareTask(HttpServletRequest req,@RequestBody TBsShareRelatedTask  entity){
		logger.info("====================请求地址：/shareRelatedTask/listPageShareTask");
		//根据项目id，进行对此项目的手动计费
		return tBsShareRelatedTaskApi.listPageShareTask(CommonUtils.getCompanyId(req),entity).getModel(); 
	}

	/**
	 * 
	 * @param req
	 * @param projectId 项目id
	 * @param taskId 分摊任务id
	 * @return BaseDto
	 */
	@RequestMapping(value="/loadRelationTaskBuiling/{projectId}/{taskId}",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto loadRelationTaskBuiling(HttpServletRequest req,@PathVariable String projectId,@PathVariable String taskId){
		logger.info("====================请求地址：/shareRelatedTask/loadRelationTaskBuiling");
		//根据项目id，进行对此项目的手动计费
		return tBsShareRelatedTaskApi.loadRelationTaskBuiling(CommonUtils.getCompanyId(req),projectId,taskId).getModel(); 
	}
	
	/**
	 * 
	 * @param req
	 * @param projectId 项目id
	 * @param taskId 分摊任务id
	 * @return BaseDto
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/deleteTaskById/{id}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap deleteTaskById(HttpServletRequest req,@PathVariable String id){
		logger.info("====================请求地址：/shareRelatedTask/deleteTaskById");
		//根据项目id，进行对此项目的手动计费
		return tBsShareRelatedTaskApi.deleteTaskById(CommonUtils.getCompanyId(req),id).getModel();
	}
	
}
