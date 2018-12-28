package com.everwing.coreservice.wy.core.mq.billing;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("wyBillingKouFeiListener")
public class WyBillingKouFeiListener extends ServiceRrcs implements MessageListener{

	private static final Logger logger = LogManager.getLogger(WyBillingKouFeiListener.class);
	
	@Override
	public void onMessage(Message message) {

		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if(jsonStr != null){
			
			logger.info("扣费消息队列: 扣费开始,传入数据: {}",jsonStr);
			
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			if(entity != null){
				this.tBsBillTotalService.koufei(entity.getCompanyId(),((JSONObject)entity.getData()).toJavaObject(TBsChargeBillTotal.class));
			}
			logger.info("扣费消息队列: 扣费完成.");
		}
		
	}

	
	
	
}
