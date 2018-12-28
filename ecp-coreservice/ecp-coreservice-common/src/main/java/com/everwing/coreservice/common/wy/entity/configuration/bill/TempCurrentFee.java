package com.everwing.coreservice.common.wy.entity.configuration.bill;

import java.io.Serializable;

public class TempCurrentFee implements Serializable{

	private static final long serialVersionUID = 4324703280221612798L;
	
	/**id**/
	private String id;
	
	private Double currentFee;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getCurrentFee() {
		return currentFee;
	}

	public void setCurrentFee(Double currentFee) {
		this.currentFee = currentFee;
	}
	
	
	
}
