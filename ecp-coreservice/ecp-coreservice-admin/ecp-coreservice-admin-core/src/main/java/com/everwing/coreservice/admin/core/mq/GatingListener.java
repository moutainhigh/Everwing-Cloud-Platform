package com.everwing.coreservice.admin.core.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.constant.MqConstants;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.gating.Gating;
import com.everwing.coreservice.platform.dao.mapper.gating.GatingExtraMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


@Service("gatingListener")
public class GatingListener implements MessageListener{

	
	private static final Logger LOG = LoggerFactory.getLogger(GatingListener.class);

	@Autowired
	private GatingExtraMapper gatingExtraMapper;
	
	@Override
	public void onMessage(Message message) {

		MqEntity entity = null;
		try {
			entity = JSON.parseObject(new String(message.getBody(),"UTF-8"),MqEntity.class);
			
			if(null == entity){
				LOG.info(CommonUtils.log("消息队列: 传入门控数据为空."));
				return;
			}

			if(entity.getOpr() == MqConstants.OPR_ADD){
				this.gatingExtraMapper.addGating(((JSONObject)entity.getData()).toJavaObject(Gating.class));
				LOG.info(CommonUtils.log("消息队列: 插入单条门控机数据 -> 平台  完成. 数据: " + JSON.toJSONString(entity.getData()) ));
				
			}else if(entity.getOpr() == MqConstants.OPR_UPDATE){
				this.gatingExtraMapper.updateGating(((JSONObject)entity.getData()).toJavaObject(Gating.class));
				LOG.info(CommonUtils.log("消息队列: 修改单条门控机数据 -> 平台  完成. 数据: " + JSON.toJSONString(entity.getData()) ));

			}else if(entity.getOpr() == MqConstants.OPR_DEL){
				this.gatingExtraMapper.delGating(((JSONObject)entity.getData()).toJavaObject(Gating.class));
				LOG.info(CommonUtils.log("消息队列: 删除单条门控机数据 -> 平台  完成. 数据: " + JSON.toJSONString(entity.getData()) ));

			}else if(entity.getOpr() == MqConstants.OPR_SCH){
				
			}else if(entity.getOpr() == MqConstants.OPR_BATCH_ADD){
				JSONArray arr = (JSONArray) entity.getData();
				this.gatingExtraMapper.batchAdd(arr.toJavaList(Gating.class));
				LOG.info(CommonUtils.log("消息队列: 批量新增单条门控机数据 -> 平台  完成. 数据: " + JSON.toJSONString(entity.getData()) ));

			}else if(entity.getOpr() == MqConstants.OPR_BATCH_DEL){
				JSONArray arr = (JSONArray) entity.getData();
				this.gatingExtraMapper.batchDel(arr.toJavaList(String.class));
				LOG.info(CommonUtils.log("消息队列: 批量删除单条门控机数据 -> 平台  完成. 数据: " + JSON.toJSONString(entity.getData()) ));
			}
			
		} catch (UnsupportedEncodingException e) {
			LOG.error(CommonUtils.log("消息队列: 传递门控数据, 出现异常. 数据 : " + JSON.toJSONString(entity.getData()) + "异常 : " + e.getMessage()));
		} catch(Exception e){
			LOG.error(CommonUtils.log("消息队列: 传递门控数据, 出现异常. 数据 : " + JSON.toJSONString(entity.getData()) + "异常 : " + e.getMessage()));
		}
	}
	
	
	
	
}
