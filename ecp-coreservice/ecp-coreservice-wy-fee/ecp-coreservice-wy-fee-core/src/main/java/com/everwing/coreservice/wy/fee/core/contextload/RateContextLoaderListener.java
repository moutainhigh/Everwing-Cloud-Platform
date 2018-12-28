package com.everwing.coreservice.wy.fee.core.contextload;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * 载入税金（暂时从配置文件读取）
 *
 * @author DELL shiny
 * @create 2018/6/7
 */
@Component
public class RateContextLoaderListener implements InitializingBean {

    @Autowired
    private SpringRedisTools springRedisTools;

    @Override
    public void afterPropertiesSet() throws Exception {
        String rateJson=getRate();
        JSONArray array=JSONArray.parseArray(rateJson);
        for(Object o:array){
            JSONObject jsonObject=JSONObject.parseObject(o.toString());
            Set<String> keySet=jsonObject.keySet();
            for(String key:keySet){
                JSONArray value=JSONArray.parseArray(jsonObject.get(key).toString());
                Object v=springRedisTools.getByKey(key);
                if(null==v){
                    springRedisTools.addData(key,value);
                }
            }
        }
    }

    private String getRate() throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("config/coreservice-wy-fee");
        String value = bundle.getString("rate");
        return value;
    }
}
