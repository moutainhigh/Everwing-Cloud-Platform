package com.everwing.coreservice.common.platform.resolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.platform.annotation.AllowedNull;
import com.everwing.coreservice.common.platform.entity.extra.LinPhoneRequest;
import com.everwing.coreservice.common.platform.exception.ArgException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyArgResolver implements HandlerMethodArgumentResolver{

    private static final Logger logger= LogManager.getLogger(MyArgResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request=nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String body= (String) request.getAttribute("body");
        LinPhoneRequest linPhoneRequest=JSON.parseObject(body,LinPhoneRequest.class);
        JSONObject jsonObject=linPhoneRequest.getData();
        Set<Map.Entry<String,Object>> entrySet=jsonObject.entrySet();
        List<Map.Entry<String,Object>> entryList=new ArrayList<>(entrySet);
        Object requestValue=null;
        String paramName=methodParameter.getParameterName();
        AllowedNull allowedNull=methodParameter.getParameterAnnotation(AllowedNull.class);
        Class clazz=methodParameter.getParameterType();
        boolean paramExists=false;
        for (int i=0;i<entryList.size();i++){
            Map.Entry<String,Object> entry=entryList.get(i);
            String requestParam=entry.getKey();
            if(paramName.equals(requestParam)) {
                paramExists=true;
                requestValue=JSON.parseObject(JSON.toJSONString(entry.getValue()),clazz);
                //requestValue = entry.getValue();
                if (webDataBinderFactory != null) {
                    WebDataBinder binder = webDataBinderFactory.createBinder(nativeWebRequest, requestValue, methodParameter.getParameterName());
                    binder.convertIfNecessary(requestValue, methodParameter.getParameterType(), methodParameter);
                }
                return requestValue;
            }
        }
        if(!paramExists&&allowedNull==null){
            throw new ArgException("参数未传入!");
        }
        return requestValue;
    }
}
