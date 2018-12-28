package com.everwing.coreservice.wy.core.mq.billing;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.wy.entity.configuration.support.BillingSupEntity;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * @TODO 消息队列监听器 , 监听需要批量插入的TBsChargeBillHistory的消息,然后调用service进行插入
 * @author DELL
 *
 */
@Service("wyBillingListener")
public class WyBillingListener extends ServiceRrcs implements MessageListener{

	
	private static final Logger logger = Logger.getLogger(WyBillingListener.class);

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
			
			if(null != entity && null != entity.getData()){
				BillingSupEntity se = ((JSONObject)entity.getData()).toJavaObject(BillingSupEntity.class);
				this.wyBillingTaskService.insert(entity.getCompanyId(),se);
			}
			
			
		}
		
	}
}
