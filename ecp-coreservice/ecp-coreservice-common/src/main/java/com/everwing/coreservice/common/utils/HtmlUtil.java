package com.everwing.coreservice.common.utils;/**
 * Created by wust on 2018/11/20.
 */

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Function:
 * Reason:
 * Date:2018/11/20
 *
 * @author wusongti@lii.com.cn
 */
public class HtmlUtil {
    private HtmlUtil(){}
    public static List<String> extractImg(String content) {
        List<String> srcList = new ArrayList<String>(); // 用来存储获取到的图片地址
        if (StringUtils.isBlank(content)) {
            return srcList;
        }
        Pattern p = Pattern.compile("<(img|IMG)(.*?)(>|></img>|/>)");// 匹配字符串中的img标签
        Matcher matcher = p.matcher(content);
        boolean hasPic = matcher.find();
        if (hasPic == true){
            while (hasPic){// 如果含有图片，那么持续进行查找，直到匹配不到
                String group = matcher.group(2);// 获取第二个分组的内容，也就是 (.*?)匹配到的
                Pattern srcText = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");// 匹配图片的地址

                Matcher matcher2 = srcText.matcher(group);
                if (matcher2.find()) {
                    srcList.add(matcher2.group(3));// 把获取到的图片地址添加到列表中
                }
                hasPic = matcher.find();// 判断是否还有img标签
            }
        }
        return srcList;
    }
}
