package com.everwing.coreservice.wy.core.mq.billing;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("waterBillingByCompanyIdListener")
public class WaterBillingByCompanyIdListener extends ServiceRrcs implements MessageListener{
	private static final Logger logger = LogManager.getLogger(WaterBillingByCompanyIdListener.class);
	@Override
	public void onMessage(Message message) {
		String jsonStr = null;
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("水费自动计费消息队列: 监听到消息格式转换失败. 数据: {}",message);
		}
		if(null != jsonStr){
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			if(null != entity){
				logger.info("水费自动计费消息队列监听 :  [平台 to 物业 -> 按CompanyId起消息队列] 开始处理消息. 数据: {}",jsonStr);
				this.waterAndElectBillingTaskService.autoBilling(entity.getCompanyId(), "water"); //水电分开是因为，水电费只要有一个计费有误(代码抛错)，就会相互影响
			}
		}
		
	}

}
