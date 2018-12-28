package com.everwing.coreservice.wy.core.mq.collection;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("wy2WyBackFileImportListener")
public class BackFileImportListener extends ServiceRrcs implements MessageListener{

	private static final Logger logger = LogManager.getLogger(BackFileImportListener.class);
	
	@Override
	public void onMessage(Message message) {
		
		String jsonStr = null;
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("监听队列: [回盘数据计入各账户], 数据格式转换失败.");
			e.printStackTrace();
		}
		
		if(jsonStr != null){
			
			MqEntity me = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			if(me == null || me.getData() == null){
				logger.warn("监听队列: [回盘数据计入各账户], 传入消息数据为空, 执行完成.");
				return;
			}
			
			String fileContent = (String) me.getData();
			String fileName = me.getSupAttr();
			String projectId = me.getProjectId();
			String userId = me.getProjectId();
			String totalId = me.getProjectName();
			String companyId = me.getCompanyId();
			Integer flag = me.getOpr();
			
			this.tBcCollectionService.importFile(companyId, fileContent, fileName, flag, totalId, projectId, userId);
			
		}
		
		
		
		
		
	}

}
