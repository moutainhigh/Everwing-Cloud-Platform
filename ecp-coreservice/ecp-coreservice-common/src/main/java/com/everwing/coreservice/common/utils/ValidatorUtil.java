package com.everwing.coreservice.common.utils;

/**
 * 验证类
 * @author Yu
 *
 */
public class ValidatorUtil {
	
	/***
	 * 粗验身份证号码位数
	 * @param idcard
	 * @return
	 */
	public static boolean isIdcard(String idcard) {
		try{
		
			if(idcard == null || "".equals(idcard))
				return false;
			
			if(java.util.regex.Pattern.matches("(^\\d{15}$)", idcard))
				return is15Idcard(idcard);
			if(java.util.regex.Pattern.matches("(\\d{17}(?:\\d|x|X)$)", idcard))
				return is18Idcard(idcard);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
    }
	
    /** 
     * 18位身份证号码的基本数字和位数验校 
     *  
     * @param idcard 
     * @return 
     */  
    public static boolean is18Idcard(String idcard) throws Exception {  
    	return idcard == null || "".equals(idcard) ? false : java.util.regex.Pattern.matches(
    			"^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$",idcard);  
    }
    
    /** 
     * 15位身份证号码的基本数字和位数验校 
     *  
     * @param idcard 
     * @return 
     */  
    public static boolean is15Idcard(String idcard) throws Exception{  
        return idcard == null || "".equals(idcard) ? false : java.util.regex.Pattern.matches(  
                "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$",idcard);  
    } 
}
