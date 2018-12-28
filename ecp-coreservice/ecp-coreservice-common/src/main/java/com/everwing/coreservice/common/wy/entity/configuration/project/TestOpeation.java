package com.everwing.coreservice.common.wy.entity.configuration.project;

import java.io.Serializable;

public class TestOpeation implements Serializable {
	private static final long serialVersionUID = 1979306946368768174L;
	
	/**公式**/
	private String formulaValue;
	
	/**计费项目值**/
	private Double billingFeeItemTestValue;

	public String getFormulaValue() {
		return formulaValue;
	}

	public void setFormulaValue(String formulaValue) {
		this.formulaValue = formulaValue;
	}

	public Double getBillingFeeItemTestValue() {
		return billingFeeItemTestValue;
	}

	public void setBillingFeeItemTestValue(Double billingFeeItemTestValue) {
		this.billingFeeItemTestValue = billingFeeItemTestValue;
	}
	
	
	
}
