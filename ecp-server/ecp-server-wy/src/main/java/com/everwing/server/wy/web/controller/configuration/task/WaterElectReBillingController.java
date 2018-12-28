package com.everwing.server.wy.web.controller.configuration.task;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;
import com.everwing.coreservice.wy.api.configuration.task.WaterElectRebillingApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/waterElectRebilling")
public class WaterElectReBillingController {
	
	@Autowired
	private WaterElectRebillingApi waterElectRebillingApi;
	
	/**
	 * @describe 水电费的重新计费
	 * @param ids 需要进行重新计费的总账单id集合
	 * @param userId 操作用户id
	 * @param meterType 表类型  0 水表，1 电表
	 * @return MessageMap 处理结果
	 */
	@RequestMapping(value="/waterElectRebilling/{meterType}/{userId}",method=RequestMethod.POST)
	public @ResponseBody MessageMap waterElectRebilling(HttpServletRequest req,@RequestBody List<String> ids,
				@PathVariable String userId,@PathVariable int meterType){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return this.waterElectRebillingApi.waterElectRebilling(ctx.getCompanyId(),ids,userId,meterType).getModel();
	}
	
	
	
	/**
	 * @describe  计费修正
	 */
	@RequestMapping(value="/rebillingCorrect/{type}",method=RequestMethod.POST)
	public @ResponseBody BaseDto rebillingCorrect(HttpServletRequest req,@RequestBody TBsRebillingInfo entity,@PathVariable Integer type){
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.waterElectRebillingApi.rebillingCorrect(ctx.getCompanyId(),entity,type));
	}
	
	
	
}
