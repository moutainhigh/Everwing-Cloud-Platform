package com.everwing.coreservice.common.wy.initialization;/**
 * Created by wust on 2018/8/15.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:将所有数据源放入缓存
 * Reason:
 * Date:2018/8/15
 * @author wusongti@lii.com.cn
 */
@Component
public class InitializeDataSource extends AbstractInitializeData {
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
     * 增加公司后，设置到缓存
     * @param companyId
     */
    @Override
    public void init(String companyId) {
        Object obj = springRedisTools.getByKey(ApplicationConstant.REDIS_TABLE_KEY_DATA_SOURCE.name());
        if(obj != null){
            Map<String, Object> map = getCompanyById(companyId);
            if (map != null && map.size() > 0) {
                JSONObject jsonObject = JSONObject.parseObject(obj.toString());
                String companyName = map.get("company_name") + "";
                String url = map.get("jdbc_url") + "";
                String username = map.get("jdbc_username") + "";
                String password = map.get("jdbc_password") + "";

                JSONObject companyJsonObject = new JSONObject();
                companyJsonObject.put("companyId",companyId);
                companyJsonObject.put("companyName",companyName);
                companyJsonObject.put("jdbcUrl",url);
                companyJsonObject.put("jdbcUsername",username);
                companyJsonObject.put("jdbcPassword",password);
                jsonObject.put(companyId,companyJsonObject);

                springRedisTools.deleteByKey(ApplicationConstant.REDIS_TABLE_KEY_DATA_SOURCE.getStringValue());
                springRedisTools.addData(ApplicationConstant.REDIS_TABLE_KEY_DATA_SOURCE.getStringValue(), jsonObject.toJSONString());
            }
        }else{
            this.init();
        }

    }

    @Override
    public void init() {
        List<Map<String, Object>> companyList = getCompanyList();
        if (CollectionUtils.isNotEmpty(companyList)) {
            JSONObject jsonObject = new JSONObject();
            for (Map<String, Object> map : companyList) {
                String companyId = map.get("company_id") + "";
                String companyName = map.get("company_name") + "";
                String url = map.get("jdbc_url") + "";
                String username = map.get("jdbc_username") + "";
                String password = map.get("jdbc_password") + "";

                JSONObject companyJsonObject = new JSONObject();
                companyJsonObject.put("companyId",companyId);
                companyJsonObject.put("companyName",companyName);
                companyJsonObject.put("jdbcUrl",url);
                companyJsonObject.put("jdbcUsername",username);
                companyJsonObject.put("jdbcPassword",password);
                jsonObject.put(companyId,companyJsonObject);
            }
            springRedisTools.deleteByKey(ApplicationConstant.REDIS_TABLE_KEY_DATA_SOURCE.getStringValue());
            springRedisTools.addData(ApplicationConstant.REDIS_TABLE_KEY_DATA_SOURCE.getStringValue(), jsonObject);
        }
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
