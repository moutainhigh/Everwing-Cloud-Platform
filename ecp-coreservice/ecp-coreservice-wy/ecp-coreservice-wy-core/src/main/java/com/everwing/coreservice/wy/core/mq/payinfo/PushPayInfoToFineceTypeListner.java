package com.everwing.coreservice.wy.core.mq.payinfo;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.wy.service.configuration.payinfo.PushPayInfoToFinanceService;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * @describe 定时任务推送业主交费信息到财务，按推送数据类型
 * 				在这里进行消费
 * @author QHC
 * @date 2017-10-27 09:00:00
 */
@Service("pushPayInfoToFineceTypeListner")
public class PushPayInfoToFineceTypeListner  implements MessageListener{

	
	private static final Logger logger = Logger.getLogger(PushPayInfoToFineceTypeListner.class);
	
	@Autowired
	private PushPayInfoToFinanceService pushPayInfoToFinanceService;
	

	@Override
	public void onMessage(Message message) {
		
		String jsonStr = null;
		logger.info("*********推送业主交费信息到财务的消息队列，按数据类型进行消费 start **********");
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if(null != jsonStr){
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			String projectId=entity.getProjectId();//包含有类型信息
			String proId="";
			String proName="";
			String type="";
			if(projectId.contains(",")) {
				proId=projectId.split(",")[0];
				type=projectId.split(",")[1];
				proName=projectId.split(",")[2];
			}
			String status = String.valueOf( entity.getData() );
			this.pushPayInfoToFinanceService.doPushPayInfoByType(entity.getCompanyId(),proId,type,proName,status);
				
			logger.info("*********推送业主交费信息到财务的消息队列，按数据类型消费  数据类型："+entity.getProjectId()+" end **********");
			
		}else {
			logger.info("*********推送业主交费信息到财务的消息队列，未找到可用信息  end **********");
		}
		
	}
}
