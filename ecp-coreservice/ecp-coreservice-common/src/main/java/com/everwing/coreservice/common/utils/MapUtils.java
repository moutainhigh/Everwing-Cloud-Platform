package com.everwing.coreservice.common.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

	private static Map<String,String> map = new HashMap<String,String>();
	
	
	public static Map<String,String> getMap(){
		return map;
	}
	
	public static void setMap(Map<String,String> paramMap){
		MapUtils.map = paramMap;
	}
	
	public static Map<String,String> putAll(Map<String,String> paramMap){
		map.putAll(paramMap);
		return map;
	}
	
}
