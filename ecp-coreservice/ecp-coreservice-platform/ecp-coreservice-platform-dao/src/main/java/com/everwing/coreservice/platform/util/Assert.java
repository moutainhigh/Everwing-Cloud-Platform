package com.everwing.coreservice.platform.util;

public class Assert {
	
	public static boolean notEmpty(Object obj) {
		if (obj instanceof String) {
			String str = (String) obj;
			if (str != null && !"".equals(str.trim())) {
				return true;
			}
		} else if (obj != null) {
			return true;
		}
		return false;
	}
	
	public static boolean notEqual(Object obj,Object compareValue) {
		if(obj!=null){
			return !obj.equals(compareValue);
		}
		return false;
	}
	
	public static void notEmpty(Object object,String message){
		if(object instanceof String){
			String s = (String)object;
			if(s==null||"".equals(s.trim())){
				throw new IllegalArgumentException(message);
			}
		}else if(object==null){
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void notNull(Object object,String message){
		if(object==null){
			throw new IllegalArgumentException(message);
		}
	}
}
