package com.everwing.myexcel.util;/**
 * Created by wust on 2017/8/8.
 */

import com.everwing.coreservice.common.constant.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/8
 * @author wusongti@lii.com.cn
 */
public class MyExcelUtils {
    private MyExcelUtils(){}

    public static boolean isDate(String date) {
        String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(date);

        boolean dateType = mat.matches();

        return dateType;
    }

    public static String null2String(Object obj) {
        if (null == obj || "".equals(((String) obj).trim()) || "null".equals(((String) obj).trim()))
            return Constants.STR_EMPTY;

        return obj.toString().trim();

    }
}
