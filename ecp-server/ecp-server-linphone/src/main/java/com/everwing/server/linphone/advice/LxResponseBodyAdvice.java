package com.everwing.server.linphone.advice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.everwing.coreservice.common.dto.LinphoneResult;
import org.apache.logging.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Iterator;
import java.util.List;


/**
 * Created by DELL on 2017/6/28.
 */
@ControllerAdvice
public class LxResponseBodyAdvice implements ResponseBodyAdvice<LinphoneResult>{

    private Logger logger= org.apache.logging.log4j.LogManager.getLogger(getClass());

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public LinphoneResult beforeBodyWrite(LinphoneResult linphoneResult, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Object object=linphoneResult.getData();
        if(object!=null){//list中移除null
            if(object instanceof List){
                List result= (List) object;
                Iterator resultIterator=result.iterator();
                while (resultIterator.hasNext()){//移除空元素
                    Object listObj=resultIterator.next();
                    if(listObj==null){
                        resultIterator.remove();
                    }
                }
            }
        }
        logger.info("返回数据:{}", JSON.toJSONString(linphoneResult, new SerializerFeature[]{SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty}));
        return linphoneResult;
    }
}