package com.everwing.coreservice.platform.util;

import com.everwing.coreservice.common.admin.util.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;

/**
 * @description Mybatis分页插件
 * @author MonKong
 * @date 2017年4月26日
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = {
		Connection.class }) })
public class PageInterceptor implements Interceptor {
	public static final String PARAM_NAME = "pageBean";// 唯一入参名字
	/*
	 * 拦截器要执行的方法
	 */

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		MappedStatement mappedStatement = (MappedStatement) metaObject
				.getValue("delegate.mappedStatement");
		String id = mappedStatement.getId();
		if (id.matches(".+ByPage$")) {
			BoundSql boundSql = statementHandler.getBoundSql();
			Map<String, Object> parameterObject = (Map<String, Object>) boundSql
					.getParameterObject();
			Object param = parameterObject.get(PARAM_NAME);
			if (param == null) {
				throw new RuntimeException(PARAM_NAME + " is null ~!");
			}
			PageBean pageBean = (PageBean) param;
			String sql = boundSql.getSql();
			String countSql = "select count(*) from (" + sql + ") a";
			Connection connection = (Connection) invocation.getArgs()[0];
			PreparedStatement countStatement = connection.prepareStatement(countSql);
			ParameterHandler parameterHandler = (ParameterHandler) metaObject
					.getValue("delegate.parameterHandler");
			parameterHandler.setParameters(countStatement);
			ResultSet rs = countStatement.executeQuery();
			if (rs.next()) {
				pageBean.setTotalSize(rs.getInt(1));
			}
			if(!StringUtils.isAnyBlank(pageBean.getOrder(),pageBean.getSort())){
				sql = sql+" order by " + pageBean.getSort() + " " + pageBean.getOrder();
			}
			String pageSql = sql + " limit " + pageBean.getLimitStart() + ","
					+ pageBean.getLimitEnd();
			metaObject.setValue("delegate.boundSql.sql", pageSql);
		}
		return invocation.proceed();
	}

	/*
	 * 拦截器需要拦截的对象
	 */
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/*
	 * 设置初始化的属性值
	 */
	public void setProperties(Properties properties) {

	}
}