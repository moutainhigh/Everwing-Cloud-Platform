package com.everwing.coreservice.common.convert;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FastJsonMessageConverter extends AbstractMessageConverter {

	private static final Logger LOG = LoggerFactory.getLogger(FastJsonMessageConverter.class);
	
	private static final String DEFAULT_CHARSET = "UTF-8";
	
    public FastJsonMessageConverter(){
    	super();
    }
	
    /**
     * 发送消息时消息转换
     */
	@Override
	protected Message createMessage(Object object, MessageProperties messageProperties) {
		byte[] bytes = null;
        try {
            String jsonString = JSON.toJSONString(object);
            bytes = jsonString.getBytes(DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new MessageConversionException("Failed to convert Message content", e);
        } 

        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        messageProperties.setContentEncoding(DEFAULT_CHARSET);

        if (bytes != null) {
            messageProperties.setContentLength(bytes.length);
        }
        return new Message(bytes, messageProperties);
	}

	/**
	 * 接受消息时,消息转换
	 */
	@Override
	public Object fromMessage(Message message) throws MessageConversionException {
	    String json = "";
        try {
            json = new String(message.getBody(),DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return JSONObject.parse(json);
	}
}
