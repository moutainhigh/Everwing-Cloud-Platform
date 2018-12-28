package com.everwing.server.wy.util;/**
 * Created by wust on 2017/9/20.
 */

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/20
 * @author wusongti@lii.com.cn
 */
public class CookieUtils {
    private CookieUtils(){}
    public static final int MAX_AGE = 60 * 60 * 24 * 7;

    /**
     * 添加COOKIE
     * @param response
     * @param key
     * @param value
     * @param maxAge
     * @throws UnsupportedEncodingException
     */
    public static void addCookie(HttpServletResponse response, String key,String value,int maxAge) throws UnsupportedEncodingException {
        value = URLEncoder.encode(value, "utf-8");
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 删除COOKIE
     * @param response
     * @param key
     */
    public static void deleteCookie(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 查找COOKIE
     * @param request
     * @param key
     * @return  String
     * @throws UnsupportedEncodingException
     */
    public static String findCookie(HttpServletRequest request, String key)
            throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (key.equals(cookies[i].getName())) {
                    return URLDecoder.decode(cookies[i].getValue(), "utf-8");
                }
            }
        }
        return null;
    }
}
