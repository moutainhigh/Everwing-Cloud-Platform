package com.everwing.coreservice.common.wy.common.enums;

public enum TcOrderSourceTypeEnum {
	TCORDER_SOURCETYPE_RECEPTION(1,"前台"),
	TCORDER_SOURCETYPE_CC(2,"CC"),
	
	TCORDER_STATUS_PROCESSING(1,"处理中"),
	TCORDER_STATUS_FINISHED(2,"完成");
	
	
	TcOrderSourceTypeEnum(int value, String description) {
		this.value = value;
		this.description = description;
	}
	private int value;
	private String description;
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public static String getDescByValue(int val){
		for(TcOrderSourceTypeEnum e:TcOrderSourceTypeEnum.values()){
			if(e.getValue() == val){
				return e.getDescription();
			}
		}
		return "";
	}
	
	
	
}
