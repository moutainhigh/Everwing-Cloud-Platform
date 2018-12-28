package com.everwing.coreservice.common.utils;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;

public class BaseDtoUtils {
	
	@SuppressWarnings("rawtypes")
	public static BaseDto getDto(RemoteModelResult<BaseDto> rst){
		BaseDto returnDto = null;
		MessageMap msgMap = null;
		if(null == rst){
			returnDto = new BaseDto();
			msgMap = new MessageMap(MessageMap.INFOR_ERROR, MessageMap.EMPTY_RESULT);
		}else{
			if(rst.isSuccess()){
				returnDto = rst.getModel();
				msgMap = returnDto.getMessageMap();
				//请求是成功的，但是获取list结果集后并不会得到messageMap，由于页面有很多处使用了对message.flag的判断，故在此做统一修改
				if(CommonUtils.isEmpty(msgMap)){
					msgMap = new MessageMap(MessageMap.INFOR_SUCCESS, rst.getMsg());
				}
			}else{
				returnDto = new BaseDto();
				msgMap = new MessageMap(MessageMap.INFOR_ERROR, rst.getMsg());
			}
		}
		returnDto.setMessageMap(msgMap);
		return returnDto;
	}
}
