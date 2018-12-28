package com.everwing.coreservice.common.wy.datasource;

public class DBContextHolder {

	/**
	 * ThreadLocal是解决线程安全问题一个很好的思路，它通过为每个线程提供一个独立的变量副本解决了变量并发访问的冲突问题。
	 * 在很多情况下，ThreadLocal比直接使用synchronized同步机制解决线程安全问题更简单，更方便，且结果程序拥有更高的并发性。
	 */
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	
	public static void setDBType(String dbType){
		 contextHolder.set(dbType);
	}
	
	public static String getDBType() {
        return (String) contextHolder.get();
    }
    public static void clearDBType() {
        contextHolder.remove();
    }
}
