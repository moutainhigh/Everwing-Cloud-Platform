package com.everwing.server.platform.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @description 转为使用FastJson序列化对象
 * @author MonKong
 * @date 2017年3月27日
 */
public class JackJson2FastJsonSerializer extends JsonSerializer{

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeRaw(JSON.toJSONString(value));
	}
	
}

