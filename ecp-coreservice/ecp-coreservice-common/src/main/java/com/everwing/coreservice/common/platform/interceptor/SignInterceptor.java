package com.everwing.coreservice.common.platform.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.constant.ResponseCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.platform.entity.extra.LinPhoneRequest;
import com.everwing.coreservice.common.platform.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author  shiny
 */
public class SignInterceptor extends BaseInterceptor implements HandlerInterceptor{

    private String key;

    private String linphoneTokenPreffix;

    private String linphoneTokenExpireTime;

    private String checkSign;

    @Autowired
    private SpringRedisTools springRedisTools;

    private org.apache.logging.log4j.Logger logger= LogManager.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String body= (String) request.getAttribute("body");
        if(!StringUtils.isNotEmpty(body)){
            logger.error("请求参数为空,拒绝处理!");
            return false;
        }
        LinPhoneRequest linPhoneRequest=JSON.parseObject(body, LinPhoneRequest.class);
        JSONObject data=linPhoneRequest.getData();
        if(data==null){
            returnJson(response,JSON.toJSONString(new LinphoneResult(ResponseCode.PARAMS_IS_NULL), SerializerFeature.WriteMapNullValue),logger);
            return false;
        }
        String timestamp=linPhoneRequest.getSign();
        if(!StringUtils.isNotEmpty(timestamp)){
            returnJson(response,JSON.toJSONString(new LinphoneResult(ResponseCode.REQUEST_EXPIRED),SerializerFeature.WriteMapNullValue),logger);
            return false;
        }
        logger.debug("开始校验时间戳!");
        if(!checkTimestamp(linPhoneRequest.getTimestamp())){
            returnJson(response,JSON.toJSONString(new LinphoneResult(ResponseCode.REQUEST_EXPIRED), SerializerFeature.WriteMapNullValue),logger);
            return false;
        }
        logger.debug("开始校验token!");
        String token=linPhoneRequest.getToken();
        if(!StringUtils.isNotEmpty(token)){
            returnJson(response,JSON.toJSONString(new LinphoneResult(ResponseCode.TOKEN_VALIDATE),SerializerFeature.WriteMapNullValue),logger);
            return false;
        }else {
            String account= (String) springRedisTools.getByKey(linphoneTokenPreffix+token);
            if(StringUtils.isNotEmpty(account)){
                request.setAttribute("account",account);
                springRedisTools.updateExpire(linphoneTokenPreffix+token,Integer.valueOf(linphoneTokenExpireTime), TimeUnit.MINUTES);
            }else {
                returnJson(response,JSON.toJSONString(new LinphoneResult(ResponseCode.TOKEN_VALIDATE),SerializerFeature.WriteMapNullValue),logger);
                return false;
            }
        }
        String sign=linPhoneRequest.getSign();
        if(!StringUtils.isNotEmpty(sign)){
            returnJson(response,JSON.toJSONString(new LinphoneResult(ResponseCode.SIGN_ERROR),SerializerFeature.WriteMapNullValue),logger);
            return false;
        }
        logger.info("checkSign:{}",checkSign);
//        if(checkSign.equals("true")) {
            logger.debug("开始校验sign!");
            if (checkSign(body, linPhoneRequest)) {
                return true;
            } else {
                returnJson(response, JSON.toJSONString(new LinphoneResult(ResponseCode.SIGN_ERROR), SerializerFeature.WriteMapNullValue), logger);
                return false;
            }
//        }else {
//            logger.info("调试模式启动不检验sign");
//        }
//        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        if(e==null){//未报错
            String requestUri=request.getRequestURI();
            if(requestUri.contains("resetPassword")||requestUri.contains("modifyPassword")||requestUri.contains("loginOut")||requestUri.contains("resetMobile")) {//重置密码
                String body = (String) request.getAttribute("body");
                if(StringUtils.isNotEmpty(body)){
                    LinPhoneRequest linPhoneRequest=JSON.parseObject(body, LinPhoneRequest.class);
                    String token=linPhoneRequest.getToken();
                    if(StringUtils.isNotEmpty(token)){
                        springRedisTools.deleteByKey(linphoneTokenPreffix+token);//移除登录信息
                    }
                }
            }
        }
    }

    private boolean checkSign(String body,LinPhoneRequest linPhoneRequest){
        JSONObject jsonObject=JSON.parseObject(body, Feature.OrderedField);
        Set<String> keySet=jsonObject.keySet();
        List<String> keys=new ArrayList<>(keySet);
        Collections.sort(keys);
        String source=dataASCIISortReturnSource(jsonObject,keys);
        logger.info(source);
        String encrypt= MD5Utils.MD5(source+key);
        if(!StringUtils.isNotEmpty(encrypt)){
            return false;
        }
        return encrypt.toUpperCase().equals(linPhoneRequest.getSign());
    }

    /**
     * 请求参数AscII排序并拼接待签名字符串
     * @return 拼接好的待签名字符串
     */
    private String dataASCIISortReturnSource(JSONObject jsonObject,List<String> keys){
        StringBuilder stringBuilder=new StringBuilder();
        for(String key:keys){
            if(key.equals("sign")){

            }else {
                if(stringBuilder.length()==0){
                    stringBuilder.append(jsonObject.get(key));
                }else {
                    stringBuilder.append("&");
                    stringBuilder.append(jsonObject.get(key));
                }
            }
        }
        return stringBuilder.toString();
    }

    private int compareAscII(String str1,String str2,int len){
        for(int j=0;j<len;j++){
            if(str1.charAt(j)>str2.charAt(j)){
                return 1;
            }else if(str1.charAt(j)==str2.charAt(j)){
                continue;
            }else {
                return -1;
            }
        }
        return 0;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLinphoneTokenPreffix(String linphoneTokenPreffix) {
        this.linphoneTokenPreffix = linphoneTokenPreffix;
    }

    public void setLinphoneTokenExpireTime(String linphoneTokenExpireTime) {
        this.linphoneTokenExpireTime = linphoneTokenExpireTime;
    }

    public void setCheckSign(String checkSign) {
        this.checkSign = checkSign;
    }

    public static void main(String[] args) {

    }

}
