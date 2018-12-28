package com.everwing.coreservice.common.platform.serialize;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * 空值转换类
 *
 * @author DELL shiny
 * @create 2018/4/10
 */
public class NullSerializeFilter implements ValueFilter{

    @Override
    public Object process(Object o, String s, Object v) {
        if(v==null) {
            return "";
        }
        return v;
    }

}
