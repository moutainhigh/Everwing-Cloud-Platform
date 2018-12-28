package com.everwing.coreservice.wy.core.mq.cmac;

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

@Service("cmacBillingByProjectListener")
public class CmacBillingByProjectListener extends ServiceRrcs implements MessageListener{

	private static final Logger logger = LogManager.getLogger(CmacBillingByProjectListener.class);
	
	@Override
	public void onMessage(Message message) {

		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("通用账户扣取消息队列: 消息格式转换失败. 数据:{}.", JSONObject.toJSONString(message));
		}
		
		if(null != jsonStr){
			
			MqEntity e = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			if(null != e){
				
				TBsProject project = ((JSONObject)e.getData()).toJavaObject(TBsProject.class);
				String companyId = e.getCompanyId();
				
				this.cmacBillingService.billing(companyId,project);
				
			}
		}
		
		
	}

}
