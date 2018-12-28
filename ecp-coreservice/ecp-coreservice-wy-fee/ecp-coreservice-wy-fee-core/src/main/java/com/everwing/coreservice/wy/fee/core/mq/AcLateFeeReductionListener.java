package com.everwing.coreservice.wy.fee.core.mq;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.fee.service.AcAccountLateFeeService;
import com.rabbitmq.client.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 这里处理老系统中进行减免操作的时候对新版账户数据的同步操作
 *
 * @author qhc
 * @create 2018/6/8
 */
public class AcLateFeeReductionListener implements ChannelAwareMessageListener {

    private static final Logger logger= LogManager.getLogger(AcLateFeeReductionListener.class);

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private AcAccountLateFeeService acAccountLateFeeService;

    @SuppressWarnings("unchecked")
	@Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        Object o = null;
        try {
            o = messageConverter.fromMessage(message);
        } catch (MessageConversionException e) {
            logger.error("减免操作mq消息转换异常,确认消息");
            channel.basicAck(deliveryTag, false);
            return;
        }
        if (o == null) {
            logger.info("减免操作mq消息为空，确认消息");
            channel.basicAck(deliveryTag, false);
        }
        try {
        	
        	 Map<String, Object> paramMap = JSON.parseObject( o.toString() );
             if(CommonUtils.isEmpty( paramMap )) {
             	 logger.info("批量充值 操作 当月收费明细修改mq消息参数信息为空，确认消息");
                  channel.basicAck(deliveryTag, false);
             }
             String companyId = (String) paramMap.get("companyId");
             String houseCodeNew =  (String) paramMap.get("houseCodeNew");
             String oprId =  (String) paramMap.get("oprId");
             String projectId =  (String) paramMap.get("projectId");
             String projectName = (String) paramMap.get("projectName");
             String oprDetailId = (String) paramMap.get("oprDetailId");
             
             Map<String, String> amountMap = (Map<String, String>) paramMap.get("amountMap");
        	
             acAccountLateFeeService.addLateFeeInfoForReduction(companyId, amountMap, houseCodeNew,projectId,projectName,oprDetailId,oprId);

            logger.info("减免操作mq消息消费完成");
            channel.basicAck(deliveryTag,false);
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("减免操作出现异常：{},进入死信队列，数据:{}",e,o.toString());
            channel.basicNack(deliveryTag,false,false);
        }
    }
}
