package com.everwing.coreservice.wy.fee.core.mq;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.wy.fee.dto.AcBusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.service.AcBusinessOperaService;
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
 * @author DELL shiny
 * @create 2018/8/14
 */
public class AcBusinessOperaListener implements ChannelAwareMessageListener {

    private static final Logger logger= LogManager.getLogger(AcBusinessOperaListener.class);

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private AcBusinessOperaService businessOperaService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Object obj=null;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            obj=messageConverter.fromMessage(message);
        } catch (MessageConversionException e) {
            logger.error("资金业务明细消息转换异常.", e);
            //直接发送ack无需处理
            channel.basicAck(deliveryTag, false);
            return;
        }
        if (obj==null){
            logger.error("收到空消息,丢弃");
            channel.basicAck(deliveryTag,false);
            return;
        }
        try {
            List<AcBusinessOperaDetailDto> businessOperaDtoList=JSON.parseArray(obj.toString(), AcBusinessOperaDetailDto.class);
            for(AcBusinessOperaDetailDto businessOperaDto:businessOperaDtoList){
                businessOperaService.addOperaDetail(businessOperaDto.getCompanyId(),businessOperaDto);
            }
            logger.info("资金业务明细消息消费成功,发送ack");
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加资金业务明细出现异常{}，进入死信队列数据{}",e, obj.toString());
            channel.basicNack(deliveryTag,false,false);
        }
    }
}
