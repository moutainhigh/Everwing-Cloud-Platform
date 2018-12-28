package com.everwing.coreservice.common.wy.common.enums;

public enum WyTbEnum {
	TBS_ACCOUNT_TYPE_COMMON(0),  //通用账户
	TBS_ACCOUNT_TYPE_WY(1), //物业管理费账户
	TBS_ACCOUNT_TYPE_WATER(2), //水费账户
	TBS_ACCOUNT_TYPE_ELECT(3),  //电费账户
	TBS_ACCOUNT_TYPE_BT(4),   //本体基金
	
	TBS_ACCOUNT_STATUS_START(0),  //启用
	TBS_ACCOUNT_STATUS_STOP(1);  //停用
	
	private Object value;
	WyTbEnum(Object value){
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
