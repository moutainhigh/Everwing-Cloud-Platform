package com.everwing.coreservice.dynamicreports.core.dsaspect;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.context.DynamicreportsBusinessContext;
import com.everwing.coreservice.common.dynamicreports.entity.system.datasource.TSysDataSource;
import com.everwing.coreservice.common.dynamicreports.entity.system.lookup.TSysLookupItemQO;
import com.everwing.coreservice.common.dynamicreports.entity.system.lookup.TSysLookupItemVO;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import com.everwing.coreservice.dynamicreports.dao.mapper.system.datasource.TSysDataSourceMapper;
import com.everwing.coreservice.dynamicreports.dao.mapper.system.lookup.TSysLookupItemMapper;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 切换数据源
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect {
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceAspect.class);

	@Autowired
	private TSysLookupItemMapper tSysLookupItemMapper;


	@Autowired
	private TSysDataSourceMapper tSysDataSourceMapper;

	@Around("execution(* com.everwing.coreservice.dynamicreports.core.service.impl..*.*(..))")
	public Object methodAspect(ProceedingJoinPoint jp) throws Throwable{
		Object[] args = jp.getArgs();
		boolean isDefaultDataSource = true;
		if (args.length>0) {
			if (args[0] instanceof DynamicreportsBusinessContext) {
				DynamicreportsBusinessContext dbctx = (DynamicreportsBusinessContext)args[0];
				if(ApplicationConstant.userType_reportPlatform.getStringValue().equalsIgnoreCase(dbctx.getUserType())){
				}else{
					LOG.info("非报表平台请求，切换数据源开始............");
					isDefaultDataSource = false;
					TSysDataSource tSysDataSource = new TSysDataSource();
					tSysDataSource.setUserId(dbctx.getUserId());
					List<TSysDataSource> tSysDataSources =  tSysDataSourceMapper.findByCondition(tSysDataSource);
					if(CollectionUtils.isNotEmpty(tSysDataSources)){
						String lookupId = tSysDataSources.get(0).getLookupId();
						TSysLookupItemQO tSysLookupItemQO = new TSysLookupItemQO();
						tSysLookupItemQO.setLookupItemId(lookupId);
						List<TSysLookupItemVO> tSysLookupItemVOS = tSysLookupItemMapper.findByCondition(tSysLookupItemQO);
						if(CollectionUtils.isNotEmpty(tSysLookupItemVOS)){
							TSysLookupItemVO tSysLookupItemVO = tSysLookupItemVOS.get(0);

							JSONObject jsonObject = new JSONObject();
							jsonObject.put("companyId",tSysLookupItemVO.getColumn4());
							jsonObject.put("companyName",tSysLookupItemVO.getName());
							jsonObject.put("jdbcUsername",tSysLookupItemVO.getColumn2());
							jsonObject.put("jdbcPassword",tSysLookupItemVO.getColumn3());
							jsonObject.put("jdbcUrl",tSysLookupItemVO.getColumn1());

							DynamicreportsBusinessContext.getContext().setDataSourceJsonObject(jsonObject);
							LOG.info("切换数据源成功");
						}else{
							LOG.info("根据lookupItemId获取到的t_sys_lookup_item记录为空，保留默认数据源");
						}
					}else{
						LOG.info("根据userId获取到的t_sys_data_source记录为空，保留默认数据源");
					}
					LOG.info("非报表平台请求，切换数据源结束............");
				}
			}
		}

		if(isDefaultDataSource){
			LOG.info("报表平台请求，切换数据源开始............");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("companyId",ApplicationConstant.userType_reportPlatform.getStringValue());
			jsonObject.put("companyName","报表平台");
			jsonObject.put("jdbcUsername","root");
			jsonObject.put("jdbcPassword","m123");
			jsonObject.put("jdbcUrl","jdbc:mysql://10.1.20.11:3306/report_platform?useSSL=false&allowPublicKeyRetrieval=true&allowMultiQueries=true");
			DynamicreportsBusinessContext.getContext().setDataSourceJsonObject(jsonObject);
			LOG.info("切换数据源成功");
			LOG.info("报表平台请求，切换数据源结束............");
		}
		return jp.proceed(jp.getArgs());
	}
}
