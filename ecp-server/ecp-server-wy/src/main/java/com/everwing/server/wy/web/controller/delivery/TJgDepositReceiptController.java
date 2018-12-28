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
import com.everwing.coreservice.common.wy.entity.delivery.TJgDepositReceipt;
import com.everwing.coreservice.wy.api.delivery.TJgDepositReceiptApi;


/***
 * @describe 存单表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Controller
@RequestMapping(value="/depositReceipt")
public class TJgDepositReceiptController{
	
	@Autowired
	private TJgDepositReceiptApi tJgDepositReceiptApi;
	
	/**
	 * @describe 新增水表信息
	 * @author QHC
	 * @date 2017-05-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/addDepositReceipt",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap addDepositReceipt(HttpServletRequest req,@RequestBody TJgDepositReceipt entity){
		return tJgDepositReceiptApi.addDepositReceipt(CommonUtils.getCompanyId(req), entity).getModel();
	}
	
	/**
	 * @describe 查询存单信息--根据totalid（查看的操作）
	 * @author QHC
	 * @date 2017-05-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/getDepositReceiptInfo/{totalId}",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto getDepositReceiptInfo(HttpServletRequest req,@PathVariable String totalId){
		return tJgDepositReceiptApi.getDepositReceiptInfo(CommonUtils.getCompanyId(req), totalId).getModel();
	}
	
	
	/**
	 * @describe 查询存单信息--交账时的操作
	 * @author QHC
	 * @date 2017-05-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/getDepositReceiptInfoForGive/{projectId}/{oprId}",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto getDepositReceiptInfoForGive(HttpServletRequest req,@PathVariable String projectId,@PathVariable String oprId){
		return tJgDepositReceiptApi.getDepositReceiptInfoForGive(CommonUtils.getCompanyId(req), projectId,oprId).getModel();
	}
	
	
	/**
	 * @describe 单个执行存单的删除操作，（交账之前）
	 * @author QHC
	 * @date 2017-05-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/delDepositReceiptInfo/{id}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap delDepositReceiptInfo(HttpServletRequest req,@PathVariable String id){
		return tJgDepositReceiptApi.delDepositReceiptInfo(CommonUtils.getCompanyId(req),id).getModel();
	}
	
	
	/**
	 * @describe 单个执行存单的删除操作，（交账之前）
	 * @param type 1:出纳根据自己用户id查看等待交账的存单信息    2：财务根据total_id 查询存单信息
	 * @author QHC
	 * @date 2017-05-04
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/listPageDepositsInfo/{type}",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listPageDepositsInfo(HttpServletRequest req,@RequestBody TJgDepositReceipt entity,@PathVariable Integer type){
		return tJgDepositReceiptApi.listPageDepositsInfo(CommonUtils.getCompanyId(req),entity,type).getModel();
	}
}