package com.everwing.coreservice.common.wy.common.enums;

public enum TcOrderStatusEnum {
	/**
	 * 处理中
	 */
	ORDER_STATUS_PROCESSING(1,"处理中"),
	ORDER_STATUS_DONE(2,"完成");
	
	
	
	TcOrderStatusEnum(int value, String description) {
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
		for(TcOrderStatusEnum e:TcOrderStatusEnum.values()){
			if(e.getValue() == val){
				return e.getDescription();
			}
		}
		return "";
	}
}
