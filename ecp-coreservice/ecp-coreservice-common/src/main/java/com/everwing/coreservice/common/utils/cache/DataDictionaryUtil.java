package com.everwing.coreservice.common.utils.cache;/**
 * Created by wust on 2018/7/4.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.wy.entity.system.areas.TSysAreasList;
import com.everwing.coreservice.common.wy.entity.system.lookup.TSysLookupItemList;
import com.everwing.coreservice.common.wy.entity.system.lookup.TSysLookupList;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Function:数据字典工具
 * Reason:可以根据key从缓存获取name，也可以根据name从缓存获取key。
 *        在修改改工具前请理解原理。
 * Date:2018/7/4
 *
 * @author wusongti@lii.com.cn
 */
@Component
public class DataDictionaryUtil {

    private static SpringRedisTools springRedisTools = null;

    private DataDictionaryUtil(){

    }



    /**************************************Lookup S****************************************/
    /**
     * 获取一条lookup记录
     * @param companyId
     * @param code
     * @return
     */
    public static TSysLookupList getLookupByCode(String companyId,String code) {
        if(companyId == null){
            companyId = WyBusinessContext.getContext().getCompanyId();
        }
        Map map = getRedisSpringBean().getMap(ApplicationConstant.REDIS_TABLE_KEY_LOOKUP.getStringValue() + "." + companyId);
        if(map != null){
            Object groupLookupByCodeMapObj = map.get("groupLookupByCodeMap");
            if(groupLookupByCodeMapObj != null){
                Map data = (Map)groupLookupByCodeMapObj;
                String key = code;
                TSysLookupList tSysLookupList = (TSysLookupList)data.get(key);
                return tSysLookupList;
            }
        }
        return null;
    }
    /**************************************Lookup E****************************************/











    /**************************************LookupItem S****************************************/

    /**
     * 获取lookupItem子列表
     * @param companyId
     * @param lookupCode
     * @param parentCode
     * @return
     */
    public static List<TSysLookupItemList> getLookupItemListByLookupCodeAndParentCode(String companyId,String lookupCode,String parentCode) {
        if(companyId == null){
            companyId = WyBusinessContext.getContext().getCompanyId();
        }
        Map map = getRedisSpringBean().getMap(ApplicationConstant.REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_BY_LOOKUP_ID_AND_PARENT_CODE_MAP.getStringValue() + "." + companyId);
        if(map != null){
            TSysLookupList tSysLookup = getLookupByCode(companyId,lookupCode);
            String key = tSysLookup.getLookupId() + "." + parentCode;
            List<TSysLookupItemList> tSysLookupItemLists = (List<TSysLookupItemList>)map.get(key);
            return tSysLookupItemLists;
        }
        return null;
    }

    /**
     * 根据编码获取对应lookupItem的名字
     * @param companyId
     * @param parentCode
     * @param codeValue
     * @return
     */
    public static String getLookupItemNameByParentCodeAndCode(String companyId,String parentCode, Object codeValue) {
        if(companyId == null){
            companyId = WyBusinessContext.getContext().getCompanyId();
        }
        Object obj = getRedisSpringBean().getByKey(ApplicationConstant.REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_NAME_BY_PARENT_CODE_AND_CODE_MAP.getStringValue() + "." + companyId);
        if(obj != null){
            JSONObject jsonObject = JSONObject.parseObject(obj.toString());
            String key = parentCode + "." + codeValue;
            String name = jsonObject.getString(key);
            return name;
        }
        return null;
    }

    /**
     * 根据名字获取对应lookupItem的code
     * @param companyId
     * @param parentCode
     * @param name
     * @return
     */
    public static String getLookupItemCodeByParentCodeAndName(String companyId,String parentCode, String name) {
        if(companyId == null){
            companyId = WyBusinessContext.getContext().getCompanyId();
        }
        Object obj = getRedisSpringBean().getByKey(ApplicationConstant.REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_CODE_BY_PARENT_CODE_AND_NAME_MAP.getStringValue() + "." + companyId);
        if(obj != null){
            JSONObject jsonObject = JSONObject.parseObject(obj.toString());
            String key = parentCode + "." + name;
            String code = jsonObject.getString(key);
            return code;
        }
        return null;
    }
    /**************************************LookupItem E****************************************/















