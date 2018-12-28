package com.everwing.coreservice.wy.core.mq.tBsProject;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("tBsProjectAutoFlushListener")
public class TBsProjectAutoFlushListener extends ServiceRrcs implements MessageListener{

	
	private static final Logger logger = LogManager.getLogger(TBsProjectAutoFlushListener.class);
	
	
	
	
	
	
	@Override
	public void onMessage(Message message) {
	
		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("项目状态自动刷新监听: 消息格式转换失败. 数据: {}" , message);
		}
		
		if(jsonStr != null){
			
			
			MqEntity e = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			if(CommonUtils.isNotEmpty(e)){
				logger.info("项目状态自动刷新监听: 开始自动刷新项目数据. 接收消息: {}", e.toString());
				BaseDto baseDto = this.tBsProjectService.autoFlush(e.getCompanyId());
				logger.info("项目状态自动刷新监听: 项目数据自动刷新完成. 返回消息: {}", baseDto.getMessageMap().toString());
			}
			
		}
		
		
		
	}

}
