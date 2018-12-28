package com.everwing.coreservice.admin.core.mq;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.admin.entity.cust.PersonCust;
import com.everwing.coreservice.common.constant.MqConstants;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.RabbitMQEnum;
import com.everwing.coreservice.platform.dao.mapper.cust.PersonCustExtraMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 * @author DELL
 *
 */
@Service("personCustListener")
public class PersonCustListener implements MessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(PersonCustListener.class);
	
	@Autowired
	private PersonCustExtraMapper personCustMapper;
	
	@Override
	public void onMessage(Message message) {
		try {
			JSONObject jsonObject = (JSONObject) JSONObject.parse(new String(message.getBody(), "UTF-8"));

			if (null == jsonObject) {
				LOG.info("消息队列: 传入个人客户数据为空: {}", jsonObject);
				return;
			}


			String op = jsonObject.get(RabbitMQEnum.opt.name()).toString();
			JSONObject dataJsonObject = jsonObject.getJSONObject(RabbitMQEnum.data.name());
			if(RabbitMQEnum.insert.name().equals(op)){
				this.add(dataJsonObject);	//插入方法
				LOG.info(CommonUtils.log("消息队列: 插入单条个人客户数据成功. 数据: " + dataJsonObject.toJSONString() ));
				
			}else if(RabbitMQEnum.del.name().equals(op)){
				this.del(dataJsonObject);	//删除
				LOG.info(CommonUtils.log("消息队列: 删除单条个人客户数据成功. 数据: " + dataJsonObject.toJSONString() ));
				
			}else if(RabbitMQEnum.modify.name().equals(op)){
				this.mod(dataJsonObject);	//修改
				LOG.info(CommonUtils.log("消息队列: 修改单条个人客户数据成功. 数据: " + dataJsonObject.toJSONString() ));

			}else if(RabbitMQEnum.insert.name().equals(op)){
				this.batchAdd(dataJsonObject);	//批量插入
				LOG.info(CommonUtils.log("消息队列: 批量新增个人客户数据成功. 数据: " + dataJsonObject.toJSONString() ));

			}else if(MqConstants.PERSON_CUST_BATCH_DEL.equals(op)){
				this.batchDel(dataJsonObject);	//批量删除
				LOG.info(CommonUtils.log("消息队列: 批量删除个人客户数据成功. 数据: " + dataJsonObject.toJSONString() ));
			}
		} catch (Exception e) {
			LOG.error("消息队列: 传递个人客户消息, 出现异常:{},参数:{}",e);
		}
	}

	
	
	private int add(JSONObject obj){
		PersonCust cust = JSONObject.toJavaObject(obj, PersonCust.class);
		return this.personCustMapper.insert(cust);
	}
	
	private int del(JSONObject obj){
		return this.personCustMapper.delete(JSONObject.toJavaObject(obj, PersonCust.class));
	}
	
	private int mod(JSONObject obj){
		return this.personCustMapper.update(JSONObject.toJavaObject(obj, PersonCust.class));
	}
	
	private int batchAdd(JSONObject obj){
		return this.personCustMapper.batchInsert(JSONObject.parseArray(obj.toJSONString(), PersonCust.class));
	}
	
	private int batchDel(JSONObject obj){
		List<String> ids = JSONObject.parseArray(obj.toJSONString(), String.class);
		return this.personCustMapper.batchDelete(ids);
	}
	
	
}
