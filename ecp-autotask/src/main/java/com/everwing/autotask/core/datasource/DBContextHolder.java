package com.everwing.autotask.core.datasource;

/**
 * 数据源存放中心
 *
 * @author DELL shiny
 * @create 2018/5/8
 */
public class DBContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setDBType(String dbType){
        contextHolder.set(dbType);
    }

    public static String getDBType() {
        return (String) contextHolder.get();
    }
}
