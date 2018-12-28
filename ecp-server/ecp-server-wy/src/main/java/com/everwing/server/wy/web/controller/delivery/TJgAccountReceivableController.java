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
import com.everwing.coreservice.common.wy.entity.delivery.TJgAccountReceivable;
import com.everwing.coreservice.wy.api.delivery.TJgAccountReceivableApi;


/***
 * @describe 收账明细表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Controller
@RequestMapping(value="/accountReceivable")
public class TJgAccountReceivableController{
	
	@Autowired
	private TJgAccountReceivableApi tJgAccountReceivableApi;
	
	/**
	 * @describe 新增一条收账明细
	 * @author QHC
	 * @date 2017-08-31 
	 */
	@SuppressWarnings({ "deprecation" })
	@RequestMapping(value="/addAccountReceivable",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap addAccountReceivable(HttpServletRequest req,@RequestBody TJgAccountReceivable entity){	
		return tJgAccountReceivableApi.addAccountReceivable(CommonUtils.getCompanyId(req), entity).getModel();
	}
	
	
	/**
	 * @describe 分页查询角色的
	 * @author QHC
	 * @date 2017-08-31 
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@RequestMapping(value="/listPageAcountReceiveInfo",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageAcountReceiveInfo(HttpServletRequest req,@RequestBody TJgAccountReceivable entity){	
		return tJgAccountReceivableApi.listPageAcountReceiveInfo(CommonUtils.getCompanyId(req), entity).getModel();
	}
	
	/**
	 * @author QHC
	 * @date 2017-08-31 
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@RequestMapping(value="/sumPaymentInfo",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto sumPaymentInfo(HttpServletRequest req,@RequestBody TJgAccountReceivable entity){	
		return tJgAccountReceivableApi.sumPaymentInfo(CommonUtils.getCompanyId(req), entity).getModel();
	}
	
	
	/**
	 * @describe 为交账汇总数据
	 * @author QHC
	 * @date 2017-08-31 
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@RequestMapping(value="/sumNotGavenAmountInfo",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto sumNotGavenAmountInfo(HttpServletRequest req,@RequestBody TJgAccountReceivable entity){	
		return tJgAccountReceivableApi.sumNotGavenAmountInfo(CommonUtils.getCompanyId(req), entity).getModel();
	}
	
	
	/**
	 * @describe 分页查询角色的现金未交总额
	 * @author QHC
	 * @date 2017-08-31 
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@RequestMapping(value="/getNotGivenCashAmount/{userId}/{projectId}",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto getNotGivenCashAmount(HttpServletRequest req,@PathVariable String userId,@PathVariable String projectId){
		return tJgAccountReceivableApi.getNotGivenCashAmount(CommonUtils.getCompanyId(req), userId,projectId).getModel();
	}
	
	
	/**
	 * @describe 分页查询
	 * @author QHC
	 * @date 2017-08-31 
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@RequestMapping(value="/listPageNotGivenInfos",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageNotGivenInfos(HttpServletRequest req,@RequestBody TJgAccountReceivable entity){	
		return tJgAccountReceivableApi.listPageNotGivenInfos(CommonUtils.getCompanyId(req), entity).getModel();
	}
	
	/**
	 * @describe 分页查询
	 * @author QHC
	 * @date 2017-08-31 
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@RequestMapping(value="/listPageReceiveByTotalId",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageReceiveByTotalId(HttpServletRequest req,@RequestBody TJgAccountReceivable entity){	
		return tJgAccountReceivableApi.listPageReceiveByTotalId(CommonUtils.getCompanyId(req), entity).getModel();
	}
	
	
	/**
	 * @describe 会计查询未交账信息
	 * @author QHC
	 * @date 2017-08-31 
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@RequestMapping(value="/listPageAccountReceiveForKJ",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageAccountReceiveForKJ(HttpServletRequest req,@RequestBody TJgAccountReceivable entity){	
		return tJgAccountReceivableApi.listPageAccountReceiveForKJ(CommonUtils.getCompanyId(req), entity).getModel();
	}
	
	
	
}
