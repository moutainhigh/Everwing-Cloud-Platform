package com.everwing.coreservice.wy.core.mq.collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.wy.common.enums.CollectionEnum;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.jrl.TBcJrlBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.jrl.TBcJrlHead;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.back.TBcUnionBackBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.back.TBcUnionBackHead;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;


@Service("backListener")
public class BackListener extends ServiceRrcs implements MessageListener{

	
	private static final Logger logger = LogManager.getLogger(BackListener.class);
	
	
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
			
			if(me.getOpr() == CollectionEnum.type_union.getV()){
				//银联
				TBcUnionBackHead head = JSON.parseObject(me.getSupAttr(), TBcUnionBackHead.class);
				List<TBcUnionBackBody> bodies = ((JSONArray) me.getData()).toJavaList(TBcUnionBackBody.class);
				this.tBcCollectionService.backUnionData2Account(me.getCompanyId(), head, bodies);
				
			}else if(me.getOpr() == CollectionEnum.type_jrl.getV()){
				//金融联
				TBcJrlHead head = JSON.parseObject(me.getSupAttr(), TBcJrlHead.class);
				List<TBcJrlBody> bodies = ((JSONArray) me.getData()).toJavaList(TBcJrlBody.class);
				this.tBcCollectionService.backJrlData2Account(me.getCompanyId(), head, bodies);
				
			}
			
			
			
			
			
			
		}
		
		
		
		
		
	}

	
	
	
}
