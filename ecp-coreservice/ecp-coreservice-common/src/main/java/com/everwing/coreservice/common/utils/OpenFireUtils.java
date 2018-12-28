package com.everwing.coreservice.common.utils;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.constant.Dict;
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
 * openfire同步类
 *
 * @author DELL shiny
 * @create 2017/7/28
 */
@Service
public class OpenFireUtils {

    protected static Logger logger = LoggerFactory.getLogger(OpenFireUtils.class);

    @Value("${openfireAPIBasePath}")
    public String basePath;

    public boolean register(List<Account> accountList)  {
        logger.info("调用Openfire放号接口");
        String result = HttpUtils.doPost(basePath + "/plugins/user/add", createUserJson(accountList));
        return handleSipResult(result);
    }

    public boolean cancel(List<Account> accountList)  {
        logger.info("调用Openfire账号注销接口");
        String result = HttpUtils.doPost(basePath + "/plugins/user/delete", createUserJson(accountList));
        return handleSipResult(result);
    }

    public boolean modifyPwd(List<Account> accountList){
        logger.info("调用openFire重置密码接口");
        String result=HttpUtils.doPost(basePath+"/plugins/user/resetPwd",createUserJson(accountList));
        return handleSipResult(result);
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
            if(account.getType().equals(Dict.ACCOUNT_TYPE_LY_USER.getIntValue())){
                data.put("mobile",account.getMobile());
                logger.info("新增参数{}",account.getMobile());
                logger.info(JSON.toJSONString(data));
            }
            dataList.add(data);
        }

        return JSON.toJSONString(map);
    }

    private boolean handleSipResult(String result) {
        System.err.println("调用Openfire返回结果："+result);
        // 返回结果: {"code": 0,"msg": "处理成功","data":""}
        Integer resultCode = JSON.parseObject(result).getInteger("code");
        boolean isSuccess = new Integer(0).equals(resultCode);
        if(!isSuccess){
            throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
        }else{
            return isSuccess;
        }
    }
}