    /**************************************Areas S****************************************/
    /**
     * 根据id获取一条TSysAreasList记录
     * @param id
     * @return TSysAreasList
     */
    public static TSysAreasList getAreasById(String id) {
        Map map = getRedisSpringBean().getMap(ApplicationConstant.REDIS_TABLE_KEY_AREAS.getStringValue());
        if(map != null){
            Object groupByIdMapObj = map.get("groupByIdMap");
            if(groupByIdMapObj != null){
                Map data = (Map)groupByIdMapObj;
                TSysAreasList tSysAreasList = (TSysAreasList)data.get(id);
                return tSysAreasList;
            }
        }
        return null;
    }

    /**
     * 根据pid获取TSysAreasList集合
     * @param pid
     * @return List<TSysAreasList>
     */
    public static List<TSysAreasList> getAreasListByPid(String pid) {
        Map map = getRedisSpringBean().getMap(ApplicationConstant.REDIS_TABLE_KEY_AREAS.getStringValue());
        if(map != null){
            Object groupByPidMapObj = map.get("groupByPidMap");
            if(groupByPidMapObj != null){
                Map data = (Map)groupByPidMapObj;
                List<TSysAreasList> tSysAreasLists = (List<TSysAreasList>)data.get(pid);
                return tSysAreasLists;
            }
        }
        return null;
    }

    /**
     * 根据父名称和当前名称获取一条记录
     * @param parentName
     * @param name
     * @return TSysAreasList
     */
    public static TSysAreasList getAreasByParentNameAndName(String parentName,String name) {
        Map map = getRedisSpringBean().getMap(ApplicationConstant.REDIS_TABLE_KEY_AREAS.getStringValue());
        if(map != null){
            Object groupByParentNameAndNameMapObj = map.get("groupByParentNameAndNameMap");
            if(groupByParentNameAndNameMapObj != null){
                Map data = (Map)groupByParentNameAndNameMapObj;
                String key = parentName + "." + name;
                TSysAreasList tSysAreasList = (TSysAreasList)data.get(key);
                return tSysAreasList;
            }
        }
        return null;
    }
    /**************************************Areas E****************************************/















    /**************************************Building S****************************************/
    /**
     * 根据项目编码获取建筑结构
     * @param companyId
     * @param projectCode
     * @return
     */
    public static String getBuildingTreeByProjectCode(String companyId,String projectCode){
        if(companyId == null){
            companyId = WyBusinessContext.getContext().getCompanyId();
        }
        Object obj = getRedisSpringBean().getByKey(ApplicationConstant.REDIS_TABLE_KEY_BUILDING_TREE.getStringValue() + "." + companyId);
        if(obj != null){
            JSONObject jsonObject = JSONObject.parseObject(obj.toString());
            if(jsonObject != null && jsonObject.containsKey(projectCode)){
                return jsonObject.getString(projectCode);
            }
        }
        return null;
    }

    /**************************************Building E****************************************/


    /**
     * 根据公司id获取平台公司信息
     * @param companyId
     * @return
     */
    public static JSONObject getPlatformCompanyById(String companyId){
        Object obj = getRedisSpringBean().getByKey(ApplicationConstant.REDIS_TABLE_KEY_DATA_SOURCE.name());
        if(obj != null){
            JSONObject jsonObject = JSONObject.parseObject(obj.toString());;
            JSONObject company = (JSONObject)jsonObject.get(companyId);
            return company;
        }
        return null;
    }

    private static SpringRedisTools getRedisSpringBean(){
        if(springRedisTools == null){
            springRedisTools = SpringContextHolder.getBean("redisDataOperator");
        }
        return springRedisTools;
    }
}
