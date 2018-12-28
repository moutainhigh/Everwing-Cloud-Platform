package com.everwing.server.wy.web.controller.configuration.bsconstant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.bsconstant.TBsConstant;
import com.everwing.coreservice.wy.api.configuration.bsconstant.TBsConstantApi;

@Controller
@RequestMapping(value="/tBsConstant")
public class TBsConstantController {

	@Autowired
	private TBsConstantApi tBsConstantApi;
	/**
	 * 新增
	 */
	@RequestMapping(value="/singleAdd",method=RequestMethod.POST)
	public @ResponseBody BaseDto singleAdd(HttpServletRequest req,@RequestBody TBsConstant tBsConstant){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		
		return BaseDtoUtils.getDto(tBsConstantApi.singleAdd(ctx, tBsConstant));
	}
	
	/**
	 * 分页查询常量
	 */
	@RequestMapping(value="/listPageConstants",method=RequestMethod.POST)
	public @ResponseBody BaseDto   listPageConstants(HttpServletRequest req,@RequestBody TBsConstant tBsConstant){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		RemoteModelResult<BaseDto> result = tBsConstantApi.listPageConstants(ctx, tBsConstant);
		if(result.isSuccess()){
			baseDto = result.getModel();
		}else{
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage(result.getMsg());
			baseDto.setMessageMap(mm);
		}
		return baseDto;
	}
	
	@RequestMapping(value="/getConstantsByProIdAndType",method=RequestMethod.POST)
	public @ResponseBody BaseDto getConstantsByProIdAndType(@RequestBody TBsConstant tBsConstant){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		RemoteModelResult<BaseDto> result = tBsConstantApi.getConstantsByProIdAndType(ctx, tBsConstant);
		if(result.isSuccess()){
			baseDto = result.getModel();
		}else{
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage(result.getMsg());
			baseDto.setMessageMap(mm);
		}
		return baseDto;
	}
	
	
	@RequestMapping(value="/singleDel/{id}",method=RequestMethod.POST)
	public @ResponseBody BaseDto singleDel(HttpServletRequest req,@PathVariable String id){
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		try {
			RemoteModelResult<BaseDto> result = this.tBsConstantApi.singleDel(ctx, id);
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
