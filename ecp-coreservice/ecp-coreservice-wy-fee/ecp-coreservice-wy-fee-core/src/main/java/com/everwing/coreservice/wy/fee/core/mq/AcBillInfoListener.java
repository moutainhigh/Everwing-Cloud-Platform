package com.everwing.coreservice.wy.fee.core.mq;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.wy.fee.dto.AcBillDetailDto;
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
 * 账单
 *
 * @author DELL shiny
 * @create 2018/6/8
 */
public class AcBillInfoListener implements ChannelAwareMessageListener {

    private static final Logger logger= LogManager.getLogger(AcBillInfoListener.class);

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private AcAccountService acAccountService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Object obj=null;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            obj=messageConverter.fromMessage(message);
        } catch (MessageConversionException e) {
            logger.error("convert bill info MQ message error.", e);
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
            List<AcBillDetailDto> list = JSON.parseArray(obj.toString(), AcBillDetailDto.class);
            for (AcBillDetailDto acBillDetailDto : list) {
                acAccountService.addBillDetail(acBillDetailDto.getCompanyId(), acBillDetailDto.getCreateTime(),
                        acBillDetailDto.getBillMonth(), acBillDetailDto.getBillState(), acBillDetailDto.getBillDetail(),
                        acBillDetailDto.getHouseCodeNew(), acBillDetailDto.getBillAmount(), acBillDetailDto.getBillPayer(),
                        acBillDetailDto.getBillAddress(), acBillDetailDto.getBillInvalid(), acBillDetailDto.getCompanyId(),
                        acBillDetailDto.getProjectName(), acBillDetailDto.getPayState());
            }
            logger.info("账单明细消息消费成功,发送ack");
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加账单明细出现异常{}，进入死信队列数据{}",e, obj.toString());
            channel.basicNack(deliveryTag,false,false);
        }
    }
}
