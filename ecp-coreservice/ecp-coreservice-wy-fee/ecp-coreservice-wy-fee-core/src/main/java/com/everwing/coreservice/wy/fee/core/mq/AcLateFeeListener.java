package com.everwing.coreservice.wy.fee.core.mq;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.wy.fee.dto.AcLateFeeDto;
import com.everwing.coreservice.common.wy.fee.service.AcAccountLateFeeService;
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
 * 资产账户滞纳金
 *
 * @author DELL shiny
 * @create 2018/6/8
 */
public class AcLateFeeListener implements ChannelAwareMessageListener {

    private static final Logger logger= LogManager.getLogger(AcLateFeeListener.class);

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private AcAccountLateFeeService acAccountLateFeeService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Object o=null;
        long deliveryTag=message.getMessageProperties().getDeliveryTag();
        try {
            o=messageConverter.fromMessage(message);
        } catch (MessageConversionException e) {
             logger.error("滞纳金mq消息转换错误");
             channel.basicAck(deliveryTag,false);
        }
        if(o==null){
            logger.error("滞纳金消息为空");
            channel.basicAck(deliveryTag,false);
        }
        try {
            List<AcLateFeeDto> list = JSON.parseArray(o.toString(), AcLateFeeDto.class);
            for(AcLateFeeDto lateFeeDto:list){
                acAccountLateFeeService.addAcLateFeeAccountDetail(lateFeeDto.getCompanyId(),lateFeeDto.getProjectId(),
                        lateFeeDto.getProjectName(),lateFeeDto.getHouseCodeNew(),lateFeeDto.getMoney(),
                        lateFeeDto.getChargingType(),lateFeeDto.getBusinessType(),lateFeeDto.getDesc(),
                        lateFeeDto.getOperateDetailId(),lateFeeDto.getPrincipal(),lateFeeDto.getOperator(),lateFeeDto.getIsPay());
            }
            logger.info("滞纳金mq消息处理完成");
            channel.basicAck(deliveryTag,false);
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("滞纳金mq消息:{}处理出现异常:{}",o.toString(),e);
            channel.basicNack(deliveryTag,false,false);
        }
    }
}
