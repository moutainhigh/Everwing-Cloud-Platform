package com.everwing.coreservice.wy.fee.core.mq;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.wy.fee.dto.ProjectProductDto;
import com.everwing.coreservice.common.wy.fee.service.ProjectAccountService;
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
 * @author shiny
 **/
public class ProjectProductListener implements ChannelAwareMessageListener {

    private static final Logger logger= LogManager.getLogger(ProjectProductListener.class);

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private ProjectAccountService projectAccountService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag=message.getMessageProperties().getDeliveryTag();
        Object o=null;
        try {
            o=messageConverter.fromMessage(message);
        } catch (MessageConversionException e) {
            logger.error("项目产品mq消息转换异常",e);
            channel.basicAck(deliveryTag,false);
        }
        if(o==null){
            logger.error("项目产品mq消息为空");
            channel.basicAck(deliveryTag,false);
        }
        try {
            List<ProjectProductDto> list = JSON.parseArray(o.toString(), ProjectProductDto.class);
            for(ProjectProductDto projectProductDto:list){
                projectAccountService.changeProductAccount(projectProductDto.getCompanyId(),projectProductDto.getProjectId()
                ,projectProductDto.getMoney(),projectProductDto.getOrderId(),projectProductDto.getOrderJson(),projectProductDto.getIsAsset()
                ,projectProductDto.getHouseCodeNew(),projectProductDto.getLogStream(),projectProductDto.getOperator(),projectProductDto.getRate());
            }
            logger.info("项目产品mq消息处理完成");
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("处理项目产品mq消息:{}出现异常{}",o.toString(),e);
            channel.basicNack(deliveryTag,false,false);
        }
    }
}
