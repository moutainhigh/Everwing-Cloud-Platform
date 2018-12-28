package com.everwing.coreservice.wy.fee.core.mq;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.wy.fee.dto.AcLastBillFeeDto;
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
 * 上月欠费
 * @author DELL shiny
 * @create 2018/6/8
 */
public class AcLastBillFeeListener implements ChannelAwareMessageListener {

    private static final Logger logger= LogManager.getLogger(AcLastBillFeeListener.class);

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private AcAccountService acAccountService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Object o=null;
        long deliveryTag=message.getMessageProperties().getDeliveryTag();
        try {
            o=messageConverter.fromMessage(message);
        } catch (MessageConversionException e) {
            logger.error("上月欠费mq消息转换异常");
            channel.basicAck(deliveryTag,false);
        }
        if(o==null){
            logger.error("上月欠费mq消息为空");
            channel.basicAck(deliveryTag,false);
        }
        try {
            List<AcLastBillFeeDto> list = JSON.parseArray(o.toString(), AcLastBillFeeDto.class);
            for(AcLastBillFeeDto lastBillFeeDto:list){
                acAccountService.addAcLastBillInfo(lastBillFeeDto.getCompanyId(),lastBillFeeDto.getProjectId(),
                        lastBillFeeDto.getProjectName(),lastBillFeeDto.getHouseCodeNew(),lastBillFeeDto.getLastBillFee(),
                        lastBillFeeDto.getChargingType(),lastBillFeeDto.getOperator());
            }
            logger.info("上月欠费mq消息消费完成");
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上月欠费mq消息异常{},进入死信队列数据:{}",e,o.toString());
            channel.basicNack(deliveryTag,false,false);
        }
    }
}
