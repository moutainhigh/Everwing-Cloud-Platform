package com.everwing.coreservice.wy.core.mq.tBsProject;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("tBsProjectTaskListener")
public class TBsProjectTaskListener extends ServiceRrcs implements MessageListener{

	
	@Override
	public void onMessage(Message message) {
		
		
		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if(null != jsonStr){
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			tBsProjectTaskService.genNextProject(entity.getCompanyId());
		}
		
		
	}

}
