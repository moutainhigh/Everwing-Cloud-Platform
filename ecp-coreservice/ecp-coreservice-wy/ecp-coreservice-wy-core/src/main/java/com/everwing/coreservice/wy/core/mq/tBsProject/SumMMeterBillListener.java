package com.everwing.coreservice.wy.core.mq.tBsProject;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.service.configuration.project.SumMmeterBillToProjectService;
import com.everwing.coreservice.wy.core.resourceDI.ServiceRrcs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service("sumMMeterBillListener")
public class SumMMeterBillListener extends ServiceRrcs implements MessageListener{

	private static final Logger logger = LogManager.getLogger(SumMMeterBillListener.class);
	@Autowired
	private SumMmeterBillToProjectService sumMmeterBillService;
	@Override
	public void onMessage(Message message) {
		String jsonStr = null;
		try {
			jsonStr = new String(message.getBody(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("未审核完成的费用总单进行虚拟表费用汇总监听: 消息格式转换失败. 数据: {}" , message);
		}
		if(jsonStr != null){
			MqEntity e = JSONObject.parseObject(jsonStr, MqEntity.class);
			if(CommonUtils.isNotEmpty(e)){
				logger.info("未审核完成的费用总单进行虚拟表费用汇总监听: 开始汇总M虚拟表的费用. 接收消息: {}", e.toString());
				TBsChargeBillTotal total = ((JSONObject)e.getData()).toJavaObject(TBsChargeBillTotal.class);
				BaseDto baseDto = this.sumMmeterBillService.sumTotalBill(e.getCompanyId(), total);
				logger.info("未审核完成的费用总单进行虚拟表费用汇总监听: 汇总M虚拟表的费用完成. 返回消息: {}", baseDto.getMessageMap().toString());
			}
		}
	}

}
