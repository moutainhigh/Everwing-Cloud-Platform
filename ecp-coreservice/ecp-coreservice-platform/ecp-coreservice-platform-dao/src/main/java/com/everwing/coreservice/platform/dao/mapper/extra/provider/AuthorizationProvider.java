package com.everwing.coreservice.platform.dao.mapper.extra.provider;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * Created by DELL on 2017/6/29.
 */
public class AuthorizationProvider {

    public String batchDelete(Map<String,Object> paramMap){
        String authorizedAccountCode= (String) paramMap.get("authorizedAccountCode");
        String[] authAccountCodeArray= (String[]) paramMap.get("authAccountCodeArray");
        StringBuilder stringBuilder=new StringBuilder();
        for (String str:authAccountCodeArray){
            stringBuilder.append("'");
            stringBuilder.append(str);
            stringBuilder.append("',");
        }
        String buildId= (String) paramMap.get("buildingId");
        return new SQL().DELETE_FROM("ly_authorization_account").WHERE("authorized_account_id='"+authorizedAccountCode+"'")
                .WHERE("authorizee_account_id in ("+stringBuilder.substring(0,stringBuilder.length()-1).toString()+")").WHERE("build_ids='"+buildId+"'").toString();
    }
}
