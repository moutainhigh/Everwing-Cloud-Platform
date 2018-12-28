package com.everwing.coreservice.wy.core.mq.collection;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


@Service("collectionListener")
public class CollectionListener extends ServiceRrcs implements MessageListener{

	private static final Logger logger = LogManager.getLogger(CollectionListener.class);
	
	
	@Override
	public void onMessage(Message message) {

		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("监听队列: [托收数据生成]  消息转换失败.");
			e.printStackTrace();
		}
		
		if(null != jsonStr){
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			if(null != entity && null != entity.getData()){
				TBcProject project = ((JSONObject)entity.getData()).toJavaObject(TBcProject.class);
				String totalId = entity.getSupAttr();
				this.tBcCollectionService.genColl(entity.getCompanyId(),project,totalId,entity.getOpr());  
				
			}else{
				
				logger.warn("监听队列: [托收数据生成]  消息数据为空. 数据:{}.", entity.toString());
				
			}
			
		}
		
	}

}
