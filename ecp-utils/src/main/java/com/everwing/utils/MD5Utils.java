package com.everwing.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Utils {


    public static String MD5(String source){
        try {
            MessageDigest messageDigest=MessageDigest.getInstance("MD5");
            messageDigest.update(source.getBytes());
            return new BigInteger(1,messageDigest.digest()).toString(16);
        }catch (Exception e){
        }
        return null;
    }
}
