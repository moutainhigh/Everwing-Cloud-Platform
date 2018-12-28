package com.everwing.coreservice.common.platform.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.everwing.coreservice.common.constant.ResponseCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.platform.entity.extra.LinPhoneRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author shiny
 */
public class AllInterceptor extends BaseInterceptor implements HandlerInterceptor {

    private Logger logger= LogManager.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String body= IOUtils.toString(request.getInputStream());
        if(!StringUtils.isNotEmpty(body)){
            logger.error("请求参数为空,拒绝处理!");
            return false;
        }
        request.setAttribute("body",body);
        LinPhoneRequest linPhoneRequest= null;
        try {
            linPhoneRequest = JSON.parseObject(body, LinPhoneRequest.class);
        } catch (Exception e) {
            logger.error("传入参数非JSON格式，拒绝处理!"+body);
            return false;
        }
        logger.info("收到请求,请求URL:{},请求参数{}",request.getRequestURI(),JSON.toJSONString(linPhoneRequest,SerializerFeature.WriteMapNullValue));
        JSONObject jsonObject=linPhoneRequest.getData();
        //解决无参数接口空指针
        if(jsonObject!=null) {
            Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
            Iterator<Map.Entry<String, Object>> it = entrySet.iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                Object object = entry.getValue();
                if (object == null) {
                    returnJson(response, JSON.toJSONString(new LinphoneResult(ResponseCode.PARAMS_IS_NULL), SerializerFeature.WriteMapNullValue), logger);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        request.removeAttribute("body");
        request.removeAttribute("account");
    }

}
