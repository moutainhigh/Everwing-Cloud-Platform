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
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareBasicsInfo;
import com.everwing.coreservice.wy.api.configuration.project.TBsShareBasicApi;

/**
 *@describe 分摊基础数据相关控制
 */
@Controller
@RequestMapping(value="/shareBasic")
public class TBsShareBasicController {

	@Autowired
	private TBsShareBasicApi tBsShareBasicApi;
	
	private static Logger logger=Logger.getLogger(TBsShareBasicController.class);
	
	
	/**
	 * @describe 新增分摊信息
	 * @return MessageMap 执行结果
	 */
	@RequestMapping(value="/addShareBasic",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap addShareBasic(HttpServletRequest req,@RequestBody TBsShareBasicsInfo  entity){
		logger.info("====================请求地址：/shareBasic/addShareBasic");
		//根据项目id，进行对此项目的手动计费
		return tBsShareBasicApi.addShareBasic(CommonUtils.getCompanyId(req),entity).getModel(); 
	}

	/**
	 * @describe 新增方案信息
	 * @return MessageMap 执行结果
	 */
	@RequestMapping(value="/listPageShareInfos",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageShareInfos(HttpServletRequest req,@RequestBody TBsShareBasicsInfo  entity){
		logger.info("====================请求地址：/shareBasic/listPageShareInfos");
		//根据项目id，进行对此项目的手动计费
		return tBsShareBasicApi.listPageShareInfos(CommonUtils.getCompanyId(req),entity).getModel(); 
	}

	
	/**
	 * @describe 删除分摊基础方案信息
	 * @return MessageMap 执行结果
	 */
	@RequestMapping(value="/deleteShareBsic/{shareId}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap deleteShareBsic(HttpServletRequest req,@PathVariable String shareId){
		logger.info("====================请求地址：/shareBasic/deleteShareBsic");
		//根据项目id，进行对此项目的手动计费
		return tBsShareBasicApi.deleteShareBsic(CommonUtils.getCompanyId(req),shareId).getModel(); 
	}


	/**
	 * @describe 失效分摊基础方案信息
	 * @return MessageMap 执行结果
	 */
	@RequestMapping(value="/innvalidShareBasic/{shareId}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap innvalidShareBasic(HttpServletRequest req,@PathVariable String shareId){
		logger.info("====================请求地址：/shareBasic/innvalidShareBasic");
		//根据项目id，进行对此项目的手动计费
		return tBsShareBasicApi.innvalidShareBasic(CommonUtils.getCompanyId(req),shareId).getModel(); 
	}
	
}
