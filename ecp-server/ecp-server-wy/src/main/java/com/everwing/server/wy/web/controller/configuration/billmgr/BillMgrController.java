package com.everwing.server.wy.web.controller.configuration.billmgr;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.wy.api.configuration.billmgr.BillMgrApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @TODO 账单管理Controller
 * @author DELL
 *
 */
@Controller
@RequestMapping("/billMgr")
public class BillMgrController {

	@Autowired
	private BillMgrApi billMgrApi; 
	
	
	/**
	 *  账单生成controller
	 *  	给页面的
	 *  			生成账单按钮
	 *  		与
	 *  			重新生成账单按钮
	 *  		提供事件
	 * 	
	 * 		两个按钮都是从TBsProject的角度出发 , 单个项目的账单生成
	 * 		需要按栋起消息队列
	 * 
	 */

	@RequestMapping(value="/genBill",method=RequestMethod.POST)
	public @ResponseBody BaseDto genBill(HttpServletRequest req , @RequestBody TBsProject project){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.billMgrApi.genBill(ctx.getCompanyId(),project)); 
	}
	
	
	@RequestMapping(value="/reGenBill",method=RequestMethod.POST)
	public @ResponseBody BaseDto reGenBill(HttpServletRequest req , @RequestBody TBsProject project){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.billMgrApi.reGenBill(ctx.getCompanyId(),project)); 
	}
	
	@RequestMapping(value="/zipBill",method=RequestMethod.POST)
	public @ResponseBody BaseDto zipBill(HttpServletRequest req , @RequestBody TBsProject project){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.billMgrApi.zipBill(ctx.getCompanyId(),project)); 
	}
	
	
	
	
	
}
