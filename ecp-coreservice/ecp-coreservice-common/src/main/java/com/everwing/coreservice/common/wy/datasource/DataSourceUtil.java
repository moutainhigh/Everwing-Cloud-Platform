package com.everwing.coreservice.common.wy.datasource;

import com.everwing.coreservice.common.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataSourceUtil{
	
	private final static Logger log = LoggerFactory.getLogger(DataSourceUtil.class);
	
	public static void changeDataSource(String companyId){
		log.debug(">> 当前操作: {} {}","切换数据源","开始");
		log.debug(">> companyId : {} <<",companyId);
		if (CommonUtils.isEmpty(companyId)) {
			companyId = "0";
		} 
		DBContextHolder.setDBType(companyId);
		log.debug(">> 当前操作: {} {}","切换数据源","完成");
	}
	
}
