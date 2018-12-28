package com.everwing.server.wy.web.controller.configuration.task;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.wy.api.configuration.task.WaterAndElectBillingTaskApi;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/electBillingTask")
public class WaterAndElectBillingTaskController {

	private static final Logger log = Logger.getLogger(WaterAndElectBillingTaskController.class);
	
	@Autowired
	private WaterAndElectBillingTaskApi waterAndElectBillingTaskApi;
	
	@RequestMapping(value="/manualElectBilling/{meterType}",method=RequestMethod.POST)
	public @ResponseBody BaseDto manualElectBilling(HttpServletRequest rq,@PathVariable String meterType,@RequestBody TBsProject entity){
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
	    try {
			RemoteModelResult<BaseDto> result = this.waterAndElectBillingTaskApi.manualElectBilling(ctx.getCompanyId(), meterType, entity);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
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
	
//	@Test
//	public void testString(){
//		System.out.println(String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(), "ooooooo"));
//	}
	
	//测试自动计费
	@RequestMapping(value="/testAutoBill/{meterFlag}",method=RequestMethod.POST)
	public @ResponseBody BaseDto testAutoBill(HttpServletRequest req,@PathVariable String meterFlag){
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		try {
			RemoteModelResult<BaseDto> result = this.waterAndElectBillingTaskApi.autoBilling(ctx.getCompanyId(), meterFlag);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统错误!");
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
}
