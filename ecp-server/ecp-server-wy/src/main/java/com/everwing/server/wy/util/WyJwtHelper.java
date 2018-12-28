package com.everwing.server.wy.util;/**
 * Created by wust on 2017/8/25.
 */

import com.everwing.coreservice.common.utils.PropertiesHelper;
import com.everwing.utils.AbstractJwtHelper;
import io.jsonwebtoken.Claims;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
 *
 * Function:物业模块的JWT工具
 * Reason:
 * Date:2017/8/25
 * @author wusongti@lii.com.cn
 */
public class WyJwtHelper extends AbstractJwtHelper {
    private WyJwtHelper(){}

    public static final String JWT_ID = "jwt";

    public static final String JWT_SECRET = "7786df7fc3a34e26a61c034d5ec8245d";

    private static WyJwtHelper instance = null;
    public static WyJwtHelper getInstance(){
        if(instance == null){
            instance = new WyJwtHelper();
        }
        return instance;
    }

    /**
     * 根据配置文件生成key
     * @return
     */
    @Override
    protected SecretKey generalKey() throws UnsupportedEncodingException {
        String key = PropertiesHelper.getInstance("config/conf.properties").getValue("profiles");

        String stringKey = key + JWT_SECRET;

        byte[] encodedKey = Base64.decodeBase64(stringKey);
        SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return secretKey;
    }


    /**
     * 生成token
     * @param key
     * @param minutes
     * @return
     * @throws Exception
     */
    public String createWyLoginToken(String key,int minutes) throws Exception {
        String token = super.createJWT(JWT_ID,key, minutes);
        return token;
    }


    /**
     * 解析token
     * @param jwt
     * @return
     * @throws Exception
     */
    public String parseWyLoginToken(String jwt) throws Exception{
        Claims claims = super.parseJWT(jwt);
        String token = claims.getSubject();
        return token;
    }
}
