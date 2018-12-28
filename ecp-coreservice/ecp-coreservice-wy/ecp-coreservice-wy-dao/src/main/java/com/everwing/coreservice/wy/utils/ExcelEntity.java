/**
 * @Title: ExcelEntity.java
 * @Package com.flf.entity
 * @Description: 导出公共类，存放InputStream
 * Copyright: Copyright (c) 2011 
 * Company:武汉闻风多奇软件有限公司
 * @date 2016年8月21日
 * @version V1.0
 */
package com.everwing.coreservice.wy.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: ExcelEntity
 * @Description: 导出公共类，存放InputStream
 * @author ylq
 * @date 2016年8月21日
 */
public class ExcelEntity {
	
	private ExcelEntity(){}
	
	private static java.util.Map<String,JSONObject> _UserMap =new java.util.HashMap<String,JSONObject>();
	
	private static java.util.Map<String,java.io.InputStream> maps=new java.util.HashMap<String,java.io.InputStream>();

	private static final Integer MAX_SIZE = 30;//默认最大存储30个InputStream
	
	private static final Integer USER_MAX_SIZE = 20000;//默认最大存储20000个TUser
	
	public static java.io.InputStream optValue(String key) {
		return maps.get(key);
	}
	
	public static JSONObject getUser(String key) {
		return _UserMap.get(key);
	}
	
	public static void addUser(String key,JSONObject user) {
		if(_UserMap.size() >= USER_MAX_SIZE){
			java.util.Set<String> keySet = _UserMap.keySet();
			java.util.Iterator<String> iterator = keySet.iterator();
            String firstKey = "";               
            if (iterator.hasNext()){
                firstKey = iterator.next();
            }
            if(!"".equals(firstKey)){
            	_UserMap.remove(firstKey);
            }
		}else{
			_UserMap.put(key, user);
		}
	}
	
//	public static void removeUser(String key) {
//    	_UserMap.remove(key);
//	}

	public static String addValue(java.io.InputStream is) {
		if(maps.size() >= MAX_SIZE){
			java.util.Set<String> keySet = maps.keySet();
			java.util.Iterator<String> iterator = keySet.iterator();
            String firstKey = "";               
            if (iterator.hasNext()){
                firstKey = iterator.next();
            }
            if(!"".equals(firstKey)){
            	maps.remove(firstKey);
            }
		}
		String key = java.util.UUID.randomUUID().toString();
		maps.put(key, is);
		return key;
	}
}
