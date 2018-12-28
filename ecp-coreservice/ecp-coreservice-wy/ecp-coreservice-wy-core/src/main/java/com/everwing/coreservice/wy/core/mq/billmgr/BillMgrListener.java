package com.everwing.coreservice.wy.core.mq.billmgr;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("billMgrListener")
public class BillMgrListener extends ServiceRrcs implements MessageListener{

	private static final Logger logger = LogManager.getLogger(BillMgrListener.class);
	
	
	@Override
	public void onMessage(Message message) {
		
		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("监听队列: [账单生成队列]  插入消息转换格式失败. ");
		}
		
		
		
		if(null != jsonStr){
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			
			if(entity.getData() != null){
				
				if(BillingEnum.mq_gen_bill_manaul_first.getIntV() == entity.getOpr()){
					logger.info("第一次生产账单!");
					//第一次生成账单
					this.billMgrService.genBillFirstByManaul(entity.getCompanyId(), ((JSONObject)entity.getData()).toJavaObject(TBsProject.class)); 
					
				}else if(BillingEnum.mq_gen_bill_manaul_regen.getIntV() == entity.getOpr()){
					logger.info("重新生成账单!");
					//重新生成账单
					this.billMgrService.reGenBillByManaul(entity.getCompanyId(), ((JSONObject)entity.getData()).toJavaObject(TBsProject.class)); 
					
				}else if(BillingEnum.mq_gen_bill_auto.getIntV() == entity.getOpr()){
					logger.info("自动生产账单!");
					//自动生成账单
					this.billMgrService.autoGenBill(entity.getCompanyId(), ((JSONObject)entity.getData()).toJavaObject(TBsProject.class)); 
				}
				
				
				
				
				
				
			}else{
				logger.warn("监听队列: [账单生成队列]  传入数据为空,无法消费此消息. 数据:{}",entity.toString());
			}
			
			
		}
		
		
		
	}

	
	
	
}
