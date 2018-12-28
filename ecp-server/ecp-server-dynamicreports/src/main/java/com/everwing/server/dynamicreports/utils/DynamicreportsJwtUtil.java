package com.everwing.server.dynamicreports.utils;/**
 * Created by wust on 2017/8/25.
 */

import com.alibaba.fastjson.JSONObject;
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
public class DynamicreportsJwtUtil extends AbstractJwtHelper {
    private DynamicreportsJwtUtil(){}

    public static final String JWT_ID = "jwt_report";

    public static final String JWT_SECRET = "be18687c-0adc-11e8-80c8-0050568e00c5";

    private static DynamicreportsJwtUtil instance = null;
    public static DynamicreportsJwtUtil getInstance(){
        if(instance == null){
            instance = new DynamicreportsJwtUtil();
        }
        return instance;
    }

    /**
     * 根据配置文件生成key
     * @return
     */
    @Override
    protected SecretKey generalKey() throws UnsupportedEncodingException {
        String key = PropertiesHelper.getInstance("config/server-dynamicreports.properties").getValue("profiles");

        String stringKey = key + JWT_SECRET;

        byte[] encodedKey = Base64.decodeBase64(stringKey);
        SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return secretKey;
    }


    /**
     * 生成token
     * @param jsonObject
     * @param minutes 分钟
     * @return
     * @throws Exception
     */
    public String createToken(JSONObject jsonObject, int minutes) throws Exception {
        String subject = jsonObject.toJSONString();
        String token = super.createJWT(JWT_ID,subject, minutes);
        return token;
    }

    /**
     * 生成刷新token
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public String createRefreshToken(JSONObject jsonObject,int minutes) throws Exception {
        String subject = jsonObject.toJSONString();
        String refreshToken = super.createJWT(JWT_ID,subject, minutes);
        return refreshToken;
    }

    /**
     * 解析token
     * @param jwt
     * @return
     * @throws Exception
     */
    public JSONObject parseToken(String jwt) throws Exception{
        Claims claims = super.parseJWT(jwt);
        String json = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(json);
        return jsonObject;
    }
}
