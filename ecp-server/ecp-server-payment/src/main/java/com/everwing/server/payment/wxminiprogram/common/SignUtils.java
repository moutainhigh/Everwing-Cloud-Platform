package com.everwing.server.payment.wxminiprogram.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @Author: zgrf
 * @Description: 签名工具类
 * @Date: Create in 2018-08-16 01:38
 **/

public class SignUtils {

    public static String createSign(Object xmlBean, String signType, String signKey, boolean isIgnoreSignType) {
        return createSign(xmlBean2Map(xmlBean), signType, signKey, isIgnoreSignType);
    }

    public static String createSign(Map<String, String> params, String signType, String signKey, boolean ignoreSignType) {
        SortedMap<String, String> sortedMap = new TreeMap<>(params);

        StringBuilder toSign = new StringBuilder();
        for (String key : sortedMap.keySet()) {
            String value = params.get(key);
            boolean shouldSign = false;
            if (ignoreSignType && "sign_type".equals(key)) {
                shouldSign = false;
            } else if (StringUtils.isNotEmpty(value)
                    && !Lists.newArrayList("sign", "key", "xmlString", "xmlDoc", "couponList").contains(key)) {
                shouldSign = true;
            }

            if (shouldSign) {
                toSign.append(key).append("=").append(value).append("&");
            }
        }

        toSign.append("key=").append(signKey);
        System.out.println(toSign.toString());
        if ("HMAC-SHA256".equals(signType)) {
            return createHmacSha256Sign(toSign.toString(), signKey);
        } else {
            return DigestUtils.md5Hex(toSign.toString()).toUpperCase();
        }
    }

    private static String createHmacSha256Sign(String message, String key) {
        try {
            Mac sha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256.init(secretKeySpec);
            byte[] bytes = sha256.doFinal(message.getBytes());
            return Hex.encodeHexString(bytes).toUpperCase();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {

        }

        return null;
    }

    /**
     * 校验签名是否正确.
     *
     * @param xmlBean  Bean需要标记有XML注解
     * @param signType 签名类型，如果为空，则默认为MD5
     * @param signKey  校验的签名Key
     * @return true - 签名校验成功，false - 签名校验失败
     */
    public static boolean checkSign(Object xmlBean, String signType, String signKey) {
        return checkSign(xmlBean2Map(xmlBean), signType, signKey);
    }

    /**
     * 校验签名是否正确.
     *
     * @param params   需要校验的参数Map
     * @param signType 签名类型，如果为空，则默认为MD5
     * @param signKey  校验的签名Key
     * @return true - 签名校验成功，false - 签名校验失败
     */
    public static boolean checkSign(Map<String, String> params, String signType, String signKey) {
        String sign = createSign(params, signType, signKey, false);
        return sign.equals(params.get("sign"));
    }

    /**
     * 将bean按照@XStreamAlias标识的字符串内容生成以之为key的map对象
     *
     * @param bean 包含@XStreamAlias的xml bean对象
     * @return map对象
     */
    public static Map<String, String> xmlBean2Map(Object bean) {
        Map<String, String> result = Maps.newHashMap();
        List<Field> fields = new ArrayList<>(Arrays.asList(bean.getClass().getDeclaredFields()));
        fields.addAll(Arrays.asList(bean.getClass().getSuperclass().getDeclaredFields()));
        for (Field field : fields) {
            try {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                if (field.get(bean) == null) {
                    field.setAccessible(isAccessible);
                    continue;
                }

                if (field.isAnnotationPresent(XStreamAlias.class)) {
                    result.put(field.getAnnotation(XStreamAlias.class).value(), field.get(bean).toString());
                } else if (!Modifier.isStatic(field.getModifiers())) {
                    //忽略掉静态成员变量
                    result.put(field.getName(), field.get(bean).toString());
                }

                field.setAccessible(isAccessible);
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {

            }

        }
        return result;
    }

}
