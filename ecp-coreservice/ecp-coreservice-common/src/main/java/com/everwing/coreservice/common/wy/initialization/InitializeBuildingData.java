package com.everwing.coreservice.common.wy.initialization;/**
 * Created by wust on 2018/6/30.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.jdbc.DBParams;
import com.everwing.coreservice.common.utils.jdbc.DBUtil;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Function:缓存建筑树结构
 * Reason:由于建筑树数据比较大，在构建树的时候很慢，因此需要系统启动时构造好树结构，并缓存起来。
 * Date:2018/6/30
 *
 * @author wusongti@lii.com.cn
 */
@Component
public class InitializeBuildingData extends AbstractInitializeData {
    static Logger logger = LogManager.getLogger(InitializeBuildingData.class);

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



    /**
     * 缓存指定公司的建筑数据
     *
     * @param companyId
     */
    public void init(String companyId) {
        Map<String, Object> companyMap = getCompanyById(companyId);
        if (companyMap != null) {
            String url = companyMap.get("jdbc_url") + "";
            String username = companyMap.get("jdbc_username") + "";
            String password = companyMap.get("jdbc_password") + "";
            Connection connection = DBUtil.getConn(getPlatformDriver(), url, username, password);
            String projectSql = "SELECT p.project_id,p.`code`,p.`name` FROM t_sys_project p WHERE p.`status` = 'enable'";
            try {
                List<Map<String, Object>> projectList = DBUtil.executeQuery(connection, projectSql);
                if (CollectionUtils.isNotEmpty(projectList)) {
                    JSONObject jsonObject = new JSONObject();
                    for (Map<String, Object> map : projectList) {
                        String projectCode = CommonUtils.null2String(map.get("code"));
                        String jsonStr = findBuildingByProjectCode(connection, projectCode);
                        jsonObject.put(projectCode,jsonStr);
                    }

                    springRedisTools.deleteByKey(ApplicationConstant.REDIS_TABLE_KEY_BUILDING_TREE.getStringValue() + "." + companyId);
                    springRedisTools.addData(ApplicationConstant.REDIS_TABLE_KEY_BUILDING_TREE.getStringValue() + "." + companyId, jsonObject.toJSONString());
                }
            } catch (SQLException e) {
                logger.error(e);
            } finally {
                DBUtil.closeCon(connection);
            }
        }


    }

    /**
     * 缓存所有公司的建筑数据
     */
    public void init() {
        List<Map<String, Object>> companyList = getCompanyList();
        if (CollectionUtils.isNotEmpty(companyList)) {
            String projectSql = "SELECT p.project_id,p.`code`,p.`name` FROM t_sys_project p WHERE p.`status` = 'enable'";
            for (Map<String, Object> map : companyList) {
                String companyId = map.get("company_id") + "";
                String url = map.get("jdbc_url") + "";
                String username = map.get("jdbc_username") + "";
                String password = map.get("jdbc_password") + "";
                Connection connection = DBUtil.getConn(getPlatformDriver(), url, username, password);
                try {
                    List<Map<String, Object>> projectList = DBUtil.executeQuery(connection, projectSql);
                    if (CollectionUtils.isNotEmpty(projectList)) {
                        JSONObject jsonObject = new JSONObject();
                        for (Map<String, Object> projectMap : projectList) {
                            String projectCode = CommonUtils.null2String(projectMap.get("code"));
                            String jsonStr = findBuildingByProjectCode(connection, projectCode);
                            jsonObject.put(projectCode,jsonStr);
                        }
                        springRedisTools.deleteByKey(ApplicationConstant.REDIS_TABLE_KEY_BUILDING_TREE.getStringValue() + "." + companyId);
                        springRedisTools.addData(ApplicationConstant.REDIS_TABLE_KEY_BUILDING_TREE.getStringValue() + "." + companyId, jsonObject.toJSONString());
                    }
                } catch (SQLException e) {
                    logger.error(e);
                } finally {
                    DBUtil.closeCon(connection);
                }
            }
        }
    }

