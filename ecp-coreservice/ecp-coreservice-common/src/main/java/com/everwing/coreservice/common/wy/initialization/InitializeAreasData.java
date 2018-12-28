package com.everwing.coreservice.common.wy.initialization;/**
 * Created by wust on 2018/6/30.
 */

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.system.areas.TSysAreasList;
import com.everwing.coreservice.common.xml.XMLDefinitionFactory;
import com.everwing.coreservice.common.xml.factory.XMLAreasFactory;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 *
 * Function:初始化城市数据
 * Reason:
 * Date:2018/6/30
 * @author wusongti@lii.com.cn
 */
@Component
public class InitializeAreasData extends AbstractInitializeData {
    @Value("${platform.jdbc.url}")
    private String platformUrl;

    @Value("${platform.jdbc.username}")
    private String platformUsername;

    @Value("${platform.jdbc.password}")
    private String platformPassword;

    @Value("${platform.jdbc.driver}")
    private String platformDriver;

    @Autowired
    private SpringRedisTools springRedisTools;


    @Override
    public void init(String companyId) {}

    public void init() {
        XMLDefinitionFactory xmlDefinitionFactory = new XMLAreasFactory();
        Map<String, List> map = xmlDefinitionFactory.createXMLResolver().getResult();
        List<TSysAreasList> tSysAreasLists = map.get("tSysAreasLists");
        if(CollectionUtils.isNotEmpty(tSysAreasLists)){
            Map<String,TSysAreasList> groupByIdMap = groupById(tSysAreasLists);
            Map<String,List<TSysAreasList>> groupByPidMap = groupByPid(tSysAreasLists);
            Map<String,TSysAreasList> groupByParentNameAndNameMap = groupByParentNameAndName(groupByIdMap);

            HashMap hashMap = new HashMap(3);
            hashMap.put("groupByIdMap",groupByIdMap);
            hashMap.put("groupByPidMap",groupByPidMap);
            hashMap.put("groupByParentNameAndNameMap",groupByParentNameAndNameMap);

            springRedisTools.deleteMap(ApplicationConstant.REDIS_TABLE_KEY_AREAS.getStringValue());
            springRedisTools.addMap(ApplicationConstant.REDIS_TABLE_KEY_AREAS.getStringValue(), hashMap);
        }
    }



    /**
     * 根据id分组数据
     * id可以确定一个唯一值，因此key对应唯一值value
     * @param tSysAreasLists
     * @return
     */
    private Map<String,TSysAreasList> groupById(final List<TSysAreasList> tSysAreasLists){
        Map<String,TSysAreasList> stringListMap = new HashMap<>(tSysAreasLists.size());
        if(CollectionUtils.isNotEmpty(tSysAreasLists)){
            for (TSysAreasList tSysAreasList : tSysAreasLists) {
                String key = CommonUtils.null2String(tSysAreasList.getId());
                stringListMap.put(key,tSysAreasList);
            }
        }
        return stringListMap;
    }


    /**
     * 根据pid分组城市数据
     * @param tSysAreasLists
     * @return
     */
    private Map<String,List<TSysAreasList>> groupByPid(final List<TSysAreasList> tSysAreasLists){
        Map<String,List<TSysAreasList>> stringListMap = new HashMap<>(3200);
        if(CollectionUtils.isNotEmpty(tSysAreasLists)){
            for (TSysAreasList tSysAreasList : tSysAreasLists) {
                String pid = tSysAreasList.getPid();
                if(stringListMap.containsKey(pid)){
                    List<TSysAreasList> areasLists = stringListMap.get(pid);
                    if(CollectionUtils.isNotEmpty(areasLists)){
                        areasLists.add(tSysAreasList);
                    }else {
                        areasLists = new ArrayList<>(50);
                    }
                    stringListMap.put(pid,areasLists);
                }else{
                    List<TSysAreasList> areasLists = new ArrayList<>(50);
                    areasLists.add(tSysAreasList);
                    stringListMap.put(pid,areasLists);
                }
            }
        }
        return stringListMap;
    }



    /**
     * 根据parentName和name分组数据
     * parentName + name可以确定一个唯一值（至少目前中国没有发现同一个行政级别区域下面会有相同名字的区域），因此key对应唯一值value
     * @param groupByIdMap
     * @return
     */
    private Map<String,TSysAreasList> groupByParentNameAndName(final Map<String,TSysAreasList> groupByIdMap){
        Map<String,TSysAreasList> resultMap = new HashMap<>(groupByIdMap.size());
        Set<Map.Entry<String, TSysAreasList>> entrySet = groupByIdMap.entrySet();
        Iterator iterator = entrySet.iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            TSysAreasList tSysAreasList = (TSysAreasList)entry.getValue();
            if(groupByIdMap.containsKey(tSysAreasList.getPid())){
                String pName = groupByIdMap.get(tSysAreasList.getPid()).getAreaName();
                String key = CommonUtils.null2String(pName) + "." + CommonUtils.null2String(tSysAreasList.getAreaName());
                resultMap.put(key,tSysAreasList);
            }
        }
        return resultMap;
    }



    @Override
    public String getPlatformUrl() {
        return this.platformUrl;
    }

    @Override
    public String getPlatformUsername() {
        return this.platformUsername;
    }

    @Override
    public String getPlatformPassword() {
        return this.platformPassword;
    }

    @Override
    public String getPlatformDriver() {
        return platformDriver;
    }
}
