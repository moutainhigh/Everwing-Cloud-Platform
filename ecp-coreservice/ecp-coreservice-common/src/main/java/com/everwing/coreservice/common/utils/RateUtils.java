package com.everwing.coreservice.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 获取税金
 *
 * @author DELL shiny
 * @create 2018/6/11
 */
@Component
public class RateUtils {

    @Autowired
    private SpringRedisTools springRedisTools;

    public BigDecimal getRateByCompanyIdAndProjectIdAndType(String companyId,String projectId,Integer type){
        JSONArray array=JSON.parseArray(springRedisTools.getByKey(companyId).toString());
        JSONObject jsonObject=null;
        JSONObject typeObject=null;
        for(int i=0;i<array.size();i++){
            Object parent=array.get(i);
            if(parent!=null) {
                jsonObject = JSON.parseObject(parent.toString());
            }
            if(jsonObject!=null){
                Object typeObj=jsonObject.get(projectId);
                if(typeObj!=null) {
                    typeObject = JSON.parseObject(typeObj.toString());
                    if(typeObject!=null){
                        Object rate=typeObject.get(type.toString());
                        if(rate!=null){
                            return new BigDecimal(rate.toString());
                        }
                    }
                }
            }
        }
        return null;
    }

    //税金公式=本金/(1+税率)*税率
    public BigDecimal calculateRate(BigDecimal rate,BigDecimal money){
        BigDecimal rateFee=new BigDecimal(0);
        if(rate!=null) {
            if (!rate.equals(new BigDecimal(0))) {
                rateFee = money.divide((new BigDecimal(1).add(rate)), 2, BigDecimal.ROUND_HALF_DOWN).multiply(rate);
            }
        }
        return rateFee;
    }
}