    final static List<String> buildingTypeList = new ArrayList<>(10);
    {
        buildingTypeList.add(LookupItemEnum.buildingType_qi.getStringValue());
        buildingTypeList.add(LookupItemEnum.buildingType_qu.getStringValue());
        buildingTypeList.add(LookupItemEnum.buildingType_dongzuo.getStringValue());
        buildingTypeList.add(LookupItemEnum.buildingType_danyuanrukou.getStringValue());
        buildingTypeList.add(LookupItemEnum.buildingType_ceng.getStringValue());
        buildingTypeList.add(LookupItemEnum.buildingType_house.getStringValue());
        buildingTypeList.add(LookupItemEnum.buildingType_store.getStringValue());
        buildingTypeList.add(LookupItemEnum.buildingType_parkspace.getStringValue());
        buildingTypeList.add(LookupItemEnum.buildingType_parentOther.getStringValue());
        buildingTypeList.add(LookupItemEnum.buildingType_leafOther.getStringValue());
    }

    /**
     * 根据项目编码获取建筑
     *
     * @param connection
     * @param projectCode
     * @return
     */
    private String findBuildingByProjectCode(Connection connection, String projectCode) {
        String buildingSql = "SELECT b.project_id,b.id,b.building_code,b.pid,b.building_name,b.building_full_name,b.building_type,b.house_code,b.house_code_new FROM tc_building b WHERE b.project_id = ? OR b.building_code = 'jzjg' ORDER BY b.building_full_name";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = connection.prepareStatement(buildingSql.toString());
            DBParams dbParams = new DBParams();
            dbParams.addParam(projectCode);
            dbParams.prepareStatement(pst);
            rs = pst.executeQuery();

            JSONArray jsonArray = new JSONArray(2000);
            JSONObject root = new JSONObject();
            root.put("id", "jzjg");
            root.put("pId", null);
            root.put("buildingId", "");
            root.put("name", "建筑结构");
            root.put("fullname", "建筑结构");
            root.put("houseCode", "");
            root.put("houseCodeNew", "");
            root.put("buildingType", "");
            root.put("isParent", true);
            root.put("open", true);
            jsonArray.add(root);

            while (rs.next()) {
                String id = CommonUtils.null2String(rs.getString("id"));
                String buildingCode = CommonUtils.null2String(rs.getString("building_code"));
                String pid = CommonUtils.null2String(rs.getString("pid"));
                String buildingName = CommonUtils.null2String(rs.getString("building_name"));
                String buildingFullName = CommonUtils.null2String(rs.getString("building_full_name"));
                String buildingType = CommonUtils.null2String(rs.getString("building_type"));
                String houseCode = CommonUtils.null2String(rs.getString("house_code"));
                String houseCodeNew = CommonUtils.null2String(rs.getString("house_code_new"));

                if(!buildingTypeList.contains(buildingType)){
                    continue;
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", buildingCode);
                jsonObject.put("pId", pid);
                jsonObject.put("buildingId", id);
                jsonObject.put("name", buildingName);
                jsonObject.put("fullname", buildingFullName);
                jsonObject.put("houseCode", houseCode);
                jsonObject.put("houseCodeNew", houseCodeNew);
                jsonObject.put("buildingType", buildingType);
                if(LookupItemEnum.buildingType_parkspace.getStringValue().equals(buildingType) ||
                        LookupItemEnum.buildingType_store.getStringValue().equals(buildingType) ||
                        LookupItemEnum.buildingType_house.getStringValue().equals(buildingType)) {
                    jsonObject.put("isParent", false);
                }else{
                    jsonObject.put("isParent", true);
                }
                jsonArray.add(jsonObject);
            }
            return jsonArray.toJSONString();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            DBUtil.closeRs(rs);
            DBUtil.closePst(pst);
        }
        return null;
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
