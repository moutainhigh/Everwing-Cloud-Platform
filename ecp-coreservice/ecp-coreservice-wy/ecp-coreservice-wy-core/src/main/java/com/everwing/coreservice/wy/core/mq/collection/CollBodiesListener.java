package com.everwing.coreservice.wy.core.mq.collection;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.jrl.TBcJrlBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.TBcUnionCollectionBody;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("collBodiesListener")
public class CollBodiesListener extends ServiceRrcs implements MessageListener{

	private static final Logger logger = LogManager.getLogger(CollBodiesListener.class);
	
	
	@Override
	public void onMessage(Message message) {

		String jsonStr = null;
		
		try {
			jsonStr = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("监听队列: [托收子文件数据批量插入]  消息转换失败.");
			e.printStackTrace();
		}
		
		if(null != jsonStr){
			MqEntity entity = JSONObject.parseObject(jsonStr, MqEntity.class);
			
			if(null != entity && null != entity.getData()){
				JSONArray array = (JSONArray) entity.getData();
				if(array.size() > 0){
					if(CommonUtils.isEquals(TBcUnionCollectionBody.class.getName(), entity.getSupAttr())){
						this.tBcCollectionService.batchInsertBodies(entity.getCompanyId(), array.toJavaList(TBcUnionCollectionBody.class));
					}else if(CommonUtils.isEquals(TBcJrlBody.class.getName(), entity.getSupAttr())){
						this.tBcCollectionService.batchInsertBodies(entity.getCompanyId(), array.toJavaList(TBcJrlBody.class));
					}
				}
			}else{
				
				logger.warn("监听队列: [托收子文件数据批量插入]  消息数据为空. 数据:{}.", entity.toString());
				
			}
			
		}
		
	}



}
