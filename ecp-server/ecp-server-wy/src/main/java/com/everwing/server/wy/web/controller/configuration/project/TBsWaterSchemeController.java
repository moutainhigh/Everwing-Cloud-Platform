package com.everwing.server.wy.web.controller.configuration.project;

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
import com.everwing.coreservice.wy.api.configuration.project.TBsWaterSchemeApi;

/**
 * @TODO 设置-收费计费-项目页面--水表方案
 * @createTime 2017年7月4日10:06:36
 */
@Controller
@RequestMapping(value="/tBsWaterScheme")
public class TBsWaterSchemeController {

	@Autowired
	private TBsWaterSchemeApi tBsWaterSchemeApi;
	
	/**
	 * @describe 水费方案详情查询，用于展示和编辑
	 * @param schemeType 方案类型（1:物业管理费，2：本体基金 3：水费 4：电费）
	 * @author QHC
	 */
	@RequestMapping(value="/selectWaterSchemeInfo",method=RequestMethod.POST)
	public @ResponseBody BaseDto selectWaterSchemeInfo(HttpServletRequest req,@RequestBody TBsChargingScheme scheme){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(tBsWaterSchemeApi.selectWaterSchemeInfo(ctx.getCompanyId(),scheme)); 
	}
	

	/**
	 * @describe 水费手动计费
	 * @return
	 */
	@RequestMapping(value="/manualChargingForWater/{projectId}/{userId}",method=RequestMethod.POST)
	public MessageMap manualChargingForWater(HttpServletRequest req,@PathVariable String projectId,@PathVariable String userId){
		//根据项目id，进行对此项目的手动计费
		return tBsWaterSchemeApi.manualChargingForWater(CommonUtils.getCompanyId(req),projectId,userId).getModel(); 
	}
	
}
