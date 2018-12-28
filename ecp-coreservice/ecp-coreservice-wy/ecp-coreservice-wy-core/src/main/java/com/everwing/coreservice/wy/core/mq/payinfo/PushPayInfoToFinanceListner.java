package com.everwing.coreservice.wy.core.mq.payinfo;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.service.configuration.payinfo.PushPayInfoToFinanceService;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * @describe 定时任务推送业主交费信息到财务，会按公司起消息队列
 * 				在这里进行消费，按项目处理掉
 * @author QHC
 * @date 2017-10-27 09:00:00
 */
@Service("pushPayInfoToFinanceListner")
public class PushPayInfoToFinanceListner  implements MessageListener{

	
	private static final Logger logger = Logger.getLogger(PushPayInfoToFinanceListner.class);
	
	@Autowired
	private PushPayInfoToFinanceService pushPayInfoToFinanceService;
	

	@Override
	public void onMessage(Message message) {
		
		String jsonStr = null;
		logger.info("*********推送业主交费信息到财务的消息队列，按项目级别进行消费 start **********");
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if(null != jsonStr){
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			String projectId = entity.getProjectId();//这里包含了code和名称
			String proId="";
			String proName="";
			if( CommonUtils.isNotEmpty(projectId) ) {
				proId = projectId.split(",")[0];
				proName=projectId.split(",")[1];
			}
			String status = String.valueOf( entity.getData() );//WC结束标示
			this.pushPayInfoToFinanceService.doPushPayInfoToFinance(entity.getCompanyId(),proId,proName,status);
				
			logger.info("*********推送业主交费信息到财务的消息队列，按公司级别进行消费  项目id："+entity.getProjectId()+" end **********");
			
		}else {
			logger.info("*********推送业主交费信息到财务的消息队列，未找到可用信息  end **********");
		}
		
	}
}
