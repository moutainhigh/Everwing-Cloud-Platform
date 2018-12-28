package com.everwing.coreservice.common.utils.jdbc;
/**
 * 
 * ClassName: ParamEntity <br/>
 * Function: 用于存储JDBC参数的键值对. <br/>
 * Reason: . <br/>
 * Date: 2016年3月1日 下午2:30:20 <br/>
 *
 * @author wust
 * @version 1.5.1.2
 * @since JDK 1.7.0_75
 */
public class ParamEntity {
	private Object value;  //参数值
	private int sqlType;   //参数值对应的SQL数据类型
	
	public ParamEntity(Object value, int sqlType){
		this.value = value;
		this.sqlType = sqlType;
	}
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public int getValueType() {
		return sqlType;
	}
	public void setValueType(int sqlType) {
		this.sqlType = sqlType;
	}
}
