package com.everwing.coreservice.wy.fee.core.mq;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.wy.fee.dto.AcSpecialDetailDto;
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
 * 专项抵扣
 *
 * @author DELL shiny
 * @create 2018/6/8
 */
public class AcSpecialDetailListener implements ChannelAwareMessageListener{

    private static final Logger logger= LogManager.getLogger(AcSpecialDetailListener.class);

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private AcAccountService acAccountService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag=message.getMessageProperties().getDeliveryTag();
        Object o=null;
        try {
            o=messageConverter.fromMessage(message);
        } catch (MessageConversionException e) {
            logger.error("专项抵扣mq消息转换异常",e);
            channel.basicAck(deliveryTag,false);
        }
        if(o==null){
            logger.error("专项抵扣mq消息为空");
            channel.basicAck(deliveryTag,false);
        }
        try {
            List<AcSpecialDetailDto> list = JSON.parseArray(o.toString(), AcSpecialDetailDto.class);
            for(AcSpecialDetailDto specialDetailDto:list){
                acAccountService.addAcSpecialAccountDetail(specialDetailDto.getCompanyId(),specialDetailDto.getProjectId(),
                        specialDetailDto.getProjectName(),specialDetailDto.getHouseCodeNew(),specialDetailDto.getMoneyPrincipal(),
                        specialDetailDto.getChargingType(),specialDetailDto.getBusinessTypeEnum(),specialDetailDto.getPayChannel(),specialDetailDto.getDesc(),
                        specialDetailDto.getDeductionDetailId(),specialDetailDto.getOperateDetailId(),specialDetailDto.getOperator());
            }
            logger.info("专项抵扣mq消息处理完成");
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("处理专项抵扣mq消息:{}出现异常{}",o.toString(),e);
            channel.basicNack(deliveryTag,false,false);
        }
    }
}
