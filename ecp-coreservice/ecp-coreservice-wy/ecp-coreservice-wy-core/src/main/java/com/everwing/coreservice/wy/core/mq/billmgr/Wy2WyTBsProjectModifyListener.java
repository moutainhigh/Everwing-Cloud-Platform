package com.everwing.coreservice.wy.core.mq.billmgr;

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

@Service("wy2WyTBsProjectModifyListener")
public class Wy2WyTBsProjectModifyListener extends ServiceRrcs implements MessageListener{

	
	private static final Logger logger = LogManager.getLogger(Wy2WyTBsProjectModifyListener.class);
	
	@Override
	public void onMessage(Message message) {
		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("监听队列: [账单上传等待, 项目状态修改队列]  插入消息转换格式失败. ");
		}
		
		
		
		if(null != jsonStr){
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			if(entity.getData() != null){
				
				TBsProject project = ((JSONObject)entity.getData()).toJavaObject(TBsProject.class);
				
				if(null != project){
					this.tBsProjectService.update(entity.getCompanyId(), project);
				}
				
			}else{
				logger.warn("监听队列: [账单上传等待, 项目状态修改队列]  传入数据为空,无法消费此消息. 数据:{}",entity.toString());
			}
			
		}
	}

}
