package com.everwing.coreservice.wy.fee.core.mq;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.fee.service.AcAccountService;
import com.rabbitmq.client.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 为老账户系统批量充值提供同步数据到新账户系统的监听
 * @author qhc
 * @create 2018/6/8
 */
public class AcChargeDetailBatchRechargeListener implements ChannelAwareMessageListener {

    private static final Logger logger= LogManager.getLogger(AcChargeDetailBatchRechargeListener.class);

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private AcAccountService acAccountService;

    @SuppressWarnings("unchecked")
	@Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        Object o = null;
        try {
            o = messageConverter.fromMessage(message);
        } catch (MessageConversionException e) {
            logger.error("批量充值 操作当月收费明细修改mq消息转换异常,确认消息");
            channel.basicAck(deliveryTag, false);
            return;
        }
        if (o == null) {
            logger.info("批量充值 操作 当月收费明细修改mq消息为空，确认消息");
            channel.basicAck(deliveryTag, false);
        }
        try {
            Map<String, Object> paramMap = JSON.parseObject(o.toString());
            if (CommonUtils.isEmpty(paramMap)) {
                logger.info("批量充值 操作 当月收费明细修改mq消息参数信息为空，确认消息");
                channel.basicAck(deliveryTag, false);
            }
            String companyId = (String) paramMap.get("companyId");
            List<String> houseCodeNews = (List<String>) paramMap.get("houseCodes");
            Map<String, String> amountMap = (Map<String, String>) paramMap.get("amountMap");
            String operateId = (String) paramMap.get("operateId");
            String batchNo = (String) paramMap.get("batchNo");
            int payType = (Integer) paramMap.get("payType");

            if (CommonUtils.isEmpty(companyId) || CommonUtils.isEmpty(houseCodeNews) || CommonUtils.isEmpty(amountMap)) {
                logger.info("批量充值 操作 当月收费明细修改mq消息参数信息为空，确认消息");
                channel.basicAck(deliveryTag, false);
            }
            acAccountService.batchRechargeToNewAccount(companyId, operateId, houseCodeNews, amountMap, null, batchNo,payType);

            logger.info("批量充值 操作 当月收费明细修改mq消息消费完成");
            channel.basicAck(deliveryTag, false);
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("批量充值 操作 当月收费明细修改出现异常：{},进入死信队列，数据:{}",e,o.toString());
            channel.basicNack(deliveryTag,false,false);
        }
    }
}
