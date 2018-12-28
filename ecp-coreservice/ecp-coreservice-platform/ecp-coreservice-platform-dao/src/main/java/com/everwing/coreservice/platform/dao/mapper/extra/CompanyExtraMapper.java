package com.everwing.coreservice.platform.dao.mapper.extra;

import org.apache.ibatis.annotations.Param;

public interface CompanyExtraMapper {
	
	//@Deprecated
	//int createCompanyDB(@Param("dbName") String dbName,@Param("user") String user,@Param("password") String password);
	
	/**
	 * @description 创建表结构
	 */
	int wy_ddl(@Param("dbName") String dbName,
			   @Param("user") String user,
			   @Param("password") String password,
			   @Param("ddlTableSql") String ddlTableSql,
			   @Param("ddlFunctionSql") String ddlFunctionSql);
	
	/**
	 * @description 初始化数据
	 */
	int wy_dml(@Param("dbName") String dbName,
			   @Param("dmlSql") String dmlSql);
	
	/**
	 * @description 初始化用户
	 */
	int init_user(@Param("dbName") String dbName,
				  @Param("accountName") String accountName,
				  @Param("companyCode") String companyCode,
				  @Param("companyId") String companyId,
				  @Param("companyName") String companyName,
				  @Param("userCode") String userCode);
	
}
