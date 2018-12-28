package com.everwing.utils;

public class TokenGenUtils {

    /**
     * 相对唯一的生成种子
     * @param accountId 账号ID
     * @return token字符串
     */
    public static String generateToken(String accountId){
        String source=accountId+System.currentTimeMillis();
        return MD5Utils.MD5(source);
    }

}
