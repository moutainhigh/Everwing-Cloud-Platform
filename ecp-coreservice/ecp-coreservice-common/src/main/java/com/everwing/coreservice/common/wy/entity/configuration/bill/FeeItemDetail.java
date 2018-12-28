package com.everwing.coreservice.common.wy.entity.configuration.bill;

import java.io.Serializable;

public class FeeItemDetail implements Serializable{

	/**
	 * 该实体主要用于各种费用项拼接成json格式字符串
	 */
	private static final long serialVersionUID = 6094590807463319143L;

	/** 费用名**/
	private String feeName;
	
	/**计算值**/
	private String countValue;

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public String getCountValue() {
		return countValue;
	}

	public void setCountValue(String countValue) {
		this.countValue = countValue;
	}
	
}
