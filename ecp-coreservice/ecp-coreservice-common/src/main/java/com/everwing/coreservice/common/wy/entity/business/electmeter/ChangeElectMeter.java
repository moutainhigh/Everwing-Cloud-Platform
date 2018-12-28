package com.everwing.coreservice.common.wy.entity.business.electmeter;

import java.io.Serializable;

public class ChangeElectMeter implements Serializable{

	private static final long serialVersionUID = 9218880327243089139L;
	
	private ElectMeter beforechangeelectricty;
	private ElectMeter afterelectricty;
	public ElectMeter getBeforechangeelectricty() {
		return beforechangeelectricty;
	}
	public void setBeforechangeelectricty(ElectMeter beforechangeelectricty) {
		this.beforechangeelectricty = beforechangeelectricty;
	}
	public ElectMeter getAfterelectricty() {
		return afterelectricty;
	}
	public void setAfterelectricty(ElectMeter afterelectricty) {
		this.afterelectricty = afterelectricty;
	}
	
	
}
