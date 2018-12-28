package com.everwing.coreservice.wy.fee.core.mq;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.wy.fee.dto.AcCommonAccountDetailDto;
import com.everwing.coreservice.common.wy.fee.service.AcAccountService;
import com.rabbitmq.client.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 资产账户通用账户mq
 *
 * @author DELL shiny
 * @create 2018/6/7
 */
public class AcCommonDetailListener implements ChannelAwareMessageListener {

    private static final Logger logger= LogManager.getLogger(AcCommonDetailListener.class);

    @Autowired
    private MessageConverter msgConverter;

    @Autowired
    private AcAccountService acAccountService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Object obj = null;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            obj = msgConverter.fromMessage(message);
        } catch (Exception e) {
            logger.error("convert MQ message error.", e);
            logger.info("receive and ack msg: {}",message);
            channel.basicAck(deliveryTag,false);
            return;
        }
        if (obj == null) {
            logger.error("receive null msg: {}", message);
            //确认消息
            channel.basicAck(deliveryTag, false);
            return;
        }
        //业务A
        try {
            List<AcCommonAccountDetailDto> list=JSON.parseArray(obj.toString(), AcCommonAccountDetailDto.class);
            for(AcCommonAccountDetailDto commonDto:list){
                acAccountService.addAcCommonAccountDetail(commonDto.getCompanyId(),commonDto.getProjectId(),
                        commonDto.getProjectName(),commonDto.getHouseCodeNew(),commonDto.getMoney(),commonDto.getBusinessTypeEnum(),
                        commonDto.getChargingType(),commonDto.getPayChannel(),commonDto.getDeductionDetailId(),commonDto.getDesc(),commonDto.getOperateDetailId(),
                        commonDto.getOperator());
            }
            logger.info("通用账户mq消息消费完成: {}",message);
            //确认消息
            channel.basicAck(deliveryTag, false);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("receive msg: {} but throws Exception:{}", obj.toString(),e);
            //进入死信队列
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
