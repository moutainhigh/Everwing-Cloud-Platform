package com.everwing.coreservice.common.wy.initialization;/**
 * Created by wust on 2018/7/3.
 */

import com.everwing.coreservice.common.utils.jdbc.DBParams;
import com.everwing.coreservice.common.utils.jdbc.DBUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Function: 初始化数据抽象类
 * Reason:
 * Date:2018/7/3
 * @author wusongti@lii.com.cn
 */
public abstract class AbstractInitializeData {
    static Logger logger = LogManager.getLogger(AbstractInitializeData.class);

    public abstract void init(String companyId);

    public abstract void init();


    protected Map<String, Object> getCompanyById(String id) {
        Map<String, Object> result = new HashMap(1);
        final Connection connection = DBUtil.getConn(getPlatformDriver(), getPlatformUrl(), getPlatformUsername(), getPlatformPassword());
        String sql = "SELECT c.company_id,c.jdbc_url,c.jdbc_username,c.jdbc_password,c.company_name FROM company c WHERE c.company_id = ?";
        DBParams params = new DBParams();
        params.addParam(id);

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = connection.prepareStatement(sql);
            params.prepareStatement(pst);
            rs = pst.executeQuery();
            while(rs.next()){
                result.put("jdbc_url",rs.getString("jdbc_url"));
                result.put("jdbc_username",rs.getString("jdbc_username"));
                result.put("jdbc_password",rs.getString("jdbc_password"));
                result.put("company_name",rs.getString("company_name"));
            }
        } catch (SQLException e) {
            logger.error(e);
        }finally {
            DBUtil.closeRs(rs);
            DBUtil.closePst(pst);
            DBUtil.closeCon(connection);
        }
        return result;
    }


    /**
     * 获取所有公司列表
     * @return
     */
    protected List<Map<String, Object>> getCompanyList() {
        final Connection connection = DBUtil.getConn(getPlatformDriver(), getPlatformUrl(), getPlatformUsername(), getPlatformPassword());
        String sql = "SELECT c.company_id,c.jdbc_url,c.jdbc_username,c.jdbc_password,c.company_name FROM company c";
        try {
            List<Map<String, Object>> companyList = DBUtil.executeQuery(connection, sql);
            if (CollectionUtils.isNotEmpty(companyList)) {
                return companyList;
            }
        } catch (SQLException e) {
            logger.error(e);
        }finally {
            DBUtil.closeCon(connection);
        }
        return null;
    }


    protected abstract String getPlatformUrl();

    protected abstract String getPlatformUsername();

    protected abstract String getPlatformPassword();

    protected abstract String getPlatformDriver();
}
