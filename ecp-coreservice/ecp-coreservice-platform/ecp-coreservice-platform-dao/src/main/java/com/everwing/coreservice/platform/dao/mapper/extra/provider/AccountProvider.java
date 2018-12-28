package com.everwing.coreservice.platform.dao.mapper.extra.provider;

import java.util.List;
import java.util.Map;

/**
 * account
 *
 * @author DELL shiny
 * @create 2017/7/7
 */
public class AccountProvider {

    public String batchInsertAccountHouse(Map<String,Object> param){
        List<Map<String,String>> paramList= (List<Map<String, String>>) param.get("paramList");
        StringBuilder stringBuilder=new StringBuilder();
        if(!paramList.isEmpty()){
            stringBuilder.append("insert into account_and_house(id,mobile,account_code,building_code,create_time) values ");
            for (Map<String,String> accountHouse:paramList){
                stringBuilder.append("(UUID(),'");
                stringBuilder.append(accountHouse.get("mobile"));
                stringBuilder.append("',");
                stringBuilder.append("'");
                stringBuilder.append(accountHouse.get("accountCode"));
                stringBuilder.append("',");
                stringBuilder.append("'");
                stringBuilder.append(accountHouse.get("buildingCode"));
                stringBuilder.append("',");
                stringBuilder.append("now()),");
            }
        }
        return stringBuilder.substring(0,stringBuilder.length()-1).toString();
    }

    public String selectAccountCodesAndCodesByCustIdsAndBIds(Map<String,Object> param){
        List<String> custIdList= (List<String>) param.get("custIdList");
        List<String> buildingIdList= (List<String>) param.get("buildingIdList");
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("select c.account_code accountCode,a.register_phone mobile,b.building_code buildingCode from person_cust a left join account c on a.register_phone=c.mobile LEFT JOIN person_building b ON a.cust_id=b.cust_id WHERE (a.cust_id,b.building_id) IN (");
        for (int i=0;i<custIdList.size();i++){
            stringBuilder.append("('");
            stringBuilder.append(custIdList.get(i));
            stringBuilder.append("','");
            stringBuilder.append(buildingIdList.get(i));
            stringBuilder.append("'),");
        }
        return stringBuilder.substring(0,stringBuilder.length()-1)+")";
    }

    public String delUserHouse(Map<String,Object> param){
        List<Map<String, String>> delList= (List<Map<String, String>>) param.get("value");
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("delete from account_and_house WHERE ");
        for(Map<String,String> del:delList){
           String code=del.get("username");
           String accountCode=del.get("bindusername");
           stringBuilder.append("(account_code='");
           stringBuilder.append(accountCode);
           stringBuilder.append("' and building_code='");
           stringBuilder.append(code);
           stringBuilder.append("')");
           stringBuilder.append(" or ");
        }
        return stringBuilder.substring(0,stringBuilder.length()-4);
    }
}
