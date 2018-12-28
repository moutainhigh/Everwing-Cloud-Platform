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
import com.everwing.coreservice.common.wy.entity.delivery.TJgTotalCalculation;
import com.everwing.coreservice.wy.api.delivery.TJgTotalCalculationApi;

/***
 * @describe 总结算表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Controller
@RequestMapping(value="/totalCalculation")
public class TJgTotalCalculationController{
	
	@Autowired
	private TJgTotalCalculationApi tJgTotalCalculationApi;
	
	/**
	 * @describe 新增水表信息
	 * @author QHC
	 * @date 2017-05-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/addTotalCalculation",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap addTotalCalculation(HttpServletRequest req,@RequestBody TJgTotalCalculation entity){
		return tJgTotalCalculationApi.addTotalCalculation(CommonUtils.getCompanyId(req), entity).getModel();
	}
	
	
	/**
	 * @describe 收银组长和收银员的交账汇总有区别
	 * @author QHC
	 * @date 2017-05-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/giveAnccountSummarForLeader/{type}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap giveAnccountSummarForLeader(HttpServletRequest req,@RequestBody TJgTotalCalculation entity,@PathVariable Integer type){
		return tJgTotalCalculationApi.giveAnccountSummarForLeader(CommonUtils.getCompanyId(req), entity,type).getModel();
	}
	
	
	
	/**
	 * @describe 查询对应角色下的结算记录
	 * @author QHC
	 * @date 2017-05-04
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@RequestMapping(value="/listPageTotalCaculation",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageTotalCaculation(HttpServletRequest req,@RequestBody TJgTotalCalculation entity){
		return tJgTotalCalculationApi.listPageTotalCaculation(CommonUtils.getCompanyId(req), entity).getModel();
	}
	
	/**
	 * @describe 审核交账记录
	 * @author QHC
	 * @date 2017-09-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/returnOrConfirmTotalInfo/{ids}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap returnOrConfirmTotalInfo(HttpServletRequest req,@PathVariable String ids,@RequestBody TJgTotalCalculation entity){
		return tJgTotalCalculationApi.returnOrConfirmTotalInfo(CommonUtils.getCompanyId(req),entity,ids).getModel();
	}
	
	
	/**
	 * @describe 汇总可进行交账操作的交账记录
	 * @author QHC
	 * @date 2017-09-04
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@RequestMapping(value="/summaryAccountForInfo",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto summaryAccountForInfo(HttpServletRequest req,@RequestBody TJgTotalCalculation entity){
		return tJgTotalCalculationApi.summaryAccountForInfo(CommonUtils.getCompanyId(req),entity).getModel();
	}
	
	
	/**
	 * @describe 查询自身角色的交账记录信息
	 * @author QHC
	 * @date 2017-09-04
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@RequestMapping(value="/listPageSelfTotalInfo",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageSelfTotalInfo(HttpServletRequest req,@RequestBody TJgTotalCalculation entity){
		return tJgTotalCalculationApi.listPageSelfTotalInfo(CommonUtils.getCompanyId(req),entity).getModel();
	}
	
	
	
	/**
	 * @describe 查询自身角色的交账记录信息
	 * @param type 1:交账后查询关联结算集合
	 *        type 2:待交账的关联结算集合
	 * @author QHC
	 * @date 2017-09-04
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@RequestMapping(value="/listPageSettlementDetails/{type}",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageSettlementDetails(HttpServletRequest req,@RequestBody TJgTotalCalculation entity,@PathVariable String type){
		return tJgTotalCalculationApi.listPageSettlementDetails(CommonUtils.getCompanyId(req),entity,type).getModel();
	}
	
	
	/**
	 * @describe 出纳审核交账记录
	 * @author QHC
	 * @date 2017-09-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/returnOrConfirmTotalInfoCN/{ids}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap returnOrConfirmTotalInfoCN(HttpServletRequest req,@PathVariable String ids,@RequestBody TJgTotalCalculation entity){
		return tJgTotalCalculationApi.returnOrConfirmTotalInfoCN(CommonUtils.getCompanyId(req),entity,ids).getModel();
	}
	

	/**
	 * @describe 财务审核交账记录
	 * @author QHC
	 * @date 2017-09-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/returnOrConfirmTotalInfoKJ/{ids}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap returnOrConfirmTotalInfoKJ(HttpServletRequest req,@PathVariable String ids,@RequestBody TJgTotalCalculation entity){
		return tJgTotalCalculationApi.returnOrConfirmTotalInfoKJ(CommonUtils.getCompanyId(req),entity,ids).getModel();
	}
	
	
	
}
