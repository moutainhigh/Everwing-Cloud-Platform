package com.everwing.coreservice.wy.core.utils;

import java.io.IOException;
import java.util.Properties;

public class MqPropertiesUtil {

	private static MqPropertiesUtil util = null;

	private static Properties properties;

	private MqPropertiesUtil(){}

	public static synchronized MqPropertiesUtil getInstance(){
		if(null == util){
			util = new MqPropertiesUtil();
			util.init();
		}
		return util;
	}


	private void init(){
		String path = "config/mq-config.properties";
		properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getV(String k){
		if(k != null && k.endsWith(".key")){
			return properties.getProperty(k) + "_" + properties.getProperty("rabbitmq.environment");
		}
		return properties.getProperty(k);
	}

	public static void main(String[] args) {
		System.out.println(MqPropertiesUtil.getInstance().getV("queue.personCust.key"));
	}

}
