package com.everwing.autotask.core.datasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.everwing.autotask.core.service.CompanyService;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * 多数据源
 *
 * @author DELL shiny
 * @create 2018/5/8
 */
public class MultipleDataSource extends AbstractRoutingDataSource {

    private static final Logger logger= LogManager.getLogger(MultipleDataSource.class);

    private DataSource defaultTargetDataSource;

    private static final Map<String,DruidDataSource> targetDataSource =new HashMap<>();

    @Override
    protected Object determineCurrentLookupKey() {
        String companyId=DBContextHolder.getDBType();
        if(companyId==null||companyId=="dataSource"){
            companyId="dataSource";
        }
        return companyId;
    }

    @Override
    protected DataSource determineTargetDataSource() {
        String dsName=determineCurrentLookupKey().toString();
        if("dataSource".equals(dsName)){
            return defaultTargetDataSource;
        }else {
            selectDataSource(dsName);
        }
        return targetDataSource.get(dsName);
    }

    public void addTargetDataSource(String key, DruidDataSource dataSource) {
        targetDataSource.put(key, dataSource);
    }

    private DruidDataSource getDataSource(String dbtype) {
        String oriType = DBContextHolder.getDBType();
        // 先切换回主库
        DBContextHolder.setDBType("dataSource");
        // 查询所需信息
        CompanyService companyService=SpringContextHolder.getBean("companyServiceImpl");
        List<Company> companyList=companyService.queryAllCompany();
        if(companyList!=null&&companyList.size()>0) {
            targetDataSource.clear();
            for (int i=0;i<companyList.size();i++){
                Company company=companyList.get(i);
                DruidDataSource druidDataSource =createDataSource(company);
                targetDataSource.put(company.getCompanyId(),druidDataSource);
            }
        }
        return targetDataSource.get(dbtype);
    }


    //创建数据源
    private DruidDataSource createDataSource(Company company) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(company.getJdbcUrl());
        druidDataSource.setUsername(company.getJdbcUsername());
        druidDataSource.setPassword(company.getJdbcPassword());
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setUseGlobalDataSourceStat(true);
        druidDataSource.setInitialSize(10);
        druidDataSource.setMaxActive(20);
        Properties properties=new Properties();
        properties.setProperty("druid.stat.mergeSql","true");
        properties.setProperty("druid.stat.slowSqlMillis","5000");
        try {
            druidDataSource.setFilters("stat,wall,log4j2");
            WallFilter wallFilter=new WallFilter();
            WallConfig wallConfig=new WallConfig();
            wallConfig.setMultiStatementAllow(true);
            wallConfig.setNoneBaseStatementAllow(true);
            wallFilter.setConfig(wallConfig);
            List<Filter> filters=druidDataSource.getProxyFilters();
            boolean exits=false;
            for(Filter filter:filters){
                if(filter instanceof WallFilter){
                    ((WallFilter) filter).setConfig(wallConfig);
                    exits=true;
                }
            }
            if(!exits){
                filters=new ArrayList<>(1);
                filters.add(wallFilter);
                druidDataSource.setProxyFilters(filters);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }

    private synchronized DataSource selectDataSource(String dbType) {
        // 再次从数据库中获取，双检锁
        DataSource obj = targetDataSource.get(dbType);
        if (null != obj) {
            return obj;
        }
        // 为空则创建数据库
        DruidDataSource dataSource = this.getDataSource(dbType);
        if (null != dataSource) {
            // 将新创建的数据库保存到map中
            this.setDataSource(dbType, dataSource);
            return dataSource;
        }else {
            logger.error("创建数据源失败!");
            return null;
        }
    }

    public void setDataSource(String type, DruidDataSource dataSource) {
        this.addTargetDataSource(type, dataSource);
        DBContextHolder.setDBType(type);
    }

    public void setDefaultTargetDataSource(DataSource defaultTargetDataSource) {
        this.defaultTargetDataSource = defaultTargetDataSource;
    }
}
