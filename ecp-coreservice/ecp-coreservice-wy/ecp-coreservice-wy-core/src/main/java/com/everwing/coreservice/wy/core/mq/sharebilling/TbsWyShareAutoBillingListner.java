package com.everwing.coreservice.wy.core.mq.sharebilling;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("tbsWyShareAutoBillingListner")
public class TbsWyShareAutoBillingListner extends ServiceRrcs implements MessageListener{

	
	private static final Logger logger = Logger.getLogger(TbsWyShareAutoBillingListner.class);

	@Override
	public void onMessage(Message message) {
		
		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if(null != jsonStr){
			logger.info("*******物业管理费分摊自动计费消息队列监听到任务开始执行S********");
			
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			
			if(BillingEnum.share_company_request.getIntV() == entity.getOpr()){
//				BillingSupEntity se = ((JSONObject)entity.getData()).toJavaObject(BillingSupEntity.class);
				this.wyShareTaskService.doWyShareByCompnay(entity.getCompanyId());
			}
			logger.info("*******物业管理费分摊自动计费消息队列监听到任务执行E********");
			
		}
		
	}
}
