package com.everwing.utils;/**
 * Created by wust on 2017/8/24.
 */

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.util.Date;


/**
 *
 * Function:JWT抽象工具
 * Reason:具体用法可参见物业平台的登录和授权过程
 * Date:2017/8/24
 * @author wusongti@lii.com.cn
 */
public abstract class AbstractJwtHelper {

    /**
     * 每个模块需要实现自己的产生key方法
     * @return
     */
    protected abstract SecretKey generalKey() throws UnsupportedEncodingException;

    /**
     * 创建jwt默认实现
     * @param id
     * @param subject
     * @param minutes  分钟
     * @return
     * @throws Exception
     */
    public String createJWT(String id,String subject, int minutes) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, secretKey);
        if (minutes >= 0) {
            Date exp =  new DateTime().plusMinutes(minutes).toDate();
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     * @param jwt
     * @return
     * @throws Exception
     */
    public Claims parseJWT(String jwt) throws Exception{
        SecretKey secretKey = generalKey();
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}
