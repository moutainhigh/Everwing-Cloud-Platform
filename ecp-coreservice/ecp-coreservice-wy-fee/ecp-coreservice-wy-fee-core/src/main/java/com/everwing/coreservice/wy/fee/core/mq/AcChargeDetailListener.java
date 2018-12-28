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
 * 收费
 *
 * @author DELL shiny
 * @create 2018/6/8
 */
public class AcChargeDetailListener implements ChannelAwareMessageListener {

    private static final Logger logger= LogManager.getLogger(AcChargeDetailListener.class);

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
            logger.error("当月收费mq消息转换异常,确认消息");
            channel.basicReject(deliveryTag, false);
//            channel.basicAck(deliveryTag, false);
            return;
        }
        if (o == null) {
            logger.info("当月收费mq消息为空，确认消息");
//            channel.basicAck(deliveryTag, false);
            channel.basicReject(deliveryTag, false);
        }
        try {
            List<AcChargeDetailDto> list = JSON.parseArray(o.toString(), AcChargeDetailDto.class);
            for(AcChargeDetailDto acChargeDetailDto:list){
                acAccountService.addChargeDetail(acChargeDetailDto.getCompanyId(),acChargeDetailDto.getProjectId(),
                        acChargeDetailDto.getProjectName(),acChargeDetailDto.getHouseCodeNew(),acChargeDetailDto.getAuditTime(),
                        acChargeDetailDto.getChargeAmount(),acChargeDetailDto.getChargingType(),acChargeDetailDto.getPayChannel(),acChargeDetailDto.getChargeTime(),
                        acChargeDetailDto.getLastChargeId(),acChargeDetailDto.getChargeDetail(),acChargeDetailDto.getCommonDiKou(),
                        acChargeDetailDto.getSpecialDiKou(),acChargeDetailDto.getPayedAmount(),acChargeDetailDto.getAssignAmount(),
                        acChargeDetailDto.getPayableAmount(),acChargeDetailDto.getCurrentArreas(),acChargeDetailDto.getOperationDetailId(),
                        acChargeDetailDto.getOperator(),acChargeDetailDto.getChargingMoth(),acChargeDetailDto.getBusinessTypeEnum());
            }
            logger.info("当月收费mq消息消费完成");
            channel.basicAck(deliveryTag,false);
//            channel.basicRecover();
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("当月收费出现异常：{},进入死信队列，数据:{}",e,o.toString());
//            channel.basicReject(deliveryTag, false);
            channel.basicNack(deliveryTag,false,false);
        }
    }
}
