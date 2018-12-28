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

/**
 * @describe 定时任务平台按公司进行对水费分摊计费的调用
 * @author QHC
 * @date 2017-08-10 17:00:00
 */
@Service("tbsWaterShareAutoBillingListner")
public class TbsWaterShareAutoBillingListner extends ServiceRrcs implements MessageListener{

	
	private static final Logger logger = Logger.getLogger(TbsWaterShareAutoBillingListner.class);

	@Override
	public void onMessage(Message message) {
		
		String jsonStr = null;
		
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if(null != jsonStr){
			logger.info("*******水费分摊自动计费消息队列监听到任务开始执行S********");
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			
			if(BillingEnum.share_company_request.getIntV() == entity.getOpr()){
//				BillingSupEntity se = ((JSONObject)entity.getData()).toJavaObject(BillingSupEntity.class);
				this.waterElectShareTaskService.doWaterElectShareBillingByCompnay(entity.getCompanyId(),BillingEnum.SHARE_WATER_TYPE.getIntV());
			}
			logger.info("*******水费分摊自动计费消息队列监听到任务执行E********");
			
		}
		
	}
}
