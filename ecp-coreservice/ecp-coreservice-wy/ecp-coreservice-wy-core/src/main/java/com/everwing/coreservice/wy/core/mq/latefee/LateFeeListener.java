package com.everwing.coreservice.wy.core.mq.latefee;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Service("lateFeeListener")
public class LateFeeListener extends ServiceRrcs implements MessageListener{

	private static final Logger logger = LogManager.getLogger(LateFeeListener.class);
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Value("${queue.wy2wy.latefee.billing.project.request.key}")
	private String route_key_lateFee_billing_by_project_id;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onMessage(Message message) {

		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("违约金计费监听任务: 消息转换失败. ");
		}
		
		
		if(null != jsonStr && !jsonStr.isEmpty()){
			MqEntity e = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			
			//获取本物业公司下所有本月的项目
			TBsProject paramObj = new TBsProject();
			paramObj.setBillingTime(new Date());
			paramObj.setStatus(0);
			BaseDto returnDto = this.tBsProjectService.findByObj(e.getCompanyId(),paramObj); 
			if(returnDto == null || CommonUtils.isEmpty(returnDto.getLstDto())){
				logger.warn("违约金计费监听任务: 未找到可计算违约金的项目, 计费完成. ");
				return;
			}
			
			List<TBsProject> projects = returnDto.getLstDto();
			
			for(TBsProject project : projects){
				MqEntity entity = new MqEntity();
				entity.setCompanyId(e.getCompanyId());
				entity.setData(project);
				this.amqpTemplate.convertAndSend(route_key_lateFee_billing_by_project_id,entity);
				
			}
		}
	}

	
	
}
