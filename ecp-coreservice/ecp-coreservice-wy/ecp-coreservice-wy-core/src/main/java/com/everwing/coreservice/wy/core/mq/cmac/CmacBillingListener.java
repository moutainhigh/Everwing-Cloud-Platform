package com.everwing.coreservice.wy.core.mq.cmac;

import com.alibaba.fastjson.JSONObject;
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
import java.util.List;

@Service("cmacBillingListener")
public class CmacBillingListener extends ServiceRrcs implements MessageListener{

	private static final Logger logger = LogManager.getLogger(CmacBillingListener.class);
	
	@Value("${queue.wy2wy.cmac.billing.project.request.key}")
	private String route_key_cmac_billing_by_project;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
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
			
			if(null != e.getCompanyId()){
				
				
				List<TBsProject> projects = this.tBsProjectService.findCanBillingCmacProject(e.getCompanyId());
				
				if(CommonUtils.isEmpty(projects)){
					logger.warn("通用账户扣取消息队列: 物业公司id: {}, 当前月份下未找到可供扣费的项目. ", e.getCompanyId());
					return;
				}
				
				//按项目起消息队列
				for(TBsProject project : projects){
					MqEntity me = new MqEntity();
					me.setData(project);
					me.setCompanyId(e.getCompanyId());
					this.amqpTemplate.convertAndSend(route_key_cmac_billing_by_project, me);
				}
			}
		}
	}
}
