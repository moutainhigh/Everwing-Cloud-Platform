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

import java.math.BigDecimal;
import java.util.Map;

/**
 * 为老账户系统批量充值提供同步数据到新账户系统的监听
 * @author qhc
 * @create 2018/6/8
 */
public class AcChargeDetailSunFictitiousListener implements ChannelAwareMessageListener {

    private static final Logger logger= LogManager.getLogger(AcChargeDetailSunFictitiousListener.class);

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
            logger.error("虚拟表汇总到新账户计费数据,确认消息");
            channel.basicAck(deliveryTag, false);
            return;
        }
        if (o == null) {
            logger.info("虚拟表汇总到新账户计费数据，确认消息");
            channel.basicAck(deliveryTag, false);
        }
        try {
        	
            Map<String, Object> paramMap = JSON.parseObject( o.toString() );
            if(CommonUtils.isEmpty( paramMap )) {
            	 logger.info("虚拟表汇总到新账户计费数据，确认消息");
                 channel.basicAck(deliveryTag, false);
            }
            
            String companyId = (String) paramMap.get("companyId");
            String houseCodeNew = (String) paramMap.get("houseCodeNew");
            BigDecimal totalAmount = new BigDecimal( (String) paramMap.get("totalAmount") );
            int accountType = (int) paramMap.get("type");
            
            if( CommonUtils.isEmpty( companyId ) || CommonUtils.isEmpty( houseCodeNew ) || CommonUtils.isEmpty( totalAmount ) ) {
            	 logger.info("虚拟表汇总到新账户计费数据，确认消息");
                 channel.basicAck(deliveryTag, false);
            }
            
            acAccountService.sumFictitiousForNewAccount(companyId, houseCodeNew,accountType, totalAmount);
            
            logger.info("虚拟表汇总到新账户计费数据mq消息消费完成");
            channel.basicAck(deliveryTag,false);
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("虚拟表汇总到新账户计费数据：{},进入死信队列，数据:{}",e,o.toString());
            channel.basicNack(deliveryTag,false,false);
        }
    }
}
