package com.everwing.coreservice.common.wy.initialization;/**
 * Created by wust on 2018/6/30.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.jdbc.DBUtil;
import com.everwing.coreservice.common.wy.entity.system.lookup.TSysLookupItemList;
import com.everwing.coreservice.common.wy.entity.system.lookup.TSysLookupList;
import com.everwing.coreservice.common.xml.XMLDefinitionFactory;
import com.everwing.coreservice.common.xml.factory.XMLLookupFactory;
import com.everwing.coreservice.common.xml.factory.XMLLookupItemFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Function:初始化lookup数据
 * Reason:
 * Date:2018/6/30
 * @author wusongti@lii.com.cn
 */
@Component
public class InitializeLookupData extends AbstractInitializeData {
    static Logger logger = LogManager.getLogger(InitializeLookupData.class);

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
    public void init(String companyId) {

    }

    /**
     * 为所有公司缓存一份数据，并用个性化数据替换原来的数据
     */
    public void init() {
        /**
         * 缓存lookup数据
         */
        XMLDefinitionFactory xmlDefinitionFactory = new XMLLookupFactory();
        Map<String, List> lookupMap = xmlDefinitionFactory.createXMLResolver().getResult();
        List<TSysLookupList> tSysLookupLists = lookupMap.get("tSysLookupLists"); // 获取通用数据

        if(CollectionUtils.isNotEmpty(tSysLookupLists)){
            insertLookup(tSysLookupLists);
        }
        recomposeLookup(tSysLookupLists); // key = companyId,value = 各种分组过的数据


        /**
         * 缓存lookup_item数据
         */
        XMLDefinitionFactory xmlLookupItemFactory = new XMLLookupItemFactory();
        Map<String, List> lookupItemMap = xmlLookupItemFactory.createXMLResolver().getResult();
        List<TSysLookupItemList> tSysLookupItemLists = lookupItemMap.get("tSysLookupItemLists");
        if(CollectionUtils.isNotEmpty(tSysLookupItemLists)){
            insertLookupItem(tSysLookupItemLists);
        }
        recomposeLookupItem(tSysLookupItemLists);
    }


