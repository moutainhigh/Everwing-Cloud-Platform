package com.everwing.coreservice.wy.core.mq.tcreadingtask;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("produceNextReadTaskListener")
public class ProduceNextReadTaskListener extends ServiceRrcs implements MessageListener{

	private static final Logger logger = LogManager.getLogger(ProduceNextReadTaskListener.class);
	@Override
	public void onMessage(Message message) {
		String jsonStr = null;
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("抄表任务审核产生下一次抄表任务消息队列: 监听到消息格式转换失败. 数据: {}",message);
		}
		if(null != jsonStr){
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			if(null != entity){
				logger.info("抄表任务审核产生下一次抄表任务消息队列监听 :  [wy2Wy产生下一批抄表任务: 开始处理消息. 数据: {}",jsonStr);
				Object obj = entity.getData();
				if(CommonUtils.isNotEmpty(obj)){
					TcReadingTask task = ((JSONObject)entity.getData()).toJavaObject(TcReadingTask.class);
					this.meterDataTaskService.productNextReadingTask(entity.getCompanyId(), task);
				}
			}
		}
		
	}

	
}
