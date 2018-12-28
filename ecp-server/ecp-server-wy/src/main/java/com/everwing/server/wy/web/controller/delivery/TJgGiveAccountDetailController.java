package com.everwing.server.wy.web.controller.delivery;

import javax.servlet.http.HttpServletRequest;

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
import com.everwing.coreservice.common.wy.entity.delivery.TJgGiveAccountDetail;
import com.everwing.coreservice.wy.api.delivery.TJgGiveAccountDetailApi;


/***
 * @describe 银账交割交账明细表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Controller
@RequestMapping(value="/giveAccountDetail")
public class TJgGiveAccountDetailController{
	
	@Autowired
	private TJgGiveAccountDetailApi tJgGiveAccountDetailApi;
	
	/**
	 * @describe 新增水表信息
	 * @author QHC
	 * @date 2017-09-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/addGiveAccountDetail",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap addGiveAccountDetail(HttpServletRequest req,@RequestBody TJgGiveAccountDetail entity){
		return tJgGiveAccountDetailApi.addGiveAccountDetail(CommonUtils.getCompanyId(req), entity).getModel();
	}
	
	
	/**
	 * @describe 查询交账明细
	 * @author QHC
	 * @date 2017-09-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/listPageGiveCashInfo",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageGiveCashInfo(HttpServletRequest req,@RequestBody TJgGiveAccountDetail entity){
		return tJgGiveAccountDetailApi.listPageGiveCashInfo(CommonUtils.getCompanyId(req),entity).getModel();
	}
	
	/**
	 * @describe 查询收账明细
	 * @author QHC
	 * @date 2017-09-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/listPageReceiveCashInfo",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageReceiveCashInfo(HttpServletRequest req,@RequestBody TJgGiveAccountDetail entity){
		return tJgGiveAccountDetailApi.listPageReceiveCashInfo(CommonUtils.getCompanyId(req),entity).getModel();
	}
	
	
	/**
	 * @describe 审核交账记录
	 * @author QHC
	 * @date 2017-09-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/returnOrConfirmGiveInfo/{ids}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap returnOrConfirmGiveInfo(HttpServletRequest req,@PathVariable String ids,@RequestBody TJgGiveAccountDetail entity){
		return tJgGiveAccountDetailApi.returnOrConfirmGiveInfo(CommonUtils.getCompanyId(req),entity,ids).getModel();
	}
	
	/**
	 * @describe 查询单条结算的交账明细
	 * @author QHC
	 * @date 2017-09-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/listPageGiveAccountByTotalId",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageGiveAccountByTotalId(HttpServletRequest req,@RequestBody TJgGiveAccountDetail entity){
		return tJgGiveAccountDetailApi.listPageGiveAccountByTotalId(CommonUtils.getCompanyId(req),entity).getModel();
	}
	
	
	/**
	 * @describe 出纳查询收账明细
	 * @author QHC
	 * @date 2017-09-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/listPageAccountReceivables/{type}",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageAccountReceivables(HttpServletRequest req,@RequestBody TJgGiveAccountDetail entity,@PathVariable String type){
		return tJgGiveAccountDetailApi.listPageAccountReceivables(CommonUtils.getCompanyId(req),entity,type).getModel();
	}
	
	/**
	 * @describe 出纳查询自己待交账的现金收账信息
	 * @author QHC
	 * @date 2017-09-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/listPageGiveAccountForCN",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageGiveAccountForCN(HttpServletRequest req,@RequestBody TJgGiveAccountDetail entity){
		return tJgGiveAccountDetailApi.listPageGiveAccountForCN(CommonUtils.getCompanyId(req),entity).getModel();
	}
	
	
}
