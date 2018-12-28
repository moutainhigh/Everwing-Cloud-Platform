package com.everwing.coreservice.wy.core.mq.latefee;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("lateFeeByProjectListener")
public class LateFeeByProjectListener extends ServiceRrcs implements MessageListener{

	private static final Logger logger = LogManager.getLogger(LateFeeByProjectListener.class);
	
	
	
	@Override
	public void onMessage(Message message) {
		
		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("项目级违约金计算队列监听: 消息转换格式失败. ");
		}
		
		
		
		if(null != jsonStr){
			
			MqEntity e = JSONObject.parseObject(jsonStr, MqEntity.class);

			if(null != e){
				this.LateFeeService.billLateFee(e.getCompanyId(),((JSONObject)e.getData()).toJavaObject(TBsProject.class));
			}
			
		}
		
		
		
	}

	
	
	
}
