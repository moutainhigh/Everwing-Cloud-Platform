package com.everwing.coreservice.wy.core.mq.billing.rebilling.single;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("singleRebillingListener")
public class SingleRebillingListener extends ServiceRrcs implements MessageListener{

	private static final Logger logger = LogManager.getLogger(SingleRebillingListener.class);
	
	@Override
	public void onMessage(Message message) {
		
		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("消息监听队列: [单项重新计费/费用修正] 消息格式转换失败. ");
		}
		
		
		if(null != jsonStr){
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			
			if(null != entity){
				logger.info("消息监听队列: [单项重新计费/费用修正]  开始消息消费 . 数据: {}", entity.toString());
				this.wyBillingTaskService.singleRebilling(entity.getCompanyId(),
															((JSONObject)entity.getData()).toJavaObject(TBsRebillingInfo.class),
														  entity.getOpr()); 
				logger.info("消息监听队列: [单项重新计费/费用修正]  消息消费完成. ");
			}
			
		}
		
		
	}

}
