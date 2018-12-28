package com.everwing.server.wy.web.controller.configuration.project;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargeType;
import com.everwing.coreservice.common.wy.entity.configuration.project.TestOpeation;
import com.everwing.coreservice.wy.api.configuration.project.TBsChargingTypeApi;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="/TBsChargingType")
public class TBsChargingTypeController {
	
	private static final Logger log = Logger.getLogger(TBsChargingTypeController.class);
	
	@Autowired
	private TBsChargingTypeApi tBsChargingTypeApi;
	
	@RequestMapping(value="/listPageChargingType",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPageChargingType(HttpServletRequest req,@RequestBody TBsChargeType tBsChargeType){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			RemoteModelResult<BaseDto> result = tBsChargingTypeApi.listPageChargingType(ctx, tBsChargeType);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统异常!");
			log.info("异常:"+e.getMessage());
		}
		baseDto.setMessageMap(msgMap);
		
		return baseDto;
	}
	
	/**
	 * 新增
	 * @param req
	 * @param tBsChargeType
	 * @return
	 */
	@RequestMapping(value="/addChargingType", method=RequestMethod.POST)
	public @ResponseBody BaseDto addChargingType(HttpServletRequest req,@RequestBody TBsChargeType tBsChargeType){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			RemoteModelResult<BaseDto> result = tBsChargingTypeApi.addChargingType(ctx, tBsChargeType);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
		} catch (Exception e) {
			log.info("异常:"+e.getMessage());
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统异常!");
		}
		baseDto.setMessageMap(msgMap);
		
		return baseDto;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/batchDel",method=RequestMethod.POST)
	public @ResponseBody BaseDto batchDel(HttpServletRequest req,@RequestBody TBsChargeType tBsChargeType){
		BaseDto baseDto =new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		try {
			RemoteModelResult<BaseDto> result = this.tBsChargingTypeApi.batchDel(ctx, tBsChargeType);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
		} catch (Exception e) {
			log.info("得到BusinessContext出错："+e.getMessage());
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统异常!");
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
	
	/**
	 * 试运算
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/testOpeation",method=RequestMethod.POST)
	public @ResponseBody BaseDto testOpeation(HttpServletRequest req,@RequestBody TestOpeation testOpeation){
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		try {
			String formulaValue = testOpeation.getFormulaValue();
			Double billingFeeItemTestValue = testOpeation.getBillingFeeItemTestValue();
			if(StringUtils.isBlank(formulaValue) || CommonUtils.isEmpty(billingFeeItemTestValue)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("传进来计费项目的值或者是公式为空;不能计算!");
			}else{
				RemoteModelResult<BaseDto> result = this.tBsChargingTypeApi.testOpeation(ctx, formulaValue, String.valueOf(billingFeeItemTestValue));
				if(result.isSuccess()){
					baseDto = result.getModel();
					msgMap=baseDto.getMessageMap();
				}else{
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage(result.getMsg());
				}
			}
			
		} catch (Exception e) {
			log.info(getLogStr(e.getMessage()));
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统异常!");
		}
		
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
	
	private String getLogStr(String error){
		return String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(),error);
	}
	
	
}
