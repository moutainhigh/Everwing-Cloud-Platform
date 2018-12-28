package com.everwing.coreservice.common.wy.datasource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;

public class DynamicDataSource extends org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource {

	private java.util.Map<Object, Object> targetDataSources;
	
	private static java.util.List<String> listCompanyId = new java.util.ArrayList<String>();

	@Override
	protected Object determineCurrentLookupKey() {
		String companyId = DBContextHolder.getDBType();
		if (companyId == null) {
			companyId = "dataSource";
		} else {
			this.selectDataSource(companyId);
			if (companyId.equals("0"))
				companyId = "dataSource";
		}
		return companyId;
	}
	
	public static java.util.List<String> getListCompanyId() {
		return listCompanyId;
	}

	private static void addListCompanyId(String companyId) {
		if(CommonUtils.isNotEmpty(companyId) && !listCompanyId.contains(companyId)){
			listCompanyId.add(companyId);
		}
	}

	public void setTargetDataSources(java.util.Map<Object, Object> targetDataSources) {
		this.targetDataSources = targetDataSources;
		super.setTargetDataSources(this.targetDataSources);
		afterPropertiesSet();
	}

	public void addTargetDataSource(String key,
			org.springframework.jdbc.datasource.DriverManagerDataSource dataSource) {
		this.targetDataSources.put(key, dataSource);
		this.setTargetDataSources(this.targetDataSources);
	}

	public org.springframework.jdbc.datasource.DriverManagerDataSource createDataSource(String driverClassName,
			String url, String username, String password) {
		org.springframework.jdbc.datasource.DriverManagerDataSource dataSource = 
				new org.springframework.jdbc.datasource.DriverManagerDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setUrl(url.replaceAll("&amp;", "&"));
		return dataSource;
	}

	/**
	 * @describe 数据源存在时不做处理，不存在时创建新的数据源链接，并将新数据链接添加至缓存
	 */
	public void selectDataSource(String companyId) {
		String sid = DBContextHolder.getDBType();
		if (CommonUtils.isEquals(Constants.STR_ZERO, companyId)) {
			DBContextHolder.setDBType("0");
			return;
		}
		Object obj = this.targetDataSources.get(companyId);
		if (obj != null && CommonUtils.isEquals(sid, companyId)) {
			return;
		} else {
			org.springframework.jdbc.datasource.DriverManagerDataSource dataSource = this.getDataSource(companyId);
			if (null != dataSource)
				this.setDataSource(companyId, dataSource);
		}
	}
	
	/**
	 * @describe 查询serverId对应的数据源记录
	 * @return
	 */
	public org.springframework.jdbc.datasource.DriverManagerDataSource getDataSource(String companyInfoStr) {

		String driverClassName ="com.mysql.jdbc.Driver";
		try{
			JSONObject jsonObj = JSON.parseObject(companyInfoStr);
			if(null == jsonObj)
				return null;
			String url = jsonObj.getString("jdbcUrl");
			String userName = jsonObj.getString("jdbcUsername");
			String password = jsonObj.getString("jdbcPassword");
			
			return	this.createDataSource(driverClassName,url, userName, password);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param dataSource
	 */
	public void setDataSource(String companyId,
			org.springframework.jdbc.datasource.DriverManagerDataSource dataSource) {
		this.addTargetDataSource(companyId + "", dataSource);
		addListCompanyId(companyId);
		DBContextHolder.setDBType(companyId + "");
	}
}
