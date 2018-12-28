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

import com.esotericsoftware.minlog.Log;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingRules;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsGetRuleBuilding;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsRuleBuildingRelation;
import com.everwing.coreservice.wy.api.configuration.project.TBsWaterElectBillingApi;

/**
 * 水电表的计费和分摊会有别于物业和本体基金
 * 		此类存放  水电费的  收费规则，收费类型，收费类型关联建筑信息  
 * 				分摊规则，分摊类型，分摊关联建筑信息
 * 				的增删改查方法，没有太多的业务逻辑所以放在一个控制器
 */
@Controller
@RequestMapping(value="/waterElectBilling")
public class TBsWaterElectBillingController {

	@Autowired
	private TBsWaterElectBillingApi tBsWaterElectBillingApi;
	
	private static Logger logger=Logger.getLogger(TBsWaterElectBillingController.class);
	
	/**
	 * @describe 分页加载规则信息
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/listPageRulesBySchemeId",method=RequestMethod.POST)
	@ResponseBody
	public  BaseDto listPageRulesBySchemeId(HttpServletRequest req,@RequestBody TBsChargingRules entity){
		logger.info("==================/waterElectBilling/listPageRulesBySchemeId====================");
		return BaseDtoUtils.getDto(tBsWaterElectBillingApi.listPageRulesBySchemeId(CommonUtils.getCompanyId(req),entity)); 
	}
	
	
	/**
	 * 根据规则ID查询规则和对应建筑
	 */
	@RequestMapping(value="/getRuleById/{id}",method=RequestMethod.POST)
	public @ResponseBody BaseDto getRuleById(HttpServletRequest req,@PathVariable String id){
		BaseDto baseDto =new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		try {
			RemoteModelResult<BaseDto> result = this.tBsWaterElectBillingApi.getRuleById(ctx, id);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
			
		} catch (Exception e) {
			logger.info("异常:"+e.getMessage());
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统异常!");
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
	
	
	
	
	
	
	

	/**
	 * @describe 水费收费规则新增
	 * @return MessageMap 执行结果
	 */
	@RequestMapping(value="/addOrEditRuleInfo",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap manualChargingForWater(HttpServletRequest req,@RequestBody TBsChargingRules entity){
		//根据项目id，进行对此项目的手动计费
		logger.info("==================/waterElectBilling/addOrEditRuleInfo====================");
		 MessageMap mm = new MessageMap(MessageMap.INFOR_SUCCESS,"");
		RemoteModelResult<MessageMap> result = this.tBsWaterElectBillingApi.addOrEditRuleInfo(CommonUtils.getCompanyId(req), entity);
		if(result.isSuccess()){
			mm = result.getModel();
		}else{
			mm.setFlag(MessageMap.INFOR_ERROR);
			mm.setMessage(result.getMsg());
		}
		return mm;
	}
	
	/**
	 * @describe 水费收费规则批量删除操作
	 * @return MessageMap 执行结果
	 */
	@RequestMapping(value="/deleteRulesByIds/{ids}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap deleteRulesByIds(HttpServletRequest req,@PathVariable String ids){
		//根据项目id，删除方案下的规则信息
		logger.info("==================/waterElectBilling/deleteRulesByIds====================");
		return tBsWaterElectBillingApi.deleteRulesByIds(CommonUtils.getCompanyId(req),ids).getModel();
	}
	
	
	/**
	 * @describe 水费收费规则新增
	 * @return MessageMap 执行结果
	 */
	@RequestMapping(value="/updateRuleById",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap updateRule(HttpServletRequest req,@RequestBody TBsChargingRules entity){
		//根据项目id，删除方案下的规则信息
		logger.info("==================/waterElectBilling/updateRuleById====================");
		return tBsWaterElectBillingApi.updateRule(CommonUtils.getCompanyId(req),entity).getModel();
	}
	
	
	/**
	 * 根据Id查询计费规则
	 */
	@RequestMapping(value="/getFeeRuleById/{ruleId}",method=RequestMethod.POST)
	public @ResponseBody BaseDto getFeeRuleById(HttpServletRequest req,@PathVariable String ruleId){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		RemoteModelResult<BaseDto> result= tBsWaterElectBillingApi.getFeeRuleById(ctx, ruleId);
		return result.getModel();
	}
	

	/**
	 * 计费关联建筑异步加载
	 */
	@RequestMapping(value="getReaBuildByChargRuleId/{chargingRuleId}",method=RequestMethod.POST)
	public @ResponseBody BaseDto getReaBuildByChargRuleId(HttpServletRequest req,@PathVariable String chargingRuleId){
		String id = CommonUtils.null2String(req.getParameter("id"));
		
		WyBusinessContext ctx = WyBusinessContext.getContext();
		TBsRuleBuildingRelation tbsRuleRelation = new TBsRuleBuildingRelation();
		tbsRuleRelation.setRelationBuildingPid(id);
		tbsRuleRelation.setChargingRuleId(chargingRuleId);
		RemoteModelResult<BaseDto> rst = tBsWaterElectBillingApi.getReaBuildByChargRuleId(ctx, tbsRuleRelation);
		return rst.getModel();
	}
	
	
	
	
	/**
	 * 查询过滤后的建筑
	 */
	@RequestMapping(value="/getFilterZeeTree/{chargingSchemeId}/{projectId}",method=RequestMethod.POST)
	public @ResponseBody BaseDto getFilterZeeTree(HttpServletRequest req,@PathVariable String chargingSchemeId,@PathVariable String projectId){
		BaseDto baseDto =new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		String id = CommonUtils.null2String(req.getParameter("id"));
//		if(StringUtils.isBlank(id)){
//			id="jzjg";
//		}
		try {
			TBsGetRuleBuilding ruleBuild = new TBsGetRuleBuilding();
			ruleBuild.setChargingSchemeId(chargingSchemeId);
			ruleBuild.setProjectId(projectId);
			ruleBuild.setpId(id);
			RemoteModelResult<BaseDto> result =  this.tBsWaterElectBillingApi.getFilterZeeTree(ctx, ruleBuild);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
		} catch (Exception e) {
			logger.info(String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(),e.getMessage()));
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统异常!");
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
	
	@RequestMapping(value="/batchRuleByIds",method=RequestMethod.POST)
	public @ResponseBody BaseDto batchRuleByIds(HttpServletRequest req,@RequestBody TBsChargingRules tBsChargingRules){
		BaseDto baseDto =new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		try {
			RemoteModelResult<BaseDto> result = this.tBsWaterElectBillingApi.batchRuleByIds(ctx, tBsChargingRules);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统错误!");
			Log.info(getLogStr(e.getMessage()));
		}
		return baseDto;
	}
	
	private String getLogStr(String error){
		return String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(),error);
	}
}
