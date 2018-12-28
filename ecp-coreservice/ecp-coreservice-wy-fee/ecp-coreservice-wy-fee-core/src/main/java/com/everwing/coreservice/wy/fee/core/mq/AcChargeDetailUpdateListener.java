package com.everwing.coreservice.wy.fee.core.mq;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.wy.fee.dto.AcChargeDetailDto;
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

/**
 * 专项账户抵扣或者是通用账户抵扣需要做的对新账户的操作
 *
 * @author qhc
 * @create 2018/6/8
 */
public class AcChargeDetailUpdateListener implements ChannelAwareMessageListener {

    private static final Logger logger= LogManager.getLogger(AcChargeDetailUpdateListener.class);

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private AcAccountService acAccountService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        Object o = null;
        try {
            o = messageConverter.fromMessage(message);
        } catch (MessageConversionException e) {
            logger.error("专项通用抵扣mq消息转换异常,确认消息");
            channel.basicAck(deliveryTag, false);
            return;
        }
        if (o == null) {
            logger.info("专项通用抵扣mq消息为空，确认消息");
            channel.basicAck(deliveryTag, false);
        }
        try {
            List<AcChargeDetailDto> list = JSON.parseArray(o.toString(), AcChargeDetailDto.class);

            for(AcChargeDetailDto acChargeDetailDto:list){
                acAccountService.updateAcCurrentChargeDetail( acChargeDetailDto.getCompanyId(), acChargeDetailDto );
            }
            logger.info("专项通用抵扣mq消息消费完成");
            channel.basicAck(deliveryTag,false);
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("专项通用抵扣出现异常：{},进入死信队列，数据:{}",e,o.toString());
            channel.basicNack(deliveryTag,false,false);
        }
    }
}
