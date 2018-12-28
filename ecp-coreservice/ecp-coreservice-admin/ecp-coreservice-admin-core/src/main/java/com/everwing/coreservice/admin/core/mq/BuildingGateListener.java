package com.everwing.coreservice.admin.core.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.constant.MqConstants;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.gating.BuildingGate;
import com.everwing.coreservice.platform.dao.mapper.gating.buildinggating.BuildingGateExtraMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("buildingGateListener")
public class BuildingGateListener implements MessageListener{

	private static final Logger LOG = LoggerFactory.getLogger(BuildingGateListener.class);
	
	@Autowired
	private BuildingGateExtraMapper buildingGateExtraMapper;
	
	
	@Override
	public void onMessage(Message message) {
		
		try {
			MqEntity entity = JSON.parseObject(new String(message.getBody(),"UTF-8"), MqEntity.class);
			
			if(null == entity){
				LOG.info(CommonUtils.log("消息队列: 传入门控房屋绑定数据为空."));
				return;
			}
			
			
			if(entity.getOpr() == MqConstants.OPR_BATCH_ADD){
				this.buildingGateExtraMapper.batchAdd(((JSONArray) entity.getData()).toJavaList(BuildingGate.class));
				LOG.info(CommonUtils.log("消息队列: 批量插入门控机/建筑关联关系 -> 平台  完成. " + entity.toString()));
				
			}else if(entity.getOpr() == MqConstants.OPR_DEL){
				this.buildingGateExtraMapper.delete(((JSONObject)entity.getData()).getString("gatingCode"));
				LOG.info(CommonUtils.log("消息队列: 删除门控机/建筑关联关系 -> 平台  完成. " + entity.toString()));
			}
			
			
		} catch (UnsupportedEncodingException e) {
			LOG.error(CommonUtils.log("消息队列: 传递门控-房屋绑定数据, 出现异常 : " + e.getMessage()));
		}
		
		
		
	}

}
