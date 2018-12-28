package com.everwing.coreservice.common.wy.common.enums;

public enum MeterDataEnum {
	
	//抄表数据当前状态 
	EX_STATUS_COMMON(1),	//正常		
	EX_STATUS_WARNING(2),	//警告
	EX_STATUS_ERROR(3),		//错误
	
	//是否有效
	IS_USE_YES(0), //有效
	IS_USE_NO(1), //无效
	
	//抄表数据状态:
	STATUS_AUDITING(0),			//审批中
	STATUS_AUDIT_COMPLETE(1),	//审批完成
	STATUS_AUDIT_NO(2), //审批不通过
	
	//是否有效
	IS_USED_YES(0),
	IS_USED_NO(1),
	
	STATUS_AGAINING(4), //重抄任务执行中
	STATUS_AGAINING_COMPLETE(5), //重抄任务完成
	
	//是否替换
	IS_REPLACE_YES(0),
	IS_REPLACE_NO(1);
	
	
	
	
	private Object value;
	MeterDataEnum(Object value){
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
