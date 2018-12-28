package com.everwing.coreservice.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.extra.AccountAndHouseSipData;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同步sip服务器类
 *
 * @author DELL shiny
 * @create 2017/7/28
 */
@Service
public class SipUtils {


    private Logger logger= LoggerFactory.getLogger(getClass());

    @Value("${sipAPIBasePath}")
    private String basePath;

    @Value("${from}")
    private String from;

    @Value("${to}")
    private String to;

    public boolean sipGating(String gatingCode,String command,Map<String,String> parameters){
        Map<String,Object> message=new HashMap<>();
        List<Map<String,Object>> param=new ArrayList<>();
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("id",System.currentTimeMillis());
        paramMap.put("to","sip:"+gatingCode+to);
        paramMap.put("from","sip:system"+from);
        Map<String,Object> body=new HashMap<>();
        body.put("cmd",command);
        body.put("param",parameters);
        paramMap.put("body",body);
        param.add(paramMap);
        message.put("message",param);
        try {
            logger.info("同步到sip服务器！");
            String result=HttpUtils.doPost(basePath+"/remote/control", JSON.toJSONString(message));
            JSONObject jsonObject=JSON.parseObject(result);
            String resultStr= (String) jsonObject.get("result");
            if("succeeded".equals(resultStr)){
                logger.info("同步到sip服务器成功!");
                return true;
            }
            logger.info("同步到sip服务器失败!");
            return false;
        } catch (Exception e) {
            logger.error("同步到sip服务器异常",e.getMessage());
            return false;
        }
    }


    public boolean register(List<Account> accountList)  {
        String result = HttpUtils.doPost(basePath + "/user", createUserJson(accountList));
        return handleSipResult(result);
    }

    public boolean cancel(List<Account> accountList)  {
        String result = HttpUtils.doPost(basePath + "/deluser", createUserJson(accountList));
        return handleSipResult(result);
    }

    public boolean bind(List<AccountAndHouseSipData> dataList)  {
        if(dataList!=null && dataList.size()>0){
            System.err.println("开始调用SIP绑定接口...");
            String result = HttpUtils.doPost(basePath.concat("/user/bind"), createAccountAndHouseJson(dataList));
            return handleSipResult(result);
        }else{
            return false;
        }
    }

    public boolean unbind(List<AccountAndHouseSipData> dataList)  {
        if(dataList!=null && dataList.size()>0){
            String result = HttpUtils.doPost(basePath.concat("/user/delbind"), createAccountAndHouseJson(dataList));
            return handleSipResult(result);
        }else{
            return false;
        }
    }

    //修改用户密码
    public boolean modifyPwd(List<Account> accountList){
        if(!accountList.isEmpty()){
            String result=HttpUtils.doPost(basePath.concat("/resetpwd"),createUserJson(accountList));
            return handleSipResult(result);
        }
        return false;
    }

    /**
     * format:{"binduser":[{"username":"房间内部账号","bindusername":"内部账号"}]}
     */
    private static String createAccountAndHouseJson(List<AccountAndHouseSipData> dataList) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        JSONArray jsonArray = new JSONArray();
        map.put("binduser", jsonArray);
        jsonArray.addAll(dataList);
        return JSON.toJSONString(map);
    }

    /**
     * format:{"user":[{"username":"","password":""}]}
     */
    private static String createUserJson(List<Account> accountList) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        ArrayList<Map> dataList = new ArrayList<Map>();
        map.put("user", dataList);

        for(Account account:accountList){
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("username", account.getAccountCode());
            data.put("password", account.getPassword());
            dataList.add(data);
        }

        return JSON.toJSONString(map);
    }

    private boolean handleSipResult(String result) {
        logger.info("调用SIP返回结果："+result);
        //返回结果居然是html... <html><body>operation succeeded.</body></html>
        boolean isSuccess = result.contains("succe");
        if(!isSuccess){
            throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
        }
        return isSuccess;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
