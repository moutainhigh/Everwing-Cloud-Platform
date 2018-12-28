package com.everwing.coreservice.wy.core.utils;

import com.everwing.coreservice.common.utils.CommonUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @TODO 读取ftl文件用
 * @author DELL
 *
 */
@Service
public class FtlUtils {
	
	private static final Logger logger = LogManager.getLogger(FtlUtils.class);
	
	private static ConcurrentHashMap<String, Template> templateMap = new ConcurrentHashMap<String, Template>();
	
	
	public Template getFtlConfig(String configPath,String fileName){
		Template template = null;
		
		if(CommonUtils.isEmpty(configPath) || CommonUtils.isEmpty(fileName)){
			logger.warn("获取freemarker : [传入ftl文件路径为空] , 获取freemarker模板失败. ");
			return null;
		}
		
		String absPath = configPath + File.separator + fileName;

		if(CommonUtils.isEmpty(templateMap.get(absPath))){
			Configuration config = new Configuration();
			try {
				File file = ResourceUtils.getFile("classpath:" + configPath);
				config.setDirectoryForTemplateLoading(file);		
				
				template = config.getTemplate(fileName);
				
				if(null == template){
					logger.warn("获取freemarker : [未获取到文件模板] , 传入路径: {}",absPath);
				}else{
					templateMap.put(absPath, template);
				}
			} catch (IOException e) {
				logger.warn("获取freemarker : [传入文件路径未读取到模板] , 传入路径: {}",absPath);
			}
		}else{
			template = templateMap.get(absPath);
		}
		return template;
	}
}
