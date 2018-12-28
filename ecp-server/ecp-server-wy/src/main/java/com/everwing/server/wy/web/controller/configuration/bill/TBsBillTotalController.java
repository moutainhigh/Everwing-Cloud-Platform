package com.everwing.server.wy.web.controller.configuration.bill;

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.wy.api.configuration.bill.TBsBillTotalApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/billTotal")
public class TBsBillTotalController {

	/**
	 * 总账单分页查询, 根据传入的                  type 0 : 通用账户, 1 : 物业管理费 , 2 : 本体基金 , 3 : 水表 , 4 : 电表 
	 * 			   		     	  billingTime  yyyy-MM
	 * 	
	 * 
	 */
	
	
	@Autowired
	private TBsBillTotalApi tBsBillTotalApi;
	
	@Autowired
	private SpringRedisTools springRedisTools;
	
	
	@RequestMapping(value="/findDataPerYear",method=RequestMethod.POST)
	public @ResponseBody BaseDto findDataPerYear(HttpServletRequest req, @RequestBody TBsChargeBillTotal entity){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsBillTotalApi.findDataPerYear(ctx.getCompanyId(),entity));
	}
	
	@RequestMapping(value="/findById/{id}",method=RequestMethod.GET)
	public @ResponseBody BaseDto findById(HttpServletRequest req, @PathVariable String id){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsBillTotalApi.findById(ctx.getCompanyId(),id));
	}
	
	@RequestMapping(value="/audit",method=RequestMethod.POST)
	public @ResponseBody BaseDto audit(HttpServletRequest req, @RequestBody TBsChargeBillTotal entity){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();

		//判断是否处于审核完成状态
		Object rslt = springRedisTools.getByKey(entity.getId());
		if(rslt != null && CommonUtils.isEquals(CommonUtils.null2String(rslt), BillingEnum.AUDIT_STATUS_COMPELTE.getIntV().toString())){
			//该账单已经审核完成,无法重复审核
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage("该账单已经审核完成,无法重复审核.");
			baseDto.setMessageMap(mm);
			return baseDto;
		}
		
		return BaseDtoUtils.getDto(this.tBsBillTotalApi.audit(ctx.getCompanyId(),entity));
	}
	
	
	/**
	 * 测试虚拟费用汇总
	 */
	@RequestMapping(value="/TestSum",method=RequestMethod.POST)
	public @ResponseBody BaseDto TestSum(HttpServletRequest req){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(tBsBillTotalApi.TestSum(ctx));
	}
	
	
}
