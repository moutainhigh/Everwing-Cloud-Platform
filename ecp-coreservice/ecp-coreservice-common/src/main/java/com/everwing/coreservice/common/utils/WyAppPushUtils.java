package com.everwing.coreservice.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.enums.WyAppPushTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.everwing.coreservice.common.enums.PushServerReturnCode;
/**
 * 物业app推送服务器调用
 *
 * @author DELL shiny
 * @create 2018/3/8
 */
@Service
public class WyAppPushUtils {

    private static final Logger logger= LogManager.getLogger(WyAppPushUtils.class);

    @Value("${wyAppPushUrl}")
    private String wyAppPushUrl;

    public void pushToOrderResponsiblePerson(String mobile){
        String pushString=generatePushString(mobile,"您有工单待处理。",WyAppPushTypeEnum.ORDER_MY_ORDER);
        String result=HttpUtils.doPost(wyAppPushUrl,pushString);
        JSONObject jsonObject=JSON.parseObject(result);
        if(!jsonObject.isEmpty()){
            int code= (int) jsonObject.get("code");
            logger.info("推送工单任务,返回值:[]",PushServerReturnCode.getMsg(code));
        }else {
            logger.error("推送工单任务，未接收到调用推送返回值");
        }

    }

    private String generatePushString(String mobile, String contents, WyAppPushTypeEnum pushTypeEnum){
        JSONObject pushString=new JSONObject();
        JSONObject msg=new JSONObject();
        msg.put("contents",contents);
        msg.put("paterType",pushTypeEnum.getPaterType());
        msg.put("childType",pushTypeEnum.getChildType());
        JSONObject data=new JSONObject();
        data.put("toBuildingCode",mobile);
        data.put("fromBuildingCode","system");
        data.put("msg",msg);
        pushString.put("data",data);
        pushString.put("token","");
        pushString.put("sign","");
        pushString.put("timestamp","");
        return pushString.toJSONString();
    }

}
