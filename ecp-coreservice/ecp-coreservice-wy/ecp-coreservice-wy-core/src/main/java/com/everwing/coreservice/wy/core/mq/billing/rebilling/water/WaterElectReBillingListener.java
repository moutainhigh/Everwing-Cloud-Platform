package com.everwing.coreservice.wy.core.mq.billing.rebilling.water;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service("waterElectReBillingListener")
public class WaterElectReBillingListener extends ServiceRrcs implements MessageListener{
	
	private static final Logger logger = LogManager.getLogger(WaterElectReBillingListener.class);

	@Override
	public void onMessage(Message message) {

		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("监听队列: [物业管理费 / 本体基金  重新计费] 消息格式转换失败. 数据: {}",message.getBody());
		}
		
		
		if(CommonUtils.isNotEmpty(jsonStr)){
			
			MqEntity e = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			if(CommonUtils.isNotEmpty(e.getData())){
				
				String feeStr = (e.getOpr() == 0) ? Constants.BILLING_WATER : Constants.BILLING_ELECT;
				int meterType= e.getOpr();
				
				logger.info("监听队列: [{}重新计费] 数据处理 [开始] , 数据: {}", feeStr, e.toString());
				List<String> ids = JSONObject.parseArray(JSON.toJSONString(e.getData()), String.class);
				this.waterElectRebillingService.waterElectRebilling(e.getCompanyId(),ids,e.getUserId(),meterType);
				logger.info("监听队列: [{}重新计费] 数据处理 [完成] , 数据: {}", feeStr, e.toString());
				
				
				
			}else{
				logger.warn("监听队列: [物业管理费 / 本体基金  重新计费] 传入数据为空. 数据: {}",e.toString());
			}
			
			
			
			
		}
		
		
	}

}
