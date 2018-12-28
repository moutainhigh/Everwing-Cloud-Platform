package com.everwing.coreservice.common.wy.common.enums;

public enum MeterEnum {

	METER_START(0), //启用
	METER_STOP(1), //停用
	METER_YES(0),//是
	METER_NO(1), //否
	METER_USERTYPE_BUS(0),//商用
	METER_USERTYPE_CIVIL(1), //民用
	METER_USERTYPE_MANAGE(2), //管理处
	METER_TYPE_INDOOR(0), //室内
	METER_TYPE_OUTDOOR(1), //室外
	METER_TYPE_REMOTE(2), //远程
	METER_ELECTTYPE_COMM(0), //普通表
	METER_ELECTTYPE_TIME(1), //分时表
	
	RECORD_METERTYPE_ELECT(1), //电表
	RECORD_METERTYPE_WARTER(0), //水表
	RECORD_OPEARTETYPE_CHANGE(0),//更换
	RECORD_OPEARTETYPE_START(1),//启用
	RECORD_OPEARTETYPE_STOP(2),//停用
	
	SERVICE_TYPE_MY(1),//我的抄表
	SERVICE_TYPE_ALL(2);//所有抄表
	
	private Object value;
	 MeterEnum(Object value){
		 this.value = value;
	}
	 
	 public String getStringValue() {
			return (String) value;
	}

	public int getIntValue() {
			return (int) value;
	}
		
	public Object getValue() {
			return value;
	}
}