    /**
     * 把数据插入lookup表
     * @param commonLookupList
     */
    private void insertLookup(List<TSysLookupList> commonLookupList){
        List<Map<String, Object>> companyList = getCompanyList();
        for (Map<String, Object> map : companyList) {
            String url = map.get("jdbc_url") + "";
            String username = map.get("jdbc_username") + "";
            String password = map.get("jdbc_password") + "";
            Connection connection = DBUtil.getConn(getPlatformDriver(), url, username, password);

            String delSql = "DELETE FROM t_sys_lookup";
            String sql = "INSERT INTO t_sys_lookup(lookup_id,`code`,`name`,`status`,item_order,description,modify_time)VALUES(?,?,?,'enable',?,?,SYSDATE())";
            PreparedStatement pstmt = null;
            try {
                connection.setAutoCommit(false);//关闭自动提交模式，此时必须通过显示调用commit()提交

                /**
                 * 先删除数据
                 */
                pstmt = connection.prepareStatement(delSql);
                pstmt.executeUpdate();


                /**
                 * 插入数据
                 */
                pstmt = connection.prepareStatement(sql);
                for (TSysLookupList tSysLookupList : commonLookupList) {
                    pstmt.setString(1,tSysLookupList.getLookupId());
                    pstmt.setString(2,tSysLookupList.getCode());
                    pstmt.setString(3,tSysLookupList.getName());
                    pstmt.setInt(4,tSysLookupList.getItemOrder());
                    pstmt.setString(5,tSysLookupList.getDescription());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();

                connection.commit();//提交事务
            } catch (SQLException e) {
                DBUtil.rollBack(connection);
                logger.error(e);
            } finally {
                DBUtil.closePst(pstmt);
                DBUtil.closeCon(connection);
            }
        }
    }


    /**
     * 把数据插入lookup_item
     * @param commonLookupItemList
     */
    private void insertLookupItem(List<TSysLookupItemList> commonLookupItemList){
        List<Map<String, Object>> companyList = getCompanyList();
        for (Map<String, Object> map : companyList) {
            String url = map.get("jdbc_url") + "";
            String username = map.get("jdbc_username") + "";
            String password = map.get("jdbc_password") + "";
            Connection connection = DBUtil.getConn(getPlatformDriver(), url, username, password);
            String delSql = "DELETE FROM t_sys_lookup_item";
            String sql = "INSERT INTO t_sys_lookup_item(lookup_item_id,lookup_id,`code`,`name`,parent_code,`status`,item_order,description,modify_time)VALUES(?,?,?,?,?,'enable',?,?,SYSDATE())";
            PreparedStatement pstmt = null;
            try {
                connection.setAutoCommit(false);//关闭自动提交模式，此时必须通过显示调用commit()提交

                /**
                 * 先删除数据
                 */
                pstmt = (PreparedStatement) connection.prepareStatement(delSql);
                pstmt.executeUpdate();


                /**
                 * 重新插入数据
                 */
                pstmt = (PreparedStatement) connection.prepareStatement(sql);
                for (TSysLookupItemList commonLookupItem : commonLookupItemList) {
                    pstmt.setString(1,commonLookupItem.getLookupItemId());
                    pstmt.setString(2,commonLookupItem.getLookupId());
                    pstmt.setString(3,commonLookupItem.getCode());
                    pstmt.setString(4,commonLookupItem.getName());
                    pstmt.setString(5,commonLookupItem.getParentCode());
                    pstmt.setInt(6,commonLookupItem.getItemOrder());
                    pstmt.setString(7,commonLookupItem.getDescription());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();

                connection.commit();//提交事务
            } catch (SQLException e) {
                DBUtil.rollBack(connection);
                logger.error(e);
            } finally {
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (SQLException e) {

                    }
                }
                DBUtil.closeCon(connection);
            }
        }
    }


    /**
     * 重组数据，把特性化数据替换通用数据
     * @param commonLookupList 通用数据
     * @return 重组后的数据
     */
    private void recomposeLookup(final List<TSysLookupList> commonLookupList){
        // 预留的扩展方法
        individualizedLookup(commonLookupList);

        List<Map<String, Object>> companyList = getCompanyList();
        for (Map<String, Object> map : companyList) {
            String companyId = map.get("company_id") + "";

            // 根据code分组
            Map<String, TSysLookupList> groupLookupByCodeMap = groupLookupByCode(commonLookupList);

            Map<String, Map> dataMap = new HashMap(1);
            dataMap.put("groupLookupByCodeMap", groupLookupByCodeMap);

            springRedisTools.deleteMap(ApplicationConstant.REDIS_TABLE_KEY_LOOKUP.getStringValue()+ "." + companyId);
            springRedisTools.addMap(ApplicationConstant.REDIS_TABLE_KEY_LOOKUP.getStringValue()+ "." + companyId, dataMap);
        }
    }

    /**
     * TODO 如果存在个性化数据，就用用个性化数据覆盖原来的数据(commonLookupList)
     * @param commonLookupList
     */
    private void individualizedLookup(final List<TSysLookupList> commonLookupList){

    }



    /**
     * 重组数据，把特性化数据替换通用数据
     * @param commonLookupItemList 通用数据
     * @return 重组后的数据
     */
    private void recomposeLookupItem(final List<TSysLookupItemList> commonLookupItemList){
        // 预留扩展方法，用个性化数据覆盖commonLookupItemList，然后再将commonLookupItemList进行分组放入缓存
        individualizedLookupItem(commonLookupItemList);

        List<Map<String, Object>> companyList = getCompanyList();
        for (Map<String, Object> map : companyList) {
            String companyId = map.get("company_id") + "";

            String groupLookupItemCodeByParentCodeAndNameMap = groupLookupItemCodeByParentCodeAndName(commonLookupItemList);

            String groupLookupItemNameByParentCodeAndCodeMap = groupLookupItemNameByParentCodeAndCode(commonLookupItemList);

            Map<String, List<TSysLookupItemList>> groupLookupItemByLookupIdAndParentCodeMap = groupLookupItemByLookupIdAndParentCode(commonLookupItemList);

            springRedisTools.deleteMap(ApplicationConstant.REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_CODE_BY_PARENT_CODE_AND_NAME_MAP.getStringValue() + "." + companyId);
            springRedisTools.addData(ApplicationConstant.REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_CODE_BY_PARENT_CODE_AND_NAME_MAP.getStringValue() + "." + companyId, groupLookupItemCodeByParentCodeAndNameMap);

            springRedisTools.deleteMap(ApplicationConstant.REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_NAME_BY_PARENT_CODE_AND_CODE_MAP.getStringValue() + "." + companyId);
            springRedisTools.addData(ApplicationConstant.REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_NAME_BY_PARENT_CODE_AND_CODE_MAP.getStringValue() + "." + companyId, groupLookupItemNameByParentCodeAndCodeMap);

            springRedisTools.deleteMap(ApplicationConstant.REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_BY_LOOKUP_ID_AND_PARENT_CODE_MAP.getStringValue() + "." + companyId);
            springRedisTools.addMap(ApplicationConstant.REDIS_TABLE_KEY_GROUP_LOOKUP_ITEM_BY_LOOKUP_ID_AND_PARENT_CODE_MAP.getStringValue() + "." + companyId, groupLookupItemByLookupIdAndParentCodeMap);
        }
    }



    /**
     * TODO 如果存在个性化数据，就用用个性化数据覆盖原来的数据(commonLookupItemList)
     * @param commonLookupItemList
     */
    private void individualizedLookupItem(final List<TSysLookupItemList> commonLookupItemList){

    }



    /**
     * 根据code分组
     * 因为code唯一，因此分组后的结果一个key只对应一个value
     * @param tSysLookupLists
     * @return
     */
    private Map<String, TSysLookupList> groupLookupByCode(List<TSysLookupList> tSysLookupLists){
        Map<String, TSysLookupList> result = new HashMap(tSysLookupLists.size());
        for (TSysLookupList tSysLookupList : tSysLookupLists) {
            String key = CommonUtils.null2String(tSysLookupList.getCode());
            result.put(key,tSysLookupList);
        }
        return result;
    }











    /**
     * 根据parent_code,name分组lookupItem Code
     * @param tSysLookupItemLists
     * @return
     */
    private String groupLookupItemCodeByParentCodeAndName(List<TSysLookupItemList> tSysLookupItemLists){
        JSONObject jsonObject = new JSONObject();
        for (TSysLookupItemList tSysLookupItemList : tSysLookupItemLists) {
            String key = CommonUtils.null2String(tSysLookupItemList.getParentCode()) + "." + CommonUtils.null2String(tSysLookupItemList.getName());
            if(jsonObject.containsKey(key)){
                continue;
            }else{
                jsonObject.put(key,tSysLookupItemList.getCode());
            }
        }
        return jsonObject.toJSONString();
    }


    /**
     * 根据parent_code,code分组lookupItem Name
     * @param tSysLookupItemLists
     * @return
     */
    private String groupLookupItemNameByParentCodeAndCode(List<TSysLookupItemList> tSysLookupItemLists){
        JSONObject jsonObject = new JSONObject();
        for (TSysLookupItemList tSysLookupItemList : tSysLookupItemLists) {
            String key = CommonUtils.null2String(tSysLookupItemList.getParentCode()) + "." + CommonUtils.null2String(tSysLookupItemList.getCode());
            if(jsonObject.containsKey(key)){
                continue;
            }else{
                jsonObject.put(key,tSysLookupItemList.getName());
            }
        }
        return jsonObject.toJSONString();
    }

    /**
     * 根据lookup_id,parent_code分组lookupItem
     * @param tSysLookupItemLists
     * @return
     */
    private Map<String, List<TSysLookupItemList>> groupLookupItemByLookupIdAndParentCode(List<TSysLookupItemList> tSysLookupItemLists){
        Map<String, List<TSysLookupItemList>> result = new HashMap(100);
        for (TSysLookupItemList tSysLookupItemList : tSysLookupItemLists) {
            String key = CommonUtils.null2String(tSysLookupItemList.getLookupId()) + "." + CommonUtils.null2String(tSysLookupItemList.getParentCode());
            if(result.containsKey(key)){
                result.get(key).add(tSysLookupItemList);
            }else{
                List<TSysLookupItemList> children = new ArrayList<>(10);
                children.add(tSysLookupItemList);
                result.put(key,children);
            }
        }
        return result;
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
