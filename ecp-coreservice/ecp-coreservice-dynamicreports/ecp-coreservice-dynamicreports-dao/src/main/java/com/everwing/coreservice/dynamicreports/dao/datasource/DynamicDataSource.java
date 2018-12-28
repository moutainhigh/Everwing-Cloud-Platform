package com.everwing.coreservice.dynamicreports.dao.datasource;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.context.DynamicreportsBusinessContext;
import com.everwing.utils.Validate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * 动态数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	private final String DRIVER = "com.mysql.jdbc.Driver";
	private final String JDBCURL = "jdbcUrl";
	private final String JDBCUSERNAME = "jdbcUsername";
	private final String JDBCPASSWORD = "jdbcPassword";
	private final String COMPANYID = "companyId";


	private java.util.Map<Object, Object> targetDataSources;

	@Override
	protected Object determineCurrentLookupKey() {
		JSONObject dataSourceJsonObject = DynamicreportsBusinessContext.getContext().getDataSourceJsonObject();
		Validate.notNull(dataSourceJsonObject,"当前上下文环境的数据源JSON对象为空");

		String ctxCompanyId = dataSourceJsonObject.getString(COMPANYID);
		Validate.notBlank(ctxCompanyId,"参数companyId的值不能为空");

		// 搜索数据源，不存在则创建
		lookupAndCreateDataSource(dataSourceJsonObject);

		return ctxCompanyId;
	}





	/**
	 * 搜索数据源，如果数据源不存在则创建数据源，并把创建的数据源添加到目标数据源
	 * @param dataSourceJsonObject
	 */
	private void lookupAndCreateDataSource(JSONObject dataSourceJsonObject) {
		String ctxCompanyId = dataSourceJsonObject.getString(COMPANYID);
		if(targetDataSources != null && targetDataSources.size() > 0){
			Object obj = this.targetDataSources.get(ctxCompanyId);
			if (obj != null) {
				return;
			} else {
				DriverManagerDataSource dataSource = createDataSource(dataSourceJsonObject);
				Validate.notNull(dataSource,"创建数据源失败");
				addTargetDataSource(ctxCompanyId,dataSource);
			}
		}else{
			DriverManagerDataSource dataSource = createDataSource(dataSourceJsonObject);
			Validate.notNull(dataSource,"创建数据源失败");
			addTargetDataSource(ctxCompanyId,dataSource);
		}
	}


	/**
	 * 创建数据源
	 * @param dataSourceJsonObject
	 * @return
	 */
	private DriverManagerDataSource createDataSource(JSONObject dataSourceJsonObject) {
		DynamicreportsBusinessContext.getContext().setDataSourceJsonObject(dataSourceJsonObject);
		String driverClassName = DRIVER;
		if(dataSourceJsonObject.containsKey(JDBCURL)
				|| dataSourceJsonObject.containsKey(JDBCUSERNAME)
				|| dataSourceJsonObject.containsKey(JDBCPASSWORD)) {

			String companyId = dataSourceJsonObject.getString(COMPANYID);
			String jdbcUrl = dataSourceJsonObject.getString(JDBCURL);
			String jdbcUsername = dataSourceJsonObject.getString(JDBCUSERNAME);
			String jdbcPassword = dataSourceJsonObject.getString(JDBCPASSWORD);

			Validate.notBlank(companyId, "参数companyId的值不能为空");
			Validate.notBlank(jdbcUrl, "参数jdbcUrl的值不能为空");
			Validate.notBlank(jdbcUsername, "参数jdbcUsername的值不能为空");
			Validate.notBlank(jdbcPassword, "参数jdbcPassword的值不能为空");

			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUsername(jdbcUsername);
			dataSource.setPassword(jdbcPassword);
			dataSource.setUrl(jdbcUrl.replaceAll("&amp;", "&"));
			return dataSource;
		}
		return null;
	}


	public void addTargetDataSource(String key,DriverManagerDataSource dataSource) {
		this.targetDataSources.put(key, dataSource);
		this.setTargetDataSources(this.targetDataSources);
	}

	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		this.targetDataSources = targetDataSources;
		super.setTargetDataSources(this.targetDataSources);
		afterPropertiesSet();
	}
}
