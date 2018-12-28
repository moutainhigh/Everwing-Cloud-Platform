package com.everwing.server.wy.web.controller.order;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.order.TcOrderComplaint;
import com.everwing.coreservice.wy.api.order.TcOrderComplaintApi;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 投诉工单
 *
 */
@Controller
@RequestMapping(value="/OrderComplaint")
public class TcOrderComplaintController {

	private static final Logger log = Logger.getLogger(TcOrderComplaintController.class);

	@Autowired
	private TcOrderComplaintApi tcOrderComplaintApi;
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/listPage",method=RequestMethod.POST)
	public @ResponseBody BaseDto listPage(HttpServletRequest req, @RequestBody TcOrderComplaint entity){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcOrderComplaintApi.listPage(ctx.getCompanyId(),entity));
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public @ResponseBody BaseDto insert(HttpServletRequest req,@RequestBody TcOrderComplaint entity){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcOrderComplaintApi.insert(ctx.getCompanyId(),entity));
	}
	
	/**
	 * 根据工单编号展示详细
	 */
	@RequestMapping(value="/showDtail/{projectId}/{orderCode}/{buildCode}/{type}",method=RequestMethod.POST)
	public @ResponseBody BaseDto showDtail(HttpServletRequest req,@PathVariable String projectId,
			      @PathVariable String orderCode,@PathVariable String buildCode,@PathVariable String type){
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		try {
			RemoteModelResult<BaseDto> result = this.tcOrderComplaintApi.showDtail(ctx, projectId, orderCode, buildCode, type);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
		} catch (Exception e) {
			log.info(CommonUtils.log(e.getMessage()));
			msgMap.setMessage("系统错误!");
			msgMap.setFlag(MessageMap.INFOR_ERROR);
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
	
}
