package com.everwing.coreservice.platform.dao.mapper.extra.provider;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 平台gating
 *
 * @author DELL shiny
 * @create 2017/7/24
 */
public class GatingLinPhoneProvider {

    public String updateStatus(Map<String,String> hashMap){
        String gatingCode=hashMap.get("gatingCode");
        String version=hashMap.get("version");
        String onlineState=hashMap.get("onlineState");
        String videosState=hashMap.get("videosState");
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("update gating set ");
        boolean flag=false;
        if(StringUtils.isNotEmpty(version)){
            stringBuilder.append(" version='");
            stringBuilder.append(version);
            stringBuilder.append("'");
            flag=true;
        }
        if(StringUtils.isNotEmpty(onlineState)){
            if(flag){
                stringBuilder.append(",");
            }
            stringBuilder.append(" online_state=");
            stringBuilder.append(onlineState);
            flag=true;
        }
        if(StringUtils.isNotEmpty(videosState)){
            if(flag){
                stringBuilder.append(",");
            }
            stringBuilder.append(" videos_state=");
            stringBuilder.append(videosState);
        }
        stringBuilder.append(" where gating_code='");
        stringBuilder.append(gatingCode);
        stringBuilder.append("'");
        return stringBuilder.toString();
    }
}
