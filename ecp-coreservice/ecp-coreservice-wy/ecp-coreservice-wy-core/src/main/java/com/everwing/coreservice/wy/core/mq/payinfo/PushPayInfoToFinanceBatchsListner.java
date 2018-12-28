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
 * @describe 定时任务推送业主交费信息到财务，分批进行处理
 * @author QHC
 * @date 2017-10-27 09:00:00
 */
@Service("pushPayInfoToFinanceBatchsListner")
public class PushPayInfoToFinanceBatchsListner  implements MessageListener{

	
	private static final Logger logger = Logger.getLogger(PushPayInfoToFinanceBatchsListner.class);
	
	@Autowired
	private PushPayInfoToFinanceService pushPayInfoToFinanceService;
	

	@Override
	public void onMessage(Message message) {
		
		//String encryptedData,String companyId,String projectId
		
		
		String jsonStr = null;
		logger.info("*********推送业主交费信息到财务的消息队列，分批进行 start **********");
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if(null != jsonStr){
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			String projectId = entity.getProjectId(); //项目编号
			String encryptedData = entity.getData().toString();  //推送数据
			String companyId = entity.getCompanyId();//公司编码
			logger.info("*******执行分批推送start******");
			this.pushPayInfoToFinanceService.pushDataToWC(companyId,encryptedData,projectId);
			logger.info("*******执行分批推送end******");
			
		}else {
		}
		
	}
}
