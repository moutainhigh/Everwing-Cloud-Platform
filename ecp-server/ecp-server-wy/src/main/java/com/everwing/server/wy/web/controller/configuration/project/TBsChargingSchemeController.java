package com.everwing.server.wy.web.controller.configuration.project;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import com.everwing.coreservice.wy.api.configuration.project.TBsChargingSchemeApi;

/**
 * 水电表的计费和分摊会有别于物业和本体基金
 * 		此类存放  水电费的  收费规则，收费类型，收费类型关联建筑信息  
 * 				分摊规则，分摊类型，分摊关联建筑信息
 * 				的增删改查方法，没有太多的业务逻辑所以放在一个控制器
 */
@Controller
@RequestMapping(value="/chargingScheme")
public class TBsChargingSchemeController {

	@Autowired
	private TBsChargingSchemeApi tBsChargingSchemeApi;
	
	
	
	/**
	 * @describe 新增方案信息
	 * @return MessageMap 执行结果
	 */
	@RequestMapping(value="/addChargingScheme",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap addChargingScheme(HttpServletRequest req,@RequestBody TBsChargingScheme entity){
		//根据项目id，进行对此项目的手动计费
		return tBsChargingSchemeApi.addChargingScheme(CommonUtils.getCompanyId(req),entity).getModel(); 
	}
	
	/**
	 * @TODO 保存
	 * @param req
	 * @param scheme
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/editedScheme",method=RequestMethod.POST)
	public @ResponseBody BaseDto editedScheme(HttpServletRequest req,@RequestBody TBsChargingScheme scheme){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(tBsChargingSchemeApi.editedScheme(ctx.getCompanyId(),scheme)); 
	}
	

	/**
	 * 根据项目编号和方案类型 查找未失效的方案且已经启用了的方案
	 * @param req
	 * @param schemeType
	 * @param projectId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/getChargingScheme/{schemeType}/{projectId}",method=RequestMethod.POST)
	public @ResponseBody BaseDto getChargingScheme(HttpServletRequest req,@PathVariable String schemeType,@PathVariable String projectId){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsChargingSchemeApi.getChargingScheme(ctx, schemeType, projectId));
	}
	

	/**
	 * @TODO 分页显示
	 * @param req
	 * @param scheme
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/listPageSchemes",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPageSchemes(HttpServletRequest req,@RequestBody TBsChargingScheme scheme){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(tBsChargingSchemeApi.listPageSchemes(ctx.getCompanyId(),scheme)); 
	}
	
	
	
	/**
	 * @TODO 找寻可用scheme
	 * @param req
	 * @param scheme
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/findUsingScheme",method=RequestMethod.POST)
	public @ResponseBody BaseDto findUsingScheme(HttpServletRequest req,@RequestBody TBsChargingScheme scheme){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(tBsChargingSchemeApi.findUsingScheme(ctx.getCompanyId(),scheme)); 
	}
	
	/**
	 * @TODO 批量停用scheme
	 * @param req
	 * @param scheme
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stopSchemes",method=RequestMethod.POST)
	public @ResponseBody BaseDto stopSchemes(HttpServletRequest req,@RequestBody List<TBsChargingScheme> schemes){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(tBsChargingSchemeApi.stopSchemes(ctx.getCompanyId(),schemes)); 
	}
	
	

}
